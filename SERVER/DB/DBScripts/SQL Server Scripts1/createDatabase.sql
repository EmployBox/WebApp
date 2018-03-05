
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
	email NVARCHAR(25) UNIQUE NOT NULL,
	rating decimal(2,1) default(0.0),
	passwordHash NVARCHAR(50) NOT NULL,
	salt UNIQUEIDENTIFIER NOT NULL,
	[version] rowversion,

	CHECK (rating >= 0.0 AND rating <= 5.0)
)

CREATE TABLE ApiDatabase.Company (
	accountId BIGINT primary key references ApiDatabase.Account,
	name NVARCHAR(40),
	yearFounded SMALLINT,
	Specialization NVARCHAR(20),
	WebPageUrl NVARCHAR(50),
	LogoUrl NVARCHAR(100),
	[description] NVARCHAR(50),
	[version] rowversion,

	FOREIGN KEY (accountID) REFERENCES ApiDatabase.Account(accountID) ON DELETE CASCADE	
)

CREATE TABLE ApiDatabase.Moderator (
	accountID BIGINT primary key references ApiDatabase.Account,

	FOREIGN KEY (accountID) REFERENCES ApiDatabase.Account(accountID) ON DELETE CASCADE	
)

CREATE TABLE ApiDatabase.[User] (
	accountId BIGINT primary key references ApiDatabase.Account,
	name NVARCHAR(40),
	summary NVARCHAR(1500),
	PhotoUrl NVARCHAR(100),
	[version] rowversion

	FOREIGN KEY (accountID) REFERENCES ApiDatabase.Account(accountID) ON DELETE CASCADE		
)

CREATE TABLE ApiDatabase.Curriculum(
	userId BIGINT references ApiDatabase.[User],
	curriculumId BIGINT,

	FOREIGN KEY (userId) REFERENCES ApiDatabase.[User] ( accountId) ON DELETE CASCADE,	
	primary key(userId,curriculumId)
)

CREATE TABLE ApiDatabase.Project (
	userId BIGINT,
	curriculumId BIGINT,
	name NVARCHAR(15),
	[description] NVARCHAR(50),
	[version] rowversion

	PRIMARY KEY (userId,curriculumID),
	FOREIGN KEY (userId,curriculumId) REFERENCES ApiDatabase.Curriculum(userId,curriculumId) ON DELETE CASCADE
)

CREATE TABLE ApiDatabase.AcademicBackground(
	userId BIGINT,
	curriculumId BIGINT,
	beginDate DATETIME DEFAULT(GETDATE()),
	endDate DATETIME,
	studyArea NVARCHAR(40),
	institution NVARCHAR(40),
	degreeObtained NVARCHAR(10),
	[version] rowversion,

	FOREIGN KEY (userId,curriculumId) REFERENCES ApiDatabase.Curriculum(userId,curriculumId) ON DELETE CASCADE,
	PRIMARY KEY(userId,curriculumId),
	check (endDate < beginDate),
	check (degreeObtained = 'basic level 1' 
						OR degreeObtained = 'basic level 2' 
						OR degreeObtained = 'basic level 3'
						OR degreeObtained = 'secundary'
						OR degreeObtained = 'bachelor'
						OR degreeObtained = 'master'
						OR degreeObtained = 'PHD')				
)

CREATE TABLE Apidatabase.PreviousJobs(
	userId BIGINT,
	curriculumId BIGINT,
	beginDate DATETIME DEFAULT(GETDATE()),
	endDate DATETIME,
	companyName NVARCHAR(20),
	[workload] NVARCHAR(20),
	[role] NVARCHAR(20),
	[version] rowversion,

	PRIMARY KEY(userId,curriculumId),
	
	FOREIGN KEY (userId,curriculumId) REFERENCES ApiDatabase.Curriculum(userId,curriculumId) ON DELETE CASCADE,
	CHECK([workLoad] = 'partial' OR [workLoad] = 'total')
)

CREATE TABLE ApiDatabase.[Local] (
	[Address] NVARCHAR(50) primary key,
	Country NVARCHAR(15),
	Street NVARCHAR(40),
	ZIPCode NVARCHAR(40),
	[version] rowversion
)


CREATE TABLE ApiDatabase.Job(
	jobId BIGINT identity primary key,
	accountId BIGINT,
	schedule NVARCHAR(20),
	wage INT check(wage > 0),
	[description] NVARCHAR(50),
	offerBeginDate DATETIME DEFAULT(GETDATE()),
	offerEndDate DATETIME,
	offerType NVARCHAR(30),
	[Address] NVARCHAR(50),
	[version] rowversion,

	FOREIGN KEY (accountID) REFERENCES ApiDatabase.Account(accountID) ON DELETE CASCADE,
	FOREIGN KEY ([Address]) REFERENCES ApiDatabase.[Local]([Address]) ON DELETE CASCADE,
	check(offerType = 'Looking for work' OR offerType = 'Looking for Worker')
)

CREATE TABLE Apidatabase.Experience(
	experienceId BIGINT IDENTITY PRIMARY KEY,
	years SMALLINT,
	Competence NVARCHAR(200),
	[version] rowversion,
)

CREATE TABLE Apidatabase.Curriculum_Experience(
	userId BIGINT,
	curriculumId BIGINT,
	experienceId BIGINT

	FOREIGN KEY (userId,curriculumId) REFERENCES ApiDatabase.Curriculum ON DELETE CASCADE,
	FOREIGN KEY (experienceId) REFERENCES ApiDatabase.Experience(experienceId) ON DELETE CASCADE,

	primary key(userId, curriculumId, experienceId)
)

CREATE TABLE Apidatabase.Job_Experience(
	jobId BIGINT,
	experienceId BIGINT,

	FOREIGN KEY (jobId) REFERENCES ApiDatabase.Job(jobId) ON DELETE CASCADE,
	FOREIGN KEY (experienceId) REFERENCES ApiDatabase.Experience(experienceId) ON DELETE CASCADE,

	primary key(jobId, experienceId)
)

CREATE TABLE ApiDatabase.[Application](
	UserId BIGINT,
	CurriculumId BIGINT,
	JobId BIGINT,
	[date] datetime default(GETDATE()),
	[version] rowversion,

	FOREIGN KEY (userId,curriculumId) REFERENCES ApiDatabase.Curriculum(userId,curriculumId),
	FOREIGN KEY (jobId) REFERENCES ApiDatabase.Job(jobId),
	primary key (UserId, JobId)
)

CREATE TABLE ApiDatabase.Rating(
	AccountIdFrom BIGINT,
	AccountIdTo BIGINT,
	moderatorId BIGINT references ApiDatabase.Moderator,
	ratingValue decimal(2,1) DEFAULT 0.0,
	[version] rowversion,
	
	FOREIGN KEY (accountIdFrom) REFERENCES ApiDatabase.Account(accountID),
	FOREIGN KEY (accountIdTo) REFERENCES ApiDatabase.Account(accountID),

	constraint rating_interval check (ratingValue >= 0.0 AND ratingValue <= 5.0),
	PRIMARY KEY(AccountIdFrom , AccountIdTo)
)

CREATE TABLE ApiDatabase.Chat(
	chatId BIGINT IDENTITY PRIMARY KEY,
	AccountIdFirst BIGINT,
	AccountIdSecond BIGINT,
	[version] rowversion

	FOREIGN KEY (accountIdFirst) REFERENCES ApiDatabase.Account(accountID),
	FOREIGN KEY (accountIdSecond) REFERENCES ApiDatabase.Account(accountID),
	UNIQUE (AccountIdFirst, AccountIdSecond)
)


CREATE TABLE ApiDatabase.[MESSAGE](
	chatId BIGINT REFERENCES ApiDatabase.Chat,
	[text] NVARCHAR(200),
	[date] datetime default(getdate()),
	[version] rowversion,

	FOREIGN KEY (chatId) REFERENCES ApiDatabase.Chat(chatId),
)

CREATE TABLE ApiDatabase.Comment (
	CommentId BIGINT identity primary key,
	AccountIdFrom BIGINT,
	AccountIdDest BIGINT,
	[date] DATETIME default(getdate()),
	[text] NVARCHAR(300),
	MainCommentId BIGINT,
	[status] bit,
	[version] rowversion

	FOREIGN KEY (accountIdFrom) REFERENCES ApiDatabase.Account(accountID),
	FOREIGN KEY (accountIdDest) REFERENCES ApiDatabase.Account(accountID),
	FOREIGN KEY (MainCommentId) REFERENCES ApiDatabase.Comment(commentId),
)

CREATE TABLE ApiDatabase.Follows (
	AccountIdFrom BIGINT references ApiDatabase.Account,
	AccountIdDest BIGINT references ApiDatabase.Account,

	FOREIGN KEY (accountIdFrom) REFERENCES ApiDatabase.Account(accountID),
	FOREIGN KEY (accountIdDest) REFERENCES ApiDatabase.Account(accountID),
	primary key (AccountIdFrom,AccountIdDest)
)


GO
CREATE VIEW dbo.Account AS
SELECT accountId,email,rating FROM ApiDatabase.Account
GO

GO
CREATE VIEW dbo.[User] AS
SELECT * FROM ApiDatabase.[User]
GO

GO
CREATE VIEW dbo.Moderator AS
SELECT * FROM ApiDatabase.Moderator
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
CREATE VIEW dbo.Project AS
SELECT * FROM ApiDatabase.Project
GO

GO
CREATE VIEW dbo.Follows AS
SELECT * FROM ApiDatabase.Follows
GO