package model;

public class Local extends DomainObject {
    private final String address;
    private final String country;
    private final String district;
    private final String zipCode;

    private Local(String address, String country, String district, String zipCode) {
        super(address);
        this.address = address;
        this.country = country;
        this.district = district;
        this.zipCode = zipCode;
    }

    public static Local create(String address, String country, String district, String zipCode){
        Local local = new Local(address, country, district, zipCode);
        local.markNew();
        return local;
    }

    public static Local load(String address, String country, String district, String zipCode){
        Local local = new Local(address, country, district, zipCode);
        local.markClean();
        return local;
    }

    public String getAddress() {
        return address;
    }

    public String getCountry() {
        return country;
    }

    public String getDistrict() {
        return district;
    }

    public String getZipCode() {
        return zipCode;
    }
}
