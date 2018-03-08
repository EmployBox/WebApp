declare @accountId BIGINT
declare @err NVARCHAR(20)

exec dbo.AddAccount 'test', 2, '123', @accountId, @err

declare @accountId BIGINT
declare @err2 BIGINT
exec dbo.AddUser 'test2', 2, '123','firstUser','DO U KNOW THE WEY?','WWW.SLBENFICA.PT/ÁGUIA.PNG', @accountId, @err2