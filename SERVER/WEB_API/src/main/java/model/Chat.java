package model;

import java.util.stream.Stream;

public class Chat extends DomainObject<Long>{
    private final long chadId;
    private final long accountIdFirst;
    private final long accountIdSecond;

    private final Stream<Message> messages;
    private final Stream<Comment> replies;

    public Chat(long chatId, long accountIdFirst, long accountIdSecond,Stream<Message> messages,Stream<Comment> replies, long version) {
        super(chatId,version);
        this.chadId = chatId;
        this.accountIdFirst = accountIdFirst;
        this.accountIdSecond = accountIdSecond;
        this.replies = replies;
        this.messages = messages;
    }

    public static Chat create(  long accountIdFirst, long accountIdSecond,Stream<Message> messages,Stream<Comment> replies, long version){
        Chat chat = new Chat(-1, accountIdFirst, accountIdSecond, messages, replies, version);
        chat.markNew();
        return chat;
    }

    public static Chat load( long chatId, long accountIdFirst, long accountIdSecond,Stream<Message> messages,Stream<Comment> replies, long version){
        Chat chat = new Chat(chatId,accountIdFirst,accountIdSecond,messages, replies, version);
        chat.markClean();
        return chat;
    }

    public long getChadId() {
        return chadId;
    }

    public long getAccountIdFirst() {
        return accountIdFirst;
    }

    public long getAccountIdSecond() {
        return accountIdSecond;
    }
}
