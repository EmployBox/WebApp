package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.account.AccountController;
import isel.ps.employbox.controllers.account.CommentController;

import java.time.Instant;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OutComment implements OutputDto {

    @JsonProperty
    private final long accountIdFrom;

    @JsonProperty
    private final long accountIdTo;

    @JsonProperty
    private final long commmentId;

    @JsonProperty
    private final long mainCommentId;

    @JsonProperty
    private final Instant datetime;

    @JsonProperty
    private final String text;

    @JsonProperty
    private final _Links _links;

    public OutComment(long accountIdFrom, long accountIdTo, long commmentId, long mainCommentId, Instant datetime, String text) {
        this.accountIdFrom = accountIdFrom;
        this.accountIdTo = accountIdTo;
        this.commmentId = commmentId;
        this.mainCommentId = mainCommentId;
        this.datetime = datetime;
        this.text = text;
        this._links = new _Links();
    }

    @JsonIgnore
    @Override
    public Object getCollectionItemOutput() {
        return new CommentItemOutput(accountIdFrom, accountIdTo, commmentId, mainCommentId, datetime, text);
    }

    class CommentItemOutput {
        @JsonProperty
        private final long accountIdFrom;

        @JsonProperty
        private final long accountIdTo;

        @JsonProperty
        private final long commmentId;

        @JsonProperty
        private final long mainCommentId;

        @JsonProperty
        private final Instant datetime;

        @JsonProperty
        private final String text;

        @JsonProperty
        private _Links _links;

        CommentItemOutput(long accountIdFrom, long accountIdTo, long commmentId, long mainCommentId, Instant datetime, String text) {
            this.accountIdFrom = accountIdFrom;
            this.accountIdTo = accountIdTo;
            this.commmentId = commmentId;
            this.mainCommentId = mainCommentId;
            this.datetime = datetime;
            this.text = text;
        }
    }

    private class _Links {
        @JsonProperty
        private _Links.Self self = new _Links.Self();

        private class Self {
            @JsonProperty
            final String href = HOSTNAME + linkTo (CommentController.class, accountIdFrom).slash(commmentId).withSelfRel().getHref();
        }

        private class account_from {
            @JsonProperty
            final String href = HOSTNAME + linkTo(methodOn(AccountController.class).getAccount(accountIdFrom)).withSelfRel().getHref();
        }

        private class account_dest {
            @JsonProperty
            final String href = HOSTNAME + linkTo(methodOn(AccountController.class).getAccount(accountIdTo)).withSelfRel().getHref();
        }
    }
}
