package streams_and_collectors;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

// http://www.baeldung.com/java-8-streams-introduction
@Slf4j
public class StreamIntroTest {
    @Test
    void stream_creation() {
        Stream<String> stream = Arrays.stream(new String[]{"a", "b", "c"});

        stream = Stream.of("a", "b", "c");

        // stream operations
        assertThat(stream.distinct().count()).isEqualTo(3);
    }

    @Test
    void iteration_tesst() {
        List<String> list = Arrays.asList("Seamless code review",
                "Code review is the surest path to better code, ",
                "and it’s fundamental to how GitHub works.",
                "Built-in review tools make code review an essential part of your team’s process.");
        boolean res = list.stream()
                .anyMatch(s -> s.contains("a"));

        assertThat(res).isEqualTo(true);
    }

    @Test
    void filtering_test() {
        ArrayList<String> list = new ArrayList<>();
        list.add("One");
        list.add("OneAndOnly");
        list.add("Derek");
        list.add("Change");
        list.add("factory");
        list.add("justBefore");
        list.add("Italy");
        list.add("Italy");
        list.add("Thursday");
        list.add("");
        list.add("");

        List<String> l = list.stream()
                .filter(s -> s.contains("d"))
                .collect(Collectors.toList());

        assertThat(l.toString()).isEqualTo("[OneAndOnly, Thursday]");
        log.error("hello");
    }
}
