package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OutUser implements OutputDto {
    @JsonProperty
    private final long id;

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

    public OutUser(long id, String name, String email, String photo_url, String summary, double rating) {
        this.id = id;
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
        return new UserItemOutput(name, email, rating);
    }

    class UserItemOutput {
        @JsonProperty
        private final String name;

        @JsonProperty
        private final String email;

        @JsonProperty
        private final double rating;

        @JsonProperty
        private _Links _links = new _Links();

        UserItemOutput(String name, String email, double rating) {
            this.name = name;
            this.email = email;
            this.rating = rating;
        }

        private class _Links {
            @JsonProperty
            private _Links.Self self = new _Links.Self();

            private class Self {
                @JsonProperty
                final String href = HOSTNAME + linkTo( UserAccountController.class).slash(id).withSelfRel().getHref();
            }
        }
    }

    public class _Links{
        @JsonProperty
        private Self self = new Self();

        @JsonProperty
        private Offered_jobs offered_jobs = new Offered_jobs();

        @JsonProperty
        private Curricula curricula = new Curricula();

        @JsonProperty
        private Applications applications = new Applications();

        @JsonProperty
        private Chats chats = new Chats();

        @JsonProperty
        private Comments comments = new Comments();

        @JsonProperty
        private Ratings ratings = new Ratings();

        @JsonProperty
        private Followers followers = new Followers();

        @JsonProperty
        private Following following = new Following();

        private class Self{
            @JsonProperty
            final String href = HOSTNAME + linkTo( UserAccountController.class).slash(id).withSelfRel().getHref();
        }

        private class Offered_jobs{
            @JsonProperty
            final String href = HOSTNAME + linkTo( JobController.class,id).withRel("offered_jobs").getHref();
        }

        private class Curricula {
            @JsonProperty
            final String href = HOSTNAME + linkTo( CurriculumController.class,id).withRel("curricula").getHref();
        }

        private class Applications {
            @JsonProperty
            final String href = HOSTNAME + linkTo( methodOn(UserAccountController.class).getAllApplications(id, 0, 0)).withRel("applications").getHref();
        }

        private class Chats {
            @JsonProperty
            final String href = HOSTNAME + linkTo( ChatController.class,id).withRel("chats").getHref();
        }

        private class Comments {
            @JsonProperty
            final String href = HOSTNAME + linkTo( CommentController.class,id).withRel("comments").getHref();
        }

        private class Ratings {
            @JsonProperty
            final String href = HOSTNAME + linkTo ( RatingController.class,id).withRel("ratings").getHref();
        }

        private class Followers {
            @JsonProperty
            final String href = HOSTNAME + linkTo( methodOn(FollowsController.class,id).getFollowers(id, 0, 0) ).withRel("followers").getHref();
        }

        private class Following {
            @JsonProperty
            final String href = HOSTNAME + linkTo( methodOn(FollowsController.class,id).getFollowing(id, 0, 0) ).withRel("following").getHref();
        }
    }
}
