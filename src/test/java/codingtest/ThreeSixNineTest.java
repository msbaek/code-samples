package codingtest;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ThreeSixNineTest {
	public String is369(String digits) {
		if (digits.length() <= 1)
			return check369(Integer.parseInt(digits));
		else
			return check369(getFirstDigit(digits)) + is369(getRestDigits(digits));
	}

	private String check369(int n) {
		if (n != 0 && n % 3 == 0)
			return "O";
		else
			return "X";
	}

	private String getRestDigits(String digits) {
		return digits.length() <= 1 ? digits : digits.substring(1);
	}

	private int getFirstDigit(String digits) {
		return Integer.parseInt(digits.substring(0, 1));
	}

	@Test
	public void is_369() throws Exception {
		assertThat(is369("1"), is("X"));
		assertThat(is369("2"), is("X"));
		assertThat(is369("3"), is("O"));
		assertThat(is369("4"), is("X"));
		assertThat(is369("5"), is("X"));
		assertThat(is369("6"), is("O"));
		assertThat(is369("7"), is("X"));
		assertThat(is369("8"), is("X"));
		assertThat(is369("9"), is("O"));

		assertThat(is369("10"), is("XX"));
		assertThat(is369("13"), is("XO"));
		assertThat(is369("30"), is("OX"));
		assertThat(is369("31"), is("OX"));
		assertThat(is369("33"), is("OO"));
		assertThat(is369("113"), is("XXO"));
		assertThat(is369("300"), is("OXX"));
		assertThat(is369("333"), is("OOO"));

		assertThat(is369("3000"), is("OXXX"));
		assertThat(is369("3333"), is("OOOO"));
	}
}
