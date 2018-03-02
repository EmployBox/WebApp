package model;

import java.sql.Date;

public class Comment extends DomainObject {
    private final long commentID;
    private final long accountIdFrom;
    private final long accountIdTo;
    private final long parentCommendID;
    private final Date date;
    private final String text;
    private final boolean status; //moderated or not
    private final long version;

    public Comment(long commentID, long accountIdFrom ,long accountIdTo, long parentCommendID, Date date, String text, boolean status, long version){
        super(commentID);
        this.commentID = commentID;
        this.accountIdFrom = accountIdFrom;
        this.accountIdTo = accountIdTo;
        this.parentCommendID = parentCommendID;
        this.date = date;
        this.text = text;
        this.status = status;
        this.version = version;
    }


    public static Comment create (
                                long accountIdFrom ,
                                long accountIdTo,
                                long parentCommendID,
                                Date date,
                                String text,
                                boolean status,
                                long version)
    {
        Comment comment = new Comment(-1, accountIdFrom, accountIdTo, parentCommendID, date , text, status, version);
        comment.markNew();
        return comment;
    }

    public static Comment load (long commentID,
                               long accountIdFrom ,
                               long accountIdTo,
                               long parentCommendID,
                               Date date,
                               String text,
                               boolean status,
                               long version)
    {
       Comment comment = new Comment(commentID, accountIdFrom, accountIdTo, parentCommendID, date , text, status, version);
       comment.markClean();
       return comment;
    }


    public long getCommentID() {
        return commentID;
    }

    public long getAccountIdFrom() {
        return accountIdFrom;
    }

    public long getAccountIdTo() {
        return accountIdTo;
    }

    public long getParentCommendID() {
        return parentCommendID;
    }

    public Date getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public boolean isStatus() {
        return status;
    }

    @Override
    public long getVersion() {
        return version;
    }
}
