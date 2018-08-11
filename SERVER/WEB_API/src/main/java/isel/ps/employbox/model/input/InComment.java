package isel.ps.employbox.model.input;

import java.time.Instant;
import java.util.List;

public class InComment {
    private long accountIdFrom;
    private long accountIdTo;
    private long commmentId;
    private long mainCommentId;
    private boolean status;
    private Instant datetime;
    private String text;
    private List<InComment> replies;
    private long version;

    public long getAccountIdFrom() {
        return accountIdFrom;
    }

    public void setAccountIdFrom(long accountIdFrom) {
        this.accountIdFrom = accountIdFrom;
    }

    public long getAccountIdTo() {
        return accountIdTo;
    }

    public void setAccountIdTo(long accountIdTo) {
        this.accountIdTo = accountIdTo;
    }

    public long getCommmentId() {
        return commmentId;
    }

    public void setCommmentId(long commmentId) {
        this.commmentId = commmentId;
    }

    public Long getMainCommentId() {
        if(mainCommentId== 0) return null;
        return mainCommentId;
    }

    public void setMainCommentId(long mainCommentId) {
        this.mainCommentId = mainCommentId;
    }

    public Instant getDatetime() {
        return datetime;
    }

    public void setDatetime(Instant datetime) {
        this.datetime = datetime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<InComment> getReplies() {
        return replies;
    }

    public void setReplies(List<InComment> replies) {
        this.replies = replies;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
