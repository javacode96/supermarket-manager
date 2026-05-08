<?php
require_once '../../config/db.php';
requireRole('employee');

$conn = getConnection();
$uid = $_SESSION['user_id'];

$salesToday  = $conn->query("SELECT COUNT(*) c FROM sales WHERE employee_id=$uid AND DATE(date)=CURDATE()")->fetch_assoc()['c'];
$revenueToday = $conn->query("SELECT COALESCE(SUM(total),0) c FROM sales WHERE employee_id=$uid AND DATE(date)=CURDATE()")->fetch_assoc()['c'];
$availProds  = $conn->query("SELECT COUNT(*) c FROM products WHERE stock > 0")->fetch_assoc()['c'];
$lowStock    = $conn->query("SELECT COUNT(*) c FROM products WHERE stock < 10")->fetch_assoc()['c'];

$recentSales = $conn->query("SELECT s.id, u.name AS customer, s.total, s.date
    FROM sales s JOIN users u ON s.customer_id=u.id
    WHERE s.employee_id=$uid ORDER BY s.date DESC LIMIT 5");

$conn->close();
$active = 'dashboard'; $role = 'employee';
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Dashboard | Employee</title>
    <link rel="stylesheet" href="../../assets/css/style.css">
</head>
<body>
<div class="app-layout">
    <?php include '../../config/sidebar.php'; ?>
    <main class="main-content">
        <div class="page-header">
            <h1>Welcome, <?= htmlspecialchars(explode(' ', $_SESSION['name'])[0]) ?></h1>
            <p>Employee dashboard — <?= date('F d, Y') ?></p>
        </div>
        <div class="stats-grid">
            <div class="stat-card"><span class="icon">💳</span><span class="value"><?= $salesToday ?></span><span class="label">My sales today</span></div>
            <div class="stat-card"><span class="icon">💰</span><span class="value">Q<?= number_format($revenueToday, 2) ?></span><span class="label">Revenue today</span></div>
            <div class="stat-card"><span class="icon">🛒</span><span class="value"><?= $availProds ?></span><span class="label">Products available</span></div>
            <?php if ($lowStock): ?>
            <div class="stat-card" style="border-left:4px solid var(--red)"><span class="icon">⚠️</span><span class="value" style="color:var(--red)"><?= $lowStock ?></span><span class="label">Low stock</span></div>
            <?php endif; ?>
        </div>
        <div class="card">
            <div class="card-header"><h3>My recent sales</h3><a href="sales.php" class="btn btn-primary btn-sm">+ New sale</a></div>
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
                        <tr><td colspan="4" style="text-align:center;color:#aaa;padding:20px">No sales recorded today</td></tr>
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
