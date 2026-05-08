<?php
require_once '../../config/db.php';
requireRole('administrator');

$conn = getConnection();
$totalUsers     = $conn->query("SELECT COUNT(*) c FROM users WHERE role_id != 1")->fetch_assoc()['c'];
$totalProducts  = $conn->query("SELECT COUNT(*) c FROM products")->fetch_assoc()['c'];
$totalCats      = $conn->query("SELECT COUNT(*) c FROM categories")->fetch_assoc()['c'];
$totalSuppliers = $conn->query("SELECT COUNT(*) c FROM suppliers")->fetch_assoc()['c'];
$totalRevenue   = $conn->query("SELECT COALESCE(SUM(total),0) c FROM sales")->fetch_assoc()['c'];
$salesToday     = $conn->query("SELECT COUNT(*) c FROM sales WHERE DATE(date) = CURDATE()")->fetch_assoc()['c'];
$lowStock       = $conn->query("SELECT COUNT(*) c FROM products WHERE stock < 10")->fetch_assoc()['c'];

$recentSales = $conn->query("SELECT s.id, u.name AS customer, s.total, s.date
    FROM sales s JOIN users u ON s.customer_id = u.id
    ORDER BY s.date DESC LIMIT 6");

$conn->close();
$active = 'dashboard'; $role = 'administrator';
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Dashboard | Administrator</title>
    <link rel="stylesheet" href="../../assets/css/style.css">
</head>
<body>
<div class="app-layout">
    <?php include '../../config/sidebar.php'; ?>
    <main class="main-content">
        <div class="page-header">
            <h1>Dashboard</h1>
            <p>System overview — <?= date('F d, Y') ?></p>
        </div>

        <div class="stats-grid">
            <div class="stat-card">
                <span class="icon">👥</span>
                <span class="value"><?= $totalUsers ?></span>
                <span class="label">Registered Users</span>
            </div>
            <div class="stat-card">
                <span class="icon">🛒</span>
                <span class="value"><?= $totalProducts ?></span>
                <span class="label">Products</span>
            </div>
            <div class="stat-card">
                <span class="icon">🗂️</span>
                <span class="value"><?= $totalCats ?></span>
                <span class="label">Categories</span>
            </div>
            <div class="stat-card">
                <span class="icon">📦</span>
                <span class="value"><?= $totalSuppliers ?></span>
                <span class="label">Suppliers</span>
            </div>
            <div class="stat-card">
                <span class="icon">💰</span>
                <span class="value">Q<?= number_format($totalRevenue, 0) ?></span>
                <span class="label">Total Revenue</span>
            </div>
            <div class="stat-card">
                <span class="icon">💳</span>
                <span class="value"><?= $salesToday ?></span>
                <span class="label">Sales Today</span>
            </div>
            <?php if ($lowStock > 0): ?>
            <div class="stat-card" style="border-left: 4px solid var(--red)">
                <span class="icon">⚠️</span>
                <span class="value" style="color:var(--red)"><?= $lowStock ?></span>
                <span class="label">Low Stock Items</span>
            </div>
            <?php endif; ?>
        </div>

        <div class="card">
            <div class="card-header">
                <h3>Recent Sales</h3>
            </div>
            <div class="table-wrap">
                <table>
                    <thead><tr><th>#</th><th>Customer</th><th>Total</th><th>Date</th></tr></thead>
                    <tbody>
                    <?php if ($recentSales && $recentSales->num_rows > 0):
                        while ($s = $recentSales->fetch_assoc()): ?>
                        <tr>
                            <td>#<?= $s['id'] ?></td>
                            <td><?= htmlspecialchars($s['customer']) ?></td>
                            <td>Q<?= number_format($s['total'], 2) ?></td>
                            <td><?= date('m/d/Y H:i', strtotime($s['date'])) ?></td>
                        </tr>
                    <?php endwhile; else: ?>
                        <tr><td colspan="4" style="text-align:center;color:#aaa;padding:20px">No sales recorded yet</td></tr>
                    <?php endif; ?>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>
<script src="../../assets/js/app.js"></script>
</body>
</html>
