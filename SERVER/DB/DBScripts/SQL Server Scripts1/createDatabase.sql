

IF DB_ID ('PS_API_DATABASE') IS NULL
	CREATE DATABASE PS_API_DATABASE;
GO
IF NOT EXISTS(SELECT * FROM sys.schemas WHERE name = 'ApiDatabase')
BEGIN
	EXEC ('CREATE SCHEMA ApiDatabase')
END

GO
USE PS_API_DATABASE
GO


CREATE TABLE ApiDatabase.Account (
	accountId BIGINT IDENTITY PRIMARY KEY,
	email NVARCHAR UNIQUE NOT NULL,
	rating decimal(2,1) default(0.0),
	passwordHash NVARCHAR(50) NOT NULL,
	salt UNIQUEIDENTIFIER NOT NULL,

	CONSTRAINT rate_const CHECK (rating >= 0.0 AND rating <= 5.0)
)

CREATE TABLE ApiDatabase.Company (
	accountId BIGINT IDENTITY primary key references ApiDatabase.Account,
	name NVARCHAR(40),
	yearFounded SMALLINT,
	Specialization NVARCHAR(20),
	WebPageUrl NVARCHAR(50),
	LogoUrl NVARCHAR(100),
	[description] NVARCHAR(50)
)

CREATE TABLE ApiDatabase.[User] (
	accountId BIGINT IDENTITY primary key references ApiDatabase.Account,
	name NVARCHAR(40),
	summary NVARCHAR(1500),
	PhotoUrl NVARCHAR(100)
)

CREATE TABLE ApiDatabase.Curriculum(
	userId BIGINT references ApiDatabase.[User],
	curriculumId BIGINT IDENTITY,

	primary key(userId,curriculumId)
)

CREATE TABLE ApiDatabase.AcademicBackground(
	userId BIGINT,
	curriculumId BIGINT,
	beginDate DATETIME DEFAULT(GETDATE()),
	endDate DATETIME,
	studyArea NVARCHAR(40),
	institution NVARCHAR(40),
	degree NVARCHAR(10),

	FOREIGN KEY(userId,curriculumId) REFERENCES ApiDatabase.Curriculum,
	CONSTRAINT endDate_check check (endDate < beginDate),
	CONSTRAINT degree check (degree = 'basic level 1' 
						OR degree = 'basic level 2' 
						OR degree = 'basic level 3'
						OR degree = 'secundary'
						OR degree = 'bachelor'
						OR degree = 'master'
						OR degree = 'PHD')
)

CREATE TABLE Apidatabase.PreviousJobs(
	accountId BIGINT,
	curriculumId BIGINT,
	beginDate DATETIME DEFAULT(GETDATE()),
	endDate DATETIME,
	companyName NVARCHAR(20),
	[workload] NVARCHAR(20),
	[role] NVARCHAR(20)

	FOREIGN KEY(accountId,curriculumId) REFERENCES ApiDatabase.Curriculum,
)

CREATE TABLE ApiDatabase.Job(
	jobId BIGINT identity primary key,
	accountId BIGINT references ApiDatabase.Account,
	userId BIGINT references ApiDatabase.[User],
	schedule NVARCHAR(20),
	wage SMALLINT check(wage > 0),
	[description] NVARCHAR(50),
	offerBeginDate DATETIME DEFAULT(GETDATE()),
	offerEndDate DATETIME NOT NULL
)

CREATE TABLE Apidatabase.Experience(
	experienceId BIGINT IDENTITY PRIMARY KEY, 
	userId BIGINT,
	curriculumId BIGINT,
	jobId BIGINT,
	years SMALLINT,
	Competences NVARCHAR(200)

	foreign key(userId,curriculumId) references ApiDatabase.Curriculum,
	foreign key(jobId) references ApiDatabase.Job
)

CREATE TABLE ApiDatabase.Rating(
	AccountIdFrom BIGINT references ApiDatabase.Account,
	AccountIdTo BIGINT references ApiDatabase.Account,
	ratingValue decimal(2,1) DEFAULT 0.0
	
	constraint rating_interval check (ratingValue >= 0.0 AND ratingValue <= 5.0)
	PRIMARY KEY(AccountIdFrom , AccountIdTo)
)

CREATE TABLE ApiDatabase.Chat(
	chatId BIGINT IDENTITY PRIMARY KEY,
	AccountIdFirst BIGINT references ApiDatabase.Account,
	AccountIdSecond BIGINT references ApiDatabase.Account,
)

CREATE TABLE ApiDatabase.[MESSAGE](
	chatId BIGINT REFERENCES ApiDatabase.Chat,
	[text] NVARCHAR(200),
	data datetime default(getdate()),
)