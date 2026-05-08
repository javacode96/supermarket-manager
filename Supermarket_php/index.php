<?php
require_once 'config/db.php';

if (isLoggedIn()) {
    header("Location: modules/{$_SESSION['role']}/dashboard.php");
    exit();
}

$error = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $conn     = getConnection();
    $email    = sanitize($conn, $_POST['email']    ?? '');
    $password = sanitize($conn, $_POST['password'] ?? '');

    if (empty($email) || empty($password)) {
        $error = 'Please fill in all fields.';
    } else {
        $pass_md5 = md5($password);
        $sql = "SELECT u.id, u.name, u.email, r.name AS role
                FROM users u
                JOIN roles r ON u.role_id = r.id
                WHERE u.email = '$email' AND u.password = '$pass_md5' AND u.active = 1";
        $result = $conn->query($sql);

        if ($result && $result->num_rows === 1) {
            $user = $result->fetch_assoc();
            $_SESSION['user_id'] = $user['id'];
            $_SESSION['name']    = $user['name'];
            $_SESSION['email']   = $user['email'];
            $_SESSION['role']    = $user['role'];
            header("Location: modules/{$user['role']}/dashboard.php");
            exit();
        } else {
            $error = 'Invalid credentials or inactive account.';
        }
    }
    $conn->close();
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign In | Supermarket</title>
    <link rel="stylesheet" href="assets/css/style.css">
</head>
<body>
<div class="auth-wrapper">
    <div class="auth-left">
        <div class="auth-brand">
            <span class="logo-icon">🛍️</span>
            <h1>Supermarket</h1>
            <p>Complete management system for products, sales, and users.</p>
        </div>
    </div>
    <div class="auth-right">
        <div class="auth-form-box">
            <h2>Welcome back</h2>
            <p class="subtitle">Enter your credentials to continue</p>

            <?php if ($error): ?>
                <div class="alert alert-error"><?= htmlspecialchars($error) ?></div>
            <?php endif; ?>
            <?php if (isset($_GET['registered'])): ?>
                <div class="alert alert-success">Account created! You can now sign in.</div>
            <?php endif; ?>
            <?php if (isset($_GET['error']) && $_GET['error'] === 'access_denied'): ?>
                <div class="alert alert-error">You do not have permission to access that section.</div>
            <?php endif; ?>

            <form method="POST">
                <div class="form-group">
                    <label>Email address</label>
                    <input type="email" name="email" placeholder="you@example.com"
                           value="<?= htmlspecialchars($_POST['email'] ?? '') ?>" required>
                </div>
                <div class="form-group">
                    <label>Password</label>
                    <input type="password" name="password" placeholder="••••••••" required>
                </div>
                <button type="submit" class="btn btn-primary btn-full">Sign in →</button>
            </form>

            <div class="auth-link">
                Don't have an account? <a href="register.php">Register here</a>
            </div>

            <div class="demo-box">
                <strong>Demo credentials:</strong><br>
                Admin: admin@supermarket.com / admin123<br>
                Employee: employee@supermarket.com / employee123<br>
                Customer: customer@supermarket.com / customer123
            </div>
        </div>
    </div>
</div>
<script src="assets/js/app.js"></script>
</body>
</html>
