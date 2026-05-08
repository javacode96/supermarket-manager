# Supermarket Management System 🛍️🏪

You are required to develop a desktop application for the management of a **supermarket** using **Java** as the sole technology stack — with **Java Swing** for the graphical user interface and **MySQL** as the database engine, connected through **JDBC**. The system must support three distinct user roles — **Administrator** 👤, **Employee** 🧑‍💼, and **Client** 🛒 — all sharing a single login screen that automatically redirects each user to their corresponding interface upon successful authentication.

The **Administrator** 🔐 has full CRUD access to four core modules: **User Management** (registering, listing, editing, and deleting employees and clients, including role assignment and active/inactive status), **Product Management** (handling product records with fields such as code, name, description, price, and stock quantity 📦), **Category Management** (organizing products into categories like dairy, meats, beverages, and more 🗂️), and **Supplier Management** (maintaining supplier records including NIT, name, phone number, and email 🏭).

The **Employee** 🧑‍🏭 has access to two modules: **Sales Registration**, which allows processing a sale by selecting a client, choosing products and quantities, automatically calculating the total with tax, generating a purchase receipt 🧾, and updating stock upon completion; and **Product Consultation**, a read-only view of the product catalog showing prices and available stock.

The **Client** 👤 also has access to two modules: **My Profile**, where they can view and update their own personal information (name, email, phone, and password); and **Purchase History** 📋, where they can review all their past transactions with full detail of products, quantities, and amounts paid.

All forms throughout the system must include **basic validations** ✅ such as empty field checks, format verification, and insufficient stock detection to ensure data integrity and a smooth user experience throughout the application.
