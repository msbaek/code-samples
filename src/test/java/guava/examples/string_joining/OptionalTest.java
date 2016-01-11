package guava.examples.string_joining;

import com.google.common.base.Optional;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OptionalTest {
    @Test
    public void isPresent_returns_true_if_it_is_not_null() {
        Optional<String> nickName = Optional.of("Barry");
        assertThat(nickName.isPresent(), is(true));
    }

    @Test
    public void absent_returns_optional_which_represents_null() {
        Optional<Object> absentValue = Optional.absent();
        assertThat(absentValue.isPresent(), is(false));
    }

    @Test
    public void assign_null() {
        try {
            Optional<String> nullString = null;
            assertThat(nullString.isPresent(), is(false));
        } catch (NullPointerException e) {
        }

        try {
            Optional.of(null);
        } catch (NullPointerException e) {
        }
    }

    @Test
    public void basicUsage() {
        Optional<String> nickname = getNickname(null);
        if (nickname == null)
            System.out.printf("can't do anything. just return....");

        if (nickname.isPresent())
            System.out.printf("your nickname is [%s]", nickname.get());
        else {
            try {
                nickname.get();
            } catch (IllegalStateException e) {
            }
            System.out.printf("throws IllegalStateException when you call get for the absent value");
        }
    }

    public Optional<String> getNickname(String name) {
        return Optional.fromNullable(name);
    }

    @Test
    public void coolerUsages() {
        Optional<String> nickname = Optional.fromNullable("Test");
        for (String actualNick : nickname.asSet())
                System.out.printf("\n[%s]\n", actualNick);

        String firstName = "first name";
        System.out.printf("\nhello %s", nickname.or(firstName));
    }
}
