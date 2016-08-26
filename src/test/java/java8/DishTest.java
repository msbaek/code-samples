package java8;

import org.junit.Test;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Math.sqrt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;
import static java8.Dish.menu;

public class DishTest {
	@Test
	public void dish() {
		Integer sum = menu.stream() //
				.map(Dish::getCalories) //
				.reduce(0, Integer::sum);
		System.out.println(sum);

		sum = menu.stream() //
				.mapToInt(Dish::getCalories) // IntStream
				.sum();
		System.out.println(sum);

		IntStream intStream = menu.stream() //
				.mapToInt(Dish::getCalories);
		Stream<Integer> boxed = intStream.boxed();

		menu.stream().collect(//
				reducing(0,//
						Dish::getCalories,//
						// Integer::sum//
						(a, b) -> a + b));

		menu.stream() //
				.collect( //
						groupingBy(Dish::getCaloricLevel) //
				);
	}

	@Test
	public void pythagoreanTriples() {
		Stream<double[]> pythogoreanTriples = IntStream.rangeClosed(1, 100) // IntStream
				.boxed() // Stream<Integer>
				.flatMap(a -> //
						IntStream.rangeClosed(a, 100) //
								.filter(b -> sqrt(a * a + b * b) % 1 == 0).mapToObj(b -> new double[] { a, b, sqrt(a * a + b * b) }));
		pythogoreanTriples.limit(3) //
				.forEach(t -> System.out.printf("%d, %d, %d\n", t[0], t[1], t[2]));
	}
}
