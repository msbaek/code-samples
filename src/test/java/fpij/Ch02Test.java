package fpij;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

public class Ch02Test {

	private final List<String> friends = Arrays.asList(
			"Brian",
			"Nate",
			"Neal",
			"Raju",
			"Sara",
			"Scott"
	);

	@Test
	public void for_each() {
//		friends.forEach(new Consumer<String>() {
//			@Override
//			public void accept(String s) {
//				System.out.printf("name; [%s]\n", s);
//			}
//		});

//		friends.forEach(s -> System.out.printf("name; [%s]\n", s));
		friends.forEach(System.out::println);
	}

	@Test
	public void transforming_a_list() {
//		final ArrayList<String> uppercaseNames = new ArrayList<>();
//		friends.forEach(name -> uppercaseNames.add(name.toUpperCase()));
//		System.out.println(uppercaseNames);

//		friends.stream()
//				.map(name -> name.toUpperCase())
//				.forEach(name -> System.out.println(name));

		friends.stream()
				.map(String::toUpperCase)
				.forEach(System.out::println);
	}

	@Test
	public void filter() {
		List<String> friendsWhoesNameStartsWithN = friends.stream()
				.filter(s -> s.startsWith("N"))
				.collect(Collectors.toList());
		
//		final Function<String, Predicate<String>> startsWithLetter =
//				(String letter) ->
//						(String name) -> name.startsWith(letter);

		final Function<String, Predicate<String>> startsWithLetter =
				letter -> name -> name.startsWith(letter);

		friends.stream()
				.filter(startsWithLetter.apply("N")).count();
	}

//	public void pickName(List<String> names, String startingLetter) {
//		String foundName = null;
//		for (String name : names) {
//			if(name.startsWith(startingLetter)) {
//				foundName = name;
//				break;
//			}
//		}
//	}

	public void pickName(List<String> names, String startingLetter) {
		Optional<String> optionalFirstResult = names.stream()
				.filter(name -> name.startsWith(startingLetter))
				.findFirst();
		String result = optionalFirstResult.orElse("not found");
		optionalFirstResult.ifPresent(s -> System.out.println(s));
	}

	@Test
	public void reducing_a_collection_to_a_single_value() {
		friends.stream()
				.mapToInt(name -> name.length())
				.sum();

		Optional<String> longestName = friends.stream()
				.reduce((name1, name2) ->
						name1.length() > name2.length() ? name1 : name2);
	}

	@Test
	public void joining_elements() {
		String.join(", ", friends);

		System.out.println(
				friends.stream().
						map(String::toUpperCase).
						collect(joining(", ")));
	}
}
