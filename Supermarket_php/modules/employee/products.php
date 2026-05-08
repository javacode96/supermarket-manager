<?php
require_once '../../config/db.php';
requireRole('employee');

$conn = getConnection();
$products = $conn->query("SELECT p.*, c.name AS cat FROM products p LEFT JOIN categories c ON p.category_id=c.id ORDER BY p.name");
$conn->close();
$active = 'products'; $role = 'employee';
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Products | Employee</title>
    <link rel="stylesheet" href="../../assets/css/style.css">
</head>
<body>
<div class="app-layout">
    <?php include '../../config/sidebar.php'; ?>
    <main class="main-content">
        <div class="page-header"><h1>Product Catalog</h1><p>Browse available products, prices and stock levels</p></div>
        <div class="card">
            <div class="card-header"><h3>All products</h3></div>
            <div class="search-bar">
                <input type="text" id="searchInput" placeholder="🔍 Search products...">
            </div>
            <div class="table-wrap">
                <table id="tableProds">
                    <thead><tr><th>Code</th><th>Name</th><th>Description</th><th>Price</th><th>Stock</th><th>Category</th></tr></thead>
                    <tbody>
                    <?php while ($p = $products->fetch_assoc()): ?>
                        <tr>
                            <td><code><?= htmlspecialchars($p['code']) ?></code></td>
                            <td><?= htmlspecialchars($p['name']) ?></td>
                            <td style="max-width:200px;font-size:0.8rem;color:#aaa"><?= htmlspecialchars($p['description'] ?? '') ?></td>
                            <td>Q<?= number_format($p['price'], 2) ?></td>
                            <td><span class="badge <?= $p['stock'] < 10 ? 'badge-red' : 'badge-teal' ?>"><?= $p['stock'] ?></span></td>
                            <td><?= htmlspecialchars($p['cat'] ?? '—') ?></td>
                        </tr>
                    <?php endwhile; ?>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>
<script src="../../assets/js/app.js"></script>
<script>filterTable('searchInput', 'tableProds');</script>
</body>
</html>
