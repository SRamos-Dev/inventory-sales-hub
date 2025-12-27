-- Inicialización de la base de datos para Inventory Sales Hub

-- Crear roles iniciales
INSERT INTO roles (name) VALUES ('ADMIN'), ('USER') ON DUPLICATE KEY UPDATE name=name;

-- Crear usuario admin por defecto (contraseña: admin123)
INSERT INTO users (name, email, password, role_id, created_at)
SELECT 'Admin User', 'admin@example.com', '$2a$10$examplehashedpassword', r.id, NOW()
FROM roles r WHERE r.name = 'ADMIN'
ON DUPLICATE KEY UPDATE name=name;