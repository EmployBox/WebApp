GO
IF DB_ID ('PS_API_DATABASE') IS NOT NULL
	BEGIN
		USE PS_API_DATABASE;

		IF OBJECT_ID(N'dbo.Follows') IS NOT NULL
			DROP VIEW dbo.Follows;
		IF OBJECT_ID(N'dbo.[Message]') IS NOT NULL
			DROP VIEW dbo.[Message];
		IF OBJECT_ID(N'dbo.[Local]') IS NOT NULL
			DROP VIEW dbo.[Local];
		IF OBJECT_ID(N'dbo.Chat') IS NOT NULL
			DROP VIEW dbo.Chat;
		IF OBJECT_ID(N'dbo.Comment') IS NOT NULL
			DROP VIEW dbo.Comment;
		IF OBJECT_ID(N'dbo.Job_Experience') IS NOT NULL
			DROP VIEW dbo.Job_Experience;
		IF OBJECT_ID(N'dbo.Curriculum_Experience') IS NOT NULL
			DROP VIEW dbo.Curriculum_Experience;
		IF OBJECT_ID(N'dbo.Experience') IS NOT NULL
			DROP VIEW dbo.Experience;
		IF OBJECT_ID(N'ApiDatabase.AcademicBackground') IS NOT NULL
			DROP VIEW dbo.AcademicBackground;
		IF OBJECT_ID(N'dbo.Curriculum') IS NOT NULL
			DROP VIEW dbo.Curriculum;
		IF OBJECT_ID(N'dbo.Job') IS NOT NULL
			DROP VIEW dbo.Job;
		IF OBJECT_ID(N'dbo.Company') IS NOT NULL
			DROP VIEW dbo.Company;
		IF OBJECT_ID(N'dbo.[User]') IS NOT NULL
			DROP VIEW dbo.[User];
		IF OBJECT_ID(N'dbo.Account') IS NOT NULL
			DROP VIEW dbo.Account;

		IF OBJECT_ID(N'ApiDatabase.Follows') IS NOT NULL
			DROP TABLE ApiDatabase.Follows;
		IF OBJECT_ID(N'ApiDatabase.Comment') IS NOT NULL
			DROP TABLE ApiDatabase.Comment;
		IF OBJECT_ID(N'ApiDatabase.[Local]') IS NOT NULL
			DROP TABLE ApiDatabase.[Local];
		IF OBJECT_ID(N'ApiDatabase.[Message]') IS NOT NULL
			DROP TABLE ApiDatabase.[Message];
		IF OBJECT_ID(N'ApiDatabase.Chat_version') IS NOT NULL
			DROP TABLE ApiDatabase.Chat_version;
		IF OBJECT_ID(N'ApiDatabase.Chat') IS NOT NULL
			DROP TABLE ApiDatabase.Chat;
		IF OBJECT_ID(N'ApiDatabase.Rating') IS NOT NULL
			DROP TABLE ApiDatabase.Rating;
		IF OBJECT_ID(N'ApiDatabase.Job_Experience') IS NOT NULL
			DROP TABLE ApiDatabase.Job_Experience;
		IF OBJECT_ID(N'ApiDatabase.Curriculum_Experience') IS NOT NULL
			DROP TABLE ApiDatabase.Curriculum_Experience;
		IF OBJECT_ID(N'ApiDatabase.Experience') IS NOT NULL
			DROP TABLE ApiDatabase.Experience;
		IF OBJECT_ID(N'ApiDatabase.Job_version') IS NOT NULL
			DROP TABLE ApiDatabase.Job_version;
		IF OBJECT_ID(N'ApiDatabase.Job') IS NOT NULL
			DROP TABLE ApiDatabase.Job;
		IF OBJECT_ID(N'ApiDatabase.PreviousJobs') IS NOT NULL
			DROP TABLE ApiDatabase.PreviousJobs;
		IF OBJECT_ID(N'ApiDatabase.AcademicBackground') IS NOT NULL
			DROP TABLE ApiDatabase.AcademicBackground;
		IF OBJECT_ID(N'ApiDatabase.Curriculum') IS NOT NULL
			DROP TABLE ApiDatabase.Curriculum;
		IF OBJECT_ID(N'ApiDatabase.[User]') IS NOT NULL
			DROP TABLE ApiDatabase.[User];
		IF OBJECT_ID(N'ApiDatabase.Company') IS NOT NULL
			DROP TABLE ApiDatabase.Company;
		IF OBJECT_ID(N'ApiDatabase.Account_version') IS NOT NULL
			DROP TABLE ApiDatabase.Account_version;
		IF OBJECT_ID(N'ApiDatabase.Account') IS NOT NULL
			DROP TABLE ApiDatabase.Account;


		IF OBJECT_ID ('ApiDatabase') IS NOT NULL
			DROP SCHEMA ApiDatabase;
	END
GO