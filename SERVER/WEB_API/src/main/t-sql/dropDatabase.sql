GO
IF DB_ID ('PS_API_DATABASE') IS NOT NULL
--IF DB_ID ('PS_TEST_API_DATABASE') IS NOT NULL
	BEGIN
		USE PS_API_DATABASE
		--USE PS_TEST_API_DATABASE

		IF OBJECT_ID(N'[Project]') IS NOT NULL
			DROP TABLE [Project];
		IF OBJECT_ID(N'[Follows]') IS NOT NULL
			DROP TABLE [Follows];
		IF OBJECT_ID(N'[Comment]') IS NOT NULL
			DROP TABLE [Comment];
		IF OBJECT_ID(N'[Message]') IS NOT NULL
			DROP TABLE [Message];
		IF OBJECT_ID(N'[Chat]') IS NOT NULL
			DROP TABLE [Chat];
		IF OBJECT_ID(N'[Rating]') IS NOT NULL
			DROP TABLE [Rating];
		IF OBJECT_ID(N'[Application]') IS NOT NULL
			DROP TABLE [Application];
		IF OBJECT_ID(N'[JobExperience]') IS NOT NULL
			DROP TABLE [JobExperience];
		IF OBJECT_ID(N'[CurriculumExperience]') IS NOT NULL
			DROP TABLE [CurriculumExperience];
		IF OBJECT_ID(N'[Job]') IS NOT NULL
			DROP TABLE [Job];
		IF OBJECT_ID(N'[PreviousJobs]') IS NOT NULL
			DROP TABLE [PreviousJobs];
		IF OBJECT_ID(N'[AcademicBackground]') IS NOT NULL
			DROP TABLE [AcademicBackground];
		IF OBJECT_ID(N'[Local]') IS NOT NULL
			DROP TABLE [Local];
		IF OBJECT_ID(N'[Curriculum]') IS NOT NULL
			DROP TABLE [Curriculum];
		IF OBJECT_ID(N'[Moderator]') IS NOT NULL
			DROP TABLE [Moderator];
		IF OBJECT_ID(N'[User]') IS NOT NULL
			DROP TABLE [User];
		IF OBJECT_ID(N'[Company]') IS NOT NULL
			DROP TABLE [Company];
		IF OBJECT_ID(N'[Account]') IS NOT NULL
			DROP TABLE [Account];

		IF OBJECT_ID ('ApiDatabase') IS NOT NULL
			DROP SCHEMA ApiDatabase;
	END
GO