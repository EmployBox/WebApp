package isel.ps.employbox;

public class ErrorMessages {



    private ErrorMessages() {
    }
    public static final String INVALID_ACCOUNT_TYPE_IN_RATING = "The account type provided to this rating is invalid";
    public static final String ALREADY_EXISTS = "This relation/item already exists";
    public static final String ALREADY_FOLLOWED = "The follow relation already exists";
    public static final String BAD_REQUEST_IDS_MISMATCH = "Given ids do not match with the ones passed in the body";
    public static final String UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH = "Account authenticated is not allowed to user this resource";
    public static final String UN_AUTHORIZED_T = "";
    public static final String RESOURCE_NOTFOUND = "Resource item wasn´t found";
    public static final String RESOURCE_NOTFOUND_USER = "User wasn't found";
    public static final String RESOURCE_NOTFOUND_ACCOUNT = "Account wasn't found";
    public static final String CONFLIT_USERNAME_TAKEN = "The username provided is already taken";
    public static final String RESOURCE_NOT_FOUND_MESSAGE = "Message wasn´t found";
    public static final String RESOURCE_NOT_FOUND_CHAT = "Chat wasn´t found";
    public static final String UN_AUTHORIZED_MESSAGE = "The chat where you tryed to add a new message doesnt belong to this user";
    public static final String RESOURCE_NOTFOUND_COMMENT = "Comment wasn´t found";
    public static final String RESOURCE_NOTFOUND_JOB = "Job wasn´t found";
    public static final String RESOURCE_NOTFOUND_RATING = "Rating wasn´t found";
    public static final String RESOURCE_NOTFOUND_APPLICATION = "Application wasn´t found";
    public static final String RESOURCE_NOTFOUND_CURRICULUM = "Curriculum wasn´t found";
    public static final String UN_AUTHORIZED_CURRICULUM = "This curriculum doesnt belong to this user";
    public static final String UN_AUTHORIZED_APPLICATION = "This application doesnt belong to this user" ;
    public static final String UN_AUTHORIZED = "The logged in user cannot perform this operation";
    public static final String BAD_REQUEST_ITEM_CREATION = "The item could not be created, check the body before sending the request again";
    public static final String BAD_REQUEST_ITEM_DELETION = "Item could not be deleted, check if the object your are trying to delete can be deleted";
    public static final String BAD_REQUEST_ITEM_UPDATE = "Item could not be updated, check if the object your are trying to update exists or you can update it";
    public static final String BAD_REQUEST_UPDATE_RATING = "You tryed to update a rating without sending the ID of the rated/destiny account ";
    public static final String RESOURCE_NOTFOUND_COMPANY = "Company could not be found";
    public static final String JOB_EXPERIENCE_ITEM_CREATION = "The job experience list you tried to create with a job couldn't be added to the database";
    public static final String RESOURCE_NOTFOUND_PREVIOUS_JOB = "Previous job wasnt found for this curriculum";
    public static final String RESOURCE_NOTFOUND_JOB_EXPERIENCE = "";
    public static final String CHILDS_CREATION = "The item was created with success but some lists belonging to this object could not be added to the DB";
}
