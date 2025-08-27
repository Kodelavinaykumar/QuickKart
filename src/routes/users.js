import express from "express";
import bcrypt from "bcrypt";
import jwt from "jsonwebtoken";
import { pool } from "../db.js";
import { authMiddleware } from "../middleware/authMiddleware.js";

const router = express.Router();

/**
 * ✅ Signup/Register
 * POST /api/users/register or /api/users/signup
 */
router.post(["/register", "/signup"], async (req, res) => {
  const { email, username, password, full_name, phone } = req.body;

  if (!email || !username || !password) {
    return res.status(400).json({ error: "Email, username, and password are required" });
  }

  try {
    // Check for existing email or phone
    const [existingUser] = await pool.query(
      "SELECT * FROM users WHERE email = ? OR phone = ?",
      [email, phone]
    );

    if (existingUser.length > 0) {
      return res.status(400).json({ error: "Email or phone already registered" });
    }

    const hashedPassword = await bcrypt.hash(password, 10);

    await pool.query(
      "INSERT INTO users (email, username, password_hash, full_name, phone) VALUES (?, ?, ?, ?, ?)",
      [email, username, hashedPassword, full_name || null, phone || null]
    );

    res.status(201).json({ message: "User registered successfully 🚀" });
  } catch (err) {
    console.error("Signup error:", err);
    res.status(500).json({ error: "User registration failed" });
  }
});

/**
 * ✅ Login
 * POST /api/users/login
 */
router.post("/login", async (req, res) => {
  const { email, password } = req.body;

  if (!email || !password) {
    return res.status(400).json({ error: "Email and password are required" });
  }

  try {
    const [rows] = await pool.query("SELECT * FROM users WHERE email = ?", [email]);

    if (rows.length === 0) {
      return res.status(400).json({ error: "User not found" });
    }

    const user = rows[0];
    const isPasswordValid = await bcrypt.compare(password, user.password_hash);

    if (!isPasswordValid) {
      return res.status(400).json({ error: "Invalid credentials" });
    }

    // Create JWT access token (short-lived)
    const accessToken = jwt.sign(
      { id: user.id, email: user.email },
      process.env.JWT_SECRET,
      { expiresIn: "15m" }
    );

    // Create JWT refresh token (long-lived)
    const refreshToken = jwt.sign(
      { id: user.id, email: user.email },
      process.env.JWT_REFRESH_SECRET,
      { expiresIn: "7d" }
    );

    res.json({ message: "Login successful ✅", accessToken, refreshToken });
  } catch (err) {
    console.error("Login error:", err);
    res.status(500).json({ error: "Login failed" });
  }
});

/**
 * ✅ Refresh Token
 * POST /api/users/refresh
 */
router.post("/refresh", async (req, res) => {
  const { refreshToken } = req.body;
  if (!refreshToken) return res.status(401).json({ error: "Refresh token missing" });

  try {
    jwt.verify(refreshToken, process.env.JWT_REFRESH_SECRET, (err, decoded) => {
      if (err) return res.status(403).json({ error: "Invalid refresh token" });

      const newAccessToken = jwt.sign(
        { id: decoded.id, email: decoded.email },
        process.env.JWT_SECRET,
        { expiresIn: "15m" }
      );

      res.json({ accessToken: newAccessToken });
    });
  } catch (err) {
    console.error("Refresh token error:", err);
    res.status(500).json({ error: "Failed to refresh token" });
  }
});

/**
 * ✅ Get Profile (Protected)
 * GET /api/users/profile
 */
router.get("/profile", authMiddleware, async (req, res) => {
  try {
    const [rows] = await pool.query(
      "SELECT id, email, username, full_name, phone, status FROM users WHERE id = ?",
      [req.user.id]
    );

    if (rows.length === 0) {
      return res.status(404).json({ error: "User not found" });
    }

    res.json(rows[0]);
  } catch (err) {
    console.error("Profile error:", err);
    res.status(500).json({ error: "Failed to fetch profile" });
  }
});

/**
 * ✅ Update Profile (Protected)
 * PUT /api/users/profile
 */
router.put("/profile", authMiddleware, async (req, res) => {
  const { full_name, phone } = req.body;

  try {
    const [result] = await pool.query(
      "UPDATE users SET full_name = ?, phone = ? WHERE id = ?",
      [full_name, phone, req.user.id]
    );

    if (result.affectedRows === 0) {
      return res.status(404).json({ error: "User not found" });
    }

    res.json({ message: "Profile updated successfully ✅" });
  } catch (err) {
    console.error("Update error:", err);
    res.status(500).json({ error: "Failed to update profile" });
  }
});

/**
 * ✅ Delete Account (Protected)
 * DELETE /api/users/profile
 */
router.delete("/profile", authMiddleware, async (req, res) => {
  try {
    const [result] = await pool.query("DELETE FROM users WHERE id = ?", [req.user.id]);

    if (result.affectedRows === 0) {
      return res.status(404).json({ error: "User not found or already deleted" });
    }

    res.json({ message: "User deleted successfully ❌" });
  } catch (err) {
    console.error("Delete error:", err);
    res.status(500).json({ error: "Failed to delete user" });
  }
});

export default router;