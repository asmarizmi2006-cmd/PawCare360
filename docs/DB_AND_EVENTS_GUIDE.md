# PawCare360 - Database & Event Setup

## Database connection
1. Start MySQL from XAMPP.
2. Open `database/pawcare360_full.sql` in MySQL Workbench or phpMyAdmin.
3. Run the complete script.
4. The project reads `db.properties` from the project working directory.
5. Default configuration:
   - URL: jdbc:mysql://localhost:3306/pawcare360
   - User: root
   - Password: blank

If your MySQL password is different, edit `db.properties`.

## Keyboard events
All MVC screens automatically use `util.UIHelper`.

- ENTER moves to the next editable input field.
- SHIFT+ENTER is not consumed, so text editing remains possible.
- On the last field, existing button/action events are allowed to run.
- Combo-box ENTER is not intercepted while its popup is open.

## Mouse events
Controllers handle table selection with mouse listeners. Selecting a row loads
the record into the form where the module supports editing. Buttons use
ActionListener events for Add, Update, Delete, Clear, Save, Cancel, etc.

## Architecture
The project follows:

View -> Controller -> Service -> DAO -> MySQL

- View: NetBeans GUI forms only.
- Controller: button, mouse and keyboard events.
- Service: validation and business rules.
- DAO: JDBC SQL and database CRUD.
- Model: data objects.
- Util: shared database, session, validation and UI helpers.

## Demo users
- admin / admin123
- manager / manager123
- vet / vet123
- nurse / nurse123
- groomer / groomer123
- reception / reception123
