package java_stream_api_extentions;

import com.google.common.collect.Streams;
import one.util.streamex.StreamEx;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class OthersTest {
    @Test
    public void multiple_concatenation_using_jlambda() {
        Seq<Integer> s1 = Seq.of(1, 2, 3);
        Seq<Integer> s2 = Seq.of(4, 5, 6);
        Seq<Integer> s3 = Seq.of(7, 8, 9);
        Seq<Integer> s4 = Seq.concat(s1, s2, s3);
        Assertions.assertEquals(9, s4.count());
    }

    @Test
    public void multiple_concatenation_using_stream() {
        Stream<Integer> s1 = Stream.of(1, 2, 3);
        Stream<Integer> s2 = Stream.of(4, 5, 6);
        Stream<Integer> s3 = Stream.of(7, 8, 9);
        Stream<Integer> s4 = Streams.concat(s1, s2, s3);
        Assertions.assertEquals(9, s4.count());
    }

    @Test
    public void partitioning_using_stream() {
        StreamEx<PersonDTO> s1 = StreamEx.of(
                PersonDTO.builder().id(1).firstName("John").lastName("Smith").city("London").street("Street1").houseNo("100").build(),
                PersonDTO.builder().id(2).firstName("Tom").lastName("Hamilton").city("Manchester").street("Street1").houseNo("101").build(),
                PersonDTO.builder().id(3).firstName("Paul").lastName("Walker").city("London").street("Street2").houseNo("200").build(),
                PersonDTO.builder().id(4).firstName("Joan").lastName("Collins").city("Manchester").street("Street2").houseNo("201").build()
        );
        Map<Boolean, List<PersonDTO>> m = s1.partitioningBy(dto -> dto.getStreet().equals("Street1"));
        Assertions.assertTrue(m.get(true).size() == 2);
        Assertions.assertTrue(m.get(false).size() == 2);
    }

    @Test
    public void partitioning_using_jlambda() {
        Seq<PersonDTO> s1 = Seq.of(
                PersonDTO.builder().id(1).firstName("John").lastName("Smith").city("London").street("Street1").houseNo("100").build(),
                PersonDTO.builder().id(2).firstName("Tom").lastName("Hamilton").city("Manchester").street("Street1").houseNo("101").build(),
                PersonDTO.builder().id(3).firstName("Paul").lastName("Walker").city("London").street("Street2").houseNo("200").build(),
                PersonDTO.builder().id(4).firstName("Joan").lastName("Collins").city("Manchester").street("Street2").houseNo("201").build()
        );
        Tuple2<Seq<PersonDTO>, Seq<PersonDTO>> t = s1.partition(dto -> dto.getStreet().equals("Street1"));
        Assertions.assertTrue(t.v1.count() == 2);
        Assertions.assertTrue(t.v2.count() == 2);
    }

    @Test
    public void aggregation() {
        Seq<Person> s1 = Seq.of(
                new Person(1, "John", "Smith", 35),
                new Person(2, "Tom", "Hamilton", 45),
                new Person(3, "Paul", "Walker", 20)
        );
        Optional<Integer> sum = s1.sum(Person::getAge);
        Assertions.assertEquals(100, sum.get().intValue());
    }

    @Test
    public void pairing() {
        StreamEx<Integer> s1 = StreamEx.of(1, 2, 1, 2, 1);
        StreamEx<Integer> s2 = s1.pairMap(Integer::sum);
        s2.forEach(i -> Assertions.assertEquals(3, i.intValue()));
    }
}
