import org.junit.Test;

import java.util.ArrayList;
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
		assertSorted(intList(2, 1, 3), intList(1, 2, 3));
		assertSorted(intList(2, 3, 1), intList(1, 2, 3));
	}

	private void sortBig(int n) {
		List<Integer> unsorted = new ArrayList<>();
		for(int i = 0; i < n; i++)
			unsorted.add((int)(Math.random() * 10000.0));
		List<Integer> sorted = sort(unsorted);
		for(int i = 0; i < n - 1; i++)
			assertThat(sorted.get(i) <= sorted.get(i + 1), is(true));
	}

	private void assertSorted(List<Integer> unsorted, List<Integer> sorted) {
		assertThat(sort(unsorted), is(sorted));
	}

	private List<Integer> intList(Integer ... ints) {
		return Arrays.asList(ints);
	}

	private List<Integer> sort(List<Integer> list) {
		List<Integer> sorted = new ArrayList<>();
		if(list.size() == 1)
			sorted.add(list.get(0));
		else {
			int l = 0;
			int m = list.get(0);
			int h = 0;
			for (int i : list) {
				if(i < m)
					l = i;
				if(i > m)
					h = i;
			}
			sorted.add(l);
			sorted.add(m);
			sorted.add(h);
		}
		return sorted;
	}
}
