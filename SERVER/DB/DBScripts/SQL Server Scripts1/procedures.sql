GO
USE PS_API_DATABASE
GO

if object_id('dbo.AddAccount') is not null
	drop procedure dbo.AddAccount
go

CREATE PROCEDURE dbo.AddAccount
    @email NVARCHAR(50),
	@rating decimal(2,1),
    @password NVARCHAR(40),
	@accountId BIGINT OUTPUT,
    @responseMessage NVARCHAR(250) OUTPUT
AS
BEGIN
    SET NOCOUNT ON

    DECLARE @salt UNIQUEIDENTIFIER=NEWID()
    BEGIN TRY

        INSERT INTO ApiDatabase.Account
        VALUES(@email, @rating, HASHBYTES('SHA2_512', @password+CAST(@salt AS NVARCHAR(36))), @salt)

		SELECT @accountId = SCOPE_IDENTITY()
       SET @responseMessage='Success'

    END TRY
    BEGIN CATCH
        SELECT ERROR_MESSAGE() 
    END CATCH

END