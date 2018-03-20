package isel.ps.employbox.dal.model;

import java.util.List;
import java.util.function.Supplier;

public class Chat extends DomainObject<Long> {
    @ID(isIdentity = true)
    private long chadId;
    private final long accountIdFirst;
    private final long accountIdSecond;

    private final Supplier<List<Message>> messages;

    public Chat(long chatId, long accountIdFirst, long accountIdSecond, long version, Supplier<List<Message>> messages) {
        super(chatId, version);
        this.chadId = chatId;
        this.accountIdFirst = accountIdFirst;
        this.accountIdSecond = accountIdSecond;
        this.messages = messages;
    }

    public static Chat create(long accountIdFirst, long accountIdSecond, long version, Supplier<List<Message>> messages){
        Chat chat = new Chat(defaultKey, accountIdFirst, accountIdSecond, version, messages);
        chat.markNew();
        return chat;
    }

    public static Chat load(long chatId, long accountIdFirst, long accountIdSecond, long version, Supplier<List<Message>> messages){
        Chat chat = new Chat(chatId, accountIdFirst, accountIdSecond, version, messages);
        chat.markClean();
        return chat;
    }

    public static Chat update(Chat chat, Supplier<List<Message>> messages){
        chat.markToBeDirty();
        Chat newChat = new Chat(chat.getChadId(), chat.getAccountIdFirst(), chat.getAccountIdSecond(), chat.getVersion(), messages);
        newChat.markDirty();
        return newChat;
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

    public Supplier<List<Message>> getMessages() {
        return messages;
    }
}
