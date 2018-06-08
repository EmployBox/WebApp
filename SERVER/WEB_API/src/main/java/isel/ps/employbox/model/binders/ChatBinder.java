package isel.ps.employbox.model.binders;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.entities.Chat;
import isel.ps.employbox.model.input.InChat;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Component
public class ChatBinder implements ModelBinder<Chat,ResourceSupport,InChat> {

    @Override
    public Mono<ResourceSupport> bindOutput(CompletableFuture<Chat> object) {
        throw new BadRequestException("Method bindOutput cannot be called for chats");
    }

    @Override
    public Chat bindInput(InChat object) {
        return new Chat(object.getAccountIdFirst(), object.getAccountIdSecond());
    }
}
