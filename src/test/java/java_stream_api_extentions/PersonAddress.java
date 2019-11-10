package java_stream_api_extentions;

import lombok.Getter;

@Getter
class PersonAddress {
    private final int id;
    private final String city;
    private final String street;
    private final String houseNo;

    public PersonAddress(int id, String city, String street, String houseNo) {
        this.id = id;
        this.city = city;
        this.street = street;
        this.houseNo = houseNo;
    }
}
