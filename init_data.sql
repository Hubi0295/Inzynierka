CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
WITH new_users AS (
    INSERT INTO users (uuid, name, surname, username, password, email, role, isEnable, isLock)
    VALUES
    (uuid_generate_v4(), 'Anna', 'Nowak', 'anna$nowak', '$2a$10$JwOJl2S5y9TDLzsVoPvnkO2BemIxFbZpXdBfhU.W3bzNp.jYxp/Bm', 'anna.nowak@magazyn.pl', 'USER', true, false),
    (uuid_generate_v4(), 'Piotr', 'Zieliński', 'piotr$zielinski', '$2a$10$JwOJl2S5y9TDLzsVoPvnkO2BemIxFbZpXdBfhU.W3bzNp.jYxp/Bm', 'piotr.zielinski@magazyn.pl', 'ADMIN', true, false),
    (uuid_generate_v4(), 'Magda', 'Wójcik', 'magda$wojcik', '$2a$10$JwOJl2S5y9TDLzsVoPvnkO2BemIxFbZpXdBfhU.W3bzNp.jYxp/Bm', 'magda.wojcik@magazyn.pl', 'SUPERVISOR', true, false),
    (uuid_generate_v4(), 'Krzysztof', 'Lewandowski', 'krzysztof$lewandowski', '$2a$10$JwOJl2S5y9TDLzsVoPvnkO2BemIxFbZpXdBfhU.W3bzNp.jYxp/Bm', 'krzysztof.l@magazyn.pl', 'USER', true, false),
    (uuid_generate_v4(), 'Ewa', 'Szymczak', 'ewa$szymczak', '$2a$10$JwOJl2S5y9TDLzsVoPvnkO2BemIxFbZpXdBfhU.W3bzNp.jYxp/Bm', 'ewa.szymczak@magazyn.pl', 'SUPERVISOR', true, false)
    RETURNING id, name
),
all_users AS (
    SELECT id, name FROM users
    UNION ALL
    SELECT id, name FROM new_users
    ORDER BY id
)
INSERT INTO reports (user_id, created_at, type, content, done)
SELECT (SELECT id FROM all_users WHERE name = 'Jan'), '2024-12-15 23:59:59+01'::timestamptz, 'NORMAL', 'Chcialbym sie umowic na spotkanie w zwiazku z migracja', true  -- Jan Kowalski (ID: 1)
WHERE EXISTS (SELECT 1 FROM all_users WHERE name = 'Jan')
UNION ALL
SELECT (SELECT id FROM all_users WHERE name = 'Jan'), '2024-12-16 23:59:59+01'::timestamptz, 'PASSWORD', 'Chce zmienic haslo, zapomnialem je', false
WHERE EXISTS (SELECT 1 FROM all_users WHERE name = 'Jan')
UNION ALL
SELECT (SELECT id FROM all_users WHERE name = 'Anna'), '2024-12-17 23:59:59+01'::timestamptz, 'NORMAL', 'Chce sie umowic na spotkanie w zwiazku z problematycznym modulem logowania', true
UNION ALL
SELECT (SELECT id FROM all_users WHERE name = 'Anna'), '2024-12-18 23:59:59+01'::timestamptz, 'NORMAL', 'Chce sie umowic na spotkanie w zwiazku z utworzeniem nowej hali', false
UNION ALL
SELECT (SELECT id FROM all_users WHERE name = 'Piotr'), '2024-12-19 23:59:59+01'::timestamptz, 'NORMAL', 'Chce sie umowic na spotkanie w zwiazku z ustaleniem planu dzialania na ten miesiac', true
UNION ALL
SELECT (SELECT id FROM all_users WHERE name = 'Piotr'), '2024-12-20 23:59:59+01'::timestamptz, 'NORMAL', 'Chce sie umowic na spotkanie w zwiazku z bledami w oprogramowaniu', true
UNION ALL
SELECT (SELECT id FROM all_users WHERE name = 'Magda'), '2024-12-21 23:59:59+01'::timestamptz, 'PASSWORD', 'Chce zmienic haslo, zapomnialam je', false
UNION ALL
SELECT (SELECT id FROM all_users WHERE name = 'Magda'), '2024-12-22 23:59:59+01'::timestamptz, 'PASSWORD', 'Chce zmienic haslo, zapomnialam je', true
UNION ALL
SELECT (SELECT id FROM all_users WHERE name = 'Krzysztof'), '2024-12-23 23:59:59+01'::timestamptz, 'PASSWORD', 'Chce zmienic haslo, zapomnialem je', false
UNION ALL
SELECT (SELECT id FROM all_users WHERE name = 'Krzysztof'), '2024-12-24 23:59:59+01'::timestamptz, 'NORMAL', 'Chce sie umowic na spotkanie w zwiazku z niezrozumieniem regulaminu', true -- Uzupełnienie Raportu
UNION ALL
SELECT (SELECT id FROM all_users WHERE name = 'Ewa'), now(), 'NORMAL', 'Raport Ewy: Stan magazynu (Q4)', false -- Uzupełnienie Raportu
UNION ALL
SELECT (SELECT id FROM all_users WHERE name = 'Ewa'), now(), 'NORMAL', 'Raport Ewy: Lista transferów historycznych', true; -- Uzupełnienie Raportu
WITH inserted_halls AS (
    INSERT INTO halls (uuid, name, id_warehouse)
    VALUES
    (uuid_generate_v4(), 'Hala A', (SELECT id FROM warehouses WHERE name='MAGAZYN1')), 
    (uuid_generate_v4(), 'Hala B', (SELECT id FROM warehouses WHERE name='MAGAZYN1')),
    (uuid_generate_v4(), 'Hala C', (SELECT id FROM warehouses WHERE name='MAGAZYN1')),
    (uuid_generate_v4(), 'Hala D', (SELECT id FROM warehouses WHERE name='MAGAZYN1'))
    RETURNING id, name
)
, inserted_shelves AS (
    INSERT INTO shelves (uuid, name, id_hall)
    SELECT
        uuid_generate_v4(),
        'Regał ' || h.name || '-' || lpad(CAST(s.val AS TEXT), 2, '0'),
        h.id
    FROM
        inserted_halls h
    CROSS JOIN
        generate_series(1, 3) AS s(val)
    RETURNING id, name
)
INSERT INTO spots (uuid, name, id_shelf, is_free)
SELECT
    uuid_generate_v4(),
    'M-' || sh.name || '-' || lpad(CAST(i.val AS TEXT), 2, '0'),
    sh.id,
    false
FROM
    inserted_shelves sh
CROSS JOIN
    generate_series(1, 4) AS i(val);
WITH all_users_map AS (
    SELECT id, name FROM users
)
INSERT INTO contractors (uuid, name, phone, email, account_manager_id)
VALUES
(uuid_generate_v4(), 'Dostawca Materiałów Budowlanych "BudMax"', '501100200', 'kontakt@budmax.pl', (SELECT id FROM all_users_map WHERE name = 'Magda')),
(uuid_generate_v4(), 'Firma Logistyczna "TransPol"', '602200300', 'logistyka@transpol.pl', (SELECT id FROM all_users_map WHERE name = 'Magda')),
(uuid_generate_v4(), 'Klient Hurtowy "MegaMarket"', '703300400', 'zakupy@megamarket.pl', (SELECT id FROM all_users_map WHERE name = 'Magda')),
(uuid_generate_v4(), 'Producent Opakowań "PakFol"', '804400500', 'biuro@pakfol.pl', (SELECT id FROM all_users_map WHERE name = 'Ewa')),
(uuid_generate_v4(), 'Serwis Techniczny "TechRem"', '905500600', 'serwis@techrem.pl', (SELECT id FROM all_users_map WHERE name = 'Ewa'));
INSERT INTO categories (name)
VALUES
('Elektronika'),
('Narzędzia ręczne'),
('Materiały eksploatacyjne'),
('Elementy montażowe'),
('Opakowania');
INSERT INTO productdetails (description, weight, width, height)
SELECT
    CASE ((s.id - 1) % 5)
        WHEN 0 THEN 'Wysokiej jakości zestaw baterii AA, 4 szt.'
        WHEN 1 THEN 'Młotek ślusarski 500g, z trzonkiem z włókna szklanego.'
        WHEN 2 THEN 'Olej maszynowy do smarowania precyzyjnego, 1L.'
        WHEN 3 THEN 'Wkręt samogwintujący M4x20mm, opakowanie 100 szt.'
        WHEN 4 THEN 'Karton klapowy, standardowy wymiar: 40x30x20cm.'
    END,
    random() * 2 + 0.01,
    random() * 10 + 1.0,
    random() * 30 + 5.0
FROM generate_series(1, 30) AS s(id);
WITH
    c_ids AS (SELECT id FROM categories ORDER BY id LIMIT 5),
    d_ids AS (SELECT id FROM productdetails ORDER BY id LIMIT 30),
    s_ids AS (SELECT id FROM spots ORDER BY id LIMIT 30),
    o_ids AS (SELECT id FROM contractors ORDER BY id LIMIT 5),
    u_ids AS (SELECT id FROM users ORDER BY id LIMIT 6)
INSERT INTO products (uuid, rfid, name,product_receipt_id,product_issue_id, category_id, product_details_id, spot_id, contractor_id, user_id, is_active)
SELECT
    uuid_generate_v4(),
    'RFID-' || lpad(CAST(s.id AS TEXT), 5, '0'),
    'Produkt ' || lpad(CAST(s.id AS TEXT), 2, '0') || ' - ' ||
        CASE ((s.id - 1) % 5)
            WHEN 0 THEN 'Bateria'
            WHEN 1 THEN 'Młotek'
            WHEN 2 THEN 'Olej'
            WHEN 3 THEN 'Wkręt'
            WHEN 4 THEN 'Karton'
        END,
        0,
        0,
    (SELECT id FROM c_ids OFFSET (s.id - 1) % 5 LIMIT 1),
    (SELECT id FROM d_ids OFFSET s.id - 1 LIMIT 1),
    (SELECT id FROM s_ids OFFSET s.id - 1 LIMIT 1),
    (SELECT id FROM o_ids OFFSET (s.id - 1) % 5 LIMIT 1),
    (SELECT id FROM u_ids OFFSET (s.id - 1) % 6 LIMIT 1),
    false
FROM
    generate_series(1, 30) AS s(id);

INSERT INTO Producthistory (product_id, user_id, action_type, created_at)
SELECT
    id,
    user_id,
    'CREATE',
    created_at - interval '5 minutes'
FROM
    products WHERE id <= 30;
