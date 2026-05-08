<?php
require_once '../../config/db.php';
requireRole('employee');

$conn = getConnection();
$msg = ''; $msgType = '';
$saleId = null;

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['cart_data'])) {
    $customer_id = intval($_POST['customer_id'] ?? 0);
    $cart        = json_decode($_POST['cart_data'] ?? '[]', true);

    if (!$customer_id) { $msg = 'Please select a customer.'; $msgType = 'error'; }
    elseif (empty($cart)) { $msg = 'The cart is empty.'; $msgType = 'error'; }
    else {
        $subtotal = 0;
        foreach ($cart as $item) { $subtotal += $item['price'] * $item['qty']; }
        $tax   = round($subtotal * 0.12, 2);
        $total = round($subtotal + $tax, 2);
        $emp   = $_SESSION['user_id'];

        $conn->begin_transaction();
        try {
            $conn->query("INSERT INTO sales (customer_id,employee_id,subtotal,tax,total) VALUES ($customer_id,$emp,$subtotal,$tax,$total)");
            $saleId = $conn->insert_id;
            foreach ($cart as $item) {
                $pid = intval($item['id']);
                $qty = intval($item['qty']);
                $pu  = floatval($item['price']);
                $sub = round($pu * $qty, 2);
                $conn->query("INSERT INTO sale_details (sale_id,product_id,quantity,unit_price,subtotal) VALUES ($saleId,$pid,$qty,$pu,$sub)");
                $conn->query("UPDATE products SET stock = stock - $qty WHERE id=$pid");
            }
            $conn->commit();
            $msg = 'Sale registered successfully.'; $msgType = 'success';
        } catch (Exception $e) {
            $conn->rollback();
            $msg = 'Error registering sale. Please try again.'; $msgType = 'error';
            $saleId = null;
        }
    }
}

$customers = $conn->query("SELECT id, name FROM users WHERE role_id=3 AND active=1 ORDER BY name");
$products  = $conn->query("SELECT id, name, price, stock FROM products WHERE stock > 0 ORDER BY name");

$receipt = null;
if ($saleId) {
    $receipt['sale']   = $conn->query("SELECT s.*, u.name AS customer FROM sales s JOIN users u ON s.customer_id=u.id WHERE s.id=$saleId")->fetch_assoc();
    $receipt['detail'] = $conn->query("SELECT sd.*, p.name AS prod FROM sale_details sd JOIN products p ON sd.product_id=p.id WHERE sd.sale_id=$saleId");
}

$active = 'sales'; $role = 'employee';
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Register Sale | Employee</title>
    <link rel="stylesheet" href="../../assets/css/style.css">
</head>
<body>
<div class="app-layout">
    <?php include '../../config/sidebar.php'; ?>
    <main class="main-content">
        <div class="page-header"><h1>Register Sale</h1><p>Select customer and add products to the cart</p></div>

        <?php if ($msg): ?><div class="alert alert-<?= $msgType ?>"><?= htmlspecialchars($msg) ?></div><?php endif; ?>

        <?php if ($receipt): ?>
        <div class="card">
            <div class="card-header">
                <h3>🧾 Sale Receipt</h3>
                <button class="btn btn-secondary btn-sm" onclick="window.print()">🖨️ Print</button>
            </div>
            <div class="receipt">
                <div class="receipt-header">
                    <h2>🛍️ Supermarket</h2>
                    <p class="receipt-meta">Sale #<?= $receipt['sale']['id'] ?> — <?= date('m/d/Y H:i', strtotime($receipt['sale']['date'])) ?></p>
                    <p class="receipt-meta">Customer: <strong><?= htmlspecialchars($receipt['sale']['customer']) ?></strong></p>
                </div>
                <?php while ($d = $receipt['detail']->fetch_assoc()): ?>
                <div class="receipt-row">
                    <span><?= htmlspecialchars($d['prod']) ?> × <?= $d['quantity'] ?></span>
                    <span>Q<?= number_format($d['subtotal'], 2) ?></span>
                </div>
                <?php endwhile; ?>
                <hr class="receipt-divider">
                <div class="receipt-row"><span>Subtotal</span><span>Q<?= number_format($receipt['sale']['subtotal'], 2) ?></span></div>
                <div class="receipt-row"><span>Tax (12%)</span><span>Q<?= number_format($receipt['sale']['tax'], 2) ?></span></div>
                <hr class="receipt-divider">
                <div class="receipt-row receipt-total"><span>TOTAL</span><span>Q<?= number_format($receipt['sale']['total'], 2) ?></span></div>
            </div>
        </div>
        <?php endif; ?>

        <div class="card">
            <div class="card-header"><h3>New sale</h3></div>
            <form method="POST">
                <div class="form-group">
                    <label>Customer</label>
                    <select name="customer_id" required>
                        <option value="">— Select customer —</option>
                        <?php while ($c = $customers->fetch_assoc()): ?>
                            <option value="<?= $c['id'] ?>"><?= htmlspecialchars($c['name']) ?></option>
                        <?php endwhile; ?>
                    </select>
                </div>

                <div style="display:flex;gap:12px;align-items:flex-end;margin-bottom:16px;flex-wrap:wrap">
                    <div class="form-group" style="flex:1;min-width:200px;margin:0">
                        <label>Product</label>
                        <select id="prod_select">
                            <option value="">— Select product —</option>
                            <?php while ($p = $products->fetch_assoc()): ?>
                                <option value="<?= $p['id'] ?>"
                                    data-price="<?= $p['price'] ?>"
                                    data-stock="<?= $p['stock'] ?>">
                                    <?= htmlspecialchars($p['name']) ?> — Q<?= number_format($p['price'],2) ?> (stock: <?= $p['stock'] ?>)
                                </option>
                            <?php endwhile; ?>
                        </select>
                    </div>
                    <div class="form-group" style="width:110px;margin:0">
                        <label>Quantity</label>
                        <input type="number" id="prod_qty" min="1" value="1">
                    </div>
                    <button type="button" class="btn btn-teal" onclick="addProduct()">+ Add</button>
                </div>

                <div class="table-wrap" style="margin-bottom:20px">
                    <table>
                        <thead><tr><th>Product</th><th>Qty</th><th>Unit price</th><th>Subtotal</th><th></th></tr></thead>
                        <tbody id="cart_body">
                            <tr><td colspan="5" style="text-align:center;color:#aaa;padding:20px">No products added yet</td></tr>
                        </tbody>
                    </table>
                </div>

                <div style="text-align:right;margin-bottom:20px;line-height:2">
                    <div>Subtotal: <strong id="subtotal_display">Q0.00</strong></div>
                    <div>Tax (12%): <strong id="tax_display">Q0.00</strong></div>
                    <div style="font-size:1.2rem">Total: <strong id="total_display" style="color:var(--teal-dark)">Q0.00</strong></div>
                </div>

                <input type="hidden" name="cart_data" id="cart_data" value="[]">
                <button type="submit" class="btn btn-primary">✅ Complete sale</button>
            </form>
        </div>
    </main>
</div>
<script src="../../assets/js/app.js"></script>
</body>
</html>
<?php $conn->close(); ?>
