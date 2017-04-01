package java8;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * https://dzone.com/articles/java-streams-groupingby-examples
 */
@Data
@AllArgsConstructor
class Player {
    private int year;
    private String teamID;
    private String lgID;
    private String playerID;
    private int salary;
}

public class StreamsGroupingByTest {
    private String filename = "./players.csv";
    private final Pattern pattern = Pattern.compile(",");

    private void consumePlayerStreams(Consumer<Stream<Player>> c) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            Stream<Player> playerStream = in
                    .lines()
                    .skip(1)
                    .map(line -> {
                        String[] arr = pattern.split(line);
                        return new Player(Integer.parseInt(arr[0]),
                                arr[1],
                                arr[2],
                                arr[3],
                                Integer.parseInt(arr[4]));
                    });

            c.accept(playerStream);
        }
    }

    @Test
    public void group_by_year() throws Exception {
        consumePlayerStreams(playerStream -> playerStream
                .collect(Collectors.groupingBy(Player::getYear))
                .entrySet()
                .forEach(System.out::println));
    }

    @Test
    public void group_by_year_and_teamId() throws Exception {
        consumePlayerStreams(playerStream -> playerStream
                .collect(Collectors.groupingBy(x -> new ArrayList<>(Arrays.asList(Integer.toString(x.getYear()), x.getTeamID()))))
                .entrySet()
                .forEach(x -> {
                    System.out.println(x.getKey());
                    x.getValue()
                            .forEach(p -> System.out.printf(" ( %2s %-10s %-10d )%n", p.getLgID(), p.getPlayerID(), p.getSalary()));
                }));
    }

    @Test
    public void collect_into_a_set() throws Exception {
        consumePlayerStreams(playerStream -> playerStream
                .collect(Collectors.groupingBy(x -> new ArrayList<>(Arrays.asList(x.getTeamID(), x.getLgID())), Collectors.toSet()))
                .entrySet()
                .forEach(x -> {
                    System.out.println(x.getKey());
                    x.getValue()
                            .forEach(p -> System.out.printf(" ( %2s %-10s %-10d )%n", p.getLgID(), p.getPlayerID(), p.getSalary()));
                }));
    }
}
