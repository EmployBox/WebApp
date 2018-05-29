CREATE PROCEDURE populateDB()
  MODIFIES SQL DATA
  begin atomic
    insert into ACCOUNT(EMAIL, PASSWORD) values ('teste@gmail.com', 'password');
    insert into ACCOUNT(EMAIL, PASSWORD) values ('lol@hotmail.com', 'teste123');

    insert into USERACCOUNT(ACCOUNTID, NAME) values (
      (select ACCOUNTID from ACCOUNT where EMAIL = 'teste@gmail.com'),
      'Bruno'
    );
    insert into USERACCOUNT(ACCOUNTID, NAME) values (
      (select ACCOUNTID from ACCOUNT where EMAIL = 'lol@hotmail.com'),
      'Maria'
    );
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
