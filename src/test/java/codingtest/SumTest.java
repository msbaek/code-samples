package codingtest;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SumTest {
	@Test
	public void test_sum() {
		assertThat(sum(10), is(55));
	}

	private int sum(int n) {
		if(n <= 1)
			return n;
		return sum(n) + sum(n - 1);
	}
}
