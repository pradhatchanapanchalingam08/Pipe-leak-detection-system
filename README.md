# Automated Public Utility Drone Pipe Leak Detection & Ultrasonic Sealing System

A simple, beginner-friendly full-stack demo application built with:

- **Backend:** Java + Spring Boot (REST APIs, layered architecture)
- **Database:** MySQL (via Spring Data JPA / Hibernate)
- **Frontend:** Plain HTML, CSS, and vanilla JavaScript (no React/Angular/Vue)
- **Build tool:** Maven

This is a **software simulation / demo only**. There is no real drone hardware,
no real ultrasonic hardware, no IoT integration, and no AI. It demonstrates a
complete business workflow: leak detection → drone dispatch → sealant
consumption → repair completion → sales order → customer invoice → payment,
plus purchasing, vendor bills, a simple chart of accounts/journal, and three
basic reports.

---

## 1. Prerequisites

Before you begin, make sure you have installed:

1. **Java JDK 17** or newer — check with `java -version`
2. **Maven 3.6+** — check with `mvn -version`
   (or use IntelliJ IDEA / Eclipse's bundled Maven)
3. **MySQL Server 8.x** (or MySQL 5.7+) running locally, with a
   username/password you know
4. **IntelliJ IDEA** or **Eclipse** (optional, for opening the project in an IDE)

---

## 2. Project Structure

```
pipe-leak-detection-system/
├── pom.xml
├── README.md
└── src/
    └── main/
        ├── java/com/example/pipedetection/
        │   ├── PipeDetectionApplication.java   (main class)
        │   ├── entity/        (JPA entities / tables)
        │   ├── repository/    (Spring Data JPA repositories)
        │   ├── service/       (business logic)
        │   ├── controller/    (REST controllers)
        │   ├── dto/           (request/response objects)
        │   ├── exception/     (custom exceptions + global handler)
        │   └── config/        (DataInitializer - sample data)
        └── resources/
            ├── application.properties   (MySQL configuration)
            └── static/                  (frontend: index.html, css/, js/)
```

---

## 3. Configure MySQL

Open `src/main/resources/application.properties` and change these three lines
to match your local MySQL setup:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/pipe_utility_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
```

- Change `root` / `root` to your actual MySQL username and password.
- You do **not** need to create the `pipe_utility_db` database yourself —
  `createDatabaseIfNotExist=true` in the URL will create it automatically
  the first time the application connects.
- Tables are created/updated automatically on startup because of:
  ```properties
  spring.jpa.hibernate.ddl-auto=update
  ```

If your MySQL server runs on a different host or port, update the URL
accordingly, e.g. `jdbc:mysql://localhost:3307/pipe_utility_db?...`.

---

## 4. Running the Application

### Option A: Command line (Maven)

From the project root folder (where `pom.xml` is located):

```bash
mvn spring-boot:run
```

Wait for the console to show:

```
Started PipeDetectionApplication in X.XXX seconds
```

### Option B: IntelliJ IDEA / Eclipse

1. Open IntelliJ IDEA or Eclipse.
2. Choose **File → Open** (IntelliJ) or **File → Import → Existing Maven Project** (Eclipse).
3. Select the `pipe-leak-detection-system` folder (the one containing `pom.xml`).
4. Let the IDE download Maven dependencies (this happens automatically).
5. Open `PipeDetectionApplication.java` (in `com.example.pipedetection` package)
   and click **Run**.

---

## 5. Open the Application

Once the application has started, open your browser and go to:

```
http://localhost:8080
```

You will see the **Pipe Leak Detection & Ultrasonic Sealing System** dashboard
with a sidebar containing all 14 modules.

---

## 6. Sample Data

On first startup, the application automatically creates sample data
(see `DataInitializer.java`), so you can try the full workflow immediately:

- **Contacts:** City Water Board (Municipal Water Board), Industrial Sealant
  Supplier (Sealant Supplier)
- **Products:** Ultrasonic Pipe Leak Repair (Service), Epoxy Resin Sealant
  Cartridge (Goods, 100 in stock), Pipe Crawler Servicing (Service)
- **Chart of Accounts:** Pipe Crawler Drone Fleet, Cash/Bank, Sealant Supplier
  Payables, Drone Component Creditors, Pipe Repair Revenues, Preventive
  Inspection Fees, Sealant Material Costs, Drone Battery Charging Expenses
- **Drones:** DRONE-001 (Crawler Alpha), DRONE-002 (Crawler Beta), both AVAILABLE
- **Pipeline Sector:** Water Main Zone West

This sample data is only inserted once — if you restart the app, it checks
whether data already exists and skips re-seeding.

---

## 7. Demonstrating the Full Workflow

Follow these steps in the dashboard (sidebar numbers match the menu):

1. **Drones** — a couple of drones already exist; you can add more if you like.
2. **Contacts** — a water board and a sealant supplier already exist.
3. **Products** — the three sample products already exist.
4. **Chart of Accounts** — the eight sample accounts already exist.
5. **Purchase Orders** — create a purchase order for the sealant supplier,
   adding the "Epoxy Resin Sealant Cartridge" product as a line item.
6. **Vendor Bills** — create a vendor bill for that supplier (you can link the
   Purchase Order ID).
7. **Payments** — record a `VENDOR_PAYMENT` with the Reference ID set to the
   Vendor Bill's ID. This marks the bill PAID.
8. **Leak Detection** — record a new leak alert (e.g. "Main St & 5th Ave",
   acoustic level 75, severity HIGH).
9. **Leak Detection** — click **Dispatch Drone** on that leak. The system
   automatically assigns the first AVAILABLE drone (or dispatches your
   specified one), and the drone status changes to DISPATCHED.
10. **Leak Detection** — use the "Record Sealant Consumption" form: enter the
    Leak ID, choose the Epoxy Resin Sealant Cartridge product, and a quantity.
    This reduces product stock and records a material cost.
11. **Leak Detection** — click **Complete Repair** on the leak. This marks the
    leak REPAIRED, frees the drone back to AVAILABLE, and records repair
    revenue.
12. **Sales Orders** — create a sales order for the City Water Board, adding
    the "Ultrasonic Pipe Leak Repair" or "Pipe Crawler Servicing" product as
    a line item.
13. **Sales Orders** — click **Complete** on that sales order.
14. **Customer Invoices** — create an invoice from the now-completed sales
    order.
15. **Payments** — record a `CUSTOMER_PAYMENT` with the Reference ID set to
    the invoice's ID. This marks the invoice PAID.
16. **Reports** — open the Reports tab to see the Balance Sheet, Profit &
    Loss, and Pipeline Sector Budget Report update based on all the
    transactions above.
17. **Dashboard** — go back to the Dashboard tab to see the summary cards
    (Total Drones, Available Drones, Active Leaks, Repaired Leaks, Unpaid
    Invoices, Unpaid Bills) reflect the latest state.

---

## 8. Verifying Data in MySQL

You can confirm the data is really being persisted by connecting to MySQL
directly, for example:

```bash
mysql -u root -p
```

```sql
USE pipe_utility_db;
SHOW TABLES;
SELECT * FROM drones;
SELECT * FROM leaks;
SELECT * FROM journal_entries;
```

You should see the tables listed in the project spec (`contacts`, `products`,
`accounts`, `drones`, `leaks`, `sealant_consumption`, `purchase_orders`,
`purchase_order_items`, `vendor_bills`, `sales_orders`, `sales_order_items`,
`customer_invoices`, `payments`, `analytic_accounts`, `budgets`,
`journal_entries`, `system_settings`) with the rows you created through the
web UI.

---

## 9. REST API Reference

All endpoints return/accept JSON. Base URL: `http://localhost:8080`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/pipes/drones` | Create a drone |
| GET | `/api/pipes/drones` | List drones |
| GET | `/api/pipes/drones/{id}` | Get a drone |
| PUT | `/api/pipes/drones/{id}` | Update a drone |
| DELETE | `/api/pipes/drones/{id}` | Delete a drone |
| POST | `/api/pipes/drones/{id}/dispatch` | Dispatch a specific drone |
| POST | `/api/pipes/drones/{id}/mark-available` | Mark drone AVAILABLE |
| POST | `/api/pipes/drones/{id}/mark-maintenance` | Mark drone MAINTENANCE |
| POST | `/api/pipes/leaks` | Record a leak alert |
| GET | `/api/pipes/leaks` | List leak alerts |
| GET | `/api/pipes/leaks/{id}` | Get leak details |
| POST | `/api/pipes/leaks/{id}/dispatch` | Dispatch an available drone to a leak (body: `{ "droneId": null }` optional) |
| POST | `/api/pipes/leaks/{id}/start-repair` | Mark leak REPAIR_IN_PROGRESS |
| POST | `/api/pipes/leaks/{id}/complete-repair` | Mark repair completed (body: `{ "repairNotes": "..." }`) |
| POST | `/api/sealant-consumption` | Record sealant used for a leak repair |
| GET | `/api/sealant-consumption` | List all sealant consumption records |
| POST | `/api/contacts` | Add contact |
| GET | `/api/contacts` | List contacts |
| PUT/DELETE | `/api/contacts/{id}` | Update / delete contact |
| POST | `/api/products` | Add product |
| GET | `/api/products` | List products |
| PUT/DELETE | `/api/products/{id}` | Update / delete product |
| POST | `/api/accounts` | Add chart-of-accounts entry |
| GET | `/api/accounts` | List accounts |
| POST | `/api/purchase-orders` | Create purchase order with items |
| GET | `/api/purchase-orders` | List purchase orders |
| GET | `/api/purchase-orders/{id}` | Purchase order detail (with items) |
| POST | `/api/purchase-orders/{id}/confirm` | Mark CONFIRMED |
| POST | `/api/purchase-orders/{id}/complete` | Mark COMPLETED |
| POST | `/api/vendor-bills` | Create vendor bill |
| GET | `/api/vendor-bills` | List vendor bills |
| POST | `/api/vendor-bills/{id}/mark-paid` | Mark vendor bill PAID |
| POST | `/api/sales-orders` | Create sales order with items |
| GET | `/api/sales-orders` | List sales orders |
| GET | `/api/sales-orders/{id}` | Sales order detail (with items) |
| POST | `/api/sales-orders/{id}/complete` | Mark COMPLETED |
| POST | `/api/invoices` | Create invoice from a completed sales order |
| GET | `/api/invoices` | List customer invoices |
| POST | `/api/invoices/{id}/mark-paid` | Mark invoice PAID |
| POST | `/api/payments` | Record a customer or vendor payment |
| GET | `/api/payments` | List payments |
| POST | `/api/analytic-accounts` | Add pipeline sector |
| GET | `/api/analytic-accounts` | List pipeline sectors |
| POST | `/api/budgets` | Add budget |
| GET | `/api/budgets` | List budgets |
| GET | `/api/reports/balance-sheet` | Balance Sheet report |
| GET | `/api/reports/profit-loss` | Profit & Loss report |
| GET | `/api/reports/budget` | Pipeline Sector Budget report |
| GET | `/api/dashboard/summary` | Dashboard summary counts |
| GET | `/api/settings` | Get system settings (leak tolerance limit) |
| PUT | `/api/settings/leak-tolerance` | Update leak tolerance limit (Admin) |

---

## 10. Business Rules Implemented

- **Drone dispatch:** A drone can only be dispatched if its status is
  `AVAILABLE`. Dispatching sets it to `DISPATCHED`.
- **Leak dispatch:** A leak can only be dispatched once (from `DETECTED`).
  If no `droneId` is given, the system automatically finds the first
  `AVAILABLE` drone.
- **Sealant consumption:** Reduces `Product.stockQuantity`. Throws a clear
  error if requested quantity exceeds available stock.
- **Repair completion:** Requires a drone to already be assigned. Sets the
  leak to `REPAIRED`, frees the drone back to `AVAILABLE`, and records repair
  revenue in the journal.
- **Customer invoice:** Can only be created from a sales order whose status
  is `COMPLETED`.
- **Payments:** Recording a `CUSTOMER_PAYMENT` automatically marks the
  referenced invoice `PAID`; recording a `VENDOR_PAYMENT` automatically marks
  the referenced bill `PAID`.
- **Journal / Ledger:** A simple, beginner-friendly bookkeeping approach —
  each business event (vendor bill, vendor payment, customer invoice,
  customer payment, sealant consumption, repair revenue) writes one or two
  journal lines to the most relevant account, following the basic rule that
  Asset/Expense accounts grow with a debit and Liability/Income accounts grow
  with a credit. This is intentionally simplified and is **not** a full
  ERP-grade accounting engine.

---

## 11. Error Handling

The API returns simple, readable JSON errors, e.g.:

```json
{
  "status": 404,
  "message": "Drone not found with id: 99",
  "timestamp": "2026-09-27T10:15:30"
}
```

Handled cases include: drone not available, drone not found, leak not found,
product not found, contact not found, insufficient sealant stock, invoice not
found, vendor bill not found, and standard request validation errors (missing
required fields, etc.).

---

## 12. Notes / Simplifications

This is a college/demo-style project. As specified, it intentionally does
**not** include: authentication/JWT/OAuth, role-based security,
microservices, Docker/Kubernetes, Kafka, Redis, WebSockets, AI/ML, real
drone/ultrasonic hardware integration, real banking/payment gateway
integration, complex ERP accounting, complex inventory management, PDF
report generation, or email notifications. Reports are simple HTML tables
rendered in the browser from JSON data returned by the REST API.

---

## 13. Troubleshooting

- **"Communications link failure" / cannot connect to MySQL** — Make sure
  MySQL is running and the host/port/username/password in
  `application.properties` are correct.
- **Port 8080 already in use** — change `server.port` in
  `application.properties` to a free port, e.g. `server.port=8081`.
- **Tables not appearing** — check the console log for Hibernate SQL output
  (enabled via `spring.jpa.show-sql=true`); confirm
  `spring.jpa.hibernate.ddl-auto=update` is set.
- **Blank page at localhost:8080** — make sure the app finished starting
  ("Started PipeDetectionApplication...") and that you are using
  `http://localhost:8080` (not `https`).


## Authentication
The application provides separate Sign In and New User options. New User registration creates a Customer account; Admin accounts are administrator-managed.
