package fpij;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static fpij.Ch06Test.Evaluation.evaluate;
import static fpij.Ch06Test.Primes.primes;
import static java.lang.Math.sqrt;

public class Ch06Test {
	class Heavy {
		public Heavy() {
			System.out.println("Heavy created");
		}

		public String toString() {
			return "quite heavy";
		}
	}

	class HolderNaive {
		private Heavy heavy;

		public HolderNaive() {
			System.out.println("Holder created");
		}

		public Heavy getHeavy() {
			if (heavy == null) {
				heavy = new Heavy();
			}
			return heavy;
		}
	}

	class Holder {
		private Supplier<Heavy> heavy = () -> createAndCacheHeavy();

		public Holder() {
			System.out.println("Holder created");
		}

		public Heavy getHeavy() {
			return heavy.get();
		}

		private synchronized Heavy createAndCacheHeavy() {
			class HeavyFactory implements Supplier<Heavy> {
				private final Heavy heavyInstance = new Heavy();

				public Heavy get() {
					return heavyInstance;
				}
			}

			if (!HeavyFactory.class.isInstance(heavy)) {
				heavy = new HeavyFactory();
			}

			return heavy.get();
		}
	}

	// lazy and cache for heavyweight object creation

	// eager evaluation
	static class Evaluation {
		public static boolean evaluate(final int value) {
			System.out.println("evaluating ..." + value);
			simulateTimeConsumingOp(2000);
			return value > 100;
		}

		// ...

		private static void simulateTimeConsumingOp(int i) {
			try {
				Thread.sleep(i);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	static void eagerEvaluator(final boolean input1, final boolean input2) {
		System.out.println("eagerEvaluator called...");
		System.out.println("accept?: " + (input1 && input2));
	}

	@Test
	public void eager_evaluate() {
		eagerEvaluator(evaluate(1), evaluate(2));
		// eagerEvaluator 호출 전에 input1,2를 미리 evaluate하므로 logical expression short-circuit의 혜택을 못 받음
	}

	static void lazyEvaluator(final Supplier<Boolean> input1, final Supplier<Boolean> input2) {
		System.out.println("lazyEvaluator called...");
		System.out.println("accept?: " + (input1.get() && input2.get()));
	}

	@Test
	public void lazy_evaluate() {
		lazyEvaluator(() -> evaluate(1), () -> evaluate(2));
	}

	static class LazyStreams {
		private static int length(final String name) {
			System.out.println("getting length for " + name);
			return name.length();
		}

		private static String toUpper(final String name) {
			System.out.println("converting to uppercase: " + name);
			return name.toUpperCase();
		}
		// ...
	}

	@Test
	public void get_first_name_in_capital_which_has_3_characters() {
		List<String> names = Arrays.asList("Brad", "Kate", "Kim", "Jack", "Joe", "Mike", "Susan", "George", "Robert", "Julia", "Parker", "Benson");

		System.out.println( //
				names.stream() //
						.filter(name -> {
							System.out.printf("filter for [%s]\n", name);
							return name.length() == 3;
						}) //
						.map(name -> {
							System.out.printf("map    for [%s]\n", name);
							return name.toUpperCase();
						}) //
						.findFirst() //
						.get());
	}

	static boolean isPrime(final int number) {
		return number > 1 && //
				IntStream.rangeClosed(2, (int) sqrt(number)) //
						.noneMatch(divisor -> number % divisor == 0);
	}

	@Test
	public void prime_number_test() {
		System.out.println(isPrime(4));
	}

	static class Primes {
		// returns a prime number that’s after the given number
		public static int primeAfter(final int number) {
			if (isPrime(number + 1))
				return number + 1;
			else
				return primeAfter(number + 1);
		}

		public static List<Integer> primes(final int fromNumber, final int count) {
			return Stream.iterate(primeAfter(fromNumber - 1), Primes::primeAfter) //
					.limit(count) //
					.collect(Collectors.<Integer> toList());
		}
		// ...
	}

	@Test
	public void iterate_test() {
		primes(1, 5).stream()//
				.forEach(System.out::println);
	}
}
