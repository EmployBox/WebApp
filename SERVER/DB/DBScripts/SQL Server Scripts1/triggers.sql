
--TRIGGERS TO CASCADE DELETE TABLES THAT CANNOT BE DELETED BY SQL USING 'ON DELETE CASCADE' KEYWORD

GO
USE PS_API_DATABASE
GO

GO
CREATE TRIGGER Apidatabase.deleteCascadeAccount
	ON Apidatabase.Account
	INSTEAD OF DELETE
	AS
	BEGIN
		DELETE Apidatabase.[Rating] WHERE Apidatabase.[Rating].AccountIdFrom IN (select deleted.AccountID FROM deleted) OR Apidatabase.[Rating].AccountIdTo IN (select deleted.AccountID FROM deleted)
		DELETE Apidatabase.[Comment] WHERE Apidatabase.[Comment].AccountIdFrom IN (select deleted.AccountID FROM deleted) OR Apidatabase.[Comment].AccountIdDest IN (select deleted.AccountID FROM deleted)
		DELETE ApiDatabase.[Follows] where ApiDatabase.[Follows].AccountIdFrom IN (select deleted.AccountID FROM deleted) OR ApiDatabase.[Follows].AccountIdDest IN (select deleted.AccountID FROM deleted)
		DELETE ApiDatabase.[Chat] where ApiDatabase.[Chat]. AccountIdFirst IN (select deleted.AccountID FROM deleted) OR ApiDatabase.[Chat].AccountIdSecond IN (select deleted.AccountID FROM deleted)
		DELETE Apidatabase.[Curriculum] where Apidatabase.[Curriculum].userId IN (select deleted.AccountID FROM deleted)
		DELETE Apidatabase.[Moderator] where Apidatabase.[Moderator].accountId IN (select deleted.AccountID FROM deleted)
		DELETE Apidatabase.[User] where Apidatabase.[User].accountId IN (select deleted.AccountID FROM deleted)
		DELETE Apidatabase.[Company] where Apidatabase.[Company].accountId IN (select deleted.AccountID FROM deleted)
		DELETE Apidatabase.[Account] where Apidatabase.[Account].accountId IN (select deleted.AccountID FROM deleted)
	END
GO
-----------------------------------------------------------------------------------------------------------------------------------
GO
CREATE TRIGGER  Apidatabase.deleteCascadeCurriculum
	ON Apidatabase.Curriculum
	INSTEAD OF DELETE 
	AS 
	BEGIN
		DELETE ApiDatabase.[Project] WHERE Apidatabase.[Project].curriculumId IN (select deleted.curriculumId FROM deleted)
		DELETE ApiDatabase.[PreviousJobs] WHERE Apidatabase.[PreviousJobs].curriculumId IN (select deleted.curriculumId FROM deleted)
		DELETE Apidatabase.[Curriculum_Experience] WHERE Apidatabase.[Curriculum_Experience].curriculumId IN (select deleted.curriculumId FROM deleted)
		DELETE Apidatabase.[AcademicBackground] WHERE Apidatabase.[AcademicBackground].curriculumId IN (select deleted.curriculumId FROM deleted)
		DELETE Apidatabase.[Application] WHERE Apidatabase.[Application].curriculumId IN (select deleted.curriculumId FROM deleted)
		DELETE Apidatabase.[Curriculum] WHERE Apidatabase.[Curriculum].curriculumId IN (select deleted.curriculumId FROM deleted)
	END
GO
-----------------------------------------------------------------------------------------------------------------------------------
GO
CREATE TRIGGER  Apidatabase.deleteCascadeJob
	ON Apidatabase.[Job]
	INSTEAD OF DELETE 
	AS 
	BEGIN
		DELETE Apidatabase.[Job_Experience] WHERE Apidatabase.[Job_Experience].jobId IN (select deleted.jobId FROM deleted)
		DELETE Apidatabase.[Application] WHERE Apidatabase.[Application].JobId IN (select deleted.jobId FROM deleted)
		DELETE Apidatabase.[Job] WHERE Apidatabase.[Job].JobId IN (select deleted.jobId FROM deleted)
	END
GO

-----------------------------------------------------------------------------------------------------------------------------------

GO
CREATE TRIGGER  Apidatabase.deleteCascadeComment
	ON Apidatabase.[Comment] 
	INSTEAD OF DELETE
	AS 
	BEGIN
		DELETE Apidatabase.[Comment] where Apidatabase.[Comment].MainCommentId IN (select deleted.commentId FROM deleted)
		DELETE Apidatabase.[Comment] where Apidatabase.[Comment].CommentId IN (select deleted.commentId FROM deleted)
	END
GO

