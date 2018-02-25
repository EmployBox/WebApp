IF DB_ID ('PS_API_DATABASE') IS NULL
	CREATE DATABASE PS_API_DATABASE;
GO

IF NOT EXISTS(SELECT * FROM sys.schemas WHERE name = 'ApiDatabase')
BEGIN
	EXEC ('CREATE SCHEMA ApiDatabase')
END

GO
USE 'PS_API_DATABASE'
GO


CREATE TABLE ApiDatabase.[Account] (
	account_id INT IDENTITY PRIMARY KEY,
	email TEXT UNIQUE NOT NULL,
	rating decimal(2,1) CONSTRAINT rate_const default(0.0),
	password_hash NVARCHAR(50) NOT NULL,
	salt UNIQUEIDENTIFIER NOT NULL

	CONSTRAINT rate_const CHECK (rating >= 0.0 AND rating <= 5.0)
)