package isel.ps.employbox.model.binders;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.entities.Chat;
import isel.ps.employbox.model.input.InChat;
import isel.ps.employbox.model.output.OutputDto;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class ChatBinder implements ModelBinder<Chat, OutputDto, InChat> {

    @Override
    public CompletableFuture<OutputDto> bindOutput(Chat object) {
        throw new BadRequestException("Method bindOutput cannot be called for chats");
    }

    @Override
    public Chat bindInput(InChat object) {
        return new Chat(object.getAccountIdFirst(), object.getAccountIdSecond());
    }
}
