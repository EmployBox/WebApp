package isel.ps.employbox.model.entities;

public class Local {
    private final String address;
    private final String country;
    private final String district;
    private final String zipCode;
    private final long version;
    private final float longitude;
    private final float latitude;

    public Local(String address, String country, String district, String zipCode, long version, float longitude, float latitude) {
        this.address = address;
        this.country = country;
        this.district = district;
        this.zipCode = zipCode;
        this.version = version;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public long getVersion() {
        return version;
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

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }
}
