IF DB_ID ('PS_API_DATABASE') IS NULL
	CREATE DATABASE PS_API_DATABASE;
GO
IF NOT EXISTS(SELECT * FROM sys.schemas WHERE name = 'ApiDatabase')
BEGIN
	EXEC ('CREATE SCHEMA ApiDatabase')
END

GO
USE PS_API_DATABASE
--USE PS_TEST_API_DATABASE
GO

CREATE TABLE ApiDatabase.[Account] (
	accountId BIGINT IDENTITY PRIMARY KEY,
	email NVARCHAR(25) UNIQUE NOT NULL,
	rating decimal(3,1) default(0.0),
	passwordHash NVARCHAR(50) NOT NULL,
	salt UNIQUEIDENTIFIER NOT NULL,
	[version] rowversion,

	CHECK (rating >= 0.0 AND rating <= 10.0)
)

CREATE TABLE ApiDatabase.[Company] (
	accountId BIGINT primary key references ApiDatabase.Account,
	name NVARCHAR(40),
	yearFounded SMALLINT,
	Specialization NVARCHAR(20),
	WebPageUrl NVARCHAR(50),
	LogoUrl NVARCHAR(100),
	[description] NVARCHAR(50),
	[version] rowversion,

	FOREIGN KEY (accountID) REFERENCES ApiDatabase.Account(accountID)
)

CREATE TABLE ApiDatabase.[Moderator] (
	accountID BIGINT primary key references ApiDatabase.Account,

	FOREIGN KEY (accountID) REFERENCES ApiDatabase.Account(accountID)
)

CREATE TABLE ApiDatabase.[User] (
	accountId BIGINT primary key references ApiDatabase.Account,
	name NVARCHAR(40),
	summary NVARCHAR(1500),
	PhotoUrl NVARCHAR(100),
	[version] rowversion

	FOREIGN KEY (accountID) REFERENCES ApiDatabase.Account(accountID)		
)

CREATE TABLE ApiDatabase.[Curriculum](
	userId BIGINT references ApiDatabase.[User],
	curriculumId BIGINT,

	FOREIGN KEY (userId) REFERENCES ApiDatabase.[User] ( accountId),	
	primary key(userId,curriculumId)
)

CREATE TABLE ApiDatabase.[Project] (
	userId BIGINT,
	curriculumId BIGINT,
	name NVARCHAR(15),
	[description] NVARCHAR(50),
	[version] rowversion

	PRIMARY KEY (userId,curriculumID),
	FOREIGN KEY (userId,curriculumId) REFERENCES ApiDatabase.Curriculum(userId,curriculumId)
)

CREATE TABLE ApiDatabase.[AcademicBackground](
	userId BIGINT,
	curriculumId BIGINT,
	beginDate DATETIME DEFAULT(GETDATE()),
	endDate DATETIME,
	studyArea NVARCHAR(40),
	institution NVARCHAR(40),
	degreeObtained NVARCHAR(10),
	[version] rowversion,

	FOREIGN KEY (userId,curriculumId) REFERENCES ApiDatabase.Curriculum(userId,curriculumId),
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

CREATE TABLE Apidatabase.[PreviousJobs](
	userId BIGINT,
	curriculumId BIGINT,
	beginDate DATETIME DEFAULT(GETDATE()),
	endDate DATETIME,
	companyName NVARCHAR(20),
	[workload] NVARCHAR(20),
	[role] NVARCHAR(20),
	[version] rowversion,

	PRIMARY KEY(userId,curriculumId),
	
	FOREIGN KEY (userId,curriculumId) REFERENCES ApiDatabase.Curriculum(userId,curriculumId),
	CHECK([workLoad] = 'partial' OR [workLoad] = 'total')
)

CREATE TABLE ApiDatabase.[Local] (
	[Address] NVARCHAR(50) primary key,
	Country NVARCHAR(15),
	Street NVARCHAR(40),
	ZIPCode NVARCHAR(40),
	[version] rowversion
)


CREATE TABLE ApiDatabase.[Job](
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

	FOREIGN KEY (accountID) REFERENCES ApiDatabase.Account(accountID),
	FOREIGN KEY ([Address]) REFERENCES ApiDatabase.[Local]([Address]),
	check(offerType = 'Looking for work' OR offerType = 'Looking for Worker')
)

CREATE TABLE Apidatabase.[Curriculum_Experience](
	userId BIGINT,
	curriculumId BIGINT,
	competences NVARCHAR(50),
	years SMALLINT,
	[version] rowversion

	FOREIGN KEY (userId,curriculumId) REFERENCES ApiDatabase.Curriculum,

	primary key(userId, curriculumId)
)

CREATE TABLE Apidatabase.[Job_Experience](
	jobId BIGINT,
	competences NVARCHAR(50),
	years SMALLINT,
	[version] rowversion

	FOREIGN KEY (jobId) REFERENCES ApiDatabase.Job(jobId),

	primary key(jobId,competences)
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
CREATE TABLE ApiDatabase.[Rating](
	AccountIdFrom BIGINT,
	AccountIdTo BIGINT,
	moderatorId BIGINT references ApiDatabase.Moderator,
	[workLoad] decimal(3,1) DEFAULT 0.0  check ([workLoad] >= 0.0 AND [workLoad] <= 10.0),
	wage decimal(3,1) DEFAULT 0.0 check (wage >= 0.0 AND wage <= 10.0),
	workEnviroment decimal(3,1) DEFAULT 0.0 check (workEnviroment >= 0.0 AND workEnviroment <= 10.0),
	competences decimal(3,1) DEFAULT 0.0 check (competences >= 0.0 AND competences <= 10.0),
	ponctuality decimal(3,1) DEFAULT 0.0 check (ponctuality >= 0.0 AND ponctuality <= 10.0),
	assiduity decimal(3,1) DEFAULT 0.0 check (assiduity>= 0.0 AND assiduity <= 10.0),
	demeanor decimal(3,1) DEFAULT 0.0 check (demeanor >= 0.0 AND demeanor <= 10.0),
	[version] rowversion,
	
	FOREIGN KEY (accountIdFrom) REFERENCES ApiDatabase.Account(accountID),
	FOREIGN KEY (accountIdTo) REFERENCES ApiDatabase.Account(accountID),

	PRIMARY KEY(AccountIdFrom , AccountIdTo)
)

CREATE TABLE ApiDatabase.[Chat](
	chatId BIGINT IDENTITY PRIMARY KEY,
	AccountIdFirst BIGINT,
	AccountIdSecond BIGINT,
	[version] rowversion

	FOREIGN KEY (accountIdFirst) REFERENCES ApiDatabase.Account(accountID),
	FOREIGN KEY (accountIdSecond) REFERENCES ApiDatabase.Account(accountID),
	UNIQUE (AccountIdFirst, AccountIdSecond)
)


CREATE TABLE ApiDatabase.[MESSAGE](
	messageId BIGINT IDENTITY,
	chatId BIGINT REFERENCES ApiDatabase.Chat,
	[text] NVARCHAR(200),
	[date] datetime default(getdate()),
	[version] rowversion,

	FOREIGN KEY (chatId) REFERENCES ApiDatabase.Chat(chatId) ON DELETE CASCADE,
	PRIMARY KEY(messageId,chatId)
)

CREATE TABLE ApiDatabase.[Comment] (
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

CREATE TABLE ApiDatabase.[Follows] (
	AccountIdFrom BIGINT references ApiDatabase.Account,
	AccountIdDest BIGINT references ApiDatabase.Account,
	[version] rowversion

	FOREIGN KEY (accountIdFrom) REFERENCES ApiDatabase.Account(accountID),
	FOREIGN KEY (accountIdDest) REFERENCES ApiDatabase.Account(accountID),
	primary key (AccountIdFrom,AccountIdDest)
)
GO