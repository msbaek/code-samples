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
	}

	private List<Integer> primeFactorsOf(int n) {
		ArrayList<Integer> factors = new ArrayList<>();
		if(n > 1)
			factors.add(2);
		return factors;
	}

	private Matcher<List> isListOf(Integer... ints) {
		return is(Arrays.asList(ints));
	}
}
