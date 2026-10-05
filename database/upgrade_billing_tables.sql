-- PawCare360 upgrade script
-- Adds the new tables only. Safe to re-run. No foreign keys, so it
-- works with the tables you already have.
USE pawcare360;

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
