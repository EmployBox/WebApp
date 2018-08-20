package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.UserAccountController;
import isel.ps.employbox.controllers.account.*;
import isel.ps.employbox.controllers.curricula.CurriculumController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OutUser implements OutputDto {
    @JsonProperty
    private final long accountId;

    @JsonProperty
    private final String name;

    @JsonProperty
    private final String email;

    @JsonProperty
    private final String photo_url;

    @JsonProperty
    private final String summary;

    @JsonProperty
    private final double rating;

    @JsonProperty
    private final _Links _links;

    public OutUser(long accountId, String name, String email, String photo_url, String summary, double rating) {
        this.accountId = accountId;
        this.name = name;
        this.email = email;
        this.photo_url = photo_url;
        this.summary = summary;
        this.rating = rating;
        this._links = new _Links();
    }

    @JsonIgnore
    @Override
    public Object getCollectionItemOutput() {
        return new UserItemOutput(accountId, name, rating, summary);
    }

    class UserItemOutput {
        @JsonProperty
        private final long accountId;
        @JsonProperty
        private final String name;
        @JsonProperty
        private final double rating;
        @JsonProperty
        private final String summary;
        @JsonProperty
        private _Links _links;

        UserItemOutput(long id, String name, double rating, String summary) {
            this.accountId = id;
            this.name = name;
            this.rating = rating;
            this.summary = summary;
            _links = new _Links();
        }

        private class _Links {
            @JsonProperty
            private _Links.Self self = new _Links.Self();

            private class Self {
                @JsonProperty
                final String href = HOSTNAME + linkTo( UserAccountController.class).slash(accountId).withSelfRel().getHref();
            }
        }
    }

    public class _Links{
        @JsonProperty
        private Self self = new Self();

        @JsonProperty
        private Offered_jobs offered_jobs = new Offered_jobs();

        @JsonProperty
        private Comments comments = new Comments();

        @JsonProperty
        private Followers followers = new Followers();

        @JsonProperty
        private Following following = new Following();

        @JsonProperty
        private Chats chats = new Chats();

        @JsonProperty
        private Ratings ratings = new Ratings();

        @JsonProperty
        private Applications applications = new Applications();

        @JsonProperty
        private Curricula curricula = new Curricula();


        private class Self{
            @JsonProperty
            final String href = HOSTNAME + linkTo( UserAccountController.class).slash(accountId).withSelfRel().getHref();
        }

        private class Offered_jobs{
            @JsonProperty
            final String href = HOSTNAME + linkTo(methodOn( AccountController.class, accountId).getOfferedJobs(accountId, 0, 5)).withRel("offered_jobs").getHref();
        }

        private class Curricula {
            @JsonProperty
            final String href = HOSTNAME + linkTo( methodOn(CurriculumController.class, accountId).getCurricula(accountId, 0, 5)).withRel("curricula").getHref();
        }

        private class Applications {
            @JsonProperty
            final String href = HOSTNAME + linkTo( methodOn(UserAccountController.class).getAllApplications(accountId, 0, 5)).withRel("applications").getHref();
        }

        private class Chats {
            @JsonProperty
            final String href = HOSTNAME + linkTo(ChatController.class, accountId).withRel("chats").getHref();
        }

        private class Comments {
            @JsonProperty
            final String href = HOSTNAME + linkTo( methodOn(CommentController.class, accountId).getAllComments(accountId, 0,5)).withRel("comments").getHref();
        }

        private class Ratings {
            @JsonProperty
            final String href = HOSTNAME + linkTo ( methodOn(RatingController.class, accountId).getRatings(accountId,0,5)).withRel("ratings").getHref();
        }

        private class Followers {
            @JsonProperty
            final String href = HOSTNAME + linkTo( methodOn(FollowsController.class, accountId).getTheAccountsWichThisAccountIsFollowed(accountId, 0, 5, null, null,null)).withRel("followers").expand().getHref();
        }

        private class Following {
            @JsonProperty
            final String href = HOSTNAME + linkTo( methodOn(FollowsController.class, accountId).getTheAccountsWichThisAccountIsFollower(accountId, 0, 5,null,null,null) ).withRel("following").expand().getHref();
        }
    }
}
