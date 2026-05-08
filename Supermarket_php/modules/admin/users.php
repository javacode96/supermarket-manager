<?php
require_once '../../config/db.php';
requireRole('administrator');

$conn = getConnection();
$msg = ''; $msgType = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $id       = intval($_POST['id'] ?? 0);
    $name     = sanitize($conn, $_POST['name']    ?? '');
    $email    = sanitize($conn, $_POST['email']   ?? '');
    $phone    = sanitize($conn, $_POST['phone']   ?? '');
    $role_id  = intval($_POST['role_id']  ?? 3);
    $active   = intval($_POST['active']   ?? 1);
    $password = $_POST['password'] ?? '';

    if (empty($name) || empty($email)) {
        $msg = 'Name and email are required.'; $msgType = 'error';
    } else {
        if ($id > 0) {
            $passClause = !empty($password) ? ", password='" . md5($password) . "'" : '';
            $conn->query("UPDATE users SET name='$name', email='$email', phone='$phone', role_id=$role_id, active=$active $passClause WHERE id=$id");
            $msg = 'User updated successfully.'; $msgType = 'success';
        } else {
            if (empty($password)) { $msg = 'Password is required.'; $msgType = 'error'; }
            else {
                $conn->query("INSERT INTO users (name,email,phone,password,role_id,active) VALUES ('$name','$email','$phone','".md5($password)."',$role_id,$active)");
                $msg = 'User created successfully.'; $msgType = 'success';
            }
        }
    }
}

if (isset($_GET['delete'])) {
    $id = intval($_GET['delete']);
    if ($id !== $_SESSION['user_id']) {
        $conn->query("DELETE FROM users WHERE id=$id");
        $msg = 'User deleted.'; $msgType = 'success';
    } else {
        $msg = 'You cannot delete your own account.'; $msgType = 'error';
    }
}

$roles = $conn->query("SELECT * FROM roles");
$users = $conn->query("SELECT u.*, r.name AS role_name FROM users u JOIN roles r ON u.role_id=r.id ORDER BY u.id DESC");

$active = 'users'; $role = 'administrator';
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Users | Admin</title>
    <link rel="stylesheet" href="../../assets/css/style.css">
</head>
<body>
<div class="app-layout">
    <?php include '../../config/sidebar.php'; ?>
    <main class="main-content">
        <div class="page-header"><h1>User Management</h1><p>Manage employees and customers</p></div>
        <?php if ($msg): ?><div class="alert alert-<?= $msgType ?>"><?= htmlspecialchars($msg) ?></div><?php endif; ?>

        <div class="card">
            <div class="card-header">
                <h3>All users</h3>
                <button class="btn btn-primary btn-sm" onclick="openModal('modalCreate')">+ New user</button>
            </div>
            <div class="search-bar">
                <input type="text" id="searchInput" placeholder="🔍 Search users...">
            </div>
            <div class="table-wrap">
                <table id="tableUsers">
                    <thead><tr><th>#</th><th>Name</th><th>Email</th><th>Phone</th><th>Role</th><th>Status</th><th>Actions</th></tr></thead>
                    <tbody>
                    <?php while ($u = $users->fetch_assoc()): ?>
                        <tr>
                            <td><?= $u['id'] ?></td>
                            <td><?= htmlspecialchars($u['name']) ?></td>
                            <td><?= htmlspecialchars($u['email']) ?></td>
                            <td><?= htmlspecialchars($u['phone'] ?? '—') ?></td>
                            <td><span class="badge badge-teal"><?= $u['role_name'] ?></span></td>
                            <td><span class="badge <?= $u['active'] ? 'badge-teal' : 'badge-red' ?>"><?= $u['active'] ? 'Active' : 'Inactive' ?></span></td>
                            <td>
                                <button class="btn btn-secondary btn-sm"
                                    onclick="editUser(<?= htmlspecialchars(json_encode($u)) ?>)">✏️ Edit</button>
                                <?php if ($u['id'] !== $_SESSION['user_id']): ?>
                                    <a href="?delete=<?= $u['id'] ?>" class="btn btn-danger btn-sm"
                                       onclick="return confirmDelete()">🗑️</a>
                                <?php endif; ?>
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
        <div class="modal-header"><h3>New user</h3><button class="modal-close" onclick="closeModal('modalCreate')">✕</button></div>
        <form method="POST">
            <input type="hidden" name="id" value="0">
            <div class="form-grid">
                <div class="form-group full"><label>Full name</label><input type="text" name="name" required></div>
                <div class="form-group"><label>Email</label><input type="email" name="email" required></div>
                <div class="form-group"><label>Phone</label><input type="text" name="phone"></div>
                <div class="form-group"><label>Password</label><input type="password" name="password" required></div>
                <div class="form-group"><label>Role</label>
                    <select name="role_id">
                        <?php $roles->data_seek(0); while ($r = $roles->fetch_assoc()): ?>
                            <option value="<?= $r['id'] ?>"><?= $r['name'] ?></option>
                        <?php endwhile; ?>
                    </select>
                </div>
                <div class="form-group"><label>Status</label>
                    <select name="active">
                        <option value="1">Active</option>
                        <option value="0">Inactive</option>
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
        <div class="modal-header"><h3>Edit user</h3><button class="modal-close" onclick="closeModal('modalEdit')">✕</button></div>
        <form method="POST">
            <input type="hidden" name="id" id="e_id">
            <div class="form-grid">
                <div class="form-group full"><label>Full name</label><input type="text" name="name" id="e_name" required></div>
                <div class="form-group"><label>Email</label><input type="email" name="email" id="e_email" required></div>
                <div class="form-group"><label>Phone</label><input type="text" name="phone" id="e_phone"></div>
                <div class="form-group full"><label>New password <span style="color:var(--muted);font-weight:400">(leave blank to keep current)</span></label><input type="password" name="password"></div>
                <div class="form-group"><label>Role</label>
                    <select name="role_id" id="e_role_id">
                        <?php $roles->data_seek(0); while ($r = $roles->fetch_assoc()): ?>
                            <option value="<?= $r['id'] ?>"><?= $r['name'] ?></option>
                        <?php endwhile; ?>
                    </select>
                </div>
                <div class="form-group"><label>Status</label>
                    <select name="active" id="e_active">
                        <option value="1">Active</option>
                        <option value="0">Inactive</option>
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
filterTable('searchInput', 'tableUsers');
function editUser(u) {
    document.getElementById('e_id').value      = u.id;
    document.getElementById('e_name').value    = u.name;
    document.getElementById('e_email').value   = u.email;
    document.getElementById('e_phone').value   = u.phone || '';
    document.getElementById('e_role_id').value = u.role_id;
    document.getElementById('e_active').value  = u.active;
    openModal('modalEdit');
}
</script>
</body>
</html>
<?php $conn->close(); ?>
