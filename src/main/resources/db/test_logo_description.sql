-- Script de test pour vérifier l'ajout des colonnes logo et description

-- 1. Vérifier la structure de la table societes
DESCRIBE societes;

-- 2. Vérifier que les colonnes logo et description existent
SELECT COLUMN_NAME, COLUMN_TYPE, IS_NULLABLE, COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'societes' 
AND COLUMN_NAME IN ('logo', 'description');

-- 3. Compter le nombre de sociétés existantes
SELECT COUNT(*) as total_societes FROM societes;

-- 4. Afficher les sociétés sans logo ni description (après migration)
SELECT id, nom, 
       CASE WHEN logo IS NULL THEN 'Pas de logo' ELSE 'Logo présent' END as logo_status,
       CASE WHEN description IS NULL THEN 'Pas de description' ELSE 'Description présente' END as desc_status
FROM societes;

-- 5. Test d'insertion d'une société avec logo et description
-- (Décommenter pour tester)
/*
INSERT INTO societes (nom, adresse, rcs, contact, tva, logo, description) 
VALUES (
    'Société Test', 
    '123 Rue Test', 
    'RCS123456', 
    '0123456789',
    'FR12345678901',
    'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==',
    'Ceci est une société de test avec un logo et une description'
);

-- Vérifier l'insertion
SELECT id, nom, 
       SUBSTRING(logo, 1, 50) as logo_preview,
       description
FROM societes 
WHERE nom = 'Société Test';

-- Nettoyer le test
DELETE FROM societes WHERE nom = 'Société Test';
*/
