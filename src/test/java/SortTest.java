import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SortTest {
	@Test
	public void sortings() {
		assertThat(sort(intList()), is(intList()));
		assertThat(sort(intList(1)), is(intList(1)));
		List<Integer> unsorted = intList(2, 1);
		List<Integer> sorted = intList(1, 2);
		assertThat(sort(unsorted), is(sorted));
	}

	private List<Integer> intList(Integer ... ints) {
		return Arrays.asList(ints);
	}

	private List<Integer> sort(List<Integer> list) {
		if(list.size() > 1) {
			if(list.get(0) > list.get(1)) {
				int temp = list.get(0);
				list.set(0, list.get(1));
				list.set(1, temp);
			}
		}
		return list;
	}
}
