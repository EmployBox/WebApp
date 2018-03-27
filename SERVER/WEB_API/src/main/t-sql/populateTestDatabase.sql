USE PS_TEST_API_DATABASE
GO

EXEC dbo.AddAccount 'Test123@hotmail.com', 0.1, 'dgwuydguaw', null, null

exec dbo.AddUser '123@gmail.com', 2.0, '345', 'Hilário', 'Bue baril', 'someurl', null