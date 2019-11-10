package java_stream_api_extentions;

import com.google.common.collect.Streams;
import lombok.Builder;
import lombok.Getter;
import one.util.streamex.StreamEx;
import org.jooq.lambda.Seq;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.stream.Stream;

/**
 * https://dzone.com/articles/overview-of-java-stream-api-extensions
 */

/**
 * zip: merge elements from two different streams in accordance with their order in those streams.
 */
public class ZipTest {
    @Test
    public void using_guava() {
        Stream<Person> s1 = Stream.of(
                new Person(1, "John", "Smith"),
                new Person(2, "Tom", "Hamilton"),
                new Person(3, "Paul", "Walker")
        );
        Stream<PersonAddress> s2 = Stream.of(
                new PersonAddress(1, "London", "Street1", "100"),
                new PersonAddress(2, "Manchester", "Street1", "101"),
                new PersonAddress(3, "London", "Street2", "200")
        );
        Stream<PersonDTO> s3 = Streams.zip(s1, s2, (p, pa) -> PersonDTO.builder()
                                                                       .id(p.getId())
                                                                       .firstName(p.getFirstName())
                                                                       .lastName(p.getLastName())
                                                                       .city(pa.getCity())
                                                                       .street(pa.getStreet())
                                                                       .houseNo(pa.getHouseNo()).build());
        s3.forEach(dto -> {
            Assertions.assertNotNull(dto.getId());
            Assertions.assertNotNull(dto.getFirstName());
            Assertions.assertNotNull(dto.getCity());
        });
    }

    @Test
    public void using_streamEx() {
        StreamEx<Person> s1 = StreamEx.of(
                new Person(1, "John", "Smith"),
                new Person(2, "Tom", "Hamilton"),
                new Person(3, "Paul", "Walker")
        );
        StreamEx<PersonAddress> s2 = StreamEx.of(
                new PersonAddress(1, "London", "Street1", "100"),
                new PersonAddress(2, "Manchester", "Street1", "101"),
                new PersonAddress(3, "London", "Street2", "200")
        );
        StreamEx<PersonDTO> s3 = s1.zipWith(s2, (p, pa) -> PersonDTO.builder()
                                                                       .id(p.getId())
                                                                       .firstName(p.getFirstName())
                                                                       .lastName(p.getLastName())
                                                                       .city(pa.getCity())
                                                                       .street(pa.getStreet())
                                                                       .houseNo(pa.getHouseNo()).build());
        s3.forEach(dto -> {
            Assertions.assertNotNull(dto.getId());
            Assertions.assertNotNull(dto.getFirstName());
            Assertions.assertNotNull(dto.getCity());
        });
    }

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
        Seq<PersonDTO> s3 = s1.zip(s2, (p, pa) -> PersonDTO.builder()
                                                                    .id(p.getId())
                                                                    .firstName(p.getFirstName())
                                                                    .lastName(p.getLastName())
                                                                    .city(pa.getCity())
                                                                    .street(pa.getStreet())
                                                                    .houseNo(pa.getHouseNo()).build());
        s3.forEach(dto -> {
            Assertions.assertNotNull(dto.getId());
            Assertions.assertNotNull(dto.getFirstName());
            Assertions.assertNotNull(dto.getCity());
        });
    }
}
