-- Step 1: Create a backup of existing photos
CREATE TABLE spare_part_photos_backup AS SELECT * FROM spare_part_photos;

-- Step 2: Drop the existing table
DROP TABLE IF EXISTS spare_part_photos;

-- Step 3: Recreate the table with proper constraints
CREATE TABLE spare_part_photos (
  spare_part_id INT NOT NULL,
  photo LONGBLOB,
  PRIMARY KEY (spare_part_id, photo(255)),
  CONSTRAINT fk_spare_part
    FOREIGN KEY (spare_part_id)
    REFERENCES spare_part (sparePartId)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

-- Step 4: Restore data from backup (if any)
-- This may fail if there are orphaned records without a valid spare_part_id
INSERT IGNORE INTO spare_part_photos (spare_part_id, photo)
SELECT spare_part_id, photo
FROM spare_part_photos_backup
WHERE spare_part_id IS NOT NULL;

-- Step 5: Drop the backup table
DROP TABLE spare_part_photos_backup;