-- PetRescueChristian - segunda entrega
-- Datos semilla equivalentes a los que estaban hardcodeados en PetData.kt / AuthRepository.kt.

INSERT INTO users (name, username, email, password) VALUES
    ('Christian', 'christian', 'christian@rescuechristian.com', '12345678')
ON CONFLICT (username) DO NOTHING;

INSERT INTO pets (name, species, breed, age, size, description, image_uri, latitude, longitude, available) VALUES
    ('Max', 'Perro', 'Labrador', 2, 'Grande', 'Max es un labrador muy juguetón y cariñoso, ideal para familias con niños.', NULL, 19.1809, -99.4667, TRUE),
    ('Luna', 'Gato', 'Siamés', 1, 'Pequeño', 'Luna es curiosa y tranquila, se lleva bien con otros gatos.', NULL, 19.1860, -99.4701, TRUE),
    ('Rocky', 'Perro', 'Mestizo', 3, 'Mediano', 'Rocky fue rescatado de la calle, es leal y muy activo.', NULL, 19.1724, -99.4590, TRUE),
    ('Kiwi', 'Ave', 'Periquito', 1, 'Pequeño', 'Kiwi canta todas las mañanas y le encanta la compañía.', NULL, 19.1882, -99.4812, TRUE),
    ('Milo', 'Gato', 'Persa', 4, 'Mediano', 'Milo ya fue adoptado y tiene un nuevo hogar feliz.', NULL, 19.1966, -99.4739, FALSE),
    ('Toby', 'Perro', 'Beagle', 2, 'Mediano', 'Toby es muy sociable y le encanta pasear.', NULL, 19.1638, -99.4539, TRUE),
    ('Nube', 'Conejo', 'Holandés', 1, 'Pequeño', 'Nube es tranquila y dócil, perfecta para departamentos.', NULL, 19.2016, -99.4889, TRUE),
    ('Simón', 'Perro', 'Pastor Alemán', 5, 'Grande', 'Simón ya encontró un hogar responsable.', NULL, 19.1596, -99.4869, FALSE);
