GO
USE 'PS_API_DATABASE'
GO

CREATE PROCEDURE dbo.AddAccount
    @accountId BIGINT, 
    @email NVARCHAR(50),
	@rating decimal(2,1),
    @password NVARCHAR(40),
	@salt UNIQUEIDENTIFIER,
    @responseMessage NVARCHAR(250) OUTPUT
AS
BEGIN
    SET NOCOUNT ON

    DECLARE @salt UNIQUEIDENTIFIER=NEWID()
    BEGIN TRY

        INSERT INTO dbo.[Account] (accountId,email, rating, passwordHash, salt)
        VALUES(@pLogin, HASHBYTES('SHA2_512', @password+CAST(@salt AS NVARCHAR(36))), @salt)

       SET @responseMessage='Success'

    END TRY
    BEGIN CATCH
        SET @responseMessage=ERROR_MESSAGE() 
    END CATCH

END
	