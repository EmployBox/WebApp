package model;

import java.sql.Date;

public class Comment extends DomainObject<Long> {
    private final long commentID;
    private final long accountIdFrom;
    private final long accountIdTo;
    private final long parentCommendID;
    private final Date date;
    private final String text;
    private final boolean status; //moderated or not

    public Comment(long commentID, long accountIdFrom ,long accountIdTo, long parentCommendID, Date date, String text, boolean status, long version){
        super(commentID, (long) -1, version);
        this.commentID = commentID;
        this.accountIdFrom = accountIdFrom;
        this.accountIdTo = accountIdTo;
        this.parentCommendID = parentCommendID;
        this.date = date;
        this.text = text;
        this.status = status;
    }


    public static Comment create (
                                long accountIdFrom ,
                                long accountIdTo,
                                long parentCommendID,
                                Date date,
                                String text,
                                boolean status)
    {
        Comment comment = new Comment(-1, accountIdFrom, accountIdTo, parentCommendID, date , text, status, 0);
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
}
