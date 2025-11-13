-- Migration: Ajout des colonnes logo et description à la table societes
-- Date: 2025-11-07
-- Description: Ajoute un champ logo (LONGTEXT) pour stocker les images en Base64
--              et un champ description (TEXT) pour décrire la société

USE navire_db;  -- Remplacer par le nom de votre base si différent

-- Vérifier si les colonnes existent déjà
SET @logo_exists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'societes'
    AND COLUMN_NAME = 'logo'
);

SET @description_exists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'societes'
    AND COLUMN_NAME = 'description'
);

-- Ajouter la colonne logo si elle n'existe pas
SET @sql_logo = IF(@logo_exists = 0,
    'ALTER TABLE societes ADD COLUMN logo LONGTEXT COMMENT ''Logo de la société encodé en Base64'';',
    'SELECT ''La colonne logo existe déjà'' AS info;'
);

PREPARE stmt_logo FROM @sql_logo;
EXECUTE stmt_logo;
DEALLOCATE PREPARE stmt_logo;

-- Ajouter la colonne description si elle n'existe pas
SET @sql_description = IF(@description_exists = 0,
    'ALTER TABLE societes ADD COLUMN description TEXT COMMENT ''Description de la société'';',
    'SELECT ''La colonne description existe déjà'' AS info;'
);

PREPARE stmt_description FROM @sql_description;
EXECUTE stmt_description;
DEALLOCATE PREPARE stmt_description;

-- Vérifier le résultat
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    CHARACTER_MAXIMUM_LENGTH,
    COLUMN_TYPE,
    COLUMN_COMMENT
FROM 
    INFORMATION_SCHEMA.COLUMNS
WHERE 
    TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'societes'
    AND COLUMN_NAME IN ('logo', 'description');

SELECT '✅ Migration terminée avec succès!' AS status;
