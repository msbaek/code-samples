import org.hamcrest.Matcher;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PrimeFactorsTest {
	@Test
	public void factors() {
		assertThat(primeFactorsOf(1), isListOf());
		assertThat(primeFactorsOf(2), isListOf(2));
		assertThat(primeFactorsOf(3), isListOf(3));
		assertThat(primeFactorsOf(4), isListOf(2,2));
		assertThat(primeFactorsOf(5), isListOf(5));
		assertThat(primeFactorsOf(6), isListOf(2,3));
		assertThat(primeFactorsOf(7), isListOf(7));
		assertThat(primeFactorsOf(8), isListOf(2,2,2));
	}

	private List<Integer> primeFactorsOf(int n) {
		ArrayList<Integer> factors = new ArrayList<>();
		if(n > 1) {
			if(n % 2 == 0) {
				factors.add(2);
				n /= 2;
			}
		}
		if(n > 1)
			factors.add(n);
		return factors;
	}

	private Matcher<List> isListOf(Integer... ints) {
		return is(Arrays.asList(ints));
	}
}
