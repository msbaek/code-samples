package java_stream_api_extentions;

import org.jooq.lambda.Seq;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 * associate elements basing on their fields like id, but not the order in a stream
 */
public class JoinTest {
    @Test
    public void using_seq() {
        Seq<Person> s1 = Seq.of(
                new Person(1, "John", "Smith"),
                new Person(2, "Tom", "Hamilton"),
                new Person(3, "Paul", "Walker")
        );
        Seq<PersonAddress> s2 = Seq.of(
                new PersonAddress(1, "London", "Street1", "100"),
                new PersonAddress(2, "Manchester", "Street1", "101"),
                new PersonAddress(3, "London", "Street2", "200")
        );
        Seq<PersonDTO> s3 = s1.innerJoin(s2, (p, pa) -> p.getId() == pa.getId()).map(t ->
                PersonDTO.builder()
                         .id(t.v1.getId())
                         .firstName(t.v1.getFirstName())
                         .lastName(t.v1.getLastName())
                         .city(t.v2.getCity())
                         .street(t.v2.getStreet())
                         .houseNo(t.v2.getHouseNo()).build());
        s3.forEach(dto -> {
            Assertions.assertNotNull(dto.getId());
            Assertions.assertNotNull(dto.getFirstName());
            Assertions.assertNotNull(dto.getCity());
        });
    }
}
