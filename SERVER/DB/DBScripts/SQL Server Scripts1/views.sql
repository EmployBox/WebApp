use PS_API_DATABASE
--use PS_TEST_API_DATABASE

if object_id('dbo.[Account]') is not null
	drop view dbo.[Account]
go
CREATE VIEW dbo.[Account] AS
SELECT accountId,email,rating FROM ApiDatabase.Account
GO

if object_id('dbo.[User]') is not null
	drop view dbo.[User]
go
CREATE VIEW dbo.[User] AS
SELECT u.accountId, u.name, u.summary, u.PhotoUrl, a.email, a.passwordHash as [password], a.rating, CAST(u.[version] AS bigint) [version]
FROM ApiDatabase.[User] u
inner join ApiDatabase.[Account] a
on a.accountId = u.accountId
GO

if object_id('dbo.[Moderator]') is not null
	drop view dbo.[Moderator]
go
CREATE VIEW dbo.[Moderator] AS
SELECT * FROM ApiDatabase.Moderator
GO

if object_id('dbo.[Company]') is not null
	drop view dbo.[Company]
go
CREATE VIEW dbo.[Company] AS
SELECT * FROM ApiDatabase.Company
GO

if object_id('dbo.[Job]') is not null
	drop view dbo.[Job]
go
CREATE VIEW dbo.[Job] AS
SELECT * FROM ApiDatabase.Job
GO

if object_id('dbo.[Curriculum]') is not null
	drop view dbo.[Curriculum]
go
CREATE VIEW dbo.[Curriculum] AS
SELECT * FROM ApiDatabase.Curriculum
GO

if object_id('dbo.[AcademicBackground]') is not null
	drop view dbo.[AcademicBackground]
go
CREATE VIEW dbo.[AcademicBackground] AS
SELECT * FROM ApiDatabase.AcademicBackground
GO

if object_id('dbo.[Curriculum_Experience]') is not null
	drop view dbo.[Curriculum_Experience]
go
CREATE VIEW dbo.[Curriculum_Experience] AS
SELECT * FROM ApiDatabase.Curriculum_Experience
GO

if object_id('dbo.[Job_Experience]') is not null
	drop view dbo.[Job_Experience]
go
CREATE VIEW dbo.[Job_Experience] AS
SELECT * FROM ApiDatabase.Job_Experience
GO

if object_id('dbo.[Comment]') is not null
	drop view dbo.[Comment]
go
CREATE VIEW dbo.[Comment] AS
SELECT * FROM ApiDatabase.Comment
GO

if object_id('dbo.[Chat]') is not null
	drop view dbo.[Chat]
go
CREATE VIEW dbo.[Chat] AS
SELECT * FROM ApiDatabase.Chat
GO

if object_id('dbo.[Local]') is not null
	drop view dbo.[Local]
go
CREATE VIEW dbo.[Local] AS
SELECT * FROM ApiDatabase.[Local]
GO

if object_id('dbo.[Message]') is not null
	drop view dbo.[Message]
go
CREATE VIEW dbo.[Message] AS
SELECT * FROM ApiDatabase.[Message]
GO

if object_id('dbo.[Project]') is not null
	drop view dbo.[Project]
go
CREATE VIEW dbo.[Project] AS
SELECT * FROM ApiDatabase.Project
GO

if object_id('dbo.[Follows]') is not null
	drop view dbo.[Follows]
go
CREATE VIEW dbo.[Follows] AS
SELECT * FROM ApiDatabase.Follows
GO

if object_id('dbo.[CompanyRating]') is not null
	drop view dbo.[CompanyRating]
go

CREATE VIEW dbo.[CompanyRating] AS
SELECT [workload],competences, workEnviroment, wage,tb2.ratingsAverage FROM ApiDatabase.[Rating] as tb1  inner join
	(select AVG(([workload]+competences+workEnviroment+wage)/4) as ratingsAverage,AccountIdFrom from ApiDatabase.Rating group by ApiDatabase.Rating.AccountIdFrom)as tb2
	on tb1.AccountIdFrom = tb2.AccountIdFrom
go

if object_id('dbo.[PersonRating]') is not null
	drop view dbo.[PersonRating]
go
CREATE VIEW dbo.[PersonRating] AS
 SELECT ponctuality, assiduity, demeanor, tb2.ratingsAverage FROM ApiDatabase.[Rating] as tb1  inner join
	(select AVG((ponctuality + assiduity + demeanor)/3) as ratingsAverage,AccountIdFrom from ApiDatabase.Rating group by ApiDatabase.Rating.AccountIdFrom)as tb2
	on tb1.AccountIdFrom = tb2.AccountIdFrom
go

GO