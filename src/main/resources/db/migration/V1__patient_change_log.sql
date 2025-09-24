-- Table journal des changements
CREATE TABLE IF NOT EXISTS patient_changes (
  change_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
  row_id     BIGINT NOT NULL,               -- = patients.id
  op         ENUM('I','U','D') NOT NULL,    -- Insert / Update / Delete
  changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_patient_changes_change_id ON patient_changes(change_id);
CREATE INDEX IF NOT EXISTS idx_patient_changes_row       ON patient_changes(row_id);

-- Triggers : enregistrent chaque modif de la table patients
DROP TRIGGER IF EXISTS trg_patients_ai;
DROP TRIGGER IF EXISTS trg_patients_au;
DROP TRIGGER IF EXISTS trg_patients_ad;

DELIMITER //
CREATE TRIGGER trg_patients_ai AFTER INSERT ON patients
FOR EACH ROW
BEGIN
  INSERT INTO patient_changes(row_id, op) VALUES (NEW.id, 'I');
END//
CREATE TRIGGER trg_patients_au AFTER UPDATE ON patients
FOR EACH ROW
BEGIN
  INSERT INTO patient_changes(row_id, op) VALUES (NEW.id, 'U');
END//
CREATE TRIGGER trg_patients_ad AFTER DELETE ON patients
FOR EACH ROW
BEGIN
  INSERT INTO patient_changes(row_id, op) VALUES (OLD.id, 'D');
END//
DELIMITER ;
