GO
USE PS_API_DATABASE
GO

begin tran
	declare @accountId BIGINT
	declare @version rowversion
	exec dbo.AddUser 'maria@gmail.com', 2, '123', 'Maria', 'Finalista do curso', 'someurl', @accountId out, @version out
	select @accountId ID, @version [Version]
rollback

/** Utils **/
select * from ApiDatabase.[Account]
select * from ApiDatabase.[User]
delete from ApiDatabase.Account where ApiDatabase.Account.accountId = 1
