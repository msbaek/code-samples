package fpij_dzone;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * https://dzone.com/articles/an-introduction-to-functional-programming-in-java
 * <p>
 * Why shouldn’t you use Lists, Sets, and so on anymore?
 * Nothing is wrong with them. But when you want to work functional, you should consider using them.
 * The standard workflow is to convert your data structure into a Stream.
 * Then, you want to work on them in a functional manner and, in the end,
 * you transform them back into the data structure of your choice.
 * <p>
 * Streams are also immutable. So every time you manipulate it, you create a new Stream.
 */
public class Part3Test {
    @Data
    @AllArgsConstructor
    public static class User {
        private String username;
        private String birthday;
    }

    @Test
    public void convert_data_structures_into_streams() throws Exception {
        // Convert Multiple Objects Into a Stream
        Stream<String> objectStream = Stream.of("Hello", "World");

        // Converting Collections (Lists, Sets, etc.) and Arrays using stream()
        new HashSet<String>().stream();
        String[] array = {"apple", "banana"};
        Arrays.stream(array);
    }

    @Test
    public void working_with_streams() throws Exception {
        // map
        List<Integer> collect = Stream.of(1, 2, 3)
                .map(n -> n * n)
                .collect(toList());
        assertThat(collect.toString(), is("[1, 4, 9]"));

        // flatMap
        List<Integer> numbers1 = Arrays.asList(1, 2, 3);
        List<Integer> numbers2 = Arrays.asList(4, 5, 6);

        collect = Stream.of(numbers1, numbers2) // Stream<List<Integer>>
                .flatMap(l -> l.stream()) // Stream<Integer>
                .collect(toList());
        assertThat(collect.toString(), is("[1, 2, 3, 4, 5, 6]"));

        // forEach
        Stream.of(0, 1, 2, 3)
                .forEach(System.out::println);

        // filter
        collect = Stream.of(0, 1, 2, 3)
                .filter(n -> n < 2)
                .collect(toList());
        assertThat(collect.toString(), is("[0, 1]"));

        // Functions that can make your life way easier
        // when creating ‘test’ functions are Predicate.negate()and Objects.nonNull().
        Predicate<Integer> small = n -> n < 2;

        collect = Stream.of(0, 1, 2, 3)
                .filter(small.negate())
                .collect(toList());
        assertThat(collect.toString(), is("[2, 3]"));

        // filter null
        collect = Stream.of(0, 1, null, 3)
                .filter(Objects::nonNull)
                .map(num -> num * 2)
                .collect(toList());
        assertThat(collect.toString(), is("[0, 2, 6]"));

        // collect with joining
        String sentence = Stream.of("Who", "are", "you?")
                .collect(Collectors.joining(" "));
        assertThat(sentence, is("Who are you?"));

        // reduce shortcuts
        Integer sum = Stream.of(1, 2, 3)
                .reduce(0, Integer::sum);
        assertThat(sum, is(6));

        // sorted shortcuts
        collect = Stream.of(3, 2, 4, 0)
                .sorted((c1, c2) -> c1 - c2)
                .collect(toList());
        assertThat(collect.toString(), is("[0, 2, 3, 4]"));

        // other kinds of streams, IntStream, LongStream, DoubleStream
        sum = Stream.of(0, 1, 2, 3)
                .mapToInt(num -> num)
                .sum();
        assertThat(sum, is(6));

        sum = IntStream.of(0, 1, 2, 3)
                .sum();
        assertThat(sum, is(6));

        /**
         * Use Streams for Tests
         *
         * Tests can also be used to test your methods. ex. anyMatch, count, maxand so on
         * If something goes wrong in your program, use peek to log data
         */
    }

    @Test
    public void sortscuts_of_shortcuts() throws Exception {
        List<User> users = Arrays.asList(
                new User("peter", "20.02.1990"),
                new User("kid", "23.02.2008"),
                new User("bruce", "20.02.1980")
        );

        String today = "20.02"; //Just to make the example easier. In production, you would use LocalDateTime or so.
        users.stream()
                .filter(user -> user.getBirthday().startsWith(today))
                .forEach(user -> sendMessage("Happy birthday, ".concat(user.getUsername()).concat("!"), user));
    }

    private void sendMessage(String greeting, User user) {
    }
}
