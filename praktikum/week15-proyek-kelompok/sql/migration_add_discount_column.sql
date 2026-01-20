-- Migration: Add discount column to transactions table
-- Run this if discount column does not exist yet

-- For PostgreSQL (if column doesn't exist)
ALTER TABLE transactions ADD COLUMN IF NOT EXISTS discount DECIMAL(12,2) NOT NULL DEFAULT 0 AFTER subtotal;
