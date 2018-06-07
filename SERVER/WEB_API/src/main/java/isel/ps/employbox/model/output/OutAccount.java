package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.AccountController;
import isel.ps.employbox.controllers.CompanyController;
import isel.ps.employbox.controllers.JobController;
import isel.ps.employbox.controllers.UserAccountController;
import isel.ps.employbox.model.entities.Company;
import org.springframework.hateoas.Link;

import java.util.HashMap;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OutAccount implements OutputDto {
    @JsonProperty
    private final long accountId;
    @JsonProperty
    private final String name;
    @JsonProperty
    private final String email;
    @JsonProperty
    private final double rating;
    @JsonProperty
    private final String accountType;
    @JsonProperty
    private final long version;
    @JsonProperty
    private final _Links _links;

    private final HashMap<String, String> accountLinks = new HashMap();

    public OutAccount(long accountID, String name, String email, double rating, String accountType, long version) {
        this.accountId = accountID;
        this.name = name;
        this.email = email;
        this.rating = rating;
        this.accountType = accountType;
        this.version = version;
        accountLinks.put("CMP", linkTo ( CompanyController.class).slash(accountId).withSelfRel().getHref());
        accountLinks.put("USR", linkTo ( UserAccountController.class).slash(accountId).withSelfRel().getHref());
        this._links = new _Links();
        //accountLinks.put("MOD", link);
    }

    public long getAccountId() {
        return accountId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public double getRating() {
        return rating;
    }

    public long getVersion() {
        return version;
    }

    @JsonIgnore
    @Override
    public Object getCollectionItemOutput() {
        return null;
    }

    private class _Links {
        @JsonProperty
        private _Links.Self self = new _Links.Self();

        private class Self {
            @JsonProperty
            final String href = HOSTNAME + accountLinks.get(accountType);
        }
    }
}
