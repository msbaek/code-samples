import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SortTest {
	@Test
	public void sortings() {
		assertThat(sort(intList()), is(intList()));
	}

	private List<Integer> intList() {
		return Arrays.asList();
	}

	private List<Integer> sort(List<Integer> list) {
		return new ArrayList<>();
	}
}
