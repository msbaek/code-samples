import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SortTest {
	@Test
	public void sortings() {
		assertThat(sort(Arrays.asList()), is(Arrays.asList()));
	}

	private List<Integer> sort(List<Integer> list) {
		return null;
	}
}
