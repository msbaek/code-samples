package codingtest;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MaxArrayTest {
	@Test
	public void get_max_from_array() {
		assertThat(getMaxFromArray(new int[]{0}, 1), is(0));
		assertThat(getMaxFromArray(new int[]{1}, 1), is(1));
		assertThat(getMaxFromArray(new int[]{1, 2}, 2), is(2));
	}

	private int getMaxFromArray(int[] numbers, int arrayLength) {
		if (arrayLength == 1)
			return numbers[0];
		return max(numbers[arrayLength - 1], getMaxFromArray(numbers, numbers.length - 1));
	}

	private int max(int x, int y) {
		return x > y ? x : y;
	}
}
