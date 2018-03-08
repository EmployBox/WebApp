CREATE VIEW dbo.[Account] AS
SELECT accountId,email,rating FROM ApiDatabase.Account
GO

GO
CREATE VIEW dbo.[User] AS
SELECT * FROM ApiDatabase.[User]
GO

GO
CREATE VIEW dbo.[Moderator] AS
SELECT * FROM ApiDatabase.Moderator
GO

GO
CREATE VIEW dbo.[Company] AS
SELECT * FROM ApiDatabase.Company
GO

GO
CREATE VIEW dbo.[Job] AS
SELECT * FROM ApiDatabase.Job
GO

GO
CREATE VIEW dbo.[Curriculum] AS
SELECT * FROM ApiDatabase.Curriculum
GO

GO
CREATE VIEW dbo.[AcademicBackground] AS
SELECT * FROM ApiDatabase.AcademicBackground
GO

GO
CREATE VIEW dbo.[Experience] AS
SELECT * FROM ApiDatabase.Experience
GO

GO
CREATE VIEW dbo.[Curriculum_Experience] AS
SELECT * FROM ApiDatabase.Curriculum_Experience
GO

GO
CREATE VIEW dbo.[Job_Experience] AS
SELECT * FROM ApiDatabase.Job_Experience
GO

GO
CREATE VIEW dbo.[Comment] AS
SELECT * FROM ApiDatabase.Comment
GO

GO
CREATE VIEW dbo.[Chat] AS
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
CREATE VIEW dbo.[Project] AS
SELECT * FROM ApiDatabase.Project
GO

GO
CREATE VIEW dbo.[Follows] AS
SELECT * FROM ApiDatabase.Follows
GO