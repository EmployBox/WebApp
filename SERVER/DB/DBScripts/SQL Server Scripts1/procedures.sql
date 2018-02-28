GO
USE PS_API_DATABASE
GO

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

        INSERT INTO ApiDatabase.Account(email, rating, passwordHash, salt)
        VALUES(@email, @rating, HASHBYTES('SHA2_512', @password+CAST(@salt AS NVARCHAR(36))), @salt)

	   SET @accountId = SCOPE_IDENTITY()
       SET @responseMessage='Success'

    END TRY
    BEGIN CATCH
        SELECT ERROR_MESSAGE() 
    END CATCH
END


GO
CREATE PROCEDURE dbo.AddUser
	@email NVARCHAR(50),
	@rating decimal(2,1),
    @password NVARCHAR(40),
	@name NVARCHAR(40),
	@summary NVARCHAR(1500),
	@PhotoUrl NVARCHAR(100),
	@accountId BIGINT OUTPUT,
    @responseMessage NVARCHAR(250) OUTPUT
AS
	BEGIN
		BEGIN TRAN
			BEGIN TRY
				SET NOCOUNT ON
				EXEC AddAccount @email, @rating, @password, @accountId OUTPUT, @responseMessage OUTPUT
				SELECT @responseMessage
				SELECT @accountId
				IF(@responseMessage != 'success')
					RETURN
				INSERT INTO [ApiDatabase].[User] values (@accountId, @name , @summary, @PhotoUrl)
				COMMIT
			END TRY
			BEGIN CATCH
				SELECT ERROR_MESSAGE() 
				ROLLBACK
			END CATCH
	END

				