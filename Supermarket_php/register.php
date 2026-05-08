<?php
require_once 'config/db.php';

if (isLoggedIn()) {
    header("Location: modules/{$_SESSION['role']}/dashboard.php");
    exit();
}

$error = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $conn     = getConnection();
    $name     = sanitize($conn, $_POST['name']             ?? '');
    $email    = sanitize($conn, $_POST['email']            ?? '');
    $phone    = sanitize($conn, $_POST['phone']            ?? '');
    $password = $_POST['password']         ?? '';
    $confirm  = $_POST['confirm_password'] ?? '';

    if (empty($name) || empty($email) || empty($password)) {
        $error = 'Name, email and password are required.';
    } elseif (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        $error = 'Invalid email format.';
    } elseif (strlen($password) < 6) {
        $error = 'Password must be at least 6 characters.';
    } elseif ($password !== $confirm) {
        $error = 'Passwords do not match.';
    } else {
        $check = $conn->query("SELECT id FROM users WHERE email = '$email'");
        if ($check->num_rows > 0) {
            $error = 'An account with that email already exists.';
        } else {
            $pass_md5 = md5($password);
            // New registrations are customers (role_id = 3)
            $sql = "INSERT INTO users (name, email, phone, password, role_id)
                    VALUES ('$name', '$email', '$phone', '$pass_md5', 3)";
            if ($conn->query($sql)) {
                header('Location: index.php?registered=1');
                exit();
            } else {
                $error = 'Registration failed. Please try again.';
            }
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
    <title>Register | Supermarket</title>
    <link rel="stylesheet" href="assets/css/style.css">
</head>
<body>
<div class="auth-wrapper">
    <div class="auth-left">
        <div class="auth-brand">
            <span class="logo-icon">🛍️</span>
            <h1>Supermarket</h1>
            <p>Create your account to track your purchases and manage your personal profile.</p>
        </div>
    </div>
    <div class="auth-right">
        <div class="auth-form-box">
            <h2>Create account</h2>
            <p class="subtitle">Fill in the form below to register as a customer</p>

            <?php if ($error): ?>
                <div class="alert alert-error"><?= htmlspecialchars($error) ?></div>
            <?php endif; ?>

            <form method="POST">
                <div class="form-group">
                    <label>Full name</label>
                    <input type="text" name="name" placeholder="Your full name"
                           value="<?= htmlspecialchars($_POST['name'] ?? '') ?>" required>
                </div>
                <div class="form-group">
                    <label>Email address</label>
                    <input type="email" name="email" placeholder="you@example.com"
                           value="<?= htmlspecialchars($_POST['email'] ?? '') ?>" required>
                </div>
                <div class="form-group">
                    <label>Phone <span style="color:var(--muted);font-weight:400">(optional)</span></label>
                    <input type="text" name="phone" placeholder="55001122"
                           value="<?= htmlspecialchars($_POST['phone'] ?? '') ?>">
                </div>
                <div class="form-group">
                    <label>Password</label>
                    <input type="password" name="password" placeholder="At least 6 characters" required>
                </div>
                <div class="form-group">
                    <label>Confirm password</label>
                    <input type="password" name="confirm_password" placeholder="Repeat your password" required>
                </div>
                <button type="submit" class="btn btn-primary btn-full">Create account →</button>
            </form>

            <div class="auth-link">
                Already have an account? <a href="index.php">Sign in</a>
            </div>
        </div>
    </div>
</div>
<script src="assets/js/app.js"></script>
</body>
</html>
