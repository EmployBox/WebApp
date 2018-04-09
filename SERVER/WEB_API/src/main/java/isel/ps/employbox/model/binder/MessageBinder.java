package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.entities.Message;
import isel.ps.employbox.model.input.InMessage;
import isel.ps.employbox.model.output.OutMessage;
import org.springframework.stereotype.Component;

@Component
public class MessageBinder extends ModelBinder<Message, OutMessage, InMessage> {

    @Override
    public OutMessage bindOutput(Message obj) {
        return new OutMessage(obj.getAccountId(), obj.getMessageId(), obj.getChadId() , obj.getDate(), obj.getText());
    }

    @Override
    public Message bindInput(InMessage object) {
        return new Message(object.getAccountId(), object.getChatId(), object.getText(), object.getDate());
    }
}
