-- Nettoyage : anciens triggers (sur chart et sur charts si déjà tentés)
DROP TRIGGER IF EXISTS trg_chart_ai;
DROP TRIGGER IF EXISTS trg_chart_au;
DROP TRIGGER IF EXISTS trg_chart_ad;
DROP TRIGGER IF EXISTS trg_charts_ai;
DROP TRIGGER IF EXISTS trg_charts_au;
DROP TRIGGER IF EXISTS trg_charts_ad;

-- Crée les triggers sur la table `chart`
-- Remarque : chaque trigger a un corps d'une seule instruction -> pas besoin de DELIMITER

CREATE TRIGGER trg_chart_ai
AFTER INSERT ON chart
FOR EACH ROW
  INSERT INTO chart_changes(row_id, op) VALUES (NEW.id, 'I');

CREATE TRIGGER trg_chart_au
AFTER UPDATE ON chart
FOR EACH ROW
  INSERT INTO chart_changes(row_id, op) VALUES (NEW.id, 'U');

CREATE TRIGGER trg_chart_ad
AFTER DELETE ON chart
FOR EACH ROW
  INSERT INTO chart_changes(row_id, op) VALUES (OLD.id, 'D');
