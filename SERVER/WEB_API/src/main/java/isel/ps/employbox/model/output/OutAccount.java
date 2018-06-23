package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.CompanyController;
import isel.ps.employbox.controllers.UserAccountController;

import java.util.HashMap;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

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
        return new AccountItemOutput(accountId, rating, name, accountType);
    }

    class AccountItemOutput {
        @JsonProperty
        private final long accountId;
        @JsonProperty
        private final String name;
        @JsonProperty
        private final double rating;
        @JsonProperty
        private final String accountType;
        @JsonProperty
        private final _Links _links;

        AccountItemOutput(long accountId, double rating, String name, String accountType) {
            this.accountId = accountId;
            this.rating = rating;
            this.name = name;
            this.accountType = accountType;
            _links = new _Links();
        }

        private class _Links {
            @JsonProperty
            private Self self = new Self();

            private class Self {
                @JsonProperty
                final String href = HOSTNAME + accountLinks.get(accountType);
            }
        }
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
