package model;

public class Account extends DomainObject{
    private final String email;
    private String password;
    private Double rate;

    private Account(String email, String password, Double rate){
        this.email = email;
        this.password = password;
        this.rate = rate;
    }

    public static Account create(String email, String password, Double rate){
        Account account = new Account(email, password, rate);
        account.markNew();
        return account;
    }

    public static Account load(String email, String password, Double rate){
        Account account = new Account(email, password, rate);
        account.markClean();
        return account;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        markDirty();
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
        markDirty();
    }
}
