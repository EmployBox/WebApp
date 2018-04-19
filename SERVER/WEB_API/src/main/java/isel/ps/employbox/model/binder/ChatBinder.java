package isel.ps.employbox.model.binder;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.entities.Chat;
import isel.ps.employbox.model.input.InChat;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.stereotype.Component;

@Component
public class ChatBinder extends ModelBinder <Chat, ResourceSupport, InChat> {

    @Override
    public ResourceSupport bindOutput(Chat object) {
        throw new BadRequestException("Method bindOutput cannot be called for chats");
    }

    @Override
    public Chat bindInput(InChat object) {
        return new Chat(object.getAccountIdFirst(), object.getAccountIdSecond());
    }
}
