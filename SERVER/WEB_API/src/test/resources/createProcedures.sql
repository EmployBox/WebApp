CREATE PROCEDURE populateDB()
  MODIFIES SQL DATA
  begin atomic
    declare account_id1, account_id2, company_id1, company_id2, job_id, curriculum_id bigint;
    /*Insert Users*/
    insert into ACCOUNT(NAME, EMAIL, PASSWORD, ACCOUNTTYPE, RATING) values ('Bruno', 'teste@gmail.com', 'password', 'USR', 2.0);
    set account_id1 = IDENTITY();
    insert into ACCOUNT(NAME, EMAIL, PASSWORD, ACCOUNTTYPE, RATING) values ('Maria', 'lol@hotmail.com', 'teste123', 'USR', 4.0);
    set account_id2 = IDENTITY();

    insert into USERACCOUNT(ACCOUNTID) values (account_id1);
    insert into USERACCOUNT(ACCOUNTID) values (account_id2);

    /*Insert Curricula*/
    insert into CURRICULUM(ACCOUNTID, TITLE) values (account_id1, 'Engenharia Civil');
    set curriculum_id = IDENTITY();

    /*Insert AcademicBackground*/
    insert into ACADEMICBACKGROUND(ACCOUNTID, CURRICULUMID, INSTITUTION) values (account_id1, curriculum_id, 'ISEL');

    /*Insert Project*/
    insert into PROJECT(ACCOUNTID, CURRICULUMID, NAME, DESCRIPTION) values (account_id1, curriculum_id, 'project1', 'project one');

    /*Insert CurriculumExperience*/
    insert into CURRICULUMEXPERIENCE(ACCOUNTID, CURRICULUMID, COMPETENCES, YEARS) values (account_id1, curriculum_id, 'Knows to do stuff', 1);

    /*Insert PreviousJobs*/
    insert into PREVIOUSJOBS(ACCOUNTID, CURRICULUMID, COMPANYNAME, WORKLOAD, ROLE) values (account_id1, curriculum_id,'ISEL', 'partial', 'TEACHER' );

    /*Insert Rating*/
    insert into RATING(ACCOUNTIDFROM, ACCOUNTIDDEST) values (account_id1, account_id2);

    /*Insert Companies*/
    insert into ACCOUNT(NAME, EMAIL, PASSWORD, ACCOUNTTYPE) values ('company1', 'company1@gmail.com', '741', 'CMP');
    set company_id1 = IDENTITY();
    insert into ACCOUNT(NAME, EMAIL, PASSWORD, ACCOUNTTYPE) values ('company2', 'company2@gmail.com', '567', 'CMP');
    set company_id2 = IDENTITY();

    insert into COMPANY(ACCOUNTID) values (company_id1);
    insert into COMPANY(ACCOUNTID) values (company_id2);

    /*Insert Jobs*/
    insert into JOB(TITLE, ACCOUNTID, WAGE, DESCRIPTION, OFFERTYPE) values ('Great Job', account_id1, 1, 'Sou uma oferta simpatica', 'Looking for work');
    set job_id = IDENTITY();

    /*Comment*/
    insert into COMMENT( ACCOUNTIDFROM, ACCOUNTIDDEST, TEXT, STATUS) values (account_id1, account_id2, 'FIRST COMMENT', 0);

    /*Insert JobExperiences*/
    insert into JOBEXPERIENCE(JOBID, COMPETENCES, YEARS) values (job_id, 'Java', 3);

    /*Insert Applications*/
    insert into APPLICATION(ACCOUNTID, JOBID) values (account_id2, job_id);

    /*Insert Follows*/
    insert into FOLLOWS(ACCOUNTIDFOLLOWER, ACCOUNTIDFOLLOWED) VALUES (account_id1, account_id2);
    insert into FOLLOWS(ACCOUNTIDFOLLOWER, ACCOUNTIDFOLLOWED) VALUES (account_id1, company_id1);
    insert into FOLLOWS(ACCOUNTIDFOLLOWER, ACCOUNTIDFOLLOWED) VALUES (account_id2, company_id2);

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
