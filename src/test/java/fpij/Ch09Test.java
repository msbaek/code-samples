package fpij;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Ch09Test {
	List<Integer> prices = Arrays.asList(11, 22, 1, 2, 13, 4, 5, 66);

	@Test
	public void get_max_imperatively() {
		Integer max = 0;
		for (Integer price : prices) {
			if (price > max)
				max = price;
		}
		System.out.printf("max=%d\n", max);

		System.out.printf("max=%d\n", //
				prices.stream()//
						.reduce(0, Math::max));
	}

	@Test
	public void returning_a_Lambda_Expression_from_a_Lambda_Expression() {
		Function<String, Predicate<String>> startsWithLetter = //
				letter -> name -> name.startsWith(letter);
		Predicate<String> aThis = startsWithLetter.apply("tr");
		System.out.println(aThis.test("thom"));
	}
}
