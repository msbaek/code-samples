package fpij_dzone;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * https://dzone.com/articles/functional-programming-in-java-8-part-0-motivation
 */
public class Part0Test {
    private List<String> names = Arrays.asList("John", "Jane", "Mark", "Michole");

    @Test
    public void reduce_strings_in_a_list() throws Exception {
        assertThat(greet(names), is("Welcome John Jane Mark Michole !"));
        assertThat(greet_nasty_fp(names), is("Welcome John Jane Mark Michole !"));
        assertThat(greet_right_fp(names), is("Welcome John Jane Mark Michole !"));
    }

    private String greet(List<String> names) {
        String greeting = "Welcome ";
        for (String name : names) {
            greeting += name + " "; // We don't care about the last space right now.
        }
        greeting += "!";

        return greeting;
        /**
         * all variables are final
         *
         * This is a perfectly valid function to create such a greet String in Java.
         * But if you you are fP, this won’t work. You change the state of greeting, which is not allowed in fP
         */
    }

    /**
     * What you basically do in fP is the concatination of all names in one line into one String.
     */
    private String greet_nasty_fp(List<String> names) {
        final String reducedString = "Welcome " + names.get(0) + " " + names.get(1) + " " + names.get(2) + " "
                + names.get(names.size() - 1) + " ";
        return reducedString + "!";
    }

    private String greet_right_fp(List<String> names) {
        String greeting = names.stream()
                .map(name -> name + " ")
                .reduce("Welcome ", (acc, name) -> acc + name);
        return greeting + "!";
    }
}
