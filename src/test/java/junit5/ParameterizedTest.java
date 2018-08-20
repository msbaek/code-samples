package junit5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class ParameterizedTest {
    @org.junit.jupiter.params.ParameterizedTest
    @ValueSource(strings = {"Hello", "JUnit"})
    void parameterizedTest(String word) {
        assertThat(word, notNullValue());
    }

    @org.junit.jupiter.params.ParameterizedTest(name = "run #{index} with [{arguments}]")
    @ValueSource(strings = {"Hello", "JUnit"})
    void withValueSource(String word) {
        assertThat(word, notNullValue());
    }

    @DisplayName("Roman numeral")
    @org.junit.jupiter.params.ParameterizedTest(name = "\"{0}\" should be {1}")
    @CsvSource({"I, 1", "II, 2", "V, 5"})
    void withNiceName(String word, int number) {
    }

    @org.junit.jupiter.params.ParameterizedTest
    @ValueSource(strings = { "Hello", "JUnit" })
    void withOtherParams(String word, TestInfo info, TestReporter reporter) {
        reporter.publishEntry(info.getDisplayName(), "Word: " + word);
    }
}
