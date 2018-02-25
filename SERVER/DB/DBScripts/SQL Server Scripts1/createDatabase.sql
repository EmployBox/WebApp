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


CREATE TABLE ApiDatabase.[Account] (
	accountId BIGINT IDENTITY PRIMARY KEY,
	email NVARCHAR UNIQUE NOT NULL,
	rating decimal(2,1) default(0.0),
	passwordHash NVARCHAR(50) NOT NULL,
	salt UNIQUEIDENTIFIER NOT NULL,

	CONSTRAINT rate_const CHECK (rating >= 0.0 AND rating <= 5.0)
)

CREATE TABLE ApiDatabase.Company (
	accountId BIGINT IDENTITY primary key references ApiDatabase.[Account],
	name NVARCHAR(40),
	yearFounded SMALLINT,
	Specialization NVARCHAR(20),
	WebPageUrl NVARCHAR(50),
	LogoUrl NVARCHAR(100),
	description NVARCHAR(50)
)

CREATE TABLE ApiDatabase.SiteUser (
	accountId BIGINT IDENTITY primary key references ApiDatabase.[Account],
	name NVARCHAR(40),
	summary NVARCHAR(1500),
	PhotoUrl  NVARCHAR(100)
)
