-- Nettoyage : anciens triggers (sur patient et sur patients si déjà tentés)
DROP TRIGGER IF EXISTS trg_patient_ai;
DROP TRIGGER IF EXISTS trg_patient_au;
DROP TRIGGER IF EXISTS trg_patient_ad;
DROP TRIGGER IF EXISTS trg_patients_ai;
DROP TRIGGER IF EXISTS trg_patients_au;
DROP TRIGGER IF EXISTS trg_patients_ad;

-- Crée les triggers sur la table `patient`
-- Remarque : chaque trigger a un corps d'une seule instruction -> pas besoin de DELIMITER

CREATE TRIGGER trg_patient_ai
AFTER INSERT ON patient
FOR EACH ROW
  INSERT INTO patient_changes(row_id, op) VALUES (NEW.id, 'I');

CREATE TRIGGER trg_patient_au
AFTER UPDATE ON patient
FOR EACH ROW
  INSERT INTO patient_changes(row_id, op) VALUES (NEW.id, 'U');

CREATE TRIGGER trg_patient_ad
AFTER DELETE ON patient
FOR EACH ROW
  INSERT INTO patient_changes(row_id, op) VALUES (OLD.id, 'D');
