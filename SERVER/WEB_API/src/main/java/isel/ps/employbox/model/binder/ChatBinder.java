package isel.ps.employbox.model.binder;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.entities.Chat;
import isel.ps.employbox.model.input.InChat;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;

import java.util.stream.Stream;

public class ChatBinder extends ModelBinder <Chat, ResourceSupport, InChat> {

    @Override
    public Stream<Chat> bindInput(Stream<InChat> list) {
        return null;
    }

    @Override
    public Resource<ResourceSupport> bindOutput(Chat object) {
        throw new BadRequestException("Method bindOutput cannot be called for chats");
    }

    @Override
    public Chat bindInput(InChat object) {
        return null;
    }
}
