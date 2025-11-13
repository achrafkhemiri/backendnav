-- Script de vérification de la structure de la table societes
-- Exécuter ce script dans MySQL Workbench ou phpMyAdmin

-- 1. Vérifier la structure complète de la table
DESCRIBE societes;

-- 2. Vérifier les colonnes spécifiques logo et description
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    CHARACTER_MAXIMUM_LENGTH,
    IS_NULLABLE,
    COLUMN_DEFAULT,
    COLUMN_TYPE
FROM 
    INFORMATION_SCHEMA.COLUMNS
WHERE 
    TABLE_SCHEMA = 'navire_db'  -- Remplacer par le nom de votre base si différent
    AND TABLE_NAME = 'societes'
    AND COLUMN_NAME IN ('logo', 'description');

-- 3. Afficher un échantillon des données (masquer le logo pour la lisibilité)
SELECT 
    id,
    nom,
    CASE 
        WHEN logo IS NOT NULL AND LENGTH(logo) > 0 THEN CONCAT('✅ Logo présent (', LENGTH(logo), ' caractères)')
        ELSE '❌ Pas de logo'
    END as logo_status,
    CASE 
        WHEN description IS NOT NULL AND LENGTH(description) > 0 THEN CONCAT('✅ ', LEFT(description, 50), '...')
        ELSE '❌ Pas de description'
    END as description_preview,
    contact,
    adresse
FROM 
    societes
LIMIT 10;
