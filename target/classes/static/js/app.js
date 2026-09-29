/* =========================================================
   Automated Public Utility Drone Pipe Leak Detection System
   Frontend application logic (vanilla JS, no frameworks)
   ========================================================= */

const API_BASE = ""; // same-origin, Spring Boot serves both API and this page

/* ---------------------------------------------------------
   Generic API helper
   --------------------------------------------------------- */
async function apiRequest(method, url, body) {
    const options = {
        method: method,
        headers: { "Content-Type": "application/json" }
    };
    if (body !== undefined) {
        options.body = JSON.stringify(body);
    }
    const response = await fetch(API_BASE + url, options);

    if (response.status === 204) {
        return null;
    }

    let data = null;
    const text = await response.text();
    if (text) {
        try {
            data = JSON.parse(text);
        } catch (e) {
            data = text;
        }
    }

    if (!response.ok) {
        const message = (data && data.message) ? data.message : "Request failed (" + response.status + ")";
        throw new Error(message);
    }
    return data;
}

/* ---------------------------------------------------------
   Toast messages
   --------------------------------------------------------- */
function showToast(message, type) {
    const container = document.getElementById("toast");
    const div = document.createElement("div");
    div.className = "toast-msg " + (type || "success");
    div.textContent = message;
    container.appendChild(div);
    setTimeout(() => div.remove(), 4000);
}

function showError(err) {
    showToast(err.message || String(err), "error");
}

/* ---------------------------------------------------------
   Navigation between sections
   --------------------------------------------------------- */
const sectionLoaders = {
    dashboard: loadDashboard,
    drones: loadDrones,
    leaks: loadLeaks,
    contacts: loadContacts,
    products: loadProducts,
    accounts: loadAccounts,
    "purchase-orders": loadPurchaseOrders,
    "vendor-bills": loadVendorBills,
    "sales-orders": loadSalesOrders,
    invoices: loadInvoices,
    payments: loadPayments,
    sectors: loadSectors,
    budgets: loadBudgets,
    reports: loadReports
};

function showSection(sectionId) {
    document.querySelectorAll(".section").forEach(s => s.classList.remove("active"));
    document.querySelectorAll(".nav-list a").forEach(a => a.classList.remove("active"));

    document.getElementById("section-" + sectionId).classList.add("active");
    document.getElementById("nav-" + sectionId).classList.add("active");

    if (sectionLoaders[sectionId]) {
        sectionLoaders[sectionId]();
    }
}

function setupNav() {
    document.querySelectorAll(".nav-list a").forEach(a => {
        a.addEventListener("click", (e) => {
            e.preventDefault();
            showSection(a.dataset.section);
        });
    });
}

/* ---------------------------------------------------------
   Helpers
   --------------------------------------------------------- */
function fmtMoney(value) {
    const n = Number(value || 0);
    return "$" + n.toFixed(2);
}

function badge(text) {
    const cls = (text || "").toLowerCase();
    return `<span class="badge ${cls}">${text}</span>`;
}

function optionList(items, valueField, labelField) {
    return items.map(i => `<option value="${i[valueField]}">${i[labelField]} (#${i[valueField]})</option>`).join("");
}

/* =========================================================
   DASHBOARD
   ========================================================= */
async function loadDashboard() {
    try {
        const summary = await apiRequest("GET", "/api/dashboard/summary");
        const grid = document.getElementById("dashboard-cards");
        grid.innerHTML = `
            <div class="stat-card"><div class="value">${summary.totalDrones}</div><div class="label">Total Drones</div></div>
            <div class="stat-card"><div class="value">${summary.availableDrones}</div><div class="label">Available Drones</div></div>
            <div class="stat-card"><div class="value">${summary.activeLeaks}</div><div class="label">Active Leaks</div></div>
            <div class="stat-card"><div class="value">${summary.repairedLeaks}</div><div class="label">Repaired Leaks</div></div>
            <div class="stat-card"><div class="value">${summary.unpaidCustomerInvoices}</div><div class="label">Unpaid Customer Invoices</div></div>
            <div class="stat-card"><div class="value">${summary.unpaidVendorBills}</div><div class="label">Unpaid Vendor Bills</div></div>
        `;
    } catch (err) {
        showError(err);
    }
}

/* =========================================================
   DRONES
   ========================================================= */
async function loadDrones() {
    try {
        const drones = await apiRequest("GET", "/api/pipes/drones");
        const tbody = document.getElementById("drones-tbody");
        tbody.innerHTML = drones.map(d => `
            <tr>
                <td>${d.id}</td>
                <td>${d.droneCode}</td>
                <td>${d.droneName}</td>
                <td>${badge(d.status)}</td>
                <td>${d.batteryLevel != null ? d.batteryLevel + "%" : ""}</td>
                <td>${d.currentLocation || ""}</td>
                <td>
                    <button class="btn small" onclick="dispatchDroneAction(${d.id})">Dispatch</button>
                    <button class="btn small success" onclick="markDroneAvailable(${d.id})">Available</button>
                    <button class="btn small secondary" onclick="markDroneMaintenance(${d.id})">Maintenance</button>
                    <button class="btn small danger" onclick="deleteDrone(${d.id})">Delete</button>
                </td>
            </tr>
        `).join("");
    } catch (err) {
        showError(err);
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("drone-form");
    if (form) {
        form.addEventListener("submit", async (e) => {
            e.preventDefault();
            try {
                await apiRequest("POST", "/api/pipes/drones", {
                    droneCode: document.getElementById("drone-code").value,
                    droneName: document.getElementById("drone-name").value,
                    status: document.getElementById("drone-status").value,
                    batteryLevel: Number(document.getElementById("drone-battery").value || 100),
                    currentLocation: document.getElementById("drone-location").value
                });
                showToast("Drone added successfully");
                form.reset();
                loadDrones();
                refreshDroneDropdowns();
            } catch (err) {
                showError(err);
            }
        });
    }
});

async function dispatchDroneAction(id) {
    try {
        await apiRequest("POST", `/api/pipes/drones/${id}/dispatch`);
        showToast("Drone dispatched");
        loadDrones();
    } catch (err) {
        showError(err);
    }
}

async function markDroneAvailable(id) {
    try {
        await apiRequest("POST", `/api/pipes/drones/${id}/mark-available`);
        showToast("Drone marked available");
        loadDrones();
    } catch (err) {
        showError(err);
    }
}

async function markDroneMaintenance(id) {
    try {
        await apiRequest("POST", `/api/pipes/drones/${id}/mark-maintenance`);
        showToast("Drone marked under maintenance");
        loadDrones();
    } catch (err) {
        showError(err);
    }
}

async function deleteDrone(id) {
    if (!confirm("Delete this drone?")) return;
    try {
        await apiRequest("DELETE", `/api/pipes/drones/${id}`);
        showToast("Drone deleted");
        loadDrones();
    } catch (err) {
        showError(err);
    }
}

/* =========================================================
   LEAK DETECTION
   ========================================================= */
async function loadLeaks() {
    try {
        const leaks = await apiRequest("GET", "/api/pipes/leaks");
        const tbody = document.getElementById("leaks-tbody");
        tbody.innerHTML = leaks.map(l => `
            <tr>
                <td>${l.id}</td>
                <td>${l.pipeLocation}</td>
                <td>${l.acousticLevel}</td>
                <td>${badge(l.leakSeverity)}</td>
                <td>${badge(l.status)}</td>
                <td>${l.assignedDroneId || "-"}</td>
                <td>${l.detectedAt ? l.detectedAt.replace("T", " ") : ""}</td>
                <td>
                    <button class="btn small" onclick="dispatchLeakDrone(${l.id})">Dispatch Drone</button>
                    <button class="btn small secondary" onclick="openConsumeSealant(${l.id})">Use Sealant</button>
                    <button class="btn small success" onclick="completeLeakRepair(${l.id})">Complete Repair</button>
                </td>
            </tr>
        `).join("");
    } catch (err) {
        showError(err);
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("leak-form");
    if (form) {
        form.addEventListener("submit", async (e) => {
            e.preventDefault();
            try {
                await apiRequest("POST", "/api/pipes/leaks", {
                    pipeLocation: document.getElementById("leak-location").value,
                    acousticLevel: Number(document.getElementById("leak-acoustic").value),
                    leakSeverity: document.getElementById("leak-severity").value,
                    analyticAccountId: document.getElementById("leak-sector").value || null
                });
                showToast("Leak alert recorded");
                form.reset();
                loadLeaks();
                loadDashboard();
            } catch (err) {
                showError(err);
            }
        });
    }
});

async function dispatchLeakDrone(leakId) {
    try {
        await apiRequest("POST", `/api/pipes/leaks/${leakId}/dispatch`, {});
        showToast("Drone dispatched to leak #" + leakId);
        loadLeaks();
        loadDrones();
        loadDashboard();
    } catch (err) {
        showError(err);
    }
}

function openConsumeSealant(leakId) {
    document.getElementById("consume-leak-id-input").value = leakId;
    document.getElementById("sealant-consume-form").scrollIntoView({ behavior: "smooth", block: "center" });
}

async function completeLeakRepair(leakId) {
    const notes = prompt("Repair notes (optional):", "Leak sealed successfully by ultrasonic sealing.");
    try {
        await apiRequest("POST", `/api/pipes/leaks/${leakId}/complete-repair`, { repairNotes: notes || "" });
        showToast("Repair completed for leak #" + leakId);
        loadLeaks();
        loadDrones();
        loadDashboard();
    } catch (err) {
        showError(err);
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("sealant-consume-form");
    if (form) {
        form.addEventListener("submit", async (e) => {
            e.preventDefault();
            try {
                await apiRequest("POST", "/api/sealant-consumption", {
                    leakId: Number(document.getElementById("consume-leak-id-input").value),
                    productId: Number(document.getElementById("consume-product-id").value),
                    quantityUsed: Number(document.getElementById("consume-qty").value)
                });
                showToast("Sealant consumption recorded");
                form.reset();
                loadLeaks();
                loadProducts();
            } catch (err) {
                showError(err);
            }
        });
    }
});

/* =========================================================
   CONTACTS
   ========================================================= */
async function loadContacts() {
    try {
        const contacts = await apiRequest("GET", "/api/contacts");
        const tbody = document.getElementById("contacts-tbody");
        tbody.innerHTML = contacts.map(c => `
            <tr>
                <td>${c.id}</td>
                <td>${c.name}</td>
                <td>${badge(c.contactType)}</td>
                <td>${c.email || ""}</td>
                <td>${c.phone || ""}</td>
                <td>${c.address || ""}</td>
                <td>
                    <button class="btn small danger" onclick="deleteContact(${c.id})">Delete</button>
                </td>
            </tr>
        `).join("");
        window.__contactsCache = contacts;
        refreshContactDropdowns(contacts);
    } catch (err) {
        showError(err);
    }
}

function refreshContactDropdowns(contacts) {
    const customers = contacts.filter(c => c.contactType === "MUNICIPAL_WATER_BOARD");
    const suppliers = contacts.filter(c => c.contactType === "SEALANT_SUPPLIER");

    ["so-customer", "invoice-customer-hint"].forEach(id => {
        const el = document.getElementById(id);
        if (el && el.tagName === "SELECT") el.innerHTML = optionList(customers, "id", "name");
    });
    ["po-supplier", "bill-supplier"].forEach(id => {
        const el = document.getElementById(id);
        if (el && el.tagName === "SELECT") el.innerHTML = optionList(suppliers, "id", "name");
    });
}

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("contact-form");
    if (form) {
        form.addEventListener("submit", async (e) => {
            e.preventDefault();
            try {
                await apiRequest("POST", "/api/contacts", {
                    name: document.getElementById("contact-name").value,
                    contactType: document.getElementById("contact-type").value,
                    email: document.getElementById("contact-email").value,
                    phone: document.getElementById("contact-phone").value,
                    address: document.getElementById("contact-address").value
                });
                showToast("Contact added");
                form.reset();
                loadContacts();
            } catch (err) {
                showError(err);
            }
        });
    }
});

async function deleteContact(id) {
    if (!confirm("Delete this contact?")) return;
    try {
        await apiRequest("DELETE", `/api/contacts/${id}`);
        showToast("Contact deleted");
        loadContacts();
    } catch (err) {
        showError(err);
    }
}

/* =========================================================
   PRODUCTS
   ========================================================= */
async function loadProducts() {
    try {
        const products = await apiRequest("GET", "/api/products");
        const tbody = document.getElementById("products-tbody");
        tbody.innerHTML = products.map(p => `
            <tr>
                <td>${p.id}</td>
                <td>${p.name}</td>
                <td>${badge(p.type)}</td>
                <td>${p.description || ""}</td>
                <td>${fmtMoney(p.unitPrice)}</td>
                <td>${p.stockQuantity}</td>
                <td>
                    <button class="btn small danger" onclick="deleteProduct(${p.id})">Delete</button>
                </td>
            </tr>
        `).join("");
        window.__productsCache = products;
        refreshProductDropdowns(products);
    } catch (err) {
        showError(err);
    }
}

function refreshProductDropdowns(products) {
    document.querySelectorAll(".product-select").forEach(el => {
        el.innerHTML = optionList(products, "id", "name");
    });
}

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("product-form");
    if (form) {
        form.addEventListener("submit", async (e) => {
            e.preventDefault();
            try {
                await apiRequest("POST", "/api/products", {
                    name: document.getElementById("product-name").value,
                    type: document.getElementById("product-type").value,
                    description: document.getElementById("product-description").value,
                    unitPrice: Number(document.getElementById("product-price").value),
                    stockQuantity: Number(document.getElementById("product-stock").value || 0)
                });
                showToast("Product added");
                form.reset();
                loadProducts();
            } catch (err) {
                showError(err);
            }
        });
    }
});

async function deleteProduct(id) {
    if (!confirm("Delete this product?")) return;
    try {
        await apiRequest("DELETE", `/api/products/${id}`);
        showToast("Product deleted");
        loadProducts();
    } catch (err) {
        showError(err);
    }
}

/* =========================================================
   CHART OF ACCOUNTS
   ========================================================= */
async function loadAccounts() {
    try {
        const accounts = await apiRequest("GET", "/api/accounts");
        const tbody = document.getElementById("accounts-tbody");
        tbody.innerHTML = accounts.map(a => `
            <tr>
                <td>${a.id}</td>
                <td>${a.accountName}</td>
                <td>${badge(a.accountType)}</td>
                <td><button class="btn small danger" onclick="deleteAccount(${a.id})">Delete</button></td>
            </tr>
        `).join("");
    } catch (err) {
        showError(err);
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("account-form");
    if (form) {
        form.addEventListener("submit", async (e) => {
            e.preventDefault();
            try {
                await apiRequest("POST", "/api/accounts", {
                    accountName: document.getElementById("account-name").value,
                    accountType: document.getElementById("account-type").value
                });
                showToast("Account added");
                form.reset();
                loadAccounts();
            } catch (err) {
                showError(err);
            }
        });
    }
});

async function deleteAccount(id) {
    if (!confirm("Delete this account?")) return;
    try {
        await apiRequest("DELETE", `/api/accounts/${id}`);
        showToast("Account deleted");
        loadAccounts();
    } catch (err) {
        showError(err);
    }
}

/* =========================================================
   PURCHASE ORDERS
   ========================================================= */
let poItemCount = 0;

function addPoItemRow() {
    poItemCount++;
    const container = document.getElementById("po-items-container");
    const row = document.createElement("div");
    row.className = "form-grid";
    row.innerHTML = `
        <div class="form-group">
            <label>Product</label>
            <select class="product-select po-item-product">${optionList(window.__productsCache || [], "id", "name")}</select>
        </div>
        <div class="form-group">
            <label>Quantity</label>
            <input type="number" class="po-item-qty" min="1" value="1" />
        </div>
    `;
    container.appendChild(row);
}

async function loadPurchaseOrders() {
    try {
        const orders = await apiRequest("GET", "/api/purchase-orders");
        const tbody = document.getElementById("po-tbody");
        tbody.innerHTML = orders.map(o => `
            <tr>
                <td>${o.id}</td>
                <td>${o.supplierId}</td>
                <td>${o.orderDate}</td>
                <td>${badge(o.status)}</td>
                <td>${fmtMoney(o.totalAmount)}</td>
                <td>
                    <button class="btn small" onclick="confirmPO(${o.id})">Confirm</button>
                    <button class="btn small success" onclick="completePO(${o.id})">Complete</button>
                    <button class="btn small secondary" onclick="viewPODetail(${o.id})">Details</button>
                </td>
            </tr>
        `).join("");
    } catch (err) {
        showError(err);
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const addBtn = document.getElementById("po-add-item-btn");
    if (addBtn) addBtn.addEventListener("click", addPoItemRow);

    const form = document.getElementById("po-form");
    if (form) {
        form.addEventListener("submit", async (e) => {
            e.preventDefault();
            const items = [];
            document.querySelectorAll("#po-items-container .form-grid").forEach(row => {
                const productId = Number(row.querySelector(".po-item-product").value);
                const qty = Number(row.querySelector(".po-item-qty").value);
                if (productId && qty) items.push({ productId: productId, quantity: qty });
            });
            if (items.length === 0) {
                showToast("Add at least one item", "error");
                return;
            }
            try {
                await apiRequest("POST", "/api/purchase-orders", {
                    supplierId: Number(document.getElementById("po-supplier").value),
                    items: items
                });
                showToast("Purchase order created");
                document.getElementById("po-items-container").innerHTML = "";
                poItemCount = 0;
                addPoItemRow();
                loadPurchaseOrders();
            } catch (err) {
                showError(err);
            }
        });
    }
});

async function confirmPO(id) {
    try {
        await apiRequest("POST", `/api/purchase-orders/${id}/confirm`);
        showToast("Purchase order confirmed");
        loadPurchaseOrders();
    } catch (err) {
        showError(err);
    }
}

async function completePO(id) {
    try {
        await apiRequest("POST", `/api/purchase-orders/${id}/complete`);
        showToast("Purchase order completed");
        loadPurchaseOrders();
    } catch (err) {
        showError(err);
    }
}

async function viewPODetail(id) {
    try {
        const detail = await apiRequest("GET", `/api/purchase-orders/${id}`);
        const lines = detail.items.map(i => `Product #${i.productId} x${i.quantity} @ ${fmtMoney(i.unitPrice)} = ${fmtMoney(i.amount)}`).join("\n");
        alert(`Purchase Order #${detail.purchaseOrder.id}\nStatus: ${detail.purchaseOrder.status}\nTotal: ${fmtMoney(detail.purchaseOrder.totalAmount)}\n\nItems:\n${lines}`);
    } catch (err) {
        showError(err);
    }
}

/* =========================================================
   VENDOR BILLS
   ========================================================= */
async function loadVendorBills() {
    try {
        const bills = await apiRequest("GET", "/api/vendor-bills");
        const tbody = document.getElementById("bills-tbody");
        tbody.innerHTML = bills.map(b => `
            <tr>
                <td>${b.id}</td>
                <td>${b.supplierId}</td>
                <td>${b.purchaseOrderId || "-"}</td>
                <td>${b.billDate}</td>
                <td>${fmtMoney(b.amount)}</td>
                <td>${badge(b.status)}</td>
                <td>
                    ${b.status === "UNPAID" ? `<button class="btn small success" onclick="markBillPaidDirect(${b.id})">Mark Paid</button>` : ""}
                </td>
            </tr>
        `).join("");
    } catch (err) {
        showError(err);
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("bill-form");
    if (form) {
        form.addEventListener("submit", async (e) => {
            e.preventDefault();
            try {
                await apiRequest("POST", "/api/vendor-bills", {
                    supplierId: Number(document.getElementById("bill-supplier").value),
                    purchaseOrderId: document.getElementById("bill-po").value ? Number(document.getElementById("bill-po").value) : null,
                    amount: Number(document.getElementById("bill-amount").value)
                });
                showToast("Vendor bill created");
                form.reset();
                loadVendorBills();
            } catch (err) {
                showError(err);
            }
        });
    }
});

async function markBillPaidDirect(id) {
    // Convenience: mark paid without going through the Payments tab
    try {
        await apiRequest("POST", `/api/vendor-bills/${id}/mark-paid`);
        showToast("Vendor bill marked as paid");
        loadVendorBills();
    } catch (err) {
        showError(err);
    }
}

/* =========================================================
   SALES ORDERS
   ========================================================= */
function addSoItemRow() {
    const container = document.getElementById("so-items-container");
    const row = document.createElement("div");
    row.className = "form-grid";
    row.innerHTML = `
        <div class="form-group">
            <label>Product</label>
            <select class="product-select so-item-product">${optionList(window.__productsCache || [], "id", "name")}</select>
        </div>
        <div class="form-group">
            <label>Quantity</label>
            <input type="number" class="so-item-qty" min="1" value="1" />
        </div>
    `;
    container.appendChild(row);
}

async function loadSalesOrders() {
    try {
        const orders = await apiRequest("GET", "/api/sales-orders");
        const tbody = document.getElementById("so-tbody");
        tbody.innerHTML = orders.map(o => `
            <tr>
                <td>${o.id}</td>
                <td>${o.customerId}</td>
                <td>${o.orderDate}</td>
                <td>${badge(o.status)}</td>
                <td>${fmtMoney(o.totalAmount)}</td>
                <td>
                    <button class="btn small success" onclick="completeSO(${o.id})">Complete</button>
                    <button class="btn small secondary" onclick="viewSODetail(${o.id})">Details</button>
                </td>
            </tr>
        `).join("");
        refreshSalesOrderDropdown(orders);
    } catch (err) {
        showError(err);
    }
}

function refreshSalesOrderDropdown(orders) {
    const completed = orders.filter(o => o.status === "COMPLETED");
    const el = document.getElementById("invoice-so-select");
    if (el) {
        el.innerHTML = completed.map(o => `<option value="${o.id}">Sales Order #${o.id} - ${fmtMoney(o.totalAmount)}</option>`).join("");
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const addBtn = document.getElementById("so-add-item-btn");
    if (addBtn) addBtn.addEventListener("click", addSoItemRow);

    const form = document.getElementById("so-form");
    if (form) {
        form.addEventListener("submit", async (e) => {
            e.preventDefault();
            const items = [];
            document.querySelectorAll("#so-items-container .form-grid").forEach(row => {
                const productId = Number(row.querySelector(".so-item-product").value);
                const qty = Number(row.querySelector(".so-item-qty").value);
                if (productId && qty) items.push({ productId: productId, quantity: qty });
            });
            if (items.length === 0) {
                showToast("Add at least one item", "error");
                return;
            }
            try {
                await apiRequest("POST", "/api/sales-orders", {
                    customerId: Number(document.getElementById("so-customer").value),
                    analyticAccountId: document.getElementById("so-sector").value || null,
                    items: items
                });
                showToast("Sales order created");
                document.getElementById("so-items-container").innerHTML = "";
                addSoItemRow();
                loadSalesOrders();
            } catch (err) {
                showError(err);
            }
        });
    }
});

async function completeSO(id) {
    try {
        await apiRequest("POST", `/api/sales-orders/${id}/complete`);
        showToast("Sales order marked completed");
        loadSalesOrders();
    } catch (err) {
        showError(err);
    }
}

async function viewSODetail(id) {
    try {
        const detail = await apiRequest("GET", `/api/sales-orders/${id}`);
        const lines = detail.items.map(i => `Product #${i.productId} x${i.quantity} @ ${fmtMoney(i.unitPrice)} = ${fmtMoney(i.amount)}`).join("\n");
        alert(`Sales Order #${detail.salesOrder.id}\nStatus: ${detail.salesOrder.status}\nTotal: ${fmtMoney(detail.salesOrder.totalAmount)}\n\nItems:\n${lines}`);
    } catch (err) {
        showError(err);
    }
}

/* =========================================================
   CUSTOMER INVOICES
   ========================================================= */
async function loadInvoices() {
    try {
        const invoices = await apiRequest("GET", "/api/invoices");
        const tbody = document.getElementById("invoices-tbody");
        tbody.innerHTML = invoices.map(i => `
            <tr>
                <td>${i.id}</td>
                <td>${i.customerId}</td>
                <td>${i.salesOrderId || "-"}</td>
                <td>${i.invoiceDate}</td>
                <td>${fmtMoney(i.amount)}</td>
                <td>${badge(i.status)}</td>
                <td>
                    ${i.status === "UNPAID" ? `<button class="btn small success" onclick="markInvoicePaidDirect(${i.id})">Mark Paid</button>` : ""}
                </td>
            </tr>
        `).join("");
    } catch (err) {
        showError(err);
    }

    const orders = await apiRequest("GET", "/api/sales-orders");
    refreshSalesOrderDropdown(orders);
}

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("invoice-form");
    if (form) {
        form.addEventListener("submit", async (e) => {
            e.preventDefault();
            try {
                await apiRequest("POST", "/api/invoices", {
                    salesOrderId: Number(document.getElementById("invoice-so-select").value)
                });
                showToast("Customer invoice created");
                loadInvoices();
            } catch (err) {
                showError(err);
            }
        });
    }
});

async function markInvoicePaidDirect(id) {
    try {
        await apiRequest("POST", `/api/invoices/${id}/mark-paid`);
        showToast("Invoice marked as paid");
        loadInvoices();
    } catch (err) {
        showError(err);
    }
}

/* =========================================================
   PAYMENTS
   ========================================================= */
async function loadPayments() {
    try {
        const payments = await apiRequest("GET", "/api/payments");
        const tbody = document.getElementById("payments-tbody");
        tbody.innerHTML = payments.map(p => `
            <tr>
                <td>${p.id}</td>
                <td>${badge(p.paymentType)}</td>
                <td>${p.referenceId}</td>
                <td>${fmtMoney(p.amount)}</td>
                <td>${p.paymentDate}</td>
                <td>${p.paymentMethod}</td>
            </tr>
        `).join("");
    } catch (err) {
        showError(err);
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("payment-form");
    if (form) {
        form.addEventListener("submit", async (e) => {
            e.preventDefault();
            try {
                await apiRequest("POST", "/api/payments", {
                    paymentType: document.getElementById("payment-type").value,
                    referenceId: Number(document.getElementById("payment-reference").value),
                    amount: Number(document.getElementById("payment-amount").value),
                    paymentMethod: "BANK_TRANSFER"
                });
                showToast("Payment recorded");
                form.reset();
                loadPayments();
                loadInvoices();
                loadVendorBills();
                loadDashboard();
            } catch (err) {
                showError(err);
            }
        });
    }
});

/* =========================================================
   PIPELINE SECTORS (Analytic Accounts)
   ========================================================= */
async function loadSectors() {
    try {
        const sectors = await apiRequest("GET", "/api/analytic-accounts");
        const tbody = document.getElementById("sectors-tbody");
        tbody.innerHTML = sectors.map(s => `
            <tr>
                <td>${s.id}</td>
                <td>${s.name}</td>
                <td>${s.description || ""}</td>
                <td><button class="btn small danger" onclick="deleteSector(${s.id})">Delete</button></td>
            </tr>
        `).join("");
        window.__sectorsCache = sectors;
        document.querySelectorAll(".sector-select").forEach(el => {
            el.innerHTML = `<option value="">-- None --</option>` + optionList(sectors, "id", "name");
        });
    } catch (err) {
        showError(err);
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("sector-form");
    if (form) {
        form.addEventListener("submit", async (e) => {
            e.preventDefault();
            try {
                await apiRequest("POST", "/api/analytic-accounts", {
                    name: document.getElementById("sector-name").value,
                    description: document.getElementById("sector-description").value
                });
                showToast("Pipeline sector added");
                form.reset();
                loadSectors();
            } catch (err) {
                showError(err);
            }
        });
    }
});

async function deleteSector(id) {
    if (!confirm("Delete this pipeline sector?")) return;
    try {
        await apiRequest("DELETE", `/api/analytic-accounts/${id}`);
        showToast("Pipeline sector deleted");
        loadSectors();
    } catch (err) {
        showError(err);
    }
}

/* =========================================================
   BUDGETS
   ========================================================= */
async function loadBudgets() {
    try {
        const budgets = await apiRequest("GET", "/api/budgets");
        const tbody = document.getElementById("budgets-tbody");
        tbody.innerHTML = budgets.map(b => `
            <tr>
                <td>${b.id}</td>
                <td>${b.analyticAccountId}</td>
                <td>${b.budgetYear}</td>
                <td>${fmtMoney(b.plannedAmount)}</td>
                <td>${fmtMoney(b.sealantPurchaseAmount)}</td>
                <td>${fmtMoney(b.droneChargingAmount)}</td>
                <td>${fmtMoney(b.repairRevenueAmount)}</td>
                <td><button class="btn small danger" onclick="deleteBudget(${b.id})">Delete</button></td>
            </tr>
        `).join("");
    } catch (err) {
        showError(err);
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("budget-form");
    if (form) {
        form.addEventListener("submit", async (e) => {
            e.preventDefault();
            try {
                await apiRequest("POST", "/api/budgets", {
                    analyticAccountId: Number(document.getElementById("budget-sector").value),
                    budgetYear: Number(document.getElementById("budget-year").value),
                    plannedAmount: Number(document.getElementById("budget-planned").value || 0),
                    sealantPurchaseAmount: Number(document.getElementById("budget-sealant").value || 0),
                    droneChargingAmount: Number(document.getElementById("budget-charging").value || 0),
                    repairRevenueAmount: Number(document.getElementById("budget-revenue").value || 0)
                });
                showToast("Budget added");
                form.reset();
                loadBudgets();
            } catch (err) {
                showError(err);
            }
        });
    }
});

async function deleteBudget(id) {
    if (!confirm("Delete this budget?")) return;
    try {
        await apiRequest("DELETE", `/api/budgets/${id}`);
        showToast("Budget deleted");
        loadBudgets();
    } catch (err) {
        showError(err);
    }
}

/* =========================================================
   REPORTS
   ========================================================= */
async function loadReports() {
    try {
        const [bs, pl, budgetReport] = await Promise.all([
            apiRequest("GET", "/api/reports/balance-sheet"),
            apiRequest("GET", "/api/reports/profit-loss"),
            apiRequest("GET", "/api/reports/budget")
        ]);

        document.getElementById("balance-sheet-body").innerHTML = `
            <tr><td>Pipe Crawler Drone Fleet</td><td>${fmtMoney(bs.pipeCrawlerDroneFleet)}</td></tr>
            <tr><td>Sealant Stock</td><td>${fmtMoney(bs.sealantStock)}</td></tr>
            <tr><td>Cash/Bank</td><td>${fmtMoney(bs.cashBank)}</td></tr>
            <tr class="total-row"><td>Total Assets</td><td>${fmtMoney(bs.totalAssets)}</td></tr>
            <tr><td>Sealant Supplier Payables</td><td>${fmtMoney(bs.sealantSupplierPayables)}</td></tr>
            <tr><td>Drone Component Creditors</td><td>${fmtMoney(bs.droneComponentCreditors)}</td></tr>
            <tr class="total-row"><td>Total Liabilities</td><td>${fmtMoney(bs.totalLiabilities)}</td></tr>
        `;

        document.getElementById("profit-loss-body").innerHTML = `
            <tr><td>Pipe Repair Revenues</td><td>${fmtMoney(pl.pipeRepairRevenues)}</td></tr>
            <tr><td>Preventive Inspection Fees</td><td>${fmtMoney(pl.preventiveInspectionFees)}</td></tr>
            <tr class="total-row"><td>Total Income</td><td>${fmtMoney(pl.totalIncome)}</td></tr>
            <tr><td>Sealant Material Costs</td><td>${fmtMoney(pl.sealantMaterialCosts)}</td></tr>
            <tr><td>Drone Battery Charging Expenses</td><td>${fmtMoney(pl.droneBatteryChargingExpenses)}</td></tr>
            <tr class="total-row"><td>Total Expenses</td><td>${fmtMoney(pl.totalExpenses)}</td></tr>
            <tr class="total-row"><td>Net Profit</td><td>${fmtMoney(pl.netProfit)}</td></tr>
        `;

        document.getElementById("budget-report-body").innerHTML = budgetReport.map(row => `
            <tr>
                <td>${row.pipelineSector}</td>
                <td>${row.budgetYear}</td>
                <td>${fmtMoney(row.plannedBudget)}</td>
                <td>${fmtMoney(row.sealantPurchases)}</td>
                <td>${fmtMoney(row.droneChargingExpenses)}</td>
                <td>${fmtMoney(row.repairRevenue)}</td>
                <td>${fmtMoney(row.remainingBudget)}</td>
            </tr>
        `).join("");
    } catch (err) {
        showError(err);
    }
}

/* =========================================================
   Dropdown refresh helper (called after drones/products load)
   ========================================================= */
async function refreshDroneDropdowns() {
    // Currently no dropdowns depend on drone list directly (dispatch is automatic),
    // reserved for future use.
}

/* =========================================================
   Init
   ========================================================= */
document.addEventListener("DOMContentLoaded", () => {
    setupNav();
    const adminLogout = document.getElementById("admin-logout");
    if (adminLogout) {
        adminLogout.addEventListener("click", async (e) => {
            e.preventDefault();
            try {
                await apiRequest("POST", "/api/auth/logout");
            } finally {
                window.location.href = "/";
            }
        });
    }
    loadDashboard();
    // Pre-load reference data needed across multiple forms
    loadContacts();
    loadProducts();
    loadSectors();
    addPoItemRow();
    addSoItemRow();
});
