CREATE TRIGGER schedule_trigger
  before update ON Schedule REFERENCING NEW ROW AS NEW OLD AS OLD
  FOR EACH ROW
  BEGIN ATOMIC
    BEGIN ATOMIC
      set NEW.version = old.version +1;
    END;
  END;

CREATE TRIGGER rating_trigger
  before update ON Rating REFERENCING NEW ROW AS NEW OLD AS OLD
FOR EACH ROW
BEGIN ATOMIC
  BEGIN ATOMIC
    set NEW.version = old.version +1;
  END;
END;

CREATE TRIGGER follows_trigger
  before update ON Follows REFERENCING NEW ROW AS NEW OLD AS OLD
FOR EACH ROW
BEGIN ATOMIC
  BEGIN ATOMIC
    set NEW.version = OLD.version +1;
  END;
END;

CREATE TRIGGER comment_trigger
  before update ON Comment REFERENCING NEW ROW AS NEW OLD AS OLD
FOR EACH ROW
BEGIN ATOMIC
  BEGIN ATOMIC
    set NEW.version = OLD.version +1;
  END;
END;

CREATE TRIGGER message_trigger
  before update ON Message REFERENCING NEW ROW AS NEW OLD AS OLD
FOR EACH ROW
BEGIN ATOMIC
  BEGIN ATOMIC
    set NEW.version = OLD.version +1;
  END;
END;

CREATE TRIGGER chat_trigger
  before update ON Chat REFERENCING NEW ROW AS NEW OLD AS OLD
FOR EACH ROW
BEGIN ATOMIC
  BEGIN ATOMIC
    set NEW.version = OLD.version +1;
  END;
END;

CREATE TRIGGER account_trigger
  before update ON Account REFERENCING NEW ROW AS NEW OLD AS OLD
FOR EACH ROW
BEGIN ATOMIC
  BEGIN ATOMIC
    set NEW.version = OLD.version +1;
  END;
END;

CREATE TRIGGER company_trigger
  before update ON Company REFERENCING NEW ROW AS NEW OLD AS OLD
FOR EACH ROW
BEGIN ATOMIC
  BEGIN ATOMIC
    set NEW.version = OLD.version +1;
  END;
END;

CREATE TRIGGER user_trigger
  before update ON UserAccount REFERENCING NEW ROW AS NEW OLD AS OLD
FOR EACH ROW
BEGIN ATOMIC
  BEGIN ATOMIC
    set NEW.version = OLD.version +1;
  END;
END;

CREATE TRIGGER curriculum_trigger
  before update ON Curriculum REFERENCING NEW ROW AS NEW OLD AS OLD
FOR EACH ROW
BEGIN ATOMIC
  BEGIN ATOMIC
    set NEW.version = OLD.version +1;
  END;
END;

CREATE TRIGGER curriculumExperience_trigger
  before update ON CurriculumExperience REFERENCING NEW ROW AS NEW OLD AS OLD
FOR EACH ROW
BEGIN ATOMIC
  BEGIN ATOMIC
    set NEW.version = OLD.version +1;
  END;
END;

CREATE TRIGGER previousJobs_trigger
  before update ON PreviousJobs REFERENCING NEW ROW AS NEW OLD AS OLD
FOR EACH ROW
BEGIN ATOMIC
  BEGIN ATOMIC
    set NEW.version = OLD.version +1;
  END;
END;

CREATE TRIGGER project_trigger
  before update ON Project REFERENCING NEW ROW AS NEW OLD AS OLD
FOR EACH ROW
BEGIN ATOMIC
  BEGIN ATOMIC
    set NEW.version = OLD.version +1;
  END;
END;

CREATE TRIGGER academicBackground_trigger
  before update ON AcademicBackground REFERENCING NEW ROW AS NEW OLD AS OLD
FOR EACH ROW
BEGIN ATOMIC
  BEGIN ATOMIC
    set NEW.version = OLD.version +1;
  END;
END;

CREATE TRIGGER application_trigger
  before update ON Application REFERENCING NEW ROW AS NEW OLD AS OLD
FOR EACH ROW
BEGIN ATOMIC
  BEGIN ATOMIC
    set NEW.version = OLD.version +1;
  END;
END;

CREATE TRIGGER job_trigger
  before update ON Job REFERENCING NEW ROW AS NEW OLD AS OLD
FOR EACH ROW
BEGIN ATOMIC
  BEGIN ATOMIC
    set NEW.version = OLD.version +1;
  END;
END;

CREATE TRIGGER jobExperience_trigger
  before update ON JobExperience REFERENCING NEW ROW AS NEW OLD AS OLD
FOR EACH ROW
BEGIN ATOMIC
  BEGIN ATOMIC
    set NEW.version = OLD.version +1;
  END;
END;

CREATE TRIGGER local_trigger
  before update ON Local REFERENCING NEW ROW AS NEW OLD AS OLD
FOR EACH ROW
BEGIN ATOMIC
  BEGIN ATOMIC
    set NEW.version = OLD.version +1;
  END;
END;