-- Bookstore Inventory Search: database and app user
-- Run once as an admin user (for example root), before schema.sql and seed.sql.
-- Replace change_me_locally with your own password, and use the same value in db.properties.

CREATE DATABASE IF NOT EXISTS bookstore DEFAULT CHARACTER SET utf8mb4;

-- The app only reads data, so its account can only SELECT from this one database.
CREATE USER IF NOT EXISTS 'bookstore_app'@'localhost' IDENTIFIED BY 'change_me_locally';
GRANT SELECT ON bookstore.* TO 'bookstore_app'@'localhost';
