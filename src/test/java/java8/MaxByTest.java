package java8;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

class PopularArticleCount {
	final String name;
	final int count;

	PopularArticleCount(String name, int count) {
		this.name = name;
		this.count = count;
	}

}


public class MaxByTest {
	@Test
	public void maxBy() {
		List<PopularArticleCount> list = new ArrayList<>();

		IntStream.rangeClosed(1, 10)//
				.forEach(i -> { //
					PopularArticleCount ac = new PopularArticleCount("regdt" + i, 100 - i);
					list.add(ac);
				});

		System.out.println(list);
	}
}
