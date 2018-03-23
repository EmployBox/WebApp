package isel.ps.employbox.api.model.output;

import java.sql.Date;

public class OutMessage {
    private final long messageId;
    private final long chatId;
    private final Date date;
    private final String text;

    public OutMessage(long messageId, long chadId, Date date, String text) {
        this.messageId = messageId;
        this.chatId = chadId;
        this.date = date;
        this.text = text;
    }

    public long getMessageId() {
        return messageId;
    }

    public long getChatId() {
        return chatId;
    }

    public Date getDate() {
        return date;
    }

    public String getText() {
        return text;
    }
}
