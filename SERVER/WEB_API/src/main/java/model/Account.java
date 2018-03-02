package model;

public class Account extends DomainObject<Long>{
    private final long accountID;
    private final String email;
    private final String password;
    private final double rating;

    protected Account(long accountID, String email, String password, double rating, long version){
        super(accountID, (long) -1, version);
        this.accountID = accountID;
        this.email = email;
        this.password = password;
        this.rating = rating;
    }

    public static Account create(String email, String password, double rating){
        Account account = new Account(-1, email, password, rating, 0);
        account.markNew();
        return account;
    }

    public static Account load(long accountID, String email, String password, double rating, long version){
        Account account = new Account(accountID, email, password, rating, version);
        account.markClean();
        return account;
    }

    public long getAccountID() {
        return accountID;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public double getRating() {
        return rating;
    }
}
