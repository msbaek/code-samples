package fpij;

import org.junit.Test;

import java.util.List;
import java.util.stream.IntStream;

import static java.lang.Math.sqrt;
import static java.util.stream.Collectors.toList;

public class FunctionalPrimeNumbersTest {
	@Test
	public void name() {
		int number = 6;
		List<Integer> numbers = IntStream.rangeClosed(2, (int) sqrt(number)) //
				.filter(divisor -> number % divisor == 0) //
				.boxed() //
				.collect(toList());
		System.out.println(numbers);
		System.out.println(sqrt(number));
	}
}
