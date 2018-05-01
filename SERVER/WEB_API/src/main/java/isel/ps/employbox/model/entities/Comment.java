package isel.ps.employbox.model.entities;

import com.github.jayield.rapper.ColumnName;
import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.Id;

import java.sql.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Comment implements DomainObject<Long> {
    @Id(isIdentity = true)
    private long commentId;

    private final long accountIdFrom;
    private final long accountIdDest;
    private final long mainCommentId;
    private final Date date;
    private final String text;
    private final boolean status; //moderated or not
    private final long version;

    @ColumnName(foreignName = "mainCommentId")
    private final CompletableFuture<List<Comment>> replies;

    public Comment(){
        accountIdFrom = 0;
        accountIdDest = 0;
        mainCommentId = 0;
        date = null;
        text = null;
        status = false;
        replies = null;
        version = 0;
    }

    public Comment(long commentID, long accountIdFrom, long accountIdTo, long parentCommendID, Date date, String text, boolean status, CompletableFuture<List<Comment>> replies, long version){
        this.commentId = commentID;
        this.accountIdFrom = accountIdFrom;
        this.accountIdDest = accountIdTo;
        this.mainCommentId = parentCommendID;
        this.date = date;
        this.text = text;
        this.status = status;
        this.replies = replies;
        this.version = version;
    }

    public boolean isStatus() {
        return status;
    }

    @Override
    public Long getIdentityKey() {
        return commentId;
    }

    public long getVersion() {
        return version;
    }

    public long getAccountIdFrom() {
        return accountIdFrom;
    }

    public long getAccountIdDest() {
        return accountIdDest;
    }

    public long getMainCommendID() {
        return mainCommentId;
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

    public CompletableFuture<List<Comment>> getReplies() {
        return replies;
    }
}
