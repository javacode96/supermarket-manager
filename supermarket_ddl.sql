-- ============================================================
-- Database: supermarket_db
-- Description: Supermarket management system
-- Charset: utf8mb4 | Collation: utf8mb4_unicode_ci
-- ============================================================

DROP DATABASE IF EXISTS supermarket_db;
CREATE DATABASE supermarket_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE supermarket_db;

-- ------------------------------------------------------------
-- TABLE: categories
-- ------------------------------------------------------------
CREATE TABLE categories (
    category_id     INT             NOT NULL AUTO_INCREMENT,
    name            VARCHAR(100)    NOT NULL,
    description     TEXT,
    PRIMARY KEY (category_id)
);

-- ------------------------------------------------------------
-- TABLE: suppliers
-- ------------------------------------------------------------
CREATE TABLE suppliers (
    supplier_id     INT             NOT NULL AUTO_INCREMENT,
    tax_id          VARCHAR(20)     NOT NULL UNIQUE,
    name            VARCHAR(150)    NOT NULL,
    phone           VARCHAR(20),
    email           VARCHAR(100),
    PRIMARY KEY (supplier_id)
);

-- ------------------------------------------------------------
-- TABLE: products
-- ------------------------------------------------------------
CREATE TABLE products (
    product_id      INT             NOT NULL AUTO_INCREMENT,
    code            VARCHAR(20)     NOT NULL UNIQUE,
    name            VARCHAR(150)    NOT NULL,
    description     TEXT,
    price           DECIMAL(10,2)   NOT NULL DEFAULT 0.00,
    stock           INT             NOT NULL DEFAULT 0,
    category_id     INT,
    PRIMARY KEY (product_id),
    CONSTRAINT fk_prod_category
        FOREIGN KEY (category_id)
        REFERENCES categories (category_id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

-- ------------------------------------------------------------
-- TABLE: users
-- ------------------------------------------------------------
CREATE TABLE users (
    user_id         INT             NOT NULL AUTO_INCREMENT,
    name            VARCHAR(150)    NOT NULL,
    email           VARCHAR(100)    NOT NULL UNIQUE,
    password        VARCHAR(255)    NOT NULL,
    role            ENUM('admin','employee','customer')  NOT NULL DEFAULT 'customer',
    status          ENUM('active','inactive')            NOT NULL DEFAULT 'active',
    phone           VARCHAR(20),
    PRIMARY KEY (user_id)
);

-- ------------------------------------------------------------
-- TABLE: sales
-- ------------------------------------------------------------
CREATE TABLE sales (
    sale_id         INT             NOT NULL AUTO_INCREMENT,
    customer_id     INT             NOT NULL,
    employee_id     INT             NOT NULL,
    sale_date       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    subtotal        DECIMAL(10,2)   NOT NULL DEFAULT 0.00,
    tax             DECIMAL(10,2)   NOT NULL DEFAULT 0.00,
    total           DECIMAL(10,2)   NOT NULL DEFAULT 0.00,
    PRIMARY KEY (sale_id),
    CONSTRAINT fk_sale_customer
        FOREIGN KEY (customer_id)
        REFERENCES users (user_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_sale_employee
        FOREIGN KEY (employee_id)
        REFERENCES users (user_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

-- ------------------------------------------------------------
-- TABLE: sale_details
-- ------------------------------------------------------------
CREATE TABLE sale_details (
    detail_id       INT             NOT NULL AUTO_INCREMENT,
    sale_id         INT             NOT NULL,
    product_id      INT             NOT NULL,
    quantity        INT             NOT NULL DEFAULT 1,
    unit_price      DECIMAL(10,2)   NOT NULL,
    subtotal        DECIMAL(10,2)   NOT NULL,
    PRIMARY KEY (detail_id),
    CONSTRAINT fk_detail_sale
        FOREIGN KEY (sale_id)
        REFERENCES sales (sale_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_detail_product
        FOREIGN KEY (product_id)
        REFERENCES products (product_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

-- ============================================================
-- SAMPLE DATA
-- ============================================================

-- Categories
INSERT INTO categories (name, description) VALUES
('Dairy',       'Milk, cheese, yogurt and derivatives'),
('Meats',       'Beef, chicken, pork and cold cuts'),
('Beverages',   'Juices, water, sodas and energy drinks'),
('Bakery',      'Bread, cakes, cookies and pastries'),
('Cleaning',    'Household cleaning products');

-- Suppliers
INSERT INTO suppliers (tax_id, name, phone, email) VALUES
('1234567-8', 'Northern Distributors', '+502-2221-1111', 'sales@northdist.com'),
('9876543-2', 'Southern Dairy Co.',    '+502-2333-2222', 'info@southerndairy.com'),
('5432167-0', 'Valley Foods Inc.',     '+502-2456-3333', 'orders@valleyfoods.com');

-- Users (plain-text passwords for testing only — use hashed passwords in production)
INSERT INTO users (name, email, password, role, status, phone) VALUES
('Administrator', 'admin@supermarket.com',    'admin123', 'admin',    'active', '+502-2200-0001'),
('John Employee', 'employee@supermarket.com', 'emp123',   'employee', 'active', '+502-2200-0002'),
('Mary Customer', 'customer@supermarket.com', 'cli123',   'customer', 'active', '+502-5555-1234'),
('Carlos Lopez',  'carlos@email.com',         'pass123',  'customer', 'active', '+502-5555-5678'),
('Ana Martinez',  'ana@email.com',            'pass123',  'employee', 'active', '+502-5555-9012');

-- Products
INSERT INTO products (code, name, description, price, stock, category_id) VALUES
('P001', 'Whole milk 1L',         'Pasteurized whole milk 1 liter',       12.50, 100, 1),
('P002', 'Fresh cheese 500g',     'Artisan fresh cheese 500 grams',       25.00,  50, 1),
('P003', 'Chicken breast 1kg',    'Fresh chicken breast per kilogram',    35.00,  80, 2),
('P004', 'Purified water 1.5L',   'Purified water bottle 1.5 liters',      5.00, 200, 3),
('P005', 'French bread',          'French bread unit',                     1.50, 150, 4),
('P006', 'Orange juice 1L',       'Natural orange juice 1 liter',         18.00,  60, 3),
('P007', 'Plain yogurt 500g',     'Unsweetened plain yogurt 500 grams',   22.00,  70, 1),
('P008', 'Detergent 1kg',         'Multipurpose powder detergent 1kg',    32.00,  45, 5);

-- Sales
INSERT INTO sales (customer_id, employee_id, subtotal, tax, total) VALUES
(3, 2,  37.50,  4.50,  42.00),
(3, 2,  25.00,  3.00,  28.00),
(4, 5,  70.00,  8.40,  78.40),
(3, 2,  18.00,  2.16,  20.16),
(4, 5, 105.00, 12.60, 117.60);

-- Sale details
INSERT INTO sale_details (sale_id, product_id, quantity, unit_price, subtotal) VALUES
(1, 1, 3, 12.50,  37.50),
(2, 2, 1, 25.00,  25.00),
(3, 3, 2, 35.00,  70.00),
(4, 6, 1, 18.00,  18.00),
(5, 3, 3, 35.00, 105.00);
