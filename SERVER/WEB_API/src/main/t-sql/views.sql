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