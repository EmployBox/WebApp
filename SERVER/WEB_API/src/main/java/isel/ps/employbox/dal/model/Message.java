package isel.ps.employbox.dal.model;

import javafx.util.Pair;

import java.sql.Date;

public class Message extends DomainObject<Pair<Long,Long>> {
    @ID
    private final long messageId;
    @ID
    private final long chadId;
    private final String text;
    private final Date date;

    public Message(long messageId, long chatId, String text, Date date, long version) {
        super(new Pair(messageId, chatId), version);
        this.chadId = chatId;
        this.date = date;
        this.text = text;
        this.messageId = messageId;
    }

    public Message( long chatId, String text, Date date) {
        super(new Pair(-1L,chatId), 0);
        this.chadId = chatId;
        this.date = date;
        this.text = text;
        this.messageId = -1;
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
