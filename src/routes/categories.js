import express from "express";
import { pool } from "../db.js";

const router = express.Router();

/**
 * ✅ Update Category
 * PUT /categories/:id
 */
router.put("/:id", async (req, res) => {
  try {
    const { id } = req.params;
    const { name, is_active } = req.body;

    if (!name) {
      return res.status(400).json({ error: "Category name is required" });
    }

    const [result] = await pool.query(
      "UPDATE categories SET name = ?, is_active = ? WHERE id = ?",
      [name, is_active, id]
    );

    if (result.affectedRows === 0) {
      return res.status(404).json({ error: "Category not found" });
    }

    res.json({ message: "✅ Category updated successfully" });
  } catch (error) {
    console.error("Error updating category:", error);
    res.status(500).json({ error: "Failed to update category" });
  }
});

export default router;
