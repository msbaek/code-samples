package java_stream_api_extentions;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
class PersonDTO {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final String city;
    private final String street;
    private final String houseNo;
}
