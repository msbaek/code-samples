import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SortTest {
	@Test
	public void sortings() {
		assertSorted(intList(), intList());
		assertSorted(intList(1), intList(1));
		assertSorted(intList(2, 1), intList(1, 2));
		assertSorted(intList(1, 3, 2), intList(1, 2, 3));
	}

	private void assertSorted(List<Integer> unsorted, List<Integer> sorted) {
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
			if(list.size() > 2) {
				if(list.get(1) > list.get(2)) {
					int temp = list.get(1);
					list.set(1, list.get(2));
					list.set(2, temp);
				}
			}
		}
		return list;
	}
}
