package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.CompanyController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class OutCompany extends OutputDto {

    @JsonProperty
    private final Long accountId;

    @JsonProperty
    private final String email;

    @JsonProperty
    private final double rating;

    @JsonProperty
    private final String name;

    @JsonProperty
    private final String specialization;

    @JsonProperty
    private final Short yearFounded;

    @JsonProperty
    private final String logoUrl;

    @JsonProperty
    private final String webpageUrl;

    @JsonProperty
    private final String description;

    @JsonProperty
    private final _Links _links;

    public OutCompany(long accountId, String email, double rating, String name, String specialization, short yearFounded, String logoUrl, String webpageUrl, String description) {
        this.accountId = accountId;
        this.email = email;
        this.rating = rating;
        this.name = name;
        this.specialization = specialization;
        this.yearFounded = yearFounded;
        this.logoUrl = logoUrl;
        this.webpageUrl = webpageUrl;
        this.description = description;
        this._links = new _Links();
    }

    @Override
    public Object getCollectionItemOutput() {
        return new CompanyItemOutput(rating, name);
    }

    class CompanyItemOutput {
        @JsonProperty
        private final String name;

        @JsonProperty
        private final double rating;

        @JsonProperty
        private final _Links _links = new _Links();

        private CompanyItemOutput(double rating, String name){
            this.rating = rating;
            this.name = name;
        }

        class _Links {
            @JsonProperty
            private _Links.Self self = new _Links.Self();

            private class Self{
                @JsonProperty
                final String href = HOSTNAME + linkTo(CompanyController.class).slash(accountId).withSelfRel().withSelfRel().getHref();
            }
        }
    }

    private class _Links {
        @JsonProperty
        private Self self = new Self();

        private class Self {
            @JsonProperty
            final String href = HOSTNAME + linkTo(CompanyController.class).slash(accountId).withSelfRel().withSelfRel().getHref();
        }
    }
}
