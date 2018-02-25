package model;

public class Account extends DomainObject{
    private final long accountID;
    private final String email;
    private String password;
    private Double rate;

    private Account(long accountID, String email, String password, Double rate, long version){
        super(email, version);
        this.accountID = accountID;
        this.email = email;
        this.password = password;
        this.rate = rate;
    }

    public static Account create(long accountID, String email, String password, Double rate){
        Account account = new Account(accountID, email, password, rate, 0);
        account.markNew();
        return account;
    }

    public static Account load(long accountID, String email, String password, Double rate, long version){
        Account account = new Account(accountID, email, password, rate, version);
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

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        markToBeDirty();
        this.rate = rate;
        markDirty();
    }
}
