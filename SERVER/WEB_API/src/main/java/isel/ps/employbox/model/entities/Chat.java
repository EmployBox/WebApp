package isel.ps.employbox.model.entities;

import org.github.isel.rapper.DomainObject;

import java.util.List;
import java.util.function.Supplier;

public class Chat implements DomainObject<Long>{
    private long chadId;
    private final long accountIdFirst;
    private final long accountIdSecond;
    private long version;
    private Supplier<List<Message>> messages;

    public Chat(long chatId, long accountIdFirst, long accountIdSecond, long version, Supplier<List<Message>> messages) {
        this.chadId = chatId;
        this.accountIdFirst = accountIdFirst;
        this.accountIdSecond = accountIdSecond;
        this.version = version;
        this.messages = messages;
    }

    public Chat(long accountIdFirst, long accountIdSecond){
        this.accountIdFirst = accountIdFirst;
        this.accountIdSecond = accountIdSecond;
    }

    @Override
    public Long getIdentityKey() {
        return chadId;
    }

    public long getVersion() {
        return version;
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
