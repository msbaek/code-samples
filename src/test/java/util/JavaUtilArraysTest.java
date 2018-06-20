package util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * http://www.baeldung.com/java-util-arrays
 */
public class JavaUtilArraysTest {

    private final String[] intro = new String[]{"once", "upon", "a", "time"};

    @Test
    void copyOf_and_copyOfRange() {
        String[] abridgement = Arrays.copyOfRange(intro, 0, 3);
        assertThat(abridgement).isEqualTo(new String[]{"once", "upon", "a"});
        assertThat(Arrays.equals(intro, abridgement)).isEqualTo(false);

        String[] revised = Arrays.copyOf(intro, 3);
        String[] expanded = Arrays.copyOf(intro, 5);
        assertThat(revised).isEqualTo(Arrays.copyOfRange(intro, 0, 3));
        assertThat(expanded[4]).isNull();
    }

    @Test
    void fill() {
        String[] stutter = new String[3];
        Arrays.fill(stutter, "once");
        assertThat(Stream.of(stutter)
                .allMatch(el -> "once".equals(el))
        ).isEqualTo(true);
    }

    @Test
    void equals_and_deepEquals() {
        assertThat(Arrays.equals(new String []{"once", "upon", "a", "time"}, intro)).isEqualTo(true);
        assertThat(Arrays.equals(new String []{"once", "upon", "a", null}, intro)).isEqualTo(false);
    }

    @Test
    void arrays_toString() {
        assertThat(Arrays.toString(intro)).isEqualTo("[once, upon, a, time]");
    }

    @Test
    void setAll() {
        String[] strings = new String[4];
        Arrays.setAll(strings, i -> i + "th element");
        assertThat(Arrays.toString(strings)).isEqualTo("[0th element, 1th element, 2th element, 3th element]");
    }
}
