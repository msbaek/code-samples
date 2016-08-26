package fpij;

import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static fpij.Ch07Test.BigFactorialTailLambda.bigFactorial;
import static fpij.Ch07Test.FactorialTailLambda.factorial;
import static fpij.Ch07Test.Memoizer.callMemoized;
import static fpij.TailCalls.call;
import static fpij.TailCalls.done;
import static java.math.BigInteger.ONE;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class Ch07Test {
	static class Factorial {
		public static int factorialRec(final int number) {
			if (number == 1)
				return number;
			else
				return number * factorialRec(number - 1);
		}
	}// cannot handle large input -- stack overflow

	static class FactorialTail {
		public static int factorialTail(final int factorial, final int number) {
			if (number == 1)
				return factorial;
			else
				return factorialTail(factorial * number, number - 1);
		}
	}

	static class FactorialTailLambda {
		public static TailCall<Integer> factorialTailLambda(final int factorial, final int number) {
			if (number == 1)
				return done(factorial);
			else
				return call(() -> factorialTailLambda(factorial * number, number - 1));
		}

		public static int factorial(final int number) {
			return factorialTailLambda(1, number).invoke();
		}
	}

	@Test
	public void test_factorial() {
		// assertThat(factorialTail(1, 1), is(1));
		// assertThat(factorialTail(1, 2), is(2));
		// assertThat(factorialTail(1, 3), is(6));
		// assertThat(factorialRec(3), is(6));
		// assertThat(factorialTail(1, 20000), is(6));
		assertThat(factorial(10), is(3628800));
		assertThat(factorial(20000), is(0));
	}

	static class BigFactorialTailLambda {
		public static BigInteger bigFactorial(final BigInteger number) {
			return factorialTailLambda(ONE, number).invoke();
		}

		private static TailCall<BigInteger> factorialTailLambda(final BigInteger factorial, final BigInteger number) {
			if (number.equals(ONE))
				return done(factorial);
			else
				return call(() -> factorialTailLambda(multiply(factorial, number), decrement(number)));
		}

		private static BigInteger multiply(final BigInteger first, final BigInteger second) {
			return first.multiply(second);
		}

		private static BigInteger decrement(final BigInteger number) {
			return number.subtract(ONE);
		}
	}

	@Test
	public void factorial_for_big_number() {
		System.out.println(bigFactorial(new BigInteger("20000")));
	}

	static class Memoizer {
		public static <T, R> R callMemoized(final BiFunction<Function<T, R>, T, R> function, final T input) {
			Function<T, R> memoized = new Function<T, R>() {
				private final Map<T, R> store = new HashMap<T, R>();

				@Override
				public R apply(T input) {
					return store.computeIfAbsent(input, key -> function.apply(this, key));
				}
			};
			return memoized.apply(input);
		}
	}

	class RodCutterBasic {
		final List<Integer> prices;

		public RodCutterBasic(List<Integer> prices) {
			this.prices = prices;
		}

		public int maxProfit(final int length) {
			int profit = length <= prices.size() ? prices.get(length - 1) : 0;
			for (int i = 1; i < length; i++) {
				int priceWhenCut = maxProfit(i) + maxProfit(length - i);
				if (profit < priceWhenCut)
					profit = priceWhenCut;
			}
			return profit;
		}

		public int maxProfitM(final int rodLength) {
			return callMemoized((final Function<Integer, Integer> func, final Integer length) -> {
				int profit = length <= prices.size() ? prices.get(length - 1) : 0;
				for (int i = 1; i < length; i++) {
					int priceWhenCut = func.apply(i) + func.apply(length - i);
					if (priceWhenCut > profit)
						profit = priceWhenCut;
				}
				return profit;
			}, rodLength);
		}
	}

	@Test
	public void basic_rod_cutter() {
		List<Integer> priceValues = Arrays.asList( //
				2, // 1
				1, // 2
				1, // 3
				2, // 4
				2, // 5
				2, // 6
				1, // 7
				8, // 8
				9, // 9
				15); // 10
		RodCutterBasic rodCutter = new RodCutterBasic(priceValues);
//		System.out.println(rodCutter.maxProfit(20)); // 4.131
		System.out.println(rodCutter.maxProfitM(20)); // 42
	}
}
