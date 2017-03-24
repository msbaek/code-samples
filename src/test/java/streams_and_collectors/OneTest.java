package streams_and_collectors;

import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * https://dzone.com/articles/using-java-collectors
 */
public class OneTest {
    private List<Integer> numbers;

    @Before
    public void setUp() {
        numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    }

    @Test
    public void summing_a_integer_list() throws Exception {
        Integer sum = numbers.stream()
                .reduce(0, (x, y) -> x + y);
        assertThat(sum, is(55));
    }

    @Test
    public void generate_random_numbers() throws Exception {
        Random random = new Random();
        numbers = random
                .ints(1, 100)
                .limit(10)
                .boxed()
                .collect(Collectors.toList());
        assertThat(
                numbers.stream()
                        .filter(n -> n < 1 || n > 100)
                        .count(), is(0l));
    }

    @Test
    public void average() throws Exception {
        Double avg = numbers.stream()
                .collect(Collectors.averagingInt(x -> x));
        assertThat(avg, is(5.5d));
    }

    @Test
    public void maxBy() throws Exception {
        Optional<Integer> max = numbers.stream()
                .collect(Collectors.maxBy(Integer::compare));
        assertThat(max.orElse(-1), is(10));
    }

    @Test
    public void summary_statistics() throws Exception {
        IntSummaryStatistics summaryStatistics = numbers.stream()
                .collect(Collectors.summarizingInt(x -> x));
        assertThat(summaryStatistics.toString(),
                is("IntSummaryStatistics{count=10, sum=55, min=1, average=5.500000, max=10}"));
    }
}
