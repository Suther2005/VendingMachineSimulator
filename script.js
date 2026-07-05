// Initial State based on the Java Application
let products = [
    { id: 1, name: "Coke", price: 20, stock: 5, icon: "🥤" },
    { id: 2, name: "Pepsi", price: 35, stock: 3, icon: "🥤" },
    { id: 3, name: "Water", price: 20, stock: 8, icon: "💧" },
    { id: 4, name: "Chips", price: 25, stock: 6, icon: "🥔" },
    { id: 5, name: "Chocolate", price: 40, stock: 3, icon: "🍫" },
    { id: 6, name: "Energy Drink", price: 50, stock: 4, icon: "⚡" }
];

let totalSales = 0.0;
let currentInput = "";
let selectedProduct = null;
let selectedMethod = "";

// DOM Elements
const productGrid = document.getElementById('product-grid');
const digitalScreen = document.getElementById('digital-screen');
const paymentPanel = document.getElementById('payment-panel');
const adminPanel = document.getElementById('admin-panel');
const dispenserDoor = document.getElementById('dispenser-door');

const cashModal = document.getElementById('cash-modal');
const receivedModal = document.getElementById('received-modal');

// Initialize UI
function renderProducts() {
    productGrid.innerHTML = "";
    products.forEach(p => {
        const card = document.createElement('div');
        card.className = `product-card ${p.stock === 0 ? 'out-of-stock' : ''}`;
        card.innerHTML = `
            <div class="product-icon">${p.icon}</div>
            <div class="product-name">${p.name}</div>
            <div class="product-price">₹${p.price}</div>
            <div class="product-id-badge">ID: ${p.id}</div>
        `;
        productGrid.appendChild(card);
    });
}

function updateScreen(msg, isError = false) {
    digitalScreen.innerHTML = msg;
    if (isError) {
        digitalScreen.classList.add('error');
        setTimeout(() => digitalScreen.classList.remove('error'), 1500);
    }
}

// Numpad Logic
document.querySelectorAll('.num-btn').forEach(btn => {
    btn.addEventListener('click', () => {
        if (btn.id === 'btn-clear') {
            currentInput = "";
            updateScreen("Welcome! <br/> Select a product.");
            paymentPanel.classList.remove('active');
            selectedProduct = null;
            adminPanel.classList.add('hidden');
        } else if (btn.id === 'btn-enter') {
            processSelection();
        } else {
            currentInput += btn.dataset.val;
            updateScreen(`ID: ${currentInput}<br/>Press ENT`);
        }
    });
});

function processSelection() {
    const id = parseInt(currentInput);
    currentInput = ""; // Reset input

    if (id === 999) {
        // Admin Mode
        updateScreen("ADMIN MODE<br/>UNLOCKED");
        adminPanel.classList.remove('hidden');
        document.getElementById('total-sales').innerText = `₹${totalSales}`;
        paymentPanel.classList.remove('active');
        return;
    }

    const product = products.find(p => p.id === id);

    if (!product) {
        updateScreen("INVALID ID", true);
        return;
    }

    if (product.stock <= 0) {
        updateScreen(`${product.name} IS<br/>OUT OF STOCK`, true);
        return;
    }

    selectedProduct = product;
    updateScreen(`${product.name} - ₹${product.price}<br/>Select Payment`);
    paymentPanel.classList.add('active');
    adminPanel.classList.add('hidden');
}

// Payment Logic
document.querySelectorAll('.pay-btn').forEach(btn => {
    btn.addEventListener('click', () => {
        if (!selectedProduct) return;
        selectedMethod = btn.dataset.pay;
        
        if (selectedMethod === "Card") {
            processPayment(selectedMethod, selectedProduct.price);
        } else {
            // Both Cash and UPI require amount entry
            const title = document.getElementById('modal-title');
            const submitBtn = document.getElementById('btn-submit-cash');
            
            if (selectedMethod === "UPI") {
                title.innerText = "Scan & Enter UPI Amount";
                submitBtn.innerText = "Pay";
            } else {
                title.innerText = "Insert Cash";
                submitBtn.innerText = "Insert";
            }
            
            cashModal.classList.remove('hidden');
            document.getElementById('cash-input').value = "";
            document.getElementById('cash-input').focus();
        }
    });
});

// Cash/UPI Modal Actions
document.getElementById('btn-cancel-cash').addEventListener('click', () => {
    cashModal.classList.add('hidden');
});

document.getElementById('btn-submit-cash').addEventListener('click', () => {
    const amountVal = parseFloat(document.getElementById('cash-input').value);
    if (isNaN(amountVal) || amountVal <= 0) {
        alert("Enter valid amount");
        return;
    }
    
    cashModal.classList.add('hidden');
    
    if (selectedMethod === "UPI") {
        if (amountVal === selectedProduct.price) {
            processPayment("UPI", amountVal);
        } else {
            updateScreen("UPI FAILED:<br/>EXACT AMOUNT REQUIRED", true);
            setTimeout(() => {
                updateScreen(`${selectedProduct.name} - ₹${selectedProduct.price}<br/>Select Payment`);
            }, 2000);
        }
    } else if (selectedMethod === "Cash") {
        if (amountVal >= selectedProduct.price) {
            processPayment("Cash", amountVal);
        } else {
            updateScreen("INSUFFICIENT<br/>FUNDS", true);
            setTimeout(() => {
                updateScreen(`${selectedProduct.name} - ₹${selectedProduct.price}<br/>Select Payment`);
            }, 2000);
        }
    }
});

// Process Transaction
function processPayment(method, amountPaid) {
    paymentPanel.classList.remove('active');
    updateScreen("PROCESSING<br/>PAYMENT...");

    setTimeout(() => {
        let change = amountPaid - selectedProduct.price;
        totalSales += selectedProduct.price;
        selectedProduct.stock--;

        // Dispense Animation
        dispenserDoor.classList.add('open');
        dispenserDoor.innerHTML = selectedProduct.icon;
        updateScreen("DISPENSING<br/>" + selectedProduct.name);

        setTimeout(() => {
            showReceipt(method, amountPaid, change);
        }, 1500);

    }, 1000);
}

function showReceipt(method, amountPaid, change) {
    dispenserDoor.classList.remove('open');
    dispenserDoor.innerHTML = "PUSH";
    renderProducts();

    const receipt = document.getElementById('receipt');
    receipt.innerHTML = `
        Product: ${selectedProduct.name}<br/>
        Price: ₹${selectedProduct.price}<br/>
        Payment: ${method}<br/>
        Paid: ₹${amountPaid}<br/>
        ${method === 'Cash' && change > 0 ? `Change: ₹${change}<br/>` : ''}
        Status: SUCCESS
    `;

    document.getElementById('received-message').innerText = `${selectedProduct.name} Dispensed!`;
    receivedModal.classList.remove('hidden');
}

document.getElementById('btn-close-receipt').addEventListener('click', () => {
    receivedModal.classList.add('hidden');
    selectedProduct = null;
    updateScreen("Welcome! <br/> Select a product.");
});

// Admin Logic
document.getElementById('btn-exit-admin').addEventListener('click', () => {
    adminPanel.classList.add('hidden');
    updateScreen("Welcome! <br/> Select a product.");
});

document.getElementById('btn-restock').addEventListener('click', () => {
    const id = parseInt(document.getElementById('restock-id').value);
    const qty = parseInt(document.getElementById('restock-qty').value);

    if (isNaN(id) || isNaN(qty) || qty <= 0) return;

    const prod = products.find(p => p.id === id);
    if (prod) {
        prod.stock += qty;
        renderProducts();
        alert(`Restocked ${prod.name} with ${qty} units.`);
        document.getElementById('restock-id').value = "";
        document.getElementById('restock-qty').value = "";
    } else {
        alert("Invalid Product ID");
    }
});

// Initial Render
renderProducts();
