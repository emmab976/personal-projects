-- Bookstore Inventory Search: test database and test user
-- Run once as an admin user (for example root), before running mvn test.
-- Replace change_me_locally with your own password in the editor, run it, and close without saving.
-- Use the same password in test-db.properties.

CREATE DATABASE IF NOT EXISTS bookstore_test DEFAULT CHARACTER SET utf8mb4;

-- The tests rebuild the tables from schema.sql and seed.sql before each run,
-- so this account has full rights, but only inside the test database.
CREATE USER IF NOT EXISTS 'bookstore_test'@'localhost' IDENTIFIED BY 'change_me_locally';
GRANT ALL PRIVILEGES ON bookstore_test.* TO 'bookstore_test'@'localhost';
