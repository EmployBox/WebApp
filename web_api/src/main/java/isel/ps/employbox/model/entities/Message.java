package isel.ps.employbox.model.entities;

import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.annotations.Id;

import java.sql.Timestamp;
import java.time.Instant;

public class Message implements DomainObject<Long> {

    @Id(isIdentity = true)
    private final long messageId;

    private final long accountId;
    private final long version;
    private final long chatId;
    private final String text;
    private final Instant date;

    public Message(){
        accountId = 0;
        messageId = 0;
        version = 0;
        chatId = 0;
        text = null;
        date = null;
    }

    public Message(long accountId, long messageId, long chatId, String text, Instant date, long version) {
        this.accountId = accountId;
        this.chatId = chatId;
        this.date = date;
        this.text = text;
        this.messageId = messageId;
        this.version = version;
    }

    public Message(long accountId, long chatId, String text, Timestamp date) {
        this.accountId = accountId;
        this.chatId = chatId;
        if(date != null)
            this.date = date.toInstant();
        else
            this.date = null;
        this.text = text;
        this.messageId = -1;
        this.version = -1;
    }

    @Override
    public Long getIdentityKey() {
        return messageId;
    }

    public long getVersion() {
        return version;
    }

    public String getText() {
        return text;
    }

    public Instant getDate() {
        return date;
    }

    public long getMessageId() {
        return messageId;
    }

    public long getChatId() {
        return chatId;
    }

    public long getAccountId() {
        return accountId;
    }
}
