# Viva Guide - PawCare 360

## 30-second pitch
Desktop system for a veterinary clinic: registers owners and pets, books appointments,
records treatments and prescriptions, grooming, boarding, bills customers and gives
management reports. Java Swing + MySQL, layered MVC, JasperReports.

## Rubric map
| Rubric | Where to show |
|---|---|
| User Interface (20) | All screens are designer forms; sidebar on every screen, same theme, maximised, Enter-key flow, search/filter, colour-coded buttons, dashboard KPIs + charts |
| Reports (10) | Reports screen: Revenue & Collections (chart), Service Performance (2 charts), Invoice. Each joins 4-5 tables. Preview + PDF export |
| Industry standards (20) | MVC + DAO + Service layers, patterns below, OOP below, naming, short comments, transactions |
| Scope (20) | 12 screens: Customers, Pets, Staff, Services, Appointments, Treatments (+medicines), Grooming, Boarding, Billing, Reports, Dashboard, Login |
| Validation + exceptions (10) | `Validator`, `ValidationException`, `DuplicateRecordException`, `AppointmentConflictException`, `BoardingConflictException`, `InsufficientStockException`, `PaymentException`, `InvalidRoleException`, `DatabaseException`, global handler |
| Deployment (10) | `dist/PawCare360.jar` + `lib`, `build-exe.bat`, SQL scripts, README, Git |

## MVC (strict)
- **View** (`view/*.form` + `.java`): NetBeans designer forms only. Passive: generated UI code, getters, display helpers. No button logic, no service/DAO calls.
- **Controller** (`controller/*Controller`): registers every listener (`view.getBtnAdd().addActionListener(...)`), reads the view, calls services, updates the view. One controller per screen; all extend `BaseController`.
- **Model** (`model`, `service`, `dao`): entities, business rules, JDBC.
- Screens open through `NavigationController` -> `XController.open()` (builds view + controller together).

## Design patterns
- **MVC** - see above.
- **Singleton** - `Session` (current user and role).
- **Factory** - `NavigationController` creates the right screen (view + controller) from a name.
- **Template Method** - `BaseController` (`guard`, `attachSidebar`, dialogs) and `BaseDAO` (query/update/transaction helpers).
- **DAO** - one class per table, all SQL here.
- **Facade / Service layer** - services hide DAOs and rules from controllers.
- **Strategy** - `TaxStrategy` / `TaxStrategies` (percentage or exempt tax) used by `InvoiceService`.
- **Builder** - `Invoice.builder()` used by `BillingController`.
- **Functional interfaces / lambdas** - `RowMapper`, `TxWork`, listeners.

## OOP concepts
- **Abstraction / inheritance** - `Person` (abstract) -> `Customer`, `Staff`; `getPersonType()` is abstract.
- **Encapsulation** - private fields + getters/setters in every model.
- **Polymorphism** - `getPersonType()` overrides; method **overloading** `UserService.login(u,p)` / `login(u,p,role)`.
- **Exceptions** - custom hierarchy, `ValidationException` is the parent of `DuplicateRecordException`.
- **Generics / collections / lambdas** - `BaseDAO.queryList<T>`, `Map`/`List`, streams in reports and stats.

## Key technical points
- Money uses `BigDecimal` (no rounding errors).
- Invoice save, payment, prescription and boarding use **JDBC transactions** (commit / rollback, `SELECT ... FOR UPDATE` against double-booking and overselling).
- Passwords: SHA-256 (`PasswordUtil`), role based menu access (`AccessControl`).
- Reports are precompiled `.jasper` files filled from DAO join queries.
- `GlobalExceptionHandler` shows friendly dialogs instead of crashes.

## Likely questions
- **Why DAO?** Keeps SQL out of the UI; easy to test and change.
- **How do you stop double booking?** `AppointmentConflictException` check on staff + time; boarding checks date overlap in a transaction.
- **What if stock is not enough?** `InsufficientStockException`, whole prescription rolls back.
- **Where is validation?** `Validator` + service layer, UI only shows the message.
- **How are roles enforced?** `AccessControl` map; `NavigationController` blocks screens the role cannot open.
- **How would you add SMS/email?** New `NotificationService` called from `AppointmentService` after a booking.

## Demo order (5 min)
Login -> Dashboard -> add Customer -> add Pet -> book Appointment (show conflict) -> Treatment + medicine
-> Billing: invoice + part payment -> Reports: Revenue (chart) + Invoice PDF -> Logout.
