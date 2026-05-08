<?php
require_once '../../config/db.php';
requireRole('customer');

$conn = getConnection();
$uid = $_SESSION['user_id'];
$msg = ''; $msgType = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $name     = sanitize($conn, $_POST['name']  ?? '');
    $email    = sanitize($conn, $_POST['email'] ?? '');
    $phone    = sanitize($conn, $_POST['phone'] ?? '');
    $password = $_POST['password'] ?? '';

    if (empty($name) || empty($email)) {
        $msg = 'Name and email are required.'; $msgType = 'error';
    } elseif (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        $msg = 'Invalid email format.'; $msgType = 'error';
    } elseif (!empty($password) && strlen($password) < 6) {
        $msg = 'Password must be at least 6 characters.'; $msgType = 'error';
    } else {
        $passClause = !empty($password) ? ", password='" . md5($password) . "'" : '';
        $conn->query("UPDATE users SET name='$name', email='$email', phone='$phone' $passClause WHERE id=$uid");
        $_SESSION['name'] = $name;
        $msg = 'Profile updated successfully.'; $msgType = 'success';
    }
}

$user = $conn->query("SELECT * FROM users WHERE id=$uid")->fetch_assoc();
$conn->close();
$active = 'profile'; $role = 'customer';
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>My Profile | Customer</title>
    <link rel="stylesheet" href="../../assets/css/style.css">
</head>
<body>
<div class="app-layout">
    <?php include '../../config/sidebar.php'; ?>
    <main class="main-content">
        <div class="page-header"><h1>My Profile</h1><p>View and update your personal information</p></div>
        <?php if ($msg): ?><div class="alert alert-<?= $msgType ?>"><?= htmlspecialchars($msg) ?></div><?php endif; ?>

        <div class="card" style="max-width:500px">
            <form method="POST">
                <div class="form-group">
                    <label>Full name</label>
                    <input type="text" name="name" value="<?= htmlspecialchars($user['name']) ?>" required>
                </div>
                <div class="form-group">
                    <label>Email address</label>
                    <input type="email" name="email" value="<?= htmlspecialchars($user['email']) ?>" required>
                </div>
                <div class="form-group">
                    <label>Phone</label>
                    <input type="text" name="phone" value="<?= htmlspecialchars($user['phone'] ?? '') ?>">
                </div>
                <hr style="border:none;border-top:1px solid #f0f0f4;margin:20px 0">
                <div class="form-group">
                    <label>New password <span style="color:var(--muted);font-weight:400">(leave blank to keep current)</span></label>
                    <input type="password" name="password" placeholder="At least 6 characters">
                </div>
                <button type="submit" class="btn btn-primary">💾 Save changes</button>
            </form>
        </div>
    </main>
</div>
<script src="../../assets/js/app.js"></script>
</body>
</html>
