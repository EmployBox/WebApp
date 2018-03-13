
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
		DELETE Apidatabase.[Comment] WHERE Apidatabase.[Comment].AccountIdFrom IN (select deleted.AccountID FROM deleted) OR Apidatabase.[Comment].AccountIdTo IN (select deleted.AccountID FROM deleted)
		DELETE ApiDatabase.[Follows] where ApiDatabase.[Follows].AccountIdFrom IN (select deleted.AccountID FROM deleted) OR ApiDatabase.[Follows].AccountIdTo IN (select deleted.AccountID FROM deleted)
		DELETE ApiDatabase.[Chat] where ApiDatabase.[Chat]. AccountIdFirst IN (select deleted.AccountID FROM deleted) OR ApiDatabase.[Chat].AccountIdSecond IN (select deleted.AccountID FROM deleted)
		DELETE Apidatabase.[Moderator] where Apidatabase.[Moderator].accountId IN (select deleted.AccountID FROM deleted)
		DELETE Apidatabase.[User] where Apidatabase.[User].accountId IN (select deleted.AccountID FROM deleted)
		DELETE Apidatabase.[Company] where Apidatabase.[Company].accountId IN (select deleted.AccountID FROM deleted)
		DELETE Apidatabase.[Account] where Apidatabase.[Account].accountId IN (select deleted.AccountID FROM deleted)
	END

GO
CREATE TRIGGER  Apidatabase.deleteCascadeJob
	ON Apidatabase.[Job]
	INSTEAD OF DELETE 
	AS 
	BEGIN
		DELETE Apidatabase.[Application] WHERE Apidatabase.[Application].JobId = deleted.jobId
	END

GO
CREATE TRIGGER  Apidatabase.deleteCascadeComment
	ON Apidatabase.[Comment] 
	BEFORE DELETE
	AS 
	BEGIN
		DELETE Apidatabase.[Comment] where deleted.MainCommentId = Apidatabase.[Comment].commentId
	END
GO

CREATE TRIGGER  Apidatabase.deleteCascadeCurriclum
	ON Apidatabase.Curriculum
	BEFORE DELETE 
	AS 
	DELETE Apidatabase.[Application] WHERE Apidatabase.[Application].curriculumId = deleted.curriculumId

