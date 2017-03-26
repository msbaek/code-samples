package fpij_dzone;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * https://dzone.com/articles/functional-programming-in-java-8-part-2-optionals
 */
public class Part2Test {
    private void assertWith(Class e, Supplier<Optional> s) {
        try {
            s.get();
        } catch (Exception e1) {
            assertThat(e1, isA(e));
            return;
        }
        fail(e.getSimpleName() + " expected !!");
    }

    @Test
    public void playing_with_optionals() throws Exception {
        String s = "Hello World!";
        String nullString = null;

        Optional<String> optionalS1 = Optional.of(s); // Will work
        Optional<String> optionalS2 = Optional.ofNullable(s); // Will work too
        assertWith(NullPointerException.class, () -> Optional.of(nullString)); // -> NullPointerException
        Optional<String> optionalNull2 = Optional.ofNullable(nullString); // Will work
        System.out.println(optionalS1.get()); // prints "Hello World!"

        // 1. use anonymous inner class
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                optionalNull2.get();
            }
        }).isInstanceOf(NoSuchElementException.class);

        // 2. use lambda
        Assertions.assertThatThrownBy(() -> optionalNull2.get()).isInstanceOf(NoSuchElementException.class);

        // 3. method reference
        Assertions.assertThatThrownBy(optionalNull2::get).isInstanceOf(NoSuchElementException.class);
        if (!optionalNull2.isPresent()) {
            System.out.println("Is empty"); // Will be printed
        }

        // ifPresent is best friend to replace the if(xx.isPresent()) ...
        optionalS1.ifPresent(System.out::println);

        // Returning a Modified Version of the Value
        Optional<Integer> i = Optional.of(1);
        assertThat(doubleValueOrZero(i), is(2));
    }

    private Integer doubleValueOrZero(Optional<Integer> v) {
        return v
                .map(n -> n * 2) // takes a function, applies it on the value and returns the result of the function,
                // wrapped in an Optional again. If the Optional is empty, it will return an empty Optional again
                .orElse(0) // returns the value of the Optional it is called on.
                // If there is no value, it returns the value you gave orElse(object) as a parameter
                ;
    }

    /**
     * should use Optionals just as a return value from a function
     */
}
