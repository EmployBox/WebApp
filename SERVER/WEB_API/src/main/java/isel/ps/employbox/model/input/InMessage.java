package isel.ps.employbox.model.input;

import java.sql.Timestamp;

public class InMessage {
    private long accountId;
    private long messageId;
    private long chatId;
    private Timestamp date;
    private String text;

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public long getAccountId() {
        return accountId;
    }
}
