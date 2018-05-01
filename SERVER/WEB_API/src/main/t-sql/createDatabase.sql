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

CREATE TABLE [Account] (
	accountId BIGINT IDENTITY(1,1) PRIMARY KEY NOT NULL,
	email NVARCHAR(25) UNIQUE NOT NULL,
	rating float(24) default(0.0),
	password NVARCHAR(100) NOT NULL,
	[version] rowversion,

	CHECK (rating >= 0.0 AND rating <= 10.0)
)

CREATE TABLE [Company] (
	accountId BIGINT primary key references Account,
	name NVARCHAR(40),
	yearFounded SMALLINT,
	specialization NVARCHAR(20),
	webPageUrl NVARCHAR(50),
	LogoUrl NVARCHAR(100),
	[description] NVARCHAR(50),
	[version] rowversion,

	FOREIGN KEY (accountID) REFERENCES Account(accountID)
)

CREATE TABLE [Moderator] (
	accountID BIGINT primary key references Account,

	FOREIGN KEY (accountID) REFERENCES Account(accountID)
)

CREATE TABLE [User] (
	accountId BIGINT primary key references Account,
	name NVARCHAR(40),
	summary NVARCHAR(1500),
	PhotoUrl NVARCHAR(100),
	[version] rowversion

	FOREIGN KEY (accountID) REFERENCES Account(accountID)		
)

CREATE TABLE [Curriculum](
	accountId BIGINT references [User],
	curriculumId BIGINT identity primary key,
	title nvarchar(50),
    [version] rowversion

	FOREIGN KEY (accountId) REFERENCES [User] (accountId)
)

CREATE TABLE [Project] (
	accountId BIGINT,
	curriculumId BIGINT,
	name NVARCHAR(15),
	[description] NVARCHAR(50),
	[version] rowversion

	PRIMARY KEY (accountId, curriculumID),
	FOREIGN KEY (curriculumId) REFERENCES curriculum(curriculumId)
)

CREATE TABLE [AcademicBackground](
	accountId BIGINT,
	curriculumId BIGINT,
	beginDate DATETIME DEFAULT(GETDATE()),
	endDate DATETIME,
	studyArea NVARCHAR(40),
	institution NVARCHAR(40),
	degreeObtained NVARCHAR(10),
	[version] rowversion,

	FOREIGN KEY (curriculumId) REFERENCES curriculum(curriculumId),
	PRIMARY KEY(accountId, curriculumId),
	check (endDate < beginDate),
	check (degreeObtained = 'basic level 1' 
			OR degreeObtained = 'basic level 2' 
			OR degreeObtained = 'basic level 3'
			OR degreeObtained = 'secundary'
			OR degreeObtained = 'bachelor'
			OR degreeObtained = 'master'
			OR degreeObtained = 'PHD')				
)

CREATE TABLE [PreviousJobs](
	accountId BIGINT,
	curriculumId BIGINT,
	beginDate DATETIME DEFAULT(GETDATE()),
	endDate DATETIME,
	companyName NVARCHAR(20),
	[workload] NVARCHAR(20),
	[role] NVARCHAR(20),
	[version] rowversion,

	PRIMARY KEY(accountId, curriculumId),
	
	FOREIGN KEY (curriculumId) REFERENCES Curriculum(curriculumId),
	CHECK([workLoad] = 'partial' OR [workLoad] = 'total')
)

CREATE TABLE [Local] (
	[Address] NVARCHAR(50) primary key,
	country NVARCHAR(15),
	zipCode NVARCHAR(40),
    district NVARCHAR(40),
    longitude real,
    latitude real,
	[version] rowversion
)


CREATE TABLE [Job](
	jobId BIGINT identity primary key,
    title nvarchar(50) not null,
	accountId BIGINT,
	schedule NVARCHAR(20),
	wage INT check(wage > 0),
	[description] NVARCHAR(50),
	offerBeginDate DATETIME DEFAULT(GETDATE()),
	offerEndDate DATETIME,
	offerType NVARCHAR(30),
	[Address] NVARCHAR(50),
	[version] rowversion,

	FOREIGN KEY (accountID) REFERENCES Account(accountId),
	check(offerType = 'Looking for work' OR offerType = 'Looking for Worker')
)

CREATE TABLE [CurriculumExperience](
	accountId BIGINT,
	curriculumId BIGINT,
	competences NVARCHAR(50),
	years SMALLINT,
	[version] rowversion

	FOREIGN KEY (curriculumId) REFERENCES Curriculum,

	primary key(accountId, curriculumId)
)

CREATE TABLE [JobExperience](
	jobId BIGINT,
	competences NVARCHAR(50),
	years SMALLINT,
	[version] rowversion

	FOREIGN KEY (jobId) REFERENCES Job(jobId),

	primary key(jobId,competences)
)

CREATE TABLE [Application](
	accountId BIGINT,
	curriculumId BIGINT,
	jobId BIGINT,
	[date] datetime default(GETDATE()),
	[version] rowversion,

	FOREIGN KEY (curriculumId) REFERENCES curriculum(curriculumId),
	FOREIGN KEY (jobId) REFERENCES Job(jobId),
	primary key (accountId, jobId)
)
CREATE TABLE [Rating](
	accountIdFrom BIGINT,
	accountIdTo BIGINT,
	moderatorId BIGINT references Moderator,
	[workLoad] real DEFAULT 0.0  check ([workLoad] >= 0.0 AND [workLoad] <= 10.0),
	wage real DEFAULT 0.0 check (wage >= 0.0 AND wage <= 10.0),
	workEnviroment real DEFAULT 0.0 check (workEnviroment >= 0.0 AND workEnviroment <= 10.0),
	competences real DEFAULT 0.0 check (competences >= 0.0 AND competences <= 10.0),
	ponctuality real DEFAULT 0.0 check (ponctuality >= 0.0 AND ponctuality <= 10.0),
	assiduity real DEFAULT 0.0 check (assiduity>= 0.0 AND assiduity <= 10.0),
	demeanor real DEFAULT 0.0 check (demeanor >= 0.0 AND demeanor <= 10.0),
	[version] rowversion,
	
	FOREIGN KEY (accountIdFrom) REFERENCES Account(accountID),
	FOREIGN KEY (accountIdTo) REFERENCES Account(accountID),

	PRIMARY KEY(AccountIdFrom , AccountIdTo)
)

CREATE TABLE [Chat](
	chatId BIGINT IDENTITY PRIMARY KEY,
	accountIdFirst BIGINT,
	accountIdSecond BIGINT,
	[version] rowversion

	FOREIGN KEY (accountIdFirst) REFERENCES Account(accountID),
	FOREIGN KEY (accountIdSecond) REFERENCES Account(accountID),
	UNIQUE (AccountIdFirst, AccountIdSecond)
)


CREATE TABLE [Message](
	accountId BIGINT,
	messageId BIGINT IDENTITY primary key,
	chatId BIGINT REFERENCES Chat,
	[text] NVARCHAR(200),
	[date] datetime default(getdate()),
	[version] rowversion,

	FOREIGN KEY (chatId) REFERENCES Chat(chatId) ON DELETE CASCADE,
	FOREIGN KEY (accountId) REFERENCES Account ON DELETE CASCADE
)

CREATE TABLE [Comment] (
	commentId BIGINT identity primary key,
	accountIdFrom BIGINT,
	accountIdDest BIGINT,
	[date] DATETIME default(getdate()),
	[text] NVARCHAR(300),
	mainCommentId BIGINT,
	[status] bit,
	[version] rowversion

	FOREIGN KEY (accountIdFrom) REFERENCES Account(accountID),
	FOREIGN KEY (accountIdDest) REFERENCES Account(accountID),
	FOREIGN KEY (MainCommentId) REFERENCES Comment(commentId),
)

CREATE TABLE [Follows] (
	accountIdFollowed BIGINT references Account,
	accountIdFollower BIGINT references Account,
	[version] rowversion

	FOREIGN KEY (accountIdFollowed) REFERENCES Account(accountId),
	FOREIGN KEY (accountIdFollower) REFERENCES Account(accountId),
	primary key (accountIdFollowed, accountIdFollower)
)
GO