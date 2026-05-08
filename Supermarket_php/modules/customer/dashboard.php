<?php
require_once '../../config/db.php';
requireRole('customer');

$conn = getConnection();
$uid = $_SESSION['user_id'];
$totalPurchases = $conn->query("SELECT COUNT(*) c FROM sales WHERE customer_id=$uid")->fetch_assoc()['c'];
$totalSpent     = $conn->query("SELECT COALESCE(SUM(total),0) c FROM sales WHERE customer_id=$uid")->fetch_assoc()['c'];
$lastPurchase   = $conn->query("SELECT date FROM sales WHERE customer_id=$uid ORDER BY date DESC LIMIT 1")->fetch_assoc();
$conn->close();

$active = 'dashboard'; $role = 'customer';
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Home | Customer</title>
    <link rel="stylesheet" href="../../assets/css/style.css">
</head>
<body>
<div class="app-layout">
    <?php include '../../config/sidebar.php'; ?>
    <main class="main-content">
        <div class="page-header">
            <h1>Hello, <?= htmlspecialchars(explode(' ', $_SESSION['name'])[0]) ?> 👋</h1>
            <p>Your personal space at the supermarket</p>
        </div>
        <div class="stats-grid">
            <div class="stat-card"><span class="icon">🧾</span><span class="value"><?= $totalPurchases ?></span><span class="label">Total purchases</span></div>
            <div class="stat-card"><span class="icon">💰</span><span class="value">Q<?= number_format($totalSpent, 2) ?></span><span class="label">Total spent</span></div>
            <div class="stat-card"><span class="icon">📅</span><span class="value" style="font-size:1.2rem"><?= $lastPurchase ? date('m/d/Y', strtotime($lastPurchase['date'])) : '—' ?></span><span class="label">Last purchase</span></div>
        </div>
        <div class="card" style="max-width:420px">
            <div class="card-header"><h3>Quick access</h3></div>
            <div style="display:flex;gap:12px;flex-wrap:wrap">
                <a href="profile.php" class="btn btn-primary">👤 My Profile</a>
                <a href="history.php" class="btn btn-teal">🧾 Purchase History</a>
            </div>
        </div>
    </main>
</div>
<script src="../../assets/js/app.js"></script>
</body>
</html>
