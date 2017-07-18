package enum_test;

import lombok.Getter;
import org.junit.Test;

import java.util.stream.Stream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

enum InterestType {
    INTEREST_FREE("Y"),
    INTEREST_PARTIAL("H"),
    INTEREST("N");

    @Getter
    private String value;

    InterestType(String value) {
        this.value = value;
    }

    public static InterestType getInterestType(String interestType) {
        return Stream.of(InterestType.values())
                .filter(s -> s.getValue().equals(interestType))
                .findFirst()
                .orElse(INTEREST_FREE);
    }

    public static InterestType getByName(String name) {
        try {
            return InterestType.valueOf(name);
        } catch (IllegalArgumentException e) {
            return INTEREST_FREE;
        }
    }
}

public class EnumTest {
    @Test
    public void get_by_type_code() throws Exception {
        assertThat(InterestType.getInterestType("Y"), is(InterestType.INTEREST_FREE));
        assertThat(InterestType.getInterestType("H"), is(InterestType.INTEREST_PARTIAL));
        assertThat(InterestType.getInterestType("N"), is(InterestType.INTEREST));
        assertThat(InterestType.getInterestType("X"), is(InterestType.INTEREST_FREE));
    }

    @Test
    public void get_by_type_name() throws Exception {
        assertThat(InterestType.getByName("INTEREST_FREE"), is(InterestType.INTEREST_FREE));
        assertThat(InterestType.getByName("INTEREST_PARTIAL"), is(InterestType.INTEREST_PARTIAL));
        assertThat(InterestType.getByName("INTEREST"), is(InterestType.INTEREST));
        assertThat(InterestType.getByName("X"), is(InterestType.INTEREST_FREE));
    }
}
