# 🛍️ Supermarket Management System
Web-based system built with PHP + MySQL

## Project Structure
```
supermarket/
├── index.php                  # Login page
├── register.php               # Customer registration
├── logout.php
├── database.sql               # Full SQL script
├── config/
│   ├── db.php                 # DB connection & session helpers
│   └── sidebar.php            # Shared navigation sidebar
├── assets/
│   ├── css/style.css
│   └── js/app.js
└── modules/
    ├── admin/
    │   ├── dashboard.php
    │   ├── users.php
    │   ├── products.php
    │   ├── categories.php
    │   └── suppliers.php
    ├── employee/
    │   ├── dashboard.php
    │   ├── sales.php
    │   └── products.php
    └── customer/
        ├── dashboard.php
        ├── profile.php
        └── history.php
```

## Requirements
- PHP 7.4+
- MySQL 8.x
- Apache or Nginx (XAMPP / WAMP recommended)

## Installation

### 1. Copy files
Place the `supermarket/` folder inside your web root:
- XAMPP: `C:/xampp/htdocs/supermarket`
- WAMP:  `C:/wamp64/www/supermarket`
- Linux: `/var/www/html/supermarket`

### 2. Create the database
1. Open phpMyAdmin at http://localhost/phpmyadmin
2. Create a database named `supermarket_db`
3. Import `database.sql`

### 3. Configure connection
Edit `config/db.php` if your MySQL credentials differ:
```php
define('DB_HOST', 'localhost');
define('DB_USER', 'root');
define('DB_PASS', '');          // your password
define('DB_NAME', 'supermarket_db');
```

### 4. Open the app
Navigate to: http://localhost/supermarket

## Demo credentials
| Role          | Email                       | Password      |
|---------------|-----------------------------|---------------|
| Administrator | admin@supermarket.com       | admin123      |
| Employee      | employee@supermarket.com    | employee123   |
| Customer      | customer@supermarket.com    | customer123   |

## Modules by role

### Administrator
- Dashboard with full stats
- User Management (CRUD)
- Product Management (CRUD)
- Category Management (CRUD)
- Supplier Management (CRUD)

### Employee
- Dashboard with daily sales
- Register Sales (cart + tax + receipt + auto stock update)
- Product Catalog (read-only)

### Customer
- Register & login
- My Profile (edit personal info)
- Purchase History (detailed)
