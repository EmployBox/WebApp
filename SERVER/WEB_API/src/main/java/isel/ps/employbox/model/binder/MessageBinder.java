package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.entities.Message;
import isel.ps.employbox.model.input.InMessage;
import isel.ps.employbox.model.output.OutMessage;
import javafx.util.Pair;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class MessageBinder extends ModelBinder<Message, OutMessage, InMessage> {


    @Override
    Stream<Message> bindInput(Stream<InMessage> list) {
        return null;
    }

    @Override
    public Resource<OutMessage> bindOutput(Message obj) {
        return new Resource<>( new OutMessage(obj.getMessageId(), obj.getChadId() , obj.getDate(), obj.getText()) );
    }

    @Override
    public Message bindInput(InMessage object) {
        return new Message(object.getChatId(), object.getText(), object.getDate());
    }
}
