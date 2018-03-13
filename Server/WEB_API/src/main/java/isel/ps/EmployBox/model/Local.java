package isel.ps.EmployBox.model;

public class Local extends DomainObject<String> {
    @ID
    private final String address;
    private final String country;
    private final String district;
    private final String zipCode;

    public Local(String address, String country, String district, String zipCode, long version) {
        super(address, version);
        this.address = address;
        this.country = country;
        this.district = district;
        this.zipCode = zipCode;
    }

    public static Local create(String address, String country, String district, String zipCode, long version){
        Local local = new Local(address, country, district, zipCode, 0);
        local.markNew();
        return local;
    }

    public static Local load(String address, String country, String district, String zipCode, long version){
        Local local = new Local(address, country, district, zipCode, version);
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
