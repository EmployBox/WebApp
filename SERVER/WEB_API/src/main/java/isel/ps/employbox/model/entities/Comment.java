package isel.ps.employbox.model.entities;

import org.github.isel.rapper.DomainObject;
import org.github.isel.rapper.Id;

import java.sql.Date;
import java.util.List;
import java.util.function.Supplier;

public class Comment implements DomainObject<Long>{
    @Id(isIdentity = true)
    private long commentId;

    private final long accountIdFrom;
    private final long accountIdTo;
    private final long parentCommendID;
    private final Date date;
    private final String text;
    private final boolean status; //moderated or not
    private final Supplier<List<Comment>> replies;
    private final long version;

    public Comment(){
        accountIdFrom = 0;
        accountIdTo = 0;
        parentCommendID = 0;
        date = null;
        text = null;
        status = false;
        replies = null;
        version = 0;
    }

    public Comment(long commentID, long accountIdFrom, long accountIdTo, long parentCommendID, Date date, String text, boolean status, Supplier<List<Comment>> replies, long version){
        this.commentId = commentID;
        this.accountIdFrom = accountIdFrom;
        this.accountIdTo = accountIdTo;
        this.parentCommendID = parentCommendID;
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

    public Supplier<List<Comment>> getReplies() {
        return replies;
    }
}
