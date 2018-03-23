package isel.ps.employbox.api.model.binder;

import isel.ps.employbox.api.model.input.InMessage;
import isel.ps.employbox.api.model.output.OutMessage;
import isel.ps.employbox.api.services.ModelBinder;
import isel.ps.employbox.dal.model.Message;
import javafx.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessageBinder implements ModelBinder<Message, OutMessage, InMessage , Pair<Long,Long>> {

    @Override
    public List<OutMessage> bindOutput(List<Message> list) {
        return list
            .stream()
            .map(curr-> new OutMessage(curr.getMessageId(),curr.getChadId(),curr.getDate(), curr.getText()) )
            .collect(Collectors.toList());
    }

    @Override
    public List<Message> bindInput(List<InMessage> list) {
        return list
            .stream()
            .map(curr-> new Message(curr.getChatId(), curr.getText(),curr.getDate()) )
            .collect(Collectors.toList());
    }

    @Override
    public OutMessage bindOutput(Message obj) {
        return new OutMessage(obj.getMessageId(), obj.getChadId(), obj.getDate(), obj.getText());
    }

    @Override
    public Message bindInput(InMessage object) {
        return new Message(object.getChatId(), object.getText(), object.getDate());
    }
}
