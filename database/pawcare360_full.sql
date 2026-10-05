-- PawCare360 full database (fresh install)
-- Run in MySQL Workbench or phpMyAdmin.
CREATE DATABASE IF NOT EXISTS pawcare360 CHARACTER SET utf8mb4;
USE pawcare360;

CREATE TABLE IF NOT EXISTS customers (
  customer_id INT AUTO_INCREMENT PRIMARY KEY,
  full_name VARCHAR(100) NOT NULL,
  phone VARCHAR(20) NOT NULL,
  email VARCHAR(100),
  address VARCHAR(255),
  status VARCHAR(20) NOT NULL DEFAULT 'Active'
);

CREATE TABLE IF NOT EXISTS pets (
  pet_id INT AUTO_INCREMENT PRIMARY KEY,
  customer_id INT NOT NULL,
  pet_name VARCHAR(100) NOT NULL,
  species VARCHAR(50),
  breed VARCHAR(100),
  gender VARCHAR(10),
  date_of_birth DATE NULL,
  weight DECIMAL(6,2) NULL,
  notes VARCHAR(255),
  FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

CREATE TABLE IF NOT EXISTS staff (
  staff_id INT AUTO_INCREMENT PRIMARY KEY,
  full_name VARCHAR(100) NOT NULL,
  role VARCHAR(50) NOT NULL,
  phone VARCHAR(20),
  email VARCHAR(100),
  specialization VARCHAR(100),
  hire_date DATE NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'Active'
);

CREATE TABLE IF NOT EXISTS users (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  staff_id INT NULL,
  username VARCHAR(50) NOT NULL UNIQUE,
  password_hash VARCHAR(100) NOT NULL,
  role VARCHAR(30) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'Active'
);

CREATE TABLE IF NOT EXISTS services (
  service_id INT AUTO_INCREMENT PRIMARY KEY,
  service_name VARCHAR(100) NOT NULL,
  category VARCHAR(50),
  price DECIMAL(10,2) NOT NULL DEFAULT 0,
  duration_minutes INT NOT NULL DEFAULT 30,
  description VARCHAR(255),
  status VARCHAR(20) NOT NULL DEFAULT 'Active'
);

CREATE TABLE IF NOT EXISTS appointments (
  appointment_id INT AUTO_INCREMENT PRIMARY KEY,
  customer_id INT NOT NULL,
  pet_id INT NOT NULL,
  staff_id INT NULL,
  service_id INT NOT NULL,
  appointment_datetime DATETIME NOT NULL,
  reason VARCHAR(255),
  status VARCHAR(20) NOT NULL DEFAULT 'Scheduled',
  notes VARCHAR(255),
  FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
  FOREIGN KEY (pet_id) REFERENCES pets(pet_id),
  FOREIGN KEY (staff_id) REFERENCES staff(staff_id),
  FOREIGN KEY (service_id) REFERENCES services(service_id)
);

CREATE TABLE IF NOT EXISTS treatments (
  treatment_id INT AUTO_INCREMENT PRIMARY KEY,
  appointment_id INT NOT NULL,
  staff_id INT NULL,
  diagnosis VARCHAR(255),
  treatment_details TEXT,
  treatment_date DATE,
  notes VARCHAR(255),
  FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id)
);

-- Medicines
CREATE TABLE IF NOT EXISTS medicines (
  medicine_id INT AUTO_INCREMENT PRIMARY KEY,
  medicine_name VARCHAR(100) NOT NULL,
  category VARCHAR(50),
  unit_price DECIMAL(10,2) NOT NULL DEFAULT 0,
  stock_quantity INT NOT NULL DEFAULT 0,
  status VARCHAR(20) NOT NULL DEFAULT 'Active'
);

-- Prescriptions
CREATE TABLE IF NOT EXISTS treatment_medicines (
  id INT AUTO_INCREMENT PRIMARY KEY,
  treatment_id INT NOT NULL,
  medicine_id INT NOT NULL,
  quantity INT NOT NULL,
  dosage VARCHAR(100),
  instructions VARCHAR(255),
  unit_price DECIMAL(10,2) NOT NULL DEFAULT 0
);

-- Boarding rooms
CREATE TABLE IF NOT EXISTS boarding_rooms (
  room_id INT AUTO_INCREMENT PRIMARY KEY,
  room_number VARCHAR(20) NOT NULL UNIQUE,
  room_type VARCHAR(50),
  price_per_day DECIMAL(10,2) NOT NULL DEFAULT 0,
  description VARCHAR(255),
  status VARCHAR(20) NOT NULL DEFAULT 'Available'
);

-- Boarding bookings
CREATE TABLE IF NOT EXISTS boarding_bookings (
  boarding_id INT AUTO_INCREMENT PRIMARY KEY,
  customer_id INT NOT NULL,
  pet_id INT NOT NULL,
  room_id INT NOT NULL,
  check_in DATE NOT NULL,
  check_out DATE NOT NULL,
  number_of_days INT NOT NULL,
  total_amount DECIMAL(10,2) NOT NULL DEFAULT 0,
  status VARCHAR(20) NOT NULL DEFAULT 'Booked',
  notes VARCHAR(255)
);

-- Invoices
CREATE TABLE IF NOT EXISTS invoices (
  invoice_id INT AUTO_INCREMENT PRIMARY KEY,
  customer_id INT NOT NULL,
  pet_id INT NULL,
  appointment_id INT NULL,
  invoice_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  subtotal DECIMAL(10,2) NOT NULL DEFAULT 0,
  discount DECIMAL(10,2) NOT NULL DEFAULT 0,
  tax DECIMAL(10,2) NOT NULL DEFAULT 0,
  total_amount DECIMAL(10,2) NOT NULL DEFAULT 0,
  paid_amount DECIMAL(10,2) NOT NULL DEFAULT 0,
  status VARCHAR(20) NOT NULL DEFAULT 'Unpaid',
  notes VARCHAR(255)
);

-- Invoice lines
CREATE TABLE IF NOT EXISTS invoice_details (
  detail_id INT AUTO_INCREMENT PRIMARY KEY,
  invoice_id INT NOT NULL,
  item_type VARCHAR(20) NOT NULL,
  item_id INT NULL,
  description VARCHAR(255),
  quantity INT NOT NULL DEFAULT 1,
  unit_price DECIMAL(10,2) NOT NULL DEFAULT 0,
  line_total DECIMAL(10,2) NOT NULL DEFAULT 0
);

-- Payments
CREATE TABLE IF NOT EXISTS payments (
  payment_id INT AUTO_INCREMENT PRIMARY KEY,
  invoice_id INT NOT NULL,
  payment_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  amount DECIMAL(10,2) NOT NULL,
  payment_method VARCHAR(20) NOT NULL DEFAULT 'Cash',
  reference_no VARCHAR(50)
);

-- Seed users (password in brackets)
INSERT INTO users (username, password_hash, role, status) VALUES
 ('admin',   '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'Admin',        'Active'),
 ('manager', '866485796cfa8d7c0cf7111640205b83076433547577511d81f8030ae99ecea5', 'Manager',      'Active'),
 ('vet',     '95668df3d5465c0efe2bddca0ae448bb213dfdcd7ed446039003e693c22284b1', 'Veterinarian', 'Active'),
 ('nurse',   '35608f3146571aa100227a3e68290979ba8a452179a080f888625106076e7de2', 'Nurse',        'Active'),
 ('groomer', '346e20dd1f475fbd29689d79a71f9661dcff04bd48fa2f2bdab8ad8d193ec0ce', 'Groomer',      'Active'),
 ('reception','5145dba3b6bda2d610d2c5c435a1c2481eefd3146b6a7e004ad73f794386e031','Receptionist', 'Active');
-- admin: admin123 | manager: manager123 | vet: vet123
-- nurse: nurse123 | groomer: groomer123 | reception: reception123

-- Seed staff
INSERT INTO staff (full_name, role, phone, email, specialization, hire_date, status) VALUES
 ('Dr. Anika Silva',   'Veterinarian', '0771111111', 'anika@pawcare.lk',  'Surgery',   '2022-01-10', 'Active'),
 ('Dr. Ruwan Perera',  'Veterinarian', '0772222222', 'ruwan@pawcare.lk',  'Dentistry', '2023-03-15', 'Active'),
 ('Nadee Fernando',    'Nurse',        '0773333333', 'nadee@pawcare.lk',  'Care',      '2023-06-01', 'Active'),
 ('Kasun Jayasuriya',  'Groomer',      '0774444444', 'kasun@pawcare.lk',  'Grooming',  '2024-02-20', 'Active');

-- Seed services
INSERT INTO services (service_name, category, price, duration_minutes, description, status) VALUES
 ('General Checkup', 'Medical', 1500.00, 30, 'Routine health check', 'Active'),
 ('Vaccination',     'Medical', 2500.00, 20, 'Core vaccines',        'Active'),
 ('Dental Cleaning', 'Medical', 6000.00, 60, 'Scale and polish',     'Active');

-- Seed customers and pets
INSERT INTO customers (full_name, phone, email, address, status) VALUES
 ('Nimal Perera',  '0771234567', 'nimal@example.com',  'Colombo 05', 'Active'),
 ('Kamala Silva',  '0712345678', 'kamala@example.com', 'Kandy',      'Active');
INSERT INTO pets (customer_id, pet_name, species, breed, gender, date_of_birth, weight, notes) VALUES
 (1, 'Rex',   'Dog', 'Labrador', 'Male',   '2021-04-12', 28.5, 'Friendly'),
 (2, 'Misty', 'Cat', 'Persian',  'Female', '2022-08-03',  4.2, 'Indoor');

-- Seed medicines
INSERT INTO medicines (medicine_name, category, unit_price, stock_quantity, status)
SELECT * FROM (
  SELECT 'Amoxicillin 250mg' AS n, 'Antibiotic' AS c, 150.00 AS p, 200 AS s, 'Active' AS st UNION ALL
  SELECT 'Carprofen 50mg', 'Pain Relief', 220.00, 120, 'Active' UNION ALL
  SELECT 'Dewormer Tablet', 'Parasite Control', 90.00, 300, 'Active' UNION ALL
  SELECT 'Flea & Tick Spot-On', 'Parasite Control', 1250.00, 60, 'Active' UNION ALL
  SELECT 'Rabies Vaccine', 'Vaccine', 1800.00, 40, 'Active' UNION ALL
  SELECT 'Multivitamin Syrup', 'Supplement', 650.00, 80, 'Active'
) t WHERE NOT EXISTS (SELECT 1 FROM medicines);

-- Seed rooms
INSERT INTO boarding_rooms (room_number, room_type, price_per_day, description, status)
SELECT * FROM (
  SELECT 'R-101' AS a, 'Standard' AS b, 1500.00 AS c, 'Small dog or cat' AS d, 'Available' AS e UNION ALL
  SELECT 'R-102', 'Standard', 1500.00, 'Small dog or cat', 'Available' UNION ALL
  SELECT 'R-201', 'Deluxe', 2500.00, 'Large dog, AC room', 'Available' UNION ALL
  SELECT 'R-301', 'Cat Suite', 1800.00, 'Quiet cat suite', 'Available'
) t WHERE NOT EXISTS (SELECT 1 FROM boarding_rooms);

-- Seed grooming services
INSERT INTO services (service_name, category, price, duration_minutes, description, status)
SELECT * FROM (
  SELECT 'Full Groom' AS a, 'Grooming' AS b, 3500.00 AS c, 90 AS d, 'Bath, trim and brush' AS e, 'Active' AS f UNION ALL
  SELECT 'Bath & Brush', 'Grooming', 2000.00, 45, 'Bath and blow dry', 'Active' UNION ALL
  SELECT 'Nail Trim', 'Grooming', 600.00, 15, 'Nail clipping', 'Active'
) t WHERE NOT EXISTS (SELECT 1 FROM services WHERE category = 'Grooming');
