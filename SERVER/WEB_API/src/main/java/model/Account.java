package model;

public class Account extends DomainObject{
    private final long accountID;
    private final String email;
    private String password;
    private double rating;

    private Account(long accountID, String email, String password, double rating, long version){
        super(accountID, version);
        this.accountID = accountID;
        this.email = email;
        this.password = password;
        this.rating = rating;
    }

    public static Account create(long accountID, String email, String password, double rating){
        Account account = new Account(accountID, email, password, rating, 0);
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

    public void setPassword(String password) {
        markToBeDirty();
        this.password = password;
        markDirty();
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        markToBeDirty();
        this.rating = rating;
        markDirty();
    }
}
