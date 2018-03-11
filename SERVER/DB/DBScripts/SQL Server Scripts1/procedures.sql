GO
--USE PS_API_DATABASE
USE PS_TEST_API_DATABASE
GO


if object_id('dbo.AddAccount') is not null
	drop procedure dbo.AddAccount
go
CREATE PROCEDURE dbo.AddAccount
    @email NVARCHAR(50),
	@rating decimal(2,1),
    @password NVARCHAR(40),
	@accountId BIGINT OUTPUT
AS
BEGIN
    SET NOCOUNT ON

    DECLARE @salt UNIQUEIDENTIFIER=NEWID()
    

	INSERT INTO ApiDatabase.Account(email, rating, passwordHash, salt)
	VALUES(@email, @rating, HASHBYTES('SHA2_512', @password+CAST(@salt AS NVARCHAR(36))), @salt)

	SET @accountId = SCOPE_IDENTITY()
     
END
GO


------------------------------------------------------------------------------------------------------------------
if object_id('dbo.getNewPasswordHash') is not null
	drop procedure dbo.getNewPasswordHash
go
CREATE PROCEDURE dbo.getNewPasswordHash
	@accountId BIGINT,
	@newPassword NVARCHAR(40),
	@newPasswordHash NVARCHAR(40) OUTPUT,
	@responseMessage NVARCHAR(250) OUTPUT
	AS
		SET NOCOUNT ON
		DECLARE @SALT UNIQUEIDENTIFIER = NULL
		SELECT @SALT = salt from ApiDatabase.Account WHERE ApiDatabase.[Account].accountId = @accountId
		SET @newPasswordHash = HASHBYTES('SHA2_512', @newPassword+CAST(@salt AS NVARCHAR(36)))
		if(@SALT != NULL AND @newPasswordHash = NULL)
			SET @responseMessage = 'error hashing the password' 
go


------------------------------------------------------------------------------------------------------------------
if object_id('dbo.AddUser') is not null
	drop procedure dbo.AddUser
go
CREATE PROCEDURE dbo.AddUser
	@email NVARCHAR(50),
	@rating decimal(2,1),
    @password NVARCHAR(40),
	@name NVARCHAR(40),
	@summary NVARCHAR(1500),
	@PhotoUrl NVARCHAR(100),
	@accountId BIGINT OUTPUT,
    @version bigint output
AS
	BEGIN
		BEGIN TRAN
			BEGIN TRY
				SET NOCOUNT ON
				EXEC AddAccount @email, @rating, @password, @accountId OUTPUT
				INSERT INTO ApiDatabase.[User](accountId, name, summary, PhotoUrl) values (@accountId, @name , @summary, @PhotoUrl)
				set @version = (select [version] from ApiDatabase.Account where accountId = @accountId)
			END TRY
			BEGIN CATCH
				ROLLBACK;
				throw
			END CATCH
		COMMIT TRAN
	END
GO


------------------------------------------------------------------------------------------------------------------

if object_id('dbo.UpdateUser') is not null
	drop procedure dbo.UpdateUser
go
CREATE PROCEDURE dbo.UpdateUser
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
				select @accountId = accountId from Apidatabase.[Account] where Apidatabase.[Account].email = @email
				DECLARE @newPasswordHash NVARCHAR(40) = NULL
				EXEC getNewPasswordHash @accountId, @password, @newPasswordHash, @responseMessage
				IF(@responseMessage != NULL)
					RETURN

				UPDATE Apidatabase.[Account] SET email = @email, rating = @rating, passwordHash = @newPasswordHash where Apidatabase.[Account].email = @email

				
				UPDATE ApiDatabase.[User] SET name = @name, summary = @summary, PhotoUrl = @PhotoUrl where Apidatabase.[User].accountId = @accountId
				COMMIT
			END TRY
			BEGIN CATCH
				IF(@responseMessage != NULL)
					SET @responseMessage = ERROR_MESSAGE() 
				ROLLBACK
			END CATCH
	END
GO


------------------------------------------------------------------------------------------------------------------

if object_id('dbo.DeleteUser') is not null
	drop procedure dbo.DeleteUser

GO
CREATE PROCEDURE dbo.DeleteUser
	@accountId BIGINT,
    @responseMessage NVARCHAR(250) OUTPUT
	AS
	BEGIN
		BEGIN TRAN
			BEGIN TRY
				SET NOCOUNT ON
				DELETE ApiDatabase.[User] where Apidatabase.[User].accountId = accountId
				DELETE Apidatabase.[Account] where Apidatabase.[Account].accountId = @accountId
				COMMIT
			END TRY
			BEGIN CATCH
				SET @responseMessage = ERROR_MESSAGE() 
				ROLLBACK
			END CATCH
	END
GO

------------------------------------------------------------------------------------------------------------------

if object_id('dbo.AddCompany') is not null
	drop procedure dbo.AddCompany
go
CREATE PROCEDURE dbo.AddCompany
	@email NVARCHAR(50),
	@rating decimal(2,1),
    @password NVARCHAR(40),
	@name NVARCHAR(40),
	@yearFounded SMALLINT,
	@Specialization NVARCHAR(20),
	@WebPageUrl NVARCHAR(50),
	@LogoUrl NVARCHAR(100),
	@description NVARCHAR(50),
	@accountId BIGINT OUTPUT,
    @responseMessage NVARCHAR(250) OUTPUT
AS
	BEGIN
		BEGIN TRAN
			BEGIN TRY
				SET NOCOUNT ON
				EXEC AddAccount @email, @rating, @password, @accountId OUTPUT, @responseMessage OUTPUT
				IF(@responseMessage != 'success')
					RETURN
				INSERT INTO ApiDatabase.[Company](
						accountId,
						name, 
						yearFounded, 
						Specialization,
						WebPageUrl,
						LogoUrl,
						[description]
				) 
				values (
						@accountId,
						@name , 
						@yearFounded, 
						@Specialization,
						@WebPageUrl,
						@LogoUrl,
						@description
				)
				COMMIT
			END TRY
			BEGIN CATCH
				SET @responseMessage = ERROR_MESSAGE() 
				ROLLBACK
			END CATCH
	END
GO

--------------------------------------------------------------------------------------------------------------------

if object_id('dbo.UpdateCompany') is not null
	drop procedure dbo.UpdateCompany
go
CREATE PROCEDURE dbo.UpdateCompany
	@email NVARCHAR(50),
	@rating decimal(2,1),
    @password NVARCHAR(40),
	@name NVARCHAR(40),
	@yearFounded SMALLINT,
	@Specialization NVARCHAR(20),
	@WebPageUrl NVARCHAR(50),
	@LogoUrl NVARCHAR(100),
	@description NVARCHAR(50),
	@accountId BIGINT OUTPUT,
    @responseMessage NVARCHAR(250) OUTPUT
AS
	BEGIN
		BEGIN TRAN
			BEGIN TRY
				SET NOCOUNT ON
				select @accountId = accountId from Apidatabase.[Account] where Apidatabase.[Account].email = @email
				DECLARE @newPasswordHash NVARCHAR(40) = NULL
				EXEC getNewPasswordHash @accountId, @password, @newPasswordHash, @responseMessage
				IF(@responseMessage != NULL)
					RETURN

				UPDATE Apidatabase.[Account] 
					SET 
						email = @email, 
						rating = @rating, 
						passwordHash = @newPasswordHash
					WHERE 
						Apidatabase.[Account].email = @email

				select @accountId = accountId from Apidatabase.[Account] where Apidatabase.[Account].email = @email

				UPDATE ApiDatabase.[Company] 
					SET
						accountId		 =   @accountId,
						name			 = 	 @name , 
						yearFounded		 = 	 @yearFounded, 
						Specialization	 = 	 @Specialization,
						WebPageUrl		 = 	 @WebPageUrl,
						LogoUrl			 = 	 @LogoUrl,
						[description]	 = 	 @description
					WHERE 
						Apidatabase.[Company].accountId = @accountId

				COMMIT
			END TRY
			BEGIN CATCH
				SET @responseMessage = ERROR_MESSAGE() 
				ROLLBACK
			END CATCH
	END
GO


--------------------------------------------------------------------------------------------------------------------

if object_id('dbo.DeleteCompany') is not null
	drop procedure dbo.DeleteCompany
go

CREATE PROCEDURE dbo.DeleteCompany
	@accountId BIGINT,
    @responseMessage NVARCHAR(250) OUTPUT
	AS
	BEGIN
		BEGIN TRAN
			BEGIN TRY
				SET NOCOUNT ON
				DELETE ApiDatabase.[Account] where Apidatabase.[Account].accountId = @accountId
				DELETE Apidatabase.[Company] where Apidatabase.[Company].accountId = @accountId
				COMMIT
			END TRY
			BEGIN CATCH
				SET @responseMessage = ERROR_MESSAGE() 
				ROLLBACK
			END CATCH
	END
GO



------------------------------------------------------------------------------------------------------------------
if object_id('dbo.AddModerator') is not null
	drop procedure dbo.AddModerator
go
CREATE PROCEDURE dbo.AddModerator
	@email NVARCHAR(50),
	@rating decimal(2,1),
    @password NVARCHAR(40),
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
				INSERT INTO ApiDatabase.[Moderator](accountId) values (@accountId)
				COMMIT
			END TRY
			BEGIN CATCH
				SET @responseMessage = ERROR_MESSAGE() 
				ROLLBACK
			END CATCH
	END
GO


------------------------------------------------------------------------------------------------------------------

if object_id('dbo.UpdateModerator') is not null
	drop procedure dbo.UpdateModerator
go
CREATE PROCEDURE dbo.UpdateModerator
	@email NVARCHAR(50),
	@rating decimal(2,1),
    @password NVARCHAR(40),
	@accountId BIGINT OUTPUT,
    @responseMessage NVARCHAR(250) OUTPUT
	AS
	BEGIN
		BEGIN TRAN
			BEGIN TRY
				select @accountId = accountId from Apidatabase.[Account] where Apidatabase.[Account].email = @email
				DECLARE @newPasswordHash NVARCHAR(40) = NULL
				EXEC getNewPasswordHash @accountId, @password, @newPasswordHash, @responseMessage
				IF(@responseMessage != NULL)
					RETURN

				UPDATE Apidatabase.[Account] SET email = @email, rating = @rating, passwordHash = @newPasswordHash where Apidatabase.[Account].email = @email
				COMMIT
			END TRY
			BEGIN CATCH
				SET @responseMessage = ERROR_MESSAGE() 
				ROLLBACK
			END CATCH
	END
GO


--------------------------------------------------------------------------------------------------------------------

if object_id('dbo.DeleteModerator') is not null
	drop procedure dbo.DeleteModerator

go
CREATE PROCEDURE dbo.DeleteModerator
	@accountId BIGINT,
    @responseMessage NVARCHAR(250) OUTPUT
	AS
	BEGIN
		BEGIN TRAN
			BEGIN TRY
				SET NOCOUNT ON
				DELETE ApiDatabase.[Account] where Apidatabase.[Account].accountId = @accountId
				DELETE Apidatabase.[Moderator] where Apidatabase.[Moderator].accountId = @accountId
				COMMIT
			END TRY
			BEGIN CATCH
				SET @responseMessage = ERROR_MESSAGE() 
				ROLLBACK
			END CATCH
	END
GO