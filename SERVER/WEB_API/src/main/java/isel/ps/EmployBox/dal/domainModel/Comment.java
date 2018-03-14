package isel.ps.EmployBox.dal.domainModel;

import isel.ps.EmployBox.dal.util.Streamable;

import java.sql.Date;
import java.util.stream.Stream;

public class Comment extends DomainObject<Long> {
    @ID(isIdentity = true)
    private long commentID;
    private final long accountIdFrom;
    private final long accountIdTo;
    private final long parentCommendID;
    private final Date date;
    private final String text;
    private final boolean status; //moderated or not

    private final Streamable<Comment> replies;

    public Comment(long commentID, long accountIdFrom, long accountIdTo, long parentCommendID, Date date, String text, boolean status, Streamable<Comment> replies, long version){
        super(commentID, version);
        this.commentID = commentID;
        this.accountIdFrom = accountIdFrom;
        this.accountIdTo = accountIdTo;
        this.parentCommendID = parentCommendID;
        this.date = date;
        this.text = text;
        this.status = status;
        this.replies = replies;
    }


    public static Comment create (
                                long accountIdFrom ,
                                long accountIdTo,
                                long parentCommendID,
                                Date date,
                                String text,
                                boolean status,
                                Streamable<Comment> replies)
    {
        Comment comment = new Comment(defaultKey, accountIdFrom, accountIdTo, parentCommendID, date , text, status, replies,0);
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
                               Streamable<Comment> replies,
                               long version)
    {
       Comment comment = new Comment(commentID, accountIdFrom, accountIdTo, parentCommendID, date , text, status, replies, version);
       comment.markClean();
       return comment;
    }

    public static Comment update(Comment comment, String text, boolean status, Streamable<Comment> replies){
        comment.markToBeDirty();
        Comment newComment = new Comment(
                comment.getCommentID(),
                comment.getAccountIdFrom(),
                comment.getAccountIdTo(),
                comment.getMainCommendID(),
                comment.getDate(),
                text,
                status,
                replies,
                comment.getNextVersion()
        );
        newComment.markDirty();
        return newComment;
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

    public long getMainCommendID() {
        return parentCommendID;
    }

    public Date getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public boolean getStatus() {
        return status;
    }

    public Stream<Comment> getReplies() {
        return replies.get().stream();
    }
}
