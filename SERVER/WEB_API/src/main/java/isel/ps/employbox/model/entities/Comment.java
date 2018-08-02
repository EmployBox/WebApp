package isel.ps.employbox.model.entities;

import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.annotations.ColumnName;
import com.github.jayield.rapper.annotations.Id;
import com.github.jayield.rapper.annotations.Version;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class Comment implements DomainObject<Long> {
    @Id(isIdentity = true)
    private long commentId;

    private final long accountIdFrom;
    private final long accountIdDest;
    private final long mainCommentId;
    private final Timestamp datetime;
    private final String text;
    private final boolean status; //moderated or not
    @Version
    private final long version;

    @ColumnName(foreignName = "mainCommentId")
    private final Supplier<CompletableFuture<List<Comment>>> replies;

    public Comment(){
        accountIdFrom = 0;
        accountIdDest = 0;
        mainCommentId = 0;
        datetime = null;
        text = null;
        status = false;
        replies = null;
        version = 0;
    }

    public Comment(
            long commentID,
            long accountIdFrom,
            long accountIdTo,
            long parentCommendID,
            Timestamp date,
            String text,
            boolean status,
            List<Comment> replies,
            long version)
    {
        this.commentId = commentID;
        this.accountIdFrom = accountIdFrom;
        this.accountIdDest = accountIdTo;
        this.mainCommentId = parentCommendID;
        this.datetime = date;
        this.text = text;
        this.status = status;
        this.replies = ()-> CompletableFuture.completedFuture(replies);
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

    public Timestamp getDatetime() {
        return datetime;
    }

    public String getText() {
        return text;
    }

    public boolean getStatus() {
        return status;
    }

    public Supplier<CompletableFuture<List<Comment>>> getReplies() {
        return replies;
    }
}
