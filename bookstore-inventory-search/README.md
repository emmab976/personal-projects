# Bookstore Inventory Search

A Java desktop app for searching a bookstore's catalog by **author**, **category** or **publisher**, showing each book's price and how many copies every branch has on hand. It runs against a MySQL database of sample bookstore data.

This started as a database course assignment (COMP 3421, University of Denver). In November 2026 I picked it back up and treated it like legacy code I'd inherited: I audited it, fixed the defects I found, hardened its security, and added a test suite. The commit history shows each step, starting from the tag `original-coursework`.

## Tech stack

- Java 17, Swing
- JDBC with MySQL 8 (Connector/J 9.7.0)
- Maven
- JUnit 6 and Maven Surefire for tests

## Project layout

```
bookstore-inventory-search/
├── pom.xml
├── db.properties.example        # app login template (copy to db.properties)
├── test-db.properties.example   # test login template (copy to test-db.properties)
├── sql/
│   ├── setup.sql                # creates the bookstore database and a read-only app user
│   ├── schema.sql               # tables and foreign keys
│   ├── seed.sql                 # sample data
│   ├── data-checks.sql          # data quality checks
│   └── setup-test.sql           # creates the bookstore_test database and test user
└── src/
    ├── main/java/bookstoresearch/   # app: Swing panels + HenryDAO (all database access)
    └── test/java/bookstoresearch/   # integration and data quality tests
```

## Setup

You'll need Java 17 or newer, Maven 3.9 or newer, and MySQL 8.

1. **Create the database and app user.** Open `sql/setup.sql` in MySQL Workbench (or the `mysql` client) as an admin user. Replace `change_me_locally` with a password of your choice, run it, and close the file without saving so the password stays out of the repo.
2. **Load the tables and data.** With `bookstore` as the active database, run `sql/schema.sql`, then `sql/seed.sql`.
3. **Add your login.** Copy the template and fill in the password from step 1:
   ```
   cp db.properties.example db.properties
   ```
   `db.properties` is git-ignored, so your password never gets committed.
4. **Run the app:**
   ```
   mvn compile exec:java
   ```

## Running the tests

The tests run against a separate `bookstore_test` database, so they never touch your real data.

1. Run `sql/setup-test.sql` as an admin user, the same way as `setup.sql` (replace the password, run, don't save).
2. Copy the test login template and fill in that password:
   ```
   cp test-db.properties.example test-db.properties
   ```
3. Run:
   ```
   mvn test
   ```

Before each run, the tests rebuild `bookstore_test` from `schema.sql` and `seed.sql`, so every run starts from the same known data. As a safety guard, the setup refuses to run unless the configured database is `bookstore_test`.

**What's covered (23 tests):**

- **`HenryDAOTest` (11 tests):** every search type, inventory counts across branches, an author with no books, and regression tests for the bugs below, including two SQL injection attempts.
- **`DataChecksTest` (12 tests):** runs each check in `sql/data-checks.sql` as its own named test. Each check returns only the rows that break a rule, so any row returned fails the test and is listed in the output. Adding a check to the SQL file adds a test automatically.

## Data quality checks

`sql/data-checks.sql` holds 11 validation queries you can also run by hand:

- **Referential integrity:** inventory, authorship and book rows that point at missing books, branches, authors or publishers.
- **Business rules:** missing or negative stock, missing or zero prices, blank titles, invalid paperback flags, books with no author, duplicate author positions, and books stocked at no branch.

It also includes three info queries for data the app has to handle, such as authors with no books.

## Bugs found and fixed

| Bug | Cause | Fix | Test |
| --- | --- | --- | --- |
| Crash on book code `138X` after upgrading the MySQL driver | Book codes were read as integers; the old driver silently turned `138X` into `138` | Book codes are handled as text throughout | `bookCodesWithLettersLoadAsText` |
| Inventory list showed the wrong branch | Loop used the selected book's index (`i`) instead of the loop counter (`j`) | One-character fix in each search panel | Checked manually (UI code) |
| Switching searches left stale price and stock on screen | Clearing a dropdown fired an event with nothing selected, which threw partway through the update | Guarded those events; added a clear empty state ("No books found", price N/A, "No copies in stock") | Checked manually; the empty result itself is covered by `booksByAuthorWithNoBooksIsEmpty` |
| SQL injection | Queries were built by joining strings | All queries use `PreparedStatement` | `publisherSearchIsNotInjectable`, `categorySearchIsNotInjectable` |
| Connection leaks | Every query opened a connection and never closed it | try-with-resources on every connection, statement and result set | Whole suite |
| Hardcoded root password | Credentials were in three source files | Git-ignored `db.properties` and a read-only `bookstore_app` user | n/a |

## How AI was used

I used an AI assistant as a code reviewer and pair programmer: it audited the original code, proposed changes as small snippets, and generated the SQL scripts from my database export. I reviewed and applied every code change myself, tested each one before committing, and kept the original code style.

## Credits

Sample data is the "Henry Books" dataset from *Concepts of Database Management* by Philip J. Pratt and Mary Z. Last.
