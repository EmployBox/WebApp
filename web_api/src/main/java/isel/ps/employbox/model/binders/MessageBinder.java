package isel.ps.employbox.model.binders;

import isel.ps.employbox.model.entities.Message;
import isel.ps.employbox.model.input.InMessage;
import isel.ps.employbox.model.output.OutMessage;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Component
public class MessageBinder implements ModelBinder<Message,OutMessage,InMessage> {

    @Override
    public CompletableFuture<OutMessage> bindOutput(Message message) {
        return CompletableFuture.completedFuture(new OutMessage(
                message.getAccountId(),
                message.getMessageId(),
                message.getChatId() ,
                message.getDate(),
                message.getText()
        ));
    }

    @Override
    public Message bindInput(InMessage object) {
        return new Message(object.getAccountId(), object.getChatId(), object.getText(), object.getDate());
    }
}
