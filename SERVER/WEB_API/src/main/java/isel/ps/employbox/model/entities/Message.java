package isel.ps.employbox.model.entities;

import java.sql.Date;

public class Message {
    private final long accountId;
    private final long messageId;
    private final long version;
    private final long chadId;
    private final String text;
    private final Date date;

    public Message(long accountId, long messageId, long chatId, String text, Date date, long version) {
        this.accountId = accountId;
        this.chadId = chatId;
        this.date = date;
        this.text = text;
        this.messageId = messageId;
        this.version = version;
    }

    public Message(long accountId, long chatId, String text, Date date) {
        this.accountId = accountId;
        this.chadId = chatId;
        this.date = date;
        this.text = text;
        this.messageId = -1;
        this.version = -1;
    }

    public long getVersion() {
        return version;
    }

    public String getText() {
        return text;
    }

    public Date getDate() {
        return date;
    }

    public long getMessageId() {
        return messageId;
    }

    public long getChadId() {
        return chadId;
    }

    public long getAccountId() {
        return accountId;
    }
}
