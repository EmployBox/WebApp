package isel.ps.EmployBox.dal.domainModel;

import java.sql.Date;

public class Message extends DomainObject<String> {
    @ID
    private final long messageId;
    @ID
    private final long chadId;
    private final String text;
    private final Date date;

    public Message(long messageId, long chatId, String text, Date date, long version) {
        super(String.format("%d %d",messageId,chatId), version);
        this.chadId = chatId;
        this.date = date;
        this.text = text;
        this.messageId = messageId;
    }

    public static Message create(long messageId, long chatId, String text, Date date){
        Message message = new Message(messageId, chatId , text, date, 0);
        message.markNew();
        return message;
    }

    public static Message load(long messageId, long chatId, String text, Date date, long version){
        Message message = new Message(messageId, chatId, text, date, version);
        message.markClean();
        return message;
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
}
