package java8;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Ch03Test {
	@Test
	public void ch_5_5() {
		Trader raoul = new Trader("Raoul", "Cambridge");
		Trader mario = new Trader("Mario", "Milan");
		Trader alan = new Trader("Alan", "Cambridge");
		Trader brian = new Trader("Brian", "Cambridge");

		List<Transaction> transactions = Arrays.asList( //
				new Transaction(brian, 2011, 300), //
				new Transaction(raoul, 2012, 1000), //
				new Transaction(raoul, 2011, 400), //
				new Transaction(mario, 2012, 710), //
				new Transaction(mario, 2012, 700), //
				new Transaction(alan, 2012, 950) //
				);

		System.out.println( //
				transactions.stream() //
						.filter(t -> t.getYear() == 2011) //
						.sorted(comparing(Transaction::getValue)) //
						.collect(toList()) //
				);

		System.out.println( //
				transactions.stream() //
						.map(tr -> tr.getTrader().getCity()) //
						.distinct() //
						.collect(toList()));

		System.out.println( //
				transactions.stream() //
						.map(tx -> tx.getTrader()).filter(tr -> tr.getCity().equals("Cambridge")) //
						.sorted(comparing(Trader::getName)) //
						.map(Trader::getName).reduce("", (a1, a2) -> a1 + " " + a2));

		System.out.println( //
				transactions.stream() //
						.map(tx -> tx.getTrader()) //
						.map(Trader::getName).distinct() //
						.sorted() //
						.collect(toList()) //
				);
	}

	public void int_predicate() {
		Function<String, Integer> string2integer = (String s) -> Integer.parseInt(s);

		BiPredicate<List<String>, String> contains = (list, element) -> list.contains(element);
		BiPredicate<List<String>, String> contains2 = List::contains;

		List<Apple> inventory = Arrays.asList(new Apple(1), new Apple(2), new Apple(3), new Apple(4), new Apple(5), new Apple(6), new Apple(7),
				new Apple(8));

		inventory.sort(comparing(Apple::getWeight));

		String[] arrayOfWords = { "Goodbye", "World" };
		Stream<String> stream = Arrays.stream(arrayOfWords);
		stream.forEach(System.out::println);

		List<String> strings = Arrays.asList("Goodbye", "World") //
				.stream() // Stream<String>
				.map(w -> w.split("")) // Stream<String[]>
				// .map(Arrays::stream) // Stream<Stream<String>>
				.flatMap(Arrays::stream) // Stream<Stream<String>>
				.distinct() //
				.collect(toList());
		System.out.printf("[%s]", strings);

		Arrays.asList(1, 2, 3, 4, 5).stream().map(v -> v ^ 2).collect(toList());

		List<Integer> numbers1 = Arrays.asList(1, 2, 3);
		List<Integer> numbers2 = Arrays.asList(4, 5);
		numbers1.stream().map(i -> numbers2.stream().filter(j -> i + j % 3 == 0).map(j -> new int[] { i, j })).collect(toList());

		List<Integer> numbers = Arrays.asList(4, 5, 6, 7, 9);
		Integer reduce = numbers.stream().reduce(0, Integer::max);
		assertThat(reduce, is(9));
	}

	static class Apple {
		private int weight;

		public Apple(int weight) {
			this.weight = weight;
		}

		public int getWeight() {
			return this.weight;
		}
	}
}
