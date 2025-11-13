-- Migration pour ajouter les colonnes logo et description à la table societes

-- Ajouter la colonne logo (LONGTEXT pour stocker les images en base64)
ALTER TABLE societes 
ADD COLUMN logo LONGTEXT AFTER tva;

-- Ajouter la colonne description (TEXT)
ALTER TABLE societes 
ADD COLUMN description TEXT AFTER logo;

-- Note: Les colonnes sont ajoutées après la colonne 'tva'
-- logo: stocke l'image encodée en base64
-- description: stocke une description textuelle de la société
