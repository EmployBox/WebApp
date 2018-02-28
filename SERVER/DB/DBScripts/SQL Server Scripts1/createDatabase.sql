
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

if object_id('ApiDatabase.Account') is not null
	drop table ApiDatabase.Account
go

CREATE TABLE ApiDatabase.Account (
	accountId BIGINT IDENTITY PRIMARY KEY,
	email NVARCHAR(25) UNIQUE NOT NULL,
	rating decimal(2,1) default(0.0),
	passwordHash NVARCHAR(50) NOT NULL,
	salt UNIQUEIDENTIFIER NOT NULL,
	[version] rowversion,

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
	accountId BIGINT primary key references ApiDatabase.Account,
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
	degreeObtained NVARCHAR(10),

	FOREIGN KEY(userId,curriculumId) REFERENCES ApiDatabase.Curriculum,
	CONSTRAINT endDate_check check (endDate < beginDate),
	CONSTRAINT degree check (degreeObtained = 'basic level 1' 
						OR degreeObtained = 'basic level 2' 
						OR degreeObtained = 'basic level 3'
						OR degreeObtained = 'secundary'
						OR degreeObtained = 'bachelor'
						OR degreeObtained = 'master'
						OR degreeObtained = 'PHD')
)

CREATE TABLE Apidatabase.PreviousJobs(
	accountId BIGINT,
	curriculumId BIGINT,
	beginDate DATETIME DEFAULT(GETDATE()),
	endDate DATETIME,
	companyName NVARCHAR(20),
	[workload] NVARCHAR(20),
	[role] NVARCHAR(20),

	FOREIGN KEY(accountId,curriculumId) REFERENCES ApiDatabase.Curriculum,
)

CREATE TABLE ApiDatabase.[Local] (
	[Address] NVARCHAR(50) primary key,
	Country NVARCHAR(15),
	Street NVARCHAR(40),
	ZIPCode NVARCHAR(40)
)


CREATE TABLE ApiDatabase.Job(
	jobId BIGINT identity primary key,
	accountId BIGINT references ApiDatabase.Account,
	userId BIGINT references ApiDatabase.[User],
	schedule NVARCHAR(20),
	wage INT check(wage > 0),
	[description] NVARCHAR(50),
	offerBeginDate DATETIME DEFAULT(GETDATE()),
	offerEndDate DATETIME NOT NULL,
	offerType NVARCHAR(30),
	[Address] NVARCHAR(50) references ApiDatabase.[Local],
	[version] rowversion,

	CONSTRAINT offerTypes check(offerType = 'Looking for work' OR offerType = 'Looking for Worker')
)

CREATE TABLE Apidatabase.Experience(
	experienceId BIGINT IDENTITY PRIMARY KEY,
	years SMALLINT,
	Competence NVARCHAR(200)
)

CREATE TABLE Apidatabase.Curriculum_Experience(
	accountId BIGINT,
	curriculumId BIGINT,
	experienceId BIGINT references ApiDatabase.Experience,

	foreign key(accountId,curriculumId) references ApiDatabase.Curriculum,
	primary key(accountId, curriculumId, experienceId)
)

CREATE TABLE Apidatabase.Job_Experience(
	jobId BIGINT references ApiDatabase.Job,
	experienceId BIGINT references ApiDatabase.Experience,

	primary key(jobId, experienceId)
)

CREATE TABLE ApiDatabase.[Application](
	UserId BIGINT,
	CurriculumId BIGINT,
	JobId BIGINT references ApiDatabase.Job,
	[date] datetime default(GETDATE()),
	
	foreign key (UserId,CurriculumId) references ApiDatabase.Curriculum,
	primary key (UserId, JobId)
)

CREATE TABLE ApiDatabase.Rating(
	AccountIdFrom BIGINT references ApiDatabase.Account,
	AccountIdTo BIGINT references ApiDatabase.Account,
	ratingValue decimal(2,1) DEFAULT 0.0,
	
	constraint rating_interval check (ratingValue >= 0.0 AND ratingValue <= 5.0),
	PRIMARY KEY(AccountIdFrom , AccountIdTo)
)

CREATE TABLE ApiDatabase.Chat(
	chatId BIGINT IDENTITY PRIMARY KEY,
	AccountIdFirst BIGINT references ApiDatabase.Account,
	AccountIdSecond BIGINT references ApiDatabase.Account,
	[version] rowversion
)


CREATE TABLE ApiDatabase.[MESSAGE](
	chatId BIGINT REFERENCES ApiDatabase.Chat,
	[text] NVARCHAR(200),
	[date] datetime default(getdate()),
)

CREATE TABLE ApiDatabase.Comment (
	CommentId BIGINT identity primary key,
	AccountIdFrom BIGINT references ApiDatabase.Account,
	AccountIdDest BIGINT references ApiDatabase.Account,
	[date] DATETIME default(getdate()),
	[text] NVARCHAR(300),
	MainCommentId BIGINT REFERENCES ApiDatabase.Comment,
	[version] rowversion
)

CREATE TABLE ApiDatabase.Follows (
	AccountIdFrom BIGINT references ApiDatabase.Account,
	AccountIdDest BIGINT references ApiDatabase.Account,

	primary key (AccountIdFrom,AccountIdDest)
)

GO
if object_id('dbo.Account') is not null
	drop view dbo.Account
go
CREATE VIEW dbo.Account AS
SELECT accountId,email,passwordHash,rating FROM ApiDatabase.Account
GO

GO
CREATE VIEW dbo.[User] AS
SELECT * FROM ApiDatabase.[User]
GO

GO
CREATE VIEW dbo.Company AS
SELECT * FROM ApiDatabase.Company
GO

GO
CREATE VIEW dbo.Job AS
SELECT * FROM ApiDatabase.Job
GO

GO
CREATE VIEW dbo.Curriculum AS
SELECT * FROM ApiDatabase.Curriculum
GO

GO
CREATE VIEW dbo.AcademicBackground AS
SELECT * FROM ApiDatabase.AcademicBackground
GO

GO
CREATE VIEW dbo.Experience AS
SELECT * FROM ApiDatabase.Experience
GO

GO
CREATE VIEW dbo.Curriculum_Experience AS
SELECT * FROM ApiDatabase.Curriculum_Experience
GO

GO
CREATE VIEW dbo.Job_Experience AS
SELECT * FROM ApiDatabase.Job_Experience
GO

GO
CREATE VIEW dbo.Comment AS
SELECT * FROM ApiDatabase.Comment
GO

GO
CREATE VIEW dbo.Chat AS
SELECT * FROM ApiDatabase.Chat
GO

GO
CREATE VIEW dbo.[Local] AS
SELECT * FROM ApiDatabase.[Local]
GO

GO
CREATE VIEW dbo.[Message] AS
SELECT * FROM ApiDatabase.[Message]
GO

GO
CREATE VIEW dbo.Follows AS
SELECT * FROM ApiDatabase.Follows
GO