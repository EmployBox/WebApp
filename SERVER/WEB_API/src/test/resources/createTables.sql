create table Account (
  accountId BIGINT IDENTITY PRIMARY KEY,
  name VARCHAR(40) not null,
  email VARCHAR(25) UNIQUE NOT NULL,
  rating float(24) default(0.0),
  password VARCHAR(100) NOT NULL,
  accountType VARCHAR (3) not null,
  version bigint default 1,

  CHECK (rating >= 0.0 AND rating <= 10.0),
  CHECK (accountType in ('USR', 'CMP', 'MOD'))
);

create table Company (
  accountId BIGINT primary key references Account,
  yearFounded SMALLINT,
  specialization VARCHAR(20),
  webPageUrl VARCHAR(50),
  LogoUrl VARCHAR(100),
  description VARCHAR(50),
  version bigint default 1
);

create table Moderator (
  accountID BIGINT primary key references Account
);

CREATE TABLE UserAccount (
  accountId BIGINT primary key references Account,
  summary VARCHAR(1500),
  PhotoUrl VARCHAR(100),
  version bigint default 1
);

create table Curriculum (
  accountId BIGINT references UserAccount,
  curriculumId BIGINT identity primary key,
  title varchar(50),
  version bigint default 1
);

create table Project (
  projectId BIGINT IDENTITY PRIMARY KEY,
  accountId BIGINT NOT NULL,
  curriculumId BIGINT NOT NULL,
  name VARCHAR(15),
  description VARCHAR(50),
  version bigint default 1,

  FOREIGN KEY (curriculumId) REFERENCES curriculum,
  FOREIGN KEY (accountId) REFERENCES Account
);

create table AcademicBackground (
  academicBackgroundKey BIGINT IDENTITY PRIMARY KEY,
  accountId BIGINT NOT NULL,
  curriculumId BIGINT NOT NULL,
  beginDate DATETIME DEFAULT CURRENT_DATE,
  endDate DATETIME,
  studyArea VARCHAR(40),
  institution VARCHAR(40),
  degreeObtained VARCHAR(10),
  version bigint default 1,

  FOREIGN KEY (accountId) REFERENCES Account,
  FOREIGN KEY (curriculumId) REFERENCES curriculum(curriculumId),
  check (endDate < beginDate),
  check (degreeObtained in ('basic level 1', 'basic level 2', 'basic level 3', 'secundary', 'bachelor', 'master', 'PHD'))
);

create table PreviousJobs (
  previousJobId BIGINT IDENTITY PRIMARY KEY,
  accountId BIGINT NOT NULL,
  curriculumId BIGINT NOT NULL,
  beginDate DATETIME DEFAULT CURRENT_DATE,
  endDate DATETIME,
  companyName VARCHAR(20),
  workload VARCHAR(20),
  role VARCHAR(20),
  version bigint default 1,

  FOREIGN KEY (curriculumId) REFERENCES Curriculum,
  CHECK(workload = 'partial' OR workload = 'total')
);

create table Local (
  Address VARCHAR(50) primary key,
  country VARCHAR(15),
  zipCode VARCHAR(40),
  district VARCHAR(40),
  longitude real,
  latitude real,
  version bigint default 1
);

create table Job (
  jobId BIGINT identity primary key,
  title varchar(50) not null,
  accountId BIGINT ,
  schedule VARCHAR(20),
  wage INT check(wage > 0),
  description VARCHAR(50),
  offerBeginDate DATETIME DEFAULT CURRENT_DATE,
  offerEndDate DATETIME,
  offerType VARCHAR(30) NOT NULL,
  Address VARCHAR(50),
  version bigint default 1,

  FOREIGN KEY (accountID) REFERENCES Account,
  check(offerType = 'Looking for work' OR offerType = 'Looking for Worker')
);

CREATE TABLE CurriculumExperience(
  curriculumExperienceId BIGINT IDENTITY PRIMARY KEY,
  accountId BIGINT NOT NULL,
  curriculumId BIGINT NOT NULL,
  competences VARCHAR(50),
  years SMALLINT,
  version bigint default 1,

  FOREIGN KEY (curriculumId) REFERENCES Curriculum,
  FOREIGN KEY (accountId) REFERENCES Account
);

CREATE TABLE JobExperience(
  jobExperienceId BIGINT IDENTITY PRIMARY KEY,
  jobId BIGINT NOT NULL,
  competences VARCHAR(50),
  years SMALLINT,
  version bigint default 1,

  FOREIGN KEY (jobId) REFERENCES Job
);

CREATE TABLE Application(
  applicationId BIGINT IDENTITY PRIMARY KEY,
  accountId BIGINT,
  curriculumId BIGINT,
  jobId BIGINT,
  datetime datetime default CURRENT_DATE,
  version bigint default 1,

  FOREIGN KEY (accountId) REFERENCES Account,
  FOREIGN KEY (curriculumId) REFERENCES curriculum,
  FOREIGN KEY (jobId) REFERENCES Job
);

CREATE TABLE Rating(
  accountIdFrom BIGINT,
  accountIdDest BIGINT,
  moderatorId BIGINT references Moderator,
  workload real DEFAULT 0.0  check (workload >= 0.0 AND workload <= 10.0),
  wage real DEFAULT 0.0 check (wage >= 0.0 AND wage <= 10.0),
  workEnviroment real DEFAULT 0.0 check (workEnviroment >= 0.0 AND workEnviroment <= 10.0),
  competences real DEFAULT 0.0 check (competences >= 0.0 AND competences <= 10.0),
  ponctuality real DEFAULT 0.0 check (ponctuality >= 0.0 AND ponctuality <= 10.0),
  assiduity real DEFAULT 0.0 check (assiduity>= 0.0 AND assiduity <= 10.0),
  demeanor real DEFAULT 0.0 check (demeanor >= 0.0 AND demeanor <= 10.0),
  version bigint default 1,

  FOREIGN KEY (accountIdFrom) REFERENCES Account(accountID),
  FOREIGN KEY (accountIdDest) REFERENCES Account(accountID),

  PRIMARY KEY(AccountIdFrom , accountIdDest)
);

CREATE TABLE Chat(
  chatId BIGINT IDENTITY PRIMARY KEY,
  accountIdFirst BIGINT,
  accountIdSecond BIGINT,
  version bigint default 1,

  FOREIGN KEY (accountIdFirst) REFERENCES Account(accountID),
  FOREIGN KEY (accountIdSecond) REFERENCES Account(accountID),
  UNIQUE (AccountIdFirst, AccountIdSecond)
);

CREATE TABLE Message(
  accountId BIGINT,
  messageId BIGINT IDENTITY primary key,
  chatId BIGINT,
  text VARCHAR(200),
  datetime datetime default SYSDATE,
  version bigint default 1,

  FOREIGN KEY (chatId) REFERENCES Chat(chatId) ON DELETE CASCADE,
  FOREIGN KEY (accountId) REFERENCES Account ON DELETE CASCADE
);

CREATE TABLE Comment (
  commentId BIGINT identity primary key,
  accountIdFrom BIGINT,
  accountIdDest BIGINT,
  datetime DATE DEFAULT current_date,
  text VARCHAR(300),
  mainCommentId BIGINT,
  status bit,
  version bigint default 1,

  FOREIGN KEY (accountIdFrom) REFERENCES Account(accountID) ON DELETE CASCADE,
  FOREIGN KEY (accountIdDest) REFERENCES Account(accountID) ON DELETE CASCADE,
  FOREIGN KEY (MainCommentId) REFERENCES Comment(commentId) ON DELETE CASCADE
);

CREATE TABLE Follows (
  accountIdFollowed BIGINT references Account,
  accountIdFollower BIGINT references Account,
  version bigint default 1,

  primary key (accountIdFollowed, accountIdFollower)
);

CREATE TABLE Schedule (
  scheduleId BIGINT IDENTITY primary key,
  jobId BIGINT,
  accountId BIGINT,
  scheduleType VARCHAR(25),
  repeats VARCHAR(25),
  startdate DATETIME,
  enddate DATETIME,
  starthour DATETIME,
  endhour DATETIME,
  version BIGINT DEFAULT 1,

  FOREIGN KEY (jobId) references JOB(jobId),
  FOREIGN KEY (accountId) REFERENCES Account(accountId)
);