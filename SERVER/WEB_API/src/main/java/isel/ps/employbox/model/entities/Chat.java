package isel.ps.employbox.model.entities;

import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.annotations.ColumnName;
import com.github.jayield.rapper.annotations.Id;
import com.github.jayield.rapper.annotations.Version;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class Chat implements DomainObject<Long> {
    @Id(isIdentity = true)
    private long chatId;
    private final long accountIdFirst;
    private final long accountIdSecond;
    @Version
    private long version;

    @ColumnName(foreignName = "chatId")
    private Supplier< CompletableFuture<List<Message>>> messages;

    public Chat(){
        accountIdFirst = 0;
        accountIdSecond = 0;
        version = 0;
        messages = null;
    }

    public Chat(long chatId, long accountIdFirst, long accountIdSecond, long version, List<Message> messages) {
        this.chatId = chatId;
        this.accountIdFirst = accountIdFirst;
        this.accountIdSecond = accountIdSecond;
        this.version = version;
        this.messages = ()-> CompletableFuture.completedFuture(messages);
    }

    public Chat(long accountIdFirst, long accountIdSecond){
        this.accountIdFirst = accountIdFirst;
        this.accountIdSecond = accountIdSecond;
    }

    @Override
    public Long getIdentityKey() {
        return chatId;
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

    public Supplier<CompletableFuture<List<Message>>> getMessages() {
        return messages;
    }
}
