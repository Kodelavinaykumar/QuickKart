import express from "express";
import dotenv from "dotenv";
import { pool } from "./db.js";

// Import Routes
import userRoutes from "./routes/users.js";

dotenv.config();
const app = express();

// ✅ Middleware
app.use(express.json());

/**
 * ✅ Health Check Route
 * GET http://localhost:4000/
 */
app.get("/", async (req, res) => {
  try {
    const [rows] = await pool.query("SELECT NOW() AS current_time");
    res.json({
      message: "QuickKart API is running 🚀",
      db_time: rows[0].current_time,
    });
  } catch (err) {
    console.error("DB Error:", err.sqlMessage || err.message);
    res.status(500).json({ error: "Database connection failed" });
  }
});

/**
 * ✅ User Routes
 * All routes prefixed with /api/users
 * Examples:
 *   POST /api/users/register
 *   POST /api/users/login
 *   GET  /api/users/profile
 */
app.use("/api/users", userRoutes);

// ✅ Start Server
const port = process.env.PORT || 4000;
app.listen(port, () => {
  console.log(`Server running on http://localhost:${port}`);
});