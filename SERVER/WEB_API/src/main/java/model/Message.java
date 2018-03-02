package model;

import java.sql.Date;

public class Message extends DomainObject {
    private final long chadId;
    private final String text;
    private final Date date;

    public Message(long chatId, String text, Date date) {
        super(chatId);
        this.chadId = chatId;
        this.date = date;
        this.text = text;
    }

    public static Message create(  String text, Date date ){
        Message message = new Message( -1 , text, date);
        message.markNew();
        return message;
    }

    public static Message load( long chatId, String text, Date date ){
        Message message = new Message( chatId, text, date);
        message.markClean();
        return message;
    }
}
