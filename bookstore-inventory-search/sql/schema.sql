-- Bookstore Inventory Search: table definitions
-- Sample data is the "Henry Books" dataset from Pratt & Last, Concepts of Database Management.
-- Run against the bookstore database after setup.sql, then load seed.sql.

DROP TABLE IF EXISTS henry_wrote;
DROP TABLE IF EXISTS henry_inventory;
DROP TABLE IF EXISTS henry_book;
DROP TABLE IF EXISTS henry_branch;
DROP TABLE IF EXISTS henry_author;
DROP TABLE IF EXISTS henry_publisher;

CREATE TABLE henry_publisher (
  publisher_code char(3) NOT NULL,
  publisher_name char(25) DEFAULT NULL,
  city char(20) DEFAULT NULL,
  PRIMARY KEY (publisher_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE henry_author (
  author_num decimal(2,0) NOT NULL,
  author_last char(12) DEFAULT NULL,
  author_first char(10) DEFAULT NULL,
  PRIMARY KEY (author_num)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE henry_branch (
  branch_num decimal(2,0) NOT NULL,
  branch_name char(50) DEFAULT NULL,
  branch_location char(50) DEFAULT NULL,
  num_employees decimal(2,0) DEFAULT NULL,
  PRIMARY KEY (branch_num)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE henry_book (
  book_code char(4) NOT NULL,
  title char(40) DEFAULT NULL,
  publisher_code char(3) DEFAULT NULL,
  type char(3) DEFAULT NULL,
  price decimal(4,2) DEFAULT NULL,
  paperback char(1) DEFAULT NULL,
  PRIMARY KEY (book_code),
  CONSTRAINT fk_book_publisher FOREIGN KEY (publisher_code) REFERENCES henry_publisher (publisher_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE henry_inventory (
  book_code char(4) NOT NULL,
  branch_num decimal(2,0) NOT NULL,
  on_hand decimal(2,0) DEFAULT NULL,
  PRIMARY KEY (book_code,branch_num),
  CONSTRAINT fk_inventory_book FOREIGN KEY (book_code) REFERENCES henry_book (book_code),
  CONSTRAINT fk_inventory_branch FOREIGN KEY (branch_num) REFERENCES henry_branch (branch_num)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE henry_wrote (
  book_code char(4) NOT NULL,
  author_num decimal(2,0) NOT NULL,
  sequence decimal(1,0) DEFAULT NULL,
  PRIMARY KEY (book_code,author_num),
  CONSTRAINT fk_wrote_book FOREIGN KEY (book_code) REFERENCES henry_book (book_code),
  CONSTRAINT fk_wrote_author FOREIGN KEY (author_num) REFERENCES henry_author (author_num)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
