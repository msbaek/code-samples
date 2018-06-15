package streams_and_collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * http://www.baeldung.com/java-difference-map-and-flatmap
 */
public class FlatMapTest {
    @Test
    @DisplayName("map은 함수가 우리가 필요한 타입을 반환한다면 Optional에 대해서 잘 동작한다")
    void map_and_flatMap_in_Optionals() {
        Optional<String> s = Optional.of("test");
        assertThat(s.map(String::toUpperCase)).isEqualTo(Optional.of("TEST"));
    }

    @Test
    @DisplayName("flatMap이 필요한 상황")
    void needs_flatMap() {
        assertThat(Optional.of("string").map(s -> Optional.of("STRING"))).isEqualTo(Optional.of(Optional.of("STRING")));

        assertThat(Optional.of("string").flatMap(s -> Optional.of("STRING"))).isEqualTo(Optional.of("STRING"));
    }

    @Test
    void map_and_flatMap_in_streams() {
        List<String> myList = Stream.of("a", "b")
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        assertThat(myList)
                .isEqualTo(asList("A", "B"));

        List<List<String>> list = asList(
                asList("a"),
                asList("b")
        );
        assertThat(list.toString())
                .isEqualTo("[[a], [b]]");
        assertThat(list.stream()
                .flatMap(Collection::stream) // flattens the input Stream of Streams to a Stream of Strings
                .collect(Collectors.toList())
                .toString()
        ).isEqualTo("[a, b]");
    }
}
