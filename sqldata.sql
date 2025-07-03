-- Insert sample categories (hierarchical structure)
INSERT INTO `categories` (`id`, `parent_category_id`, `name`) VALUES
(1, NULL, 'Fiction'),
(2, 1, 'Science Fiction'),
(3, 1, 'Fantasy'),
(4, NULL, 'Non-Fiction'),
(5, 4, 'Biography'),
(6, 4, 'History');

-- Reset auto-increment counter for categories
ALTER TABLE `categories` AUTO_INCREMENT = 7;

-- Insert sample publishers
INSERT INTO `publishers` (`id`, `email`, `name`) VALUES
(1, 'penguin@example.com', 'Penguin Books'),
(2, 'harper@example.com', 'HarperCollins'),
(3, 'randomhouse@example.com', 'Random House');

-- Reset auto-increment counter for publishers
ALTER TABLE `publishers` AUTO_INCREMENT = 4;

-- Insert sample authors
INSERT INTO `authors` (`id`, `bio`, `email`, `name`, `nationality`) VALUES
(1, 'Renowned science fiction writer', 'asimov@example.com', 'Isaac Asimov', 'American'),
(2, 'Fantasy author famous for Middle-earth', 'tolkien@example.com', 'J.R.R. Tolkien', 'British'),
(3, 'Contemporary fantasy writer', 'rowling@example.com', 'J.K. Rowling', 'British'),
(4, 'Historical biographer', 'mccullough@example.com', 'David McCullough', 'American');

-- Reset auto-increment counter for authors
ALTER TABLE `authors` AUTO_INCREMENT = 5;

-- Insert sample books
INSERT INTO `books` (
  `borrowed_count`, `publication_year`, `remaining_quantity`, `total_quantity`,
  `category_id`, `created_at`, `publisher_id`, `updated_at`,
  `edition`, `isbn`, `language`, `summary`, `title`
) VALUES
-- Science Fiction
(5, 1951, 8, 10, 2, NOW(), 1, NOW(), 
 '1st', '9780553293357', 'English', 'Foundation begins the epic saga of the Foundation', 'Foundation'),
 
(3, 1950, 5, 5, 2, NOW(), 1, NOW(),
 'Revised', '9780553803716', 'English', 'A robot must protect humans', 'I, Robot'),
 
-- Fantasy
(10, 1954, 2, 12, 3, NOW(), 2, NOW(),
 '50th Anniversary', '9780618640157', 'English', 'The fellowship begins their quest', 'The Lord of the Rings'),
 
(15, 1997, 5, 20, 3, NOW(), 3, NOW(),
 '1st', '9780747532743', 'English', 'A boy discovers he is a wizard', 'Harry Potter and the Philosopher''s Stone'),
 
-- Biography
(2, 2001, 3, 5, 5, NOW(), 2, NOW(),
 '1st', '9780684862155', 'English', 'Biography of John Adams', 'John Adams');

-- Insert book-author relationships
INSERT INTO `book_authors` (`author_id`, `book_id`) VALUES
(1, 1), -- Asimov wrote Foundation
(1, 2), -- Asimov wrote I, Robot
(2, 3), -- Tolkien wrote LOTR
(3, 4), -- Rowling wrote Harry Potter
(4, 5); -- McCullough wrote John Adams

-- Insert sample borrowers
INSERT INTO `borrowers` (
  `nid`, `address`, `email`, `name`, `phone`, `created_at`, `updated_at`
) VALUES
('12345678901234', '123 Main St, Anytown', 'john.doe@example.com', 'John Doe', '555-0101', NOW(), NOW()),
('23456789012345', '456 Oak Ave, Somewhere', 'jane.smith@example.com', 'Jane Smith', '555-0202', NOW(), NOW()),
('34567890123456', '789 Pine Rd, Nowhere', 'robert.johnson@example.com', 'Robert Johnson', '555-0303', NOW(), NOW());

-- Insert sample borrow transactions
INSERT INTO `borrow_transactions` (
  `borrow_date`, `due_date`, `return_date`, `returned`, `book_id`, `borrower_id`, `created_at`, `updated_at`
) VALUES
-- Current borrows
('2023-10-01', '2023-10-15', NULL, b'0', 1, 1, NOW(), NOW()),
('2023-10-05', '2023-10-19', NULL, b'0', 3, 2, NOW(), NOW()),
-- Returned borrows
('2023-09-15', '2023-09-29', '2023-09-28', b'1', 4, 3, NOW(), NOW()),
('2023-09-20', '2023-10-04', '2023-10-03', b'1', 2, 1, NOW(), NOW());
