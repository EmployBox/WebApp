CREATE PROCEDURE populateDB()
  MODIFIES SQL DATA
  begin atomic
    declare authorId int;
    declare bookId int;
    begin atomic
        insert into JOB(TITLE) values ('very gud job');
    end;
  end;

create procedure deleteDB()
  MODIFIES SQL DATA
  begin atomic
    delete from AcademicBackground;
    delete from Project;
    delete from PreviousJobs;
    delete from CurriculumExperience;
    delete from Curriculum;
    delete from Comment;
    delete from Follows;
    delete from Rating;
    delete from Message;
    delete from Chat;
    delete from JobExperience;
    delete from Local;
    delete from Application;
    delete from Moderator;
    delete from Company;
    delete from Job;
    delete from UserAccount;
    delete from Account;
  end;
