CREATE PROCEDURE populateDB()
  MODIFIES SQL DATA
  begin atomic
    declare account_id1, account_id2, company_id1, company_id2, job_id bigint;
    /*Insert Users*/
    insert into ACCOUNT(EMAIL, PASSWORD) values ('teste@gmail.com', 'password');
    set account_id1 = IDENTITY();
    insert into ACCOUNT(EMAIL, PASSWORD) values ('lol@hotmail.com', 'teste123');
    set account_id2 = IDENTITY();

    insert into USERACCOUNT(ACCOUNTID, NAME) values (account_id1, 'Bruno');
    insert into USERACCOUNT(ACCOUNTID, NAME) values (account_id2, 'Maria');

    /*Insert Companies*/
    insert into ACCOUNT(EMAIL, PASSWORD) values ('company1@gmail.com', '741');
    set company_id1 = IDENTITY();
    insert into ACCOUNT(EMAIL, PASSWORD) values ('company2@gmail.com', '567');
    set company_id2 = IDENTITY();

    insert into COMPANY(ACCOUNTID) values (company_id1);
    insert into COMPANY(ACCOUNTID) values (company_id2);

    /*Insert Jobs*/
    insert into JOB(TITLE, ACCOUNTID, WAGE, DESCRIPTION) values ('Great Job', account_id1, 1, 'Sou uma oferta simpatica');
    set job_id = IDENTITY();

    /*Insert JobExperiences*/
    insert into JOBEXPERIENCE(JOBID, COMPETENCES, YEARS) values (job_id, 'Java', 3);

    /*Insert Applications*/
    insert into APPLICATION(ACCOUNTID, JOBID) values (account_id2, job_id);
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
