package isel.ps.employbox;

public class ErrorMessages {
    public static String badRequest_IdsMismatch = "Given ids do not match with the ones passed in the body";
    public static String unAuthorized_IdAndEmailMismatch = "Account authenticated is not allowed to user this resource";
    public static String unAuthorized_t = "";
    public static String resourceNotfound_user = "User wasn't found";
    public static String resourceNotfound_account = "Account wasn't found";
    public static String conflit_UsernameTaken = "The username provided is already taken";
    public static String resourceNotFound_message = "Message wasn´t found";
    public static String resourceNotFound_chat = "Chat wasn´t found";
    public static String unAuthorized_message = "The chat where you tryed to add a new message doesnt belong to this user";
    public static String resourceNotfound_comment = "Comment wasn´t found";
    public static String resourceNotfound_job = "Job wasn´t found";
    public static String resourceNotfound_Rating = "Rating wasn´t found";
    public static String resourceNotfound_application = "Application wasn´t found";
    public static String resourceNotfound_curriculum = "Curriculum wasn´t found";
    public static String unAuthorized_curriculum = "This curriculum doesnt belong to this user";
    public static String unAuthorized_application = "This application doesnt belong to this user" ;
    public static String unAuthorized = "The logged user cannot do this operation";
}
