package streams_and_collectors;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * http://www.baeldung.com/java-flatten-nested-collections
 */
public class AnotherFlatMapTest {
    List<List<String>> nestedList = asList(
            asList("one:one"),
            asList("two:one", "two:two", "two:three"),
            asList("three:one", "three:two", "three:three", "three:four"));

    @Test
    void flattening_the_list_with_forEach() {
        List<String> ls = new ArrayList<>();
        nestedList.forEach(ls::addAll);
        assertThat(ls.toString())
                .isEqualTo("[one:one, two:one, two:two, two:three, three:one, three:two, three:three, three:four]");
    }

    @Test
    void flattening_the_List_with_forEach() {
        assertThat(nestedList.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList())
                .toString())
                .isEqualTo("[one:one, two:one, two:two, two:three, three:one, three:two, three:three, three:four]");
    }
}
