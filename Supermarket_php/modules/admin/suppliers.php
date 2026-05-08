<?php
require_once '../../config/db.php';
requireRole('administrator');

$conn = getConnection();
$msg = ''; $msgType = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $id    = intval($_POST['id'] ?? 0);
    $nit   = sanitize($conn, $_POST['nit']   ?? '');
    $name  = sanitize($conn, $_POST['name']  ?? '');
    $phone = sanitize($conn, $_POST['phone'] ?? '');
    $email = sanitize($conn, $_POST['email'] ?? '');
    if (empty($nit) || empty($name)) { $msg = 'NIT and name are required.'; $msgType = 'error'; }
    else {
        if ($id > 0) $conn->query("UPDATE suppliers SET nit='$nit',name='$name',phone='$phone',email='$email' WHERE id=$id");
        else $conn->query("INSERT INTO suppliers (nit,name,phone,email) VALUES ('$nit','$name','$phone','$email')");
        $msg = 'Supplier saved.'; $msgType = 'success';
    }
}
if (isset($_GET['delete'])) {
    $id = intval($_GET['delete']);
    if (!$conn->query("DELETE FROM suppliers WHERE id=$id")) {
        $msg = 'Cannot delete: supplier has associated products.'; $msgType = 'error';
    } else { $msg = 'Supplier deleted.'; $msgType = 'success'; }
}

$suppliers = $conn->query("SELECT * FROM suppliers ORDER BY id DESC");
$active = 'suppliers'; $role = 'administrator';
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Suppliers | Admin</title>
    <link rel="stylesheet" href="../../assets/css/style.css">
</head>
<body>
<div class="app-layout">
    <?php include '../../config/sidebar.php'; ?>
    <main class="main-content">
        <div class="page-header"><h1>Supplier Management</h1><p>Manage companies and distributors</p></div>
        <?php if ($msg): ?><div class="alert alert-<?= $msgType ?>"><?= htmlspecialchars($msg) ?></div><?php endif; ?>

        <div class="card">
            <div class="card-header">
                <h3>All suppliers</h3>
                <button class="btn btn-primary btn-sm" onclick="openModal('modalCreate')">+ New supplier</button>
            </div>
            <div class="search-bar">
                <input type="text" id="searchInput" placeholder="🔍 Search suppliers...">
            </div>
            <div class="table-wrap">
                <table id="tableSuppliers">
                    <thead><tr><th>#</th><th>NIT</th><th>Name</th><th>Phone</th><th>Email</th><th>Actions</th></tr></thead>
                    <tbody>
                    <?php while ($s = $suppliers->fetch_assoc()): ?>
                        <tr>
                            <td><?= $s['id'] ?></td>
                            <td><?= htmlspecialchars($s['nit']) ?></td>
                            <td><?= htmlspecialchars($s['name']) ?></td>
                            <td><?= htmlspecialchars($s['phone'] ?? '—') ?></td>
                            <td><?= htmlspecialchars($s['email'] ?? '—') ?></td>
                            <td>
                                <button class="btn btn-secondary btn-sm" onclick="editSup(<?= htmlspecialchars(json_encode($s)) ?>)">✏️</button>
                                <a href="?delete=<?= $s['id'] ?>" class="btn btn-danger btn-sm" onclick="return confirmDelete()">🗑️</a>
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
        <div class="modal-header"><h3>New supplier</h3><button class="modal-close" onclick="closeModal('modalCreate')">✕</button></div>
        <form method="POST">
            <input type="hidden" name="id" value="0">
            <div class="form-grid">
                <div class="form-group"><label>NIT</label><input type="text" name="nit" required></div>
                <div class="form-group"><label>Phone</label><input type="text" name="phone"></div>
                <div class="form-group full"><label>Company name</label><input type="text" name="name" required></div>
                <div class="form-group full"><label>Email</label><input type="email" name="email"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" onclick="closeModal('modalCreate')">Cancel</button>
                <button type="submit" class="btn btn-primary">Save</button>
            </div>
        </form>
    </div>
</div>

<div class="modal-overlay" id="modalEdit">
    <div class="modal">
        <div class="modal-header"><h3>Edit supplier</h3><button class="modal-close" onclick="closeModal('modalEdit')">✕</button></div>
        <form method="POST">
            <input type="hidden" name="id" id="e_id">
            <div class="form-grid">
                <div class="form-group"><label>NIT</label><input type="text" name="nit" id="e_nit" required></div>
                <div class="form-group"><label>Phone</label><input type="text" name="phone" id="e_phone"></div>
                <div class="form-group full"><label>Company name</label><input type="text" name="name" id="e_name" required></div>
                <div class="form-group full"><label>Email</label><input type="email" name="email" id="e_email"></div>
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
filterTable('searchInput', 'tableSuppliers');
function editSup(s) {
    document.getElementById('e_id').value    = s.id;
    document.getElementById('e_nit').value   = s.nit;
    document.getElementById('e_name').value  = s.name;
    document.getElementById('e_phone').value = s.phone || '';
    document.getElementById('e_email').value = s.email || '';
    openModal('modalEdit');
}
</script>
</body>
</html>
<?php $conn->close(); ?>
