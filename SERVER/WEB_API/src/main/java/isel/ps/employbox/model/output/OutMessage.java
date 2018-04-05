package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.ChatController;
import org.springframework.hateoas.ResourceSupport;

import java.sql.Date;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OutMessage extends ResourceSupport {

    @JsonProperty
    private final long messageId;

    @JsonProperty
    private final long chatId;

    @JsonProperty
    private final Date date;

    @JsonProperty
    private final String text;

    public OutMessage(long accountId, long messageId, long chadId, Date date, String text) {
        this.messageId = messageId;
        this.chatId = chadId;
        this.date = date;
        this.text = text;
        this.add( linkTo( methodOn(ChatController.class, accountId).getChatMessage(chatId, messageId)).withSelfRel());
    }
}
