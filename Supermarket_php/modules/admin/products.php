<?php
require_once '../../config/db.php';
requireRole('administrator');

$conn = getConnection();
$msg = ''; $msgType = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $id          = intval($_POST['id'] ?? 0);
    $code        = sanitize($conn, $_POST['code']        ?? '');
    $name        = sanitize($conn, $_POST['name']        ?? '');
    $description = sanitize($conn, $_POST['description'] ?? '');
    $price       = floatval($_POST['price'] ?? 0);
    $stock       = intval($_POST['stock']   ?? 0);
    $cat_id      = intval($_POST['category_id']  ?? 0);
    $sup_id      = intval($_POST['supplier_id']  ?? 0);

    if (empty($code) || empty($name) || $price <= 0) {
        $msg = 'Code, name and price are required.'; $msgType = 'error';
    } else {
        $catVal = $cat_id ?: 'NULL';
        $supVal = $sup_id ?: 'NULL';
        if ($id > 0) {
            $sql = "UPDATE products SET code='$code',name='$name',description='$description',price=$price,stock=$stock,category_id=$catVal,supplier_id=$supVal WHERE id=$id";
        } else {
            $sql = "INSERT INTO products (code,name,description,price,stock,category_id,supplier_id) VALUES ('$code','$name','$description',$price,$stock,$catVal,$supVal)";
        }
        if ($conn->query($sql)) { $msg = $id > 0 ? 'Product updated.' : 'Product created.'; $msgType = 'success'; }
        else { $msg = 'Error: ' . $conn->error; $msgType = 'error'; }
    }
}

if (isset($_GET['delete'])) {
    $conn->query("DELETE FROM products WHERE id=" . intval($_GET['delete']));
    $msg = 'Product deleted.'; $msgType = 'success';
}

$categories = $conn->query("SELECT * FROM categories ORDER BY name");
$suppliers  = $conn->query("SELECT * FROM suppliers ORDER BY name");
$products   = $conn->query("SELECT p.*, c.name AS cat, s.name AS sup FROM products p LEFT JOIN categories c ON p.category_id=c.id LEFT JOIN suppliers s ON p.supplier_id=s.id ORDER BY p.id DESC");

$active = 'products'; $role = 'administrator';
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Products | Admin</title>
    <link rel="stylesheet" href="../../assets/css/style.css">
</head>
<body>
<div class="app-layout">
    <?php include '../../config/sidebar.php'; ?>
    <main class="main-content">
        <div class="page-header"><h1>Product Management</h1><p>Full product catalog control</p></div>
        <?php if ($msg): ?><div class="alert alert-<?= $msgType ?>"><?= htmlspecialchars($msg) ?></div><?php endif; ?>

        <div class="card">
            <div class="card-header">
                <h3>All products</h3>
                <button class="btn btn-primary btn-sm" onclick="openModal('modalCreate')">+ New product</button>
            </div>
            <div class="search-bar">
                <input type="text" id="searchInput" placeholder="🔍 Search products...">
            </div>
            <div class="table-wrap">
                <table id="tableProds">
                    <thead><tr><th>Code</th><th>Name</th><th>Price</th><th>Stock</th><th>Category</th><th>Supplier</th><th>Actions</th></tr></thead>
                    <tbody>
                    <?php while ($p = $products->fetch_assoc()): ?>
                        <tr>
                            <td><code><?= htmlspecialchars($p['code']) ?></code></td>
                            <td><?= htmlspecialchars($p['name']) ?></td>
                            <td>Q<?= number_format($p['price'], 2) ?></td>
                            <td><span class="badge <?= $p['stock'] < 10 ? 'badge-red' : 'badge-teal' ?>"><?= $p['stock'] ?></span></td>
                            <td><?= htmlspecialchars($p['cat'] ?? '—') ?></td>
                            <td><?= htmlspecialchars($p['sup'] ?? '—') ?></td>
                            <td>
                                <button class="btn btn-secondary btn-sm" onclick="editProd(<?= htmlspecialchars(json_encode($p)) ?>)">✏️</button>
                                <a href="?delete=<?= $p['id'] ?>" class="btn btn-danger btn-sm" onclick="return confirmDelete()">🗑️</a>
                            </td>
                        </tr>
                    <?php endwhile; ?>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>

<!-- Modal Create -->
<div class="modal-overlay" id="modalCreate">
    <div class="modal">
        <div class="modal-header"><h3>New product</h3><button class="modal-close" onclick="closeModal('modalCreate')">✕</button></div>
        <form method="POST">
            <input type="hidden" name="id" value="0">
            <div class="form-grid">
                <div class="form-group"><label>Code</label><input type="text" name="code" placeholder="PRD001" required></div>
                <div class="form-group"><label>Price (Q)</label><input type="number" name="price" step="0.01" min="0.01" required></div>
                <div class="form-group full"><label>Name</label><input type="text" name="name" required></div>
                <div class="form-group full"><label>Description</label><textarea name="description"></textarea></div>
                <div class="form-group"><label>Initial stock</label><input type="number" name="stock" min="0" value="0"></div>
                <div class="form-group"><label>Category</label>
                    <select name="category_id">
                        <option value="">— None —</option>
                        <?php $categories->data_seek(0); while ($c = $categories->fetch_assoc()): ?>
                            <option value="<?= $c['id'] ?>"><?= htmlspecialchars($c['name']) ?></option>
                        <?php endwhile; ?>
                    </select>
                </div>
                <div class="form-group"><label>Supplier</label>
                    <select name="supplier_id">
                        <option value="">— None —</option>
                        <?php $suppliers->data_seek(0); while ($s = $suppliers->fetch_assoc()): ?>
                            <option value="<?= $s['id'] ?>"><?= htmlspecialchars($s['name']) ?></option>
                        <?php endwhile; ?>
                    </select>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" onclick="closeModal('modalCreate')">Cancel</button>
                <button type="submit" class="btn btn-primary">Save</button>
            </div>
        </form>
    </div>
</div>

<!-- Modal Edit -->
<div class="modal-overlay" id="modalEdit">
    <div class="modal">
        <div class="modal-header"><h3>Edit product</h3><button class="modal-close" onclick="closeModal('modalEdit')">✕</button></div>
        <form method="POST">
            <input type="hidden" name="id" id="e_id">
            <div class="form-grid">
                <div class="form-group"><label>Code</label><input type="text" name="code" id="e_code" required></div>
                <div class="form-group"><label>Price (Q)</label><input type="number" name="price" id="e_price" step="0.01" min="0.01" required></div>
                <div class="form-group full"><label>Name</label><input type="text" name="name" id="e_name" required></div>
                <div class="form-group full"><label>Description</label><textarea name="description" id="e_desc"></textarea></div>
                <div class="form-group"><label>Stock</label><input type="number" name="stock" id="e_stock" min="0"></div>
                <div class="form-group"><label>Category</label>
                    <select name="category_id" id="e_cat">
                        <option value="">— None —</option>
                        <?php $categories->data_seek(0); while ($c = $categories->fetch_assoc()): ?>
                            <option value="<?= $c['id'] ?>"><?= htmlspecialchars($c['name']) ?></option>
                        <?php endwhile; ?>
                    </select>
                </div>
                <div class="form-group"><label>Supplier</label>
                    <select name="supplier_id" id="e_sup">
                        <option value="">— None —</option>
                        <?php $suppliers->data_seek(0); while ($s = $suppliers->fetch_assoc()): ?>
                            <option value="<?= $s['id'] ?>"><?= htmlspecialchars($s['name']) ?></option>
                        <?php endwhile; ?>
                    </select>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" onclick="closeModal('modalEdit')">Cancel</button>
                <button type="submit" class="btn btn-primary">Update</button>
            </div>
        </form>
    </div>
</div>

<script src="../../assets/js/app.js"></script>
<script>
filterTable('searchInput', 'tableProds');
function editProd(p) {
    document.getElementById('e_id').value    = p.id;
    document.getElementById('e_code').value  = p.code;
    document.getElementById('e_name').value  = p.name;
    document.getElementById('e_desc').value  = p.description || '';
    document.getElementById('e_price').value = p.price;
    document.getElementById('e_stock').value = p.stock;
    document.getElementById('e_cat').value   = p.category_id || '';
    document.getElementById('e_sup').value   = p.supplier_id || '';
    openModal('modalEdit');
}
</script>
</body>
</html>
<?php $conn->close(); ?>
