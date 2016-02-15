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
		assertSorted(intList(1, 3, 2), intList(1, 2, 3));
		assertSorted(intList(3, 1, 2), intList(1, 2, 3));
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
		if(list.size() == 0)
			return list;
		else {
			List<Integer> l = new ArrayList<>();
			int m = list.get(0);
			List<Integer> h = new ArrayList<>();
			for (int i : list) {
				if(i < m)
					l.add(i);
				if(i > m)
					h.add(i);
			}
			if(l != null) sorted.addAll(sort(l));
			sorted.add(m);
			if(h != null) sorted.addAll(sort(h));
		}
		return sorted;
	}
}
