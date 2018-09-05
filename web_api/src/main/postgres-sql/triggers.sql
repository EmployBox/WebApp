CREATE OR REPLACE FUNCTION incrementVersion()
  RETURNS trigger AS
$BODY$
BEGIN
 NEW.version = NEW.version + 1;
 
 RETURN NEW;
END;
$BODY$ LANGUAGE plpgsql;

CREATE TRIGGER incrementVersion
  BEFORE UPDATE
  ON academicbackground
  FOR EACH ROW
  EXECUTE PROCEDURE incrementVersion();

CREATE TRIGGER incrementVersion
  BEFORE UPDATE
  ON account
  FOR EACH ROW
  EXECUTE PROCEDURE incrementVersion();
  
CREATE TRIGGER incrementVersion
  BEFORE UPDATE
  ON application
  FOR EACH ROW
  EXECUTE PROCEDURE incrementVersion();

CREATE TRIGGER incrementVersion
  BEFORE UPDATE
  ON chat
  FOR EACH ROW
  EXECUTE PROCEDURE incrementVersion();

CREATE TRIGGER incrementVersion
  BEFORE UPDATE
  ON comment
  FOR EACH ROW
  EXECUTE PROCEDURE incrementVersion();
  
CREATE TRIGGER incrementVersion
  BEFORE UPDATE
  ON company
  FOR EACH ROW
  EXECUTE PROCEDURE incrementVersion();
  
CREATE TRIGGER incrementVersion
  BEFORE UPDATE
  ON curriculum
  FOR EACH ROW
  EXECUTE PROCEDURE incrementVersion();
  
CREATE TRIGGER incrementVersion
  BEFORE UPDATE
  ON curriculumexperience
  FOR EACH ROW
  EXECUTE PROCEDURE incrementVersion();
  
CREATE TRIGGER incrementVersion
  BEFORE UPDATE
  ON follows
  FOR EACH ROW
  EXECUTE PROCEDURE incrementVersion();
  
CREATE TRIGGER incrementVersion
  BEFORE UPDATE
  ON job
  FOR EACH ROW
  EXECUTE PROCEDURE incrementVersion();
  
CREATE TRIGGER incrementVersion
  BEFORE UPDATE
  ON jobexperience
  FOR EACH ROW
  EXECUTE PROCEDURE incrementVersion();
  
CREATE TRIGGER incrementVersion
  BEFORE UPDATE
  ON message
  FOR EACH ROW
  EXECUTE PROCEDURE incrementVersion();
  
CREATE TRIGGER incrementVersion
  BEFORE UPDATE
  ON moderator
  FOR EACH ROW
  EXECUTE PROCEDURE incrementVersion();
  
CREATE TRIGGER incrementVersion
  BEFORE UPDATE
  ON previousjobs
  FOR EACH ROW
  EXECUTE PROCEDURE incrementVersion();
  
CREATE TRIGGER incrementVersion
  BEFORE UPDATE
  ON project
  FOR EACH ROW
  EXECUTE PROCEDURE incrementVersion();
  
CREATE TRIGGER incrementVersion
  BEFORE UPDATE
  ON rating
  FOR EACH ROW
  EXECUTE PROCEDURE incrementVersion();
  
CREATE TRIGGER incrementVersion
  BEFORE UPDATE
  ON schedule
  FOR EACH ROW
  EXECUTE PROCEDURE incrementVersion();
  
CREATE TRIGGER incrementVersion
  BEFORE UPDATE
  ON useraccount
  FOR EACH ROW
  EXECUTE PROCEDURE incrementVersion();