<?php
require_once '../../config/db.php';
requireRole('customer');

$conn = getConnection();
$uid = $_SESSION['user_id'];

$sales = $conn->query("SELECT s.id, s.subtotal, s.tax, s.total, s.date, u.name AS employee
    FROM sales s JOIN users u ON s.employee_id=u.id
    WHERE s.customer_id=$uid ORDER BY s.date DESC");

$active = 'history'; $role = 'customer';
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Purchase History | Customer</title>
    <link rel="stylesheet" href="../../assets/css/style.css">
</head>
<body>
<div class="app-layout">
    <?php include '../../config/sidebar.php'; ?>
    <main class="main-content">
        <div class="page-header"><h1>Purchase History</h1><p>All your transactions in one place</p></div>

        <?php if ($sales->num_rows === 0): ?>
        <div class="card" style="text-align:center;color:#aaa;padding:60px 40px">
            <p style="font-size:3rem;margin-bottom:12px">🛒</p>
            <p>You have no purchases yet.</p>
        </div>
        <?php else:
            while ($s = $sales->fetch_assoc()):
                $detail = $conn->query("SELECT sd.quantity, sd.unit_price, sd.subtotal, p.name AS prod
                    FROM sale_details sd JOIN products p ON sd.product_id=p.id WHERE sd.sale_id={$s['id']}");
        ?>
        <div class="card" style="margin-bottom:20px">
            <div class="card-header">
                <div>
                    <h3>Purchase #<?= $s['id'] ?></h3>
                    <p style="font-size:0.8rem;color:var(--muted);margin-top:2px">
                        📅 <?= date('m/d/Y H:i', strtotime($s['date'])) ?> — Served by: <?= htmlspecialchars($s['employee']) ?>
                    </p>
                </div>
                <span style="font-size:1.4rem;font-family:'Playfair Display',serif;color:var(--teal-dark)">Q<?= number_format($s['total'], 2) ?></span>
            </div>
            <div class="table-wrap">
                <table>
                    <thead><tr><th>Product</th><th>Quantity</th><th>Unit price</th><th>Subtotal</th></tr></thead>
                    <tbody>
                    <?php while ($d = $detail->fetch_assoc()): ?>
                        <tr>
                            <td><?= htmlspecialchars($d['prod']) ?></td>
                            <td><?= $d['quantity'] ?></td>
                            <td>Q<?= number_format($d['unit_price'], 2) ?></td>
                            <td>Q<?= number_format($d['subtotal'], 2) ?></td>
                        </tr>
                    <?php endwhile; ?>
                    </tbody>
                </table>
            </div>
            <div style="text-align:right;margin-top:10px;font-size:0.82rem;color:var(--muted)">
                Subtotal: Q<?= number_format($s['subtotal'],2) ?> &nbsp;|&nbsp;
                Tax: Q<?= number_format($s['tax'],2) ?> &nbsp;|&nbsp;
                <strong style="color:var(--dark)">Total: Q<?= number_format($s['total'],2) ?></strong>
            </div>
        </div>
        <?php endwhile; endif; ?>
    </main>
</div>
<script src="../../assets/js/app.js"></script>
</body>
</html>
<?php $conn->close(); ?>
