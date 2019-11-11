package junit5_in_spring;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * https://cheese10yun.github.io/junit5-in-spring/
 *
 * https://www.baeldung.com/parameterized-tests-junit-5
 */
public class Junit5InSpringTest {
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void isBlank(String value) {
        System.out.printf("value: [%s]", value);
        assertThat(StringUtils.hasText(value)).isFalse();
    }

    enum Quarter {
        Q1(1, "1분기"),
        Q2(2, "2분기"),
        Q3(3, "3분기"),
        Q4(4, "4분기");

        private final int value;
        private String description;

        Quarter(int value, String description) {
            this.value = value;
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    @ParameterizedTest
    @EnumSource(Quarter.class)
    void quarter_value_should_be_1to4(Quarter quarter) {
        System.out.printf("[%s]", quarter.getDescription());
        assertThat(quarter.value).isBetween(1, 4);
    }

    @ParameterizedTest
    @EnumSource(value = Quarter.class, names = {"Q1", "Q2"})
    void can_get_specific_enums_using_names_parameter(Quarter quarter) {
        System.out.printf("[%s]", quarter.getDescription());
        assertThat(quarter.value).isBetween(1, 2);
    }

    @ParameterizedTest
    @CsvSource(value = {"010-1234-1234,01012341234", "010-2333-2333,01023332333", "02-223-1232,022231232"})
        // @CsvFileSource(resources = "/data.csv", numLinesToSkip = 1)
    void phone_number_should_remove_minus_sign(String value, String expected) {
        String resutl = value.replace("-", "");
        assertThat(resutl).isEqualTo(expected);
    }

    static class Amount {
        private int price;
        private int ea;

        public Amount(int price, int ea) {
            this.price = price;
            this.ea = ea;
        }

        public int totalPrice() {
            return price * ea;
        }
    }

    @ParameterizedTest
    @MethodSource("providerAmount")
    void test_amount_total(Amount amount, int expectedTotalPrice) {
        assertThat(amount.totalPrice()).isEqualTo(expectedTotalPrice);
    }

    static Stream<Arguments> providerAmount() {
        return Stream.of(
                Arguments.of(new Amount(1000, 2), 2000),
                Arguments.of(new Amount(2000, 5), 10000),
                Arguments.of(new Amount(4000, 5), 20000),
                Arguments.of(new Amount(5000, 3), 15000)
        );
    }
}
