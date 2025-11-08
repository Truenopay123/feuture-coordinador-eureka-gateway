-- ================== INSERTAR MATERIAS ====================
INSERT INTO materias (nombre, descripcion, activo) VALUES 
('Programación Orientada a Objetos', 'Curso fundamental de programación que cubre los conceptos de OOP', true),
('Bases de Datos', 'Diseño y administración de bases de datos relacionales', true),
('Desarrollo Web', 'Fundamentos de desarrollo web front-end y back-end', true),
('Matemáticas Discretas', 'Conceptos matemáticos fundamentales para la computación', true),
('Estructura de Datos', 'Estudio de estructuras de datos y algoritmos', true),
('Ingeniería de Software', 'Metodologías y prácticas de desarrollo de software', true),
('Redes de Computadoras', 'Fundamentos de redes y comunicaciones', true),
('Sistemas Operativos', 'Gestión y administración de sistemas operativos', true),
('Inteligencia Artificial', 'Introducción a la IA y machine learning', true),
('Seguridad Informática', 'Principios de seguridad y criptografía', true);


-- ================== ASIGNAR MATERIAS A PROFESORES (CORREGIDO AL 100%) ====================
-- ¡¡IMPORTANTE!! La columna se llama: materias_id (NO materia_id)

-- Ing. Juan Manuel Pérez López (ID: 5)
INSERT INTO profesor_materias (profesor_id, materias_id, activo) VALUES 
(5, 1, true),  -- Programación Orientada a Objetos
(5, 5, true);  -- Estructura de Datos

-- Mtra. Laura Sofía García Hernández (ID: 6)
INSERT INTO profesor_materias (profesor_id, materias_id, activo) VALUES 
(6, 2, true),  -- Bases de Datos
(6, 3, true);  -- Desarrollo Web

-- Dr. Roberto Carlos Sánchez (ID: 7)
INSERT INTO profesor_materias (profesor_id, materias_id, activo) VALUES 
(7, 4, true),  -- Matemáticas Discretas
(7, 9, true);  -- Inteligencia Artificial

-- Mtro. Francisco Javier Torres (ID: 9)
INSERT INTO profesor_materias (profesor_id, materias_id, activo) VALUES 
(9, 6, true),  -- Ingeniería de Software
(9, 10, true); -- Seguridad Informática

-- Dra. Gabriela Ivonne Morales (ID: 10)
INSERT INTO profesor_materias (profesor_id, materias_id, activo) VALUES 
(10, 7, true), -- Redes de Computadoras
(10, 8, true); -- Sistemas Operativos