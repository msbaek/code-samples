package convert;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

// https://www.baeldung.com/java-map-to-string-conversion

public class MapToStringConversionTest {
    Map<Integer, String> wordsByKey = new HashMap<>();

    @BeforeEach
    void setUp() {
        wordsByKey.put(1, "one");
        wordsByKey.put(2, "two");
        wordsByKey.put(3, "three");
        wordsByKey.put(4, "four");
    }

    public String convertWithIteration(Map<Integer, ?> map) {
        StringBuilder mapAsString = new StringBuilder("{");
        for (Integer key : map.keySet()) {
            mapAsString.append(key + "=" + map.get(key) + ", ");
        }
        mapAsString.delete(mapAsString.length() - 2, mapAsString.length()).append("}");
        return mapAsString.toString();
    }

    @Test
    public void givenMap_WhenUsingIteration_ThenResultingStringIsCorrect() {
        String mapAsString = convertWithIteration(wordsByKey);
        Assert.assertEquals("{1=one, 2=two, 3=three, 4=four}", mapAsString);

        mapAsString = convertWithStream(wordsByKey);
        Assert.assertEquals("{1=one, 2=two, 3=three, 4=four}", mapAsString);
        Map<String, String> stringStringMap = convertWithStream(mapAsString);
        Assert.assertEquals("{ 2=two,  3=three,  4=four}, {1=one}", stringStringMap.toString());
    }

    public String convertWithStream(Map<Integer, ?> map) {
        String mapAsString = map.keySet().stream()
                                .map(key -> key + "=" + map.get(key))
                                .collect(Collectors.joining(", ", "{", "}"));
        return mapAsString;
    }

    public Map<String, String> convertWithStream(String mapAsString) {
        Map<String, String> map = Arrays.stream(mapAsString.split(","))
                                        .map(entry -> entry.split("="))
                                        .collect(Collectors.toMap(entry -> entry[0], entry -> entry[1]));
        return map;
    }
}
