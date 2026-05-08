// ============================================
// SUPERMARKET - Global JavaScript
// ============================================

function openModal(id) {
    document.getElementById(id).classList.add('active');
}
function closeModal(id) {
    document.getElementById(id).classList.remove('active');
    const form = document.querySelector('#' + id + ' form');
    if (form) form.reset();
}

document.addEventListener('click', function(e) {
    if (e.target.classList.contains('modal-overlay')) {
        e.target.classList.remove('active');
    }
});

function filterTable(inputId, tableId) {
    const input = document.getElementById(inputId);
    if (!input) return;
    input.addEventListener('keyup', function() {
        const val = this.value.toLowerCase();
        document.querySelectorAll('#' + tableId + ' tbody tr').forEach(row => {
            row.style.display = row.textContent.toLowerCase().includes(val) ? '' : 'none';
        });
    });
}

function confirmDelete(msg) {
    return confirm(msg || 'Are you sure you want to delete this record?');
}

document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.alert').forEach(a => {
        setTimeout(() => { a.style.transition = 'opacity 0.5s'; a.style.opacity = '0'; }, 3500);
    });
});

// ===== SHOPPING CART (Sales module) =====
let cart = [];

function addProduct() {
    const sel  = document.getElementById('prod_select');
    const qty  = parseInt(document.getElementById('prod_qty').value);

    if (!sel.value || !qty || qty < 1) {
        alert('Please select a product and enter a valid quantity.');
        return;
    }

    const option = sel.options[sel.selectedIndex];
    const price  = parseFloat(option.dataset.price);
    const stock  = parseInt(option.dataset.stock);
    const name   = option.text.split(' — ')[0];

    if (qty > stock) {
        alert('Insufficient stock. Available: ' + stock);
        return;
    }

    const existing = cart.find(i => i.id === sel.value);
    if (existing) {
        existing.qty += qty;
        if (existing.qty > stock) existing.qty = stock;
    } else {
        cart.push({ id: sel.value, name, price, qty, stock });
    }
    renderCart();
}

function removeItem(id) {
    cart = cart.filter(i => i.id !== id);
    renderCart();
}

function renderCart() {
    const tbody     = document.getElementById('cart_body');
    const subtotalEl = document.getElementById('subtotal_display');
    const taxEl      = document.getElementById('tax_display');
    const totalEl    = document.getElementById('total_display');
    const hiddenInput = document.getElementById('cart_data');
    if (!tbody) return;

    tbody.innerHTML = '';
    let subtotal = 0;

    if (cart.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" style="text-align:center;color:#aaa;padding:20px">No products added yet</td></tr>';
    } else {
        cart.forEach(item => {
            const lineTotal = item.price * item.qty;
            subtotal += lineTotal;
            tbody.innerHTML += `
                <tr>
                    <td>${item.name}</td>
                    <td>${item.qty}</td>
                    <td>Q${item.price.toFixed(2)}</td>
                    <td>Q${lineTotal.toFixed(2)}</td>
                    <td><button type="button" class="btn btn-danger btn-sm" onclick="removeItem('${item.id}')">✕</button></td>
                </tr>`;
        });
    }

    const tax   = subtotal * 0.12;
    const total = subtotal + tax;

    if (subtotalEl) subtotalEl.textContent = 'Q' + subtotal.toFixed(2);
    if (taxEl)      taxEl.textContent      = 'Q' + tax.toFixed(2);
    if (totalEl)    totalEl.textContent    = 'Q' + total.toFixed(2);
    if (hiddenInput) hiddenInput.value     = JSON.stringify(cart);
}
