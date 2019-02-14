package java8.collectors;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.assertThat;

// https://www.baeldung.com/java-8-collectors

public class Java8sCollectorsTest {
    private List<String> givenList;

    @BeforeEach
    void setUp() {
        givenList = Arrays.asList("a", "bb", "ccc", "dd");
    }

    @Test
    void eollctos_toList() {
        List<String> result = givenList.stream()
                                       .collect(toList());
        assertThat(result.toString()).isEqualTo("[a, bb, ccc, dd]");
    }

    @Test
    void collectors_toSet() {
        Set<String> result = givenList.stream()
                                      .collect(toSet());
        assertThat(result.toString()).isEqualTo("[bb, dd, a, ccc]");
    }

    @Test
    void collectors_toCollection() {
        List<String> result = givenList.stream()
                                       .collect(toCollection(LinkedList::new));
        assertThat(result.toString()).isEqualTo("[a, bb, ccc, dd]");
    }

    @Test
    void collectors_toMap() {
        Map<String, Integer> result = givenList.stream()
                                               .collect(toMap(Function.identity(), String::length));
        // Function.identity() is just a shortcut for defining function that accepts and return the same value;
        assertThat(result.toString()).isEqualTo("{dd=2, bb=2, a=1, ccc=3}");

        // key collision
        result = givenList.stream()
                          .collect(toMap(Function.identity(), String::length, (i1, i2) -> i1));
        assertThat(result.toString()).isEqualTo("{dd=2, bb=2, a=1, ccc=3}");
    }

    @Test
    void collectors_collectingAndThen() {
        List<String> result = givenList.stream()
                                       .collect(collectingAndThen(toList(), ImmutableList::copyOf));
        assertThat(result.toString()).isEqualTo("[a, bb, ccc, dd]");
        assertThat(result.getClass().getSimpleName()).isEqualTo("RegularImmutableList");
    }

    @Test
    void collectors__oining() {
        String result = givenList.stream()
                                 .collect(joining());
        assertThat(result).isEqualTo("abbcccdd");

        result = givenList.stream()
                          .collect(joining(" "));
        assertThat(result).isEqualTo("a bb ccc dd");


        result = givenList.stream()
                          .collect(joining(" ", "PRE-", "-POST"));
        assertThat(result).isEqualTo("PRE-a bb ccc dd-POST");
    }

    @Test
    void collectors_counting() {
        Long result = givenList.stream()
                               .collect(counting());
        assertThat(result).isEqualTo(4l);
    }

    @Test
    void collectors_summarizingDouble_Long_Int() {
        DoubleSummaryStatistics result = givenList.stream()
                                                  .collect(summarizingDouble(String::length));
        assertThat(result.getAverage()).isEqualTo(2.0);
        assertThat(result.getCount()).isEqualTo(4L);
        assertThat(result.getMax()).isEqualTo(3.0);
        assertThat(result.getMin()).isEqualTo(1.0);
        assertThat(result.getSum()).isEqualTo(8.0);
    }

    @Test
    void collectors_averagingDouble_Long_Int() {
        Double result = givenList.stream()
                                 .collect(averagingDouble(String::length));
    }

    @Test
    void collectors_summingDouble_Long_Int() {
        Double result = givenList.stream()
                                 .collect(summingDouble(String::length));
    }

    @Test
    void collectors_maxBy_minBy() {
        Optional<String> result = givenList.stream()
                                           .collect(maxBy(Comparator.naturalOrder()));
    }

    @Test
    void collectors_groupingBy() {
        Map<Integer, Set<String>> result = givenList.stream()
                                                    .collect(groupingBy(String::length, toSet()));
        assertThat(result)
                .containsEntry(1, newHashSet("a"))
                .containsEntry(2, newHashSet("bb", "dd"))
                .containsEntry(3, newHashSet("ccc"));
    }

    @Test
    void collectors_partitioningBy() {
        Map<Boolean, List<String>> result = givenList.stream()
                                                     .collect(partitioningBy(s -> s.length() > 2));

        assertThat(result)
                .containsEntry(false, newArrayList("a", "bb", "dd"))
                .containsEntry(true, newArrayList("ccc"));
    }

    @Test
    void custom_collectors() {
    }
}
