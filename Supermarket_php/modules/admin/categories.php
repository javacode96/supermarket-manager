<?php
require_once '../../config/db.php';
requireRole('administrator');

$conn = getConnection();
$msg = ''; $msgType = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $id   = intval($_POST['id'] ?? 0);
    $name = sanitize($conn, $_POST['name'] ?? '');
    $desc = sanitize($conn, $_POST['description'] ?? '');
    if (empty($name)) { $msg = 'Name is required.'; $msgType = 'error'; }
    else {
        if ($id > 0) $conn->query("UPDATE categories SET name='$name',description='$desc' WHERE id=$id");
        else $conn->query("INSERT INTO categories (name,description) VALUES ('$name','$desc')");
        $msg = 'Category saved.'; $msgType = 'success';
    }
}
if (isset($_GET['delete'])) {
    $id = intval($_GET['delete']);
    if (!$conn->query("DELETE FROM categories WHERE id=$id")) {
        $msg = 'Cannot delete: category has associated products.'; $msgType = 'error';
    } else { $msg = 'Category deleted.'; $msgType = 'success'; }
}

$cats = $conn->query("SELECT c.*, COUNT(p.id) AS total FROM categories c LEFT JOIN products p ON p.category_id=c.id GROUP BY c.id ORDER BY c.id DESC");
$active = 'categories'; $role = 'administrator';
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Categories | Admin</title>
    <link rel="stylesheet" href="../../assets/css/style.css">
</head>
<body>
<div class="app-layout">
    <?php include '../../config/sidebar.php'; ?>
    <main class="main-content">
        <div class="page-header"><h1>Category Management</h1><p>Organize products by type</p></div>
        <?php if ($msg): ?><div class="alert alert-<?= $msgType ?>"><?= htmlspecialchars($msg) ?></div><?php endif; ?>

        <div class="card">
            <div class="card-header">
                <h3>All categories</h3>
                <button class="btn btn-primary btn-sm" onclick="openModal('modalCreate')">+ New category</button>
            </div>
            <div class="table-wrap">
                <table>
                    <thead><tr><th>#</th><th>Name</th><th>Description</th><th>Products</th><th>Actions</th></tr></thead>
                    <tbody>
                    <?php while ($c = $cats->fetch_assoc()): ?>
                        <tr>
                            <td><?= $c['id'] ?></td>
                            <td><?= htmlspecialchars($c['name']) ?></td>
                            <td><?= htmlspecialchars($c['description'] ?? '—') ?></td>
                            <td><span class="badge badge-teal"><?= $c['total'] ?></span></td>
                            <td>
                                <button class="btn btn-secondary btn-sm" onclick="editCat(<?= htmlspecialchars(json_encode($c)) ?>)">✏️</button>
                                <a href="?delete=<?= $c['id'] ?>" class="btn btn-danger btn-sm" onclick="return confirmDelete()">🗑️</a>
                            </td>
                        </tr>
                    <?php endwhile; ?>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>

<div class="modal-overlay" id="modalCreate">
    <div class="modal">
        <div class="modal-header"><h3>New category</h3><button class="modal-close" onclick="closeModal('modalCreate')">✕</button></div>
        <form method="POST">
            <input type="hidden" name="id" value="0">
            <div class="form-group"><label>Name</label><input type="text" name="name" required></div>
            <div class="form-group"><label>Description</label><textarea name="description"></textarea></div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" onclick="closeModal('modalCreate')">Cancel</button>
                <button type="submit" class="btn btn-primary">Save</button>
            </div>
        </form>
    </div>
</div>

<div class="modal-overlay" id="modalEdit">
    <div class="modal">
        <div class="modal-header"><h3>Edit category</h3><button class="modal-close" onclick="closeModal('modalEdit')">✕</button></div>
        <form method="POST">
            <input type="hidden" name="id" id="e_id">
            <div class="form-group"><label>Name</label><input type="text" name="name" id="e_name" required></div>
            <div class="form-group"><label>Description</label><textarea name="description" id="e_desc"></textarea></div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" onclick="closeModal('modalEdit')">Cancel</button>
                <button type="submit" class="btn btn-primary">Update</button>
            </div>
        </form>
    </div>
</div>

<script src="../../assets/js/app.js"></script>
<script>
function editCat(c) {
    document.getElementById('e_id').value   = c.id;
    document.getElementById('e_name').value = c.name;
    document.getElementById('e_desc').value = c.description || '';
    openModal('modalEdit');
}
</script>
</body>
</html>
<?php $conn->close(); ?>
