package isel.ps.employbox.model.entities;

import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.annotations.ColumnName;
import com.github.jayield.rapper.annotations.Id;
import com.github.jayield.rapper.annotations.Version;
import com.github.jayield.rapper.unitofwork.UnitOfWork;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class Comment implements DomainObject<Long> {
    @Id(isIdentity = true)
    private long commentId;

    private final long accountIdFrom;
    private final long accountIdDest;
    private final Long mainCommentId;
    private final Instant datetime;
    private final String text;
    private final boolean status; //moderated or not
    @Version
    private final long version;

    @ColumnName(foreignName = "mainCommentId")
    private final Function<UnitOfWork, CompletableFuture<List<Comment>>> replies;

    public Comment(){
        accountIdFrom = 0;
        accountIdDest = 0;
        mainCommentId = 0L;
        datetime = null;
        text = null;
        status = false;
        replies = null;
        version = 0;
    }

    public Comment(
            long commentId,
            long accountIdFrom,
            long accountIdTo,
            long parentCommendID,
            Instant date,
            String text,
            boolean status,
            List<Comment> replies,
            long version)
    {
        this.commentId = commentId;
        this.accountIdFrom = accountIdFrom;
        this.accountIdDest = accountIdTo;
        this.mainCommentId = parentCommendID;
        this.datetime = date;
        this.text = text;
        this.status = status;
        this.replies = (__)-> CompletableFuture.completedFuture(replies);
        this.version = version;
    }

    public Comment(
            long commentId,
            long accountIdFrom,
            long accountIdTo,
            Long parentCommendID,
            Timestamp date,
            String text,
            boolean status,
            long version)
    {
        this.commentId = commentId;
        this.accountIdFrom = accountIdFrom;
        this.accountIdDest = accountIdTo;
        this.mainCommentId = parentCommendID;
        System.out.println("date " + date);
        if(date != null)
            this.datetime = date.toInstant();
        else
            this.datetime = null;
        this.text = text;
        this.status = status;
        this.replies = null;
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
        return mainCommentId != null ? mainCommentId.longValue() : 0;
    }

    public Instant getDatetime() {
        return datetime;
    }

    public String getText() {
        return text;
    }

    public boolean getStatus() {
        return status;
    }

    public Function<UnitOfWork, CompletableFuture<List<Comment>>> getReplies() {
        return replies;
    }
}
