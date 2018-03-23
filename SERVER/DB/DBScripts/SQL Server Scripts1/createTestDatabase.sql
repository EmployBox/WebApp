IF DB_ID ('PS_TEST_API_DATABASE') IS NULL
	CREATE DATABASE PS_TEST_API_DATABASE;
GO

use PS_TEST_API_DATABASE

create table Person (
	nif int primary key,
	[name] nvarchar,
	birthday date
)