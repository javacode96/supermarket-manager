-- ============================================
-- SUPERMARKET MANAGEMENT SYSTEM
-- Full SQL Script - Database, Tables & Sample Data
-- ============================================

CREATE DATABASE IF NOT EXISTS supermarket_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE supermarket_db;

-- Roles table
CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO roles (name) VALUES ('administrator'), ('employee'), ('customer');

-- Categories table
CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO categories (name, description) VALUES
('Dairy',        'Milk, cheese, yogurt and dairy products'),
('Meats',        'Fresh and packaged meat products'),
('Beverages',    'Soft drinks, juices, water and beverages'),
('Bakery',       'Bread, pastries and baked goods'),
('Vegetables',   'Fresh vegetables and greens'),
('Snacks',       'Chips, crackers and snack foods'),
('Cleaning',     'Household cleaning and hygiene products');

-- Suppliers table
CREATE TABLE suppliers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nit VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO suppliers (nit, name, phone, email) VALUES
('1234567-8', 'Fresh Foods Distributor S.A.', '22334455', 'contact@freshfoods.com'),
('9876543-2', 'Global Supply Co.',            '55667788', 'sales@globalsupply.com');

-- Users table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    password VARCHAR(255) NOT NULL,
    role_id INT NOT NULL,
    active TINYINT(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Passwords: admin123, employee123, customer123 (MD5 for demo)
INSERT INTO users (name, email, phone, password, role_id) VALUES
('Main Administrator', 'admin@supermarket.com',    '55001100', MD5('admin123'),     1),
('John Employee',      'employee@supermarket.com', '55002200', MD5('employee123'),  2),
('Mary Customer',      'customer@supermarket.com', '55003300', MD5('customer123'),  3);

-- Products table
CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(30) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    category_id INT,
    supplier_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
);

INSERT INTO products (code, name, description, price, stock, category_id, supplier_id) VALUES
('PRD001', 'Whole Milk 1L',       'Fresh whole milk, 1 liter bottle',          12.50, 200, 1, 1),
('PRD002', 'Chicken Breast 1kg',  'Fresh boneless chicken breast per kilogram', 38.00,  80, 2, 1),
('PRD003', 'Orange Juice 500ml',  'Natural squeezed orange juice',              18.00, 150, 3, 2),
('PRD004', 'White Bread',         'Soft sliced white bread loaf',                9.50, 120, 4, 2),
('PRD005', 'Tomatoes 1kg',        'Fresh ripe tomatoes per kilogram',           10.00,  60, 5, 1),
('PRD006', 'Potato Chips 150g',   'Salted crispy potato chips',                 14.00, 100, 6, 2),
('PRD007', 'All-Purpose Cleaner', 'Multi-surface household cleaner 750ml',      22.00,  45, 7, 2);

-- Sales table
CREATE TABLE sales (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    employee_id INT NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    tax DECIMAL(10,2) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES users(id),
    FOREIGN KEY (employee_id) REFERENCES users(id)
);

-- Sale detail table
CREATE TABLE sale_details (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sale_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (sale_id) REFERENCES sales(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);
