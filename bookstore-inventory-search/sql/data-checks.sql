-- Bookstore Inventory Search: data quality checks
-- Run against the bookstore database after seed.sql (or against any copy of the data).
-- Each "Check" query returns only the rows that break a rule, so an empty result means it passed.
-- "Info" queries are not failures. They list data the app has to handle, such as authors with no books.

-- ------------------------------------------------------------
-- Referential integrity
-- The foreign keys in schema.sql now block these, but older copies of the data
-- (like the original coursework database) had no foreign keys.
-- ------------------------------------------------------------

-- Check 1: Inventory rows for a book that doesn't exist
-- Expected: 0 rows
SELECT henry_inventory.book_code, henry_inventory.branch_num
FROM henry_inventory
LEFT JOIN henry_book ON henry_inventory.book_code = henry_book.book_code
WHERE henry_book.book_code IS NULL;

-- Check 2: Inventory rows for a branch that doesn't exist
-- Expected: 0 rows
SELECT henry_inventory.book_code, henry_inventory.branch_num
FROM henry_inventory
LEFT JOIN henry_branch ON henry_inventory.branch_num = henry_branch.branch_num
WHERE henry_branch.branch_num IS NULL;

-- Check 3: Authorship rows for a book or author that doesn't exist
-- Expected: 0 rows
SELECT henry_wrote.book_code, henry_wrote.author_num
FROM henry_wrote
LEFT JOIN henry_book ON henry_wrote.book_code = henry_book.book_code
LEFT JOIN henry_author ON henry_wrote.author_num = henry_author.author_num
WHERE henry_book.book_code IS NULL OR henry_author.author_num IS NULL;

-- Check 4: Books whose publisher doesn't exist
-- Expected: 0 rows
SELECT henry_book.book_code, henry_book.title, henry_book.publisher_code
FROM henry_book
LEFT JOIN henry_publisher ON henry_book.publisher_code = henry_publisher.publisher_code
WHERE henry_publisher.publisher_code IS NULL;

-- ------------------------------------------------------------
-- Business rules
-- ------------------------------------------------------------

-- Check 5: Missing or negative stock counts
-- Expected: 0 rows
SELECT book_code, branch_num, on_hand
FROM henry_inventory
WHERE on_hand IS NULL OR on_hand < 0;

-- Check 6: Books with a missing or non-positive price
-- Expected: 0 rows
SELECT book_code, title, price
FROM henry_book
WHERE price IS NULL OR price <= 0;

-- Check 7: Books with a missing title
-- Expected: 0 rows
SELECT book_code, title
FROM henry_book
WHERE title IS NULL OR TRIM(title) = '';

-- Check 8: Paperback flag that isn't Y or N
-- Expected: 0 rows
SELECT book_code, title, paperback
FROM henry_book
WHERE paperback IS NULL OR paperback NOT IN ('Y', 'N');

-- Check 9: Books with no author
-- Expected: 0 rows
SELECT henry_book.book_code, henry_book.title
FROM henry_book
WHERE henry_book.book_code NOT IN (SELECT book_code FROM henry_wrote);

-- Check 10: Two authors listed in the same position on one book
-- Expected: 0 rows
SELECT book_code, sequence, COUNT(*) AS authors_in_position
FROM henry_wrote
GROUP BY book_code, sequence
HAVING COUNT(*) > 1;

-- Check 11: Books that aren't stocked at any branch
-- The app only lists books that appear in inventory, so these would be hidden from every search.
-- Expected: 0 rows
SELECT henry_book.book_code, henry_book.title
FROM henry_book
WHERE henry_book.book_code NOT IN (SELECT book_code FROM henry_inventory);

-- ------------------------------------------------------------
-- Info: data the app has to handle
-- ------------------------------------------------------------

-- Info 1: Authors with no books
-- The Author tab shows "No books found" for these.
-- Expected with the sample data: 2 rows (Riva Castelman, Barbara Owen)
SELECT henry_author.author_num, henry_author.author_first, henry_author.author_last
FROM henry_author
WHERE henry_author.author_num NOT IN (SELECT author_num FROM henry_wrote);

-- Info 2: Publishers with no books
-- The Publisher tab leaves these out of its dropdown.
-- Expected with the sample data: 7 rows
SELECT henry_publisher.publisher_code, henry_publisher.publisher_name
FROM henry_publisher
WHERE henry_publisher.publisher_code NOT IN (SELECT publisher_code FROM henry_book);

-- Info 3: Copies on hand at each branch
-- Expected with the sample data: 4 rows
SELECT henry_branch.branch_name, COUNT(henry_inventory.book_code) AS titles, SUM(henry_inventory.on_hand) AS copies
FROM henry_branch
LEFT JOIN henry_inventory ON henry_branch.branch_num = henry_inventory.branch_num
GROUP BY henry_branch.branch_num, henry_branch.branch_name
ORDER BY henry_branch.branch_num;
