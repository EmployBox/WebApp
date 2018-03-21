package isel.ps.employbox.api.model.input;

import java.util.List;

public class InComment {
    private long accountIdFrom;
    private long accountIdTo;
    private long commmentId;
    private long mainCommentId;
    private String datetime;
    private String text;
    private List<InComment> replies;

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

    public long getMainCommentId() {
        return mainCommentId;
    }

    public void setMainCommentId(long mainCommentId) {
        this.mainCommentId = mainCommentId;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
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
}
