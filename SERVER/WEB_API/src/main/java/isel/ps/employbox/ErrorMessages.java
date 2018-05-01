package isel.ps.employbox;

public class ErrorMessages {
    public static final String badRequest_IdsMismatch = "Given ids do not match with the ones passed in the body";
    public static final String unAuthorized_IdAndEmailMismatch = "Account authenticated is not allowed to user this resource";
    public static final String unAuthorized_t = "";
    public static final String resourceNotfound_user = "User wasn't found";
    public static final String resourceNotfound_account = "Account wasn't found";
    public static final String conflit_UsernameTaken = "The username provided is already taken";
    public static final String resourceNotFound_message = "Message wasn´t found";
    public static final String resourceNotFound_chat = "Chat wasn´t found";
    public static final String unAuthorized_message = "The chat where you tryed to add a new message doesnt belong to this user";
    public static final String resourceNotfound_comment = "Comment wasn´t found";
    public static final String resourceNotfound_job = "Job wasn´t found";
    public static final String resourceNotfound_Rating = "Rating wasn´t found";
    public static final String resourceNotfound_application = "Application wasn´t found";
    public static final String resourceNotfound_curriculum = "Curriculum wasn´t found";
    public static final String unAuthorized_curriculum = "This curriculum doesnt belong to this user";
    public static final String unAuthorized_application = "This application doesnt belong to this user" ;
    public static final String unAuthorized = "The logged in user cannot perform this operation";
    public static final String badRequest_ItemCreation = "The item could not be created, check the body before sending the request again";
    public static final String badRequest_ItemDeletion = "Item could not be deleted, check if the object your are trying to delete can be deleted";
    public static final String badRequest_ItemUpdate = "Item could not be updated, check if the object your are trying to update exists or you can update it";
    public static final String resourceNotfound_company = "Company could not be found";
    public static final String jobExperience_ItemCreation = "The job experience list you tried to create with a job couldn't be added to the database";
}
