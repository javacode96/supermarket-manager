<?php
// config/sidebar.php
// Expects: $active (string), $role (string)

$menus = [
    'administrator' => [
        ['key' => 'dashboard',  'label' => 'Dashboard',       'icon' => '📊', 'href' => 'dashboard.php'],
        ['key' => 'users',      'label' => 'Users',           'icon' => '👥', 'href' => 'users.php'],
        ['key' => 'products',   'label' => 'Products',        'icon' => '🛒', 'href' => 'products.php'],
        ['key' => 'categories', 'label' => 'Categories',      'icon' => '🗂️',  'href' => 'categories.php'],
        ['key' => 'suppliers',  'label' => 'Suppliers',       'icon' => '📦', 'href' => 'suppliers.php'],
    ],
    'employee' => [
        ['key' => 'dashboard',  'label' => 'Dashboard',       'icon' => '📊', 'href' => 'dashboard.php'],
        ['key' => 'sales',      'label' => 'Register Sale',   'icon' => '💳', 'href' => 'sales.php'],
        ['key' => 'products',   'label' => 'Products',        'icon' => '🛒', 'href' => 'products.php'],
    ],
    'customer' => [
        ['key' => 'dashboard',  'label' => 'Home',            'icon' => '🏠', 'href' => 'dashboard.php'],
        ['key' => 'profile',    'label' => 'My Profile',      'icon' => '👤', 'href' => 'profile.php'],
        ['key' => 'history',    'label' => 'Purchase History','icon' => '🧾', 'href' => 'history.php'],
    ],
];

$currentMenu = $menus[$role] ?? [];
$roleLabels  = [
    'administrator' => 'Administrator',
    'employee'      => 'Employee',
    'customer'      => 'Customer',
];
?>
<nav class="sidebar">
    <div class="sidebar-brand">
        <span class="brand-icon">🛍️</span>
        <div class="sidebar-brand-text">
            <h2>Supermarket</h2>
            <p><?= $roleLabels[$role] ?? $role ?></p>
        </div>
    </div>
    <div class="sidebar-nav">
        <div class="nav-label">Navigation</div>
        <?php foreach ($currentMenu as $item): ?>
            <a href="<?= $item['href'] ?>" class="<?= ($active === $item['key']) ? 'active' : '' ?>">
                <span class="icon"><?= $item['icon'] ?></span>
                <?= $item['label'] ?>
            </a>
        <?php endforeach; ?>
    </div>
    <div class="sidebar-footer">
        <div class="user-info">
            <div class="user-avatar">👤</div>
            <div class="user-details">
                <p><?= htmlspecialchars($_SESSION['name']) ?></p>
                <span><?= $role ?></span>
            </div>
        </div>
        <a href="../../logout.php">⬅ Sign out</a>
    </div>
</nav>
