package isel.ps.employbox.model.entities;

import org.github.isel.rapper.DomainObject;

public class Local implements DomainObject<String>{
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

    @Override
    public String getIdentityKey() {
        return address;
    }

    public long getVersion() {
        return version;
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
