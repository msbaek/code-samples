package fpij;

import org.junit.Test;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.function.BinaryOperator.maxBy;
import static java.util.stream.Collectors.*;

public class Ch03Test {
	List<Person> people = Arrays.asList(
			new Person("John", 20),
			new Person("Sara", 21),
			new Person("Jane", 21),
			new Person("Greg", 35)
			);

	@Test
	public void iterating_a_string() {
		String str = "w00t";
//		str.chars().forEach(System.out::println);
		str.chars().forEach(Ch03Test::printChar);
	}

	public static void printChar(int aChar) {
		System.out.println((char)aChar);
	}

	@Test
	public void sorting_with_a_comparator() {
		// ascending
//		people.stream()
//				.sorted((p1, p2) -> p1.ageDifference(p2))
//				.collect(toList());
		people.stream()
				.sorted(Person::ageDifference)
				.collect(toList());
		// decending
		people.stream()
				.sorted((p1, p2) -> p2.ageDifference(p1))
				.collect(toList());
		// cann't be MR because of parameter orders
		Comparator<Person> ascendingComparator = (p1, p2) -> p1.ageDifference(p2);
		Comparator<Person> decendingComparator = ascendingComparator.reversed();
	}

	@Test
	public void min_max() {
		people.stream()
				.max(Person::ageDifference)
				.ifPresent(eldes -> System.out.println("Eldest: " + eldes));
	}

	@Test
	public void sorted_comparing() {
		Function<Person, String> byName = Person::getName;
		people.stream()
				.sorted(comparing(byName));
	}

	@Test
	public void multiple_and_fluent_comparison() {
		people.stream()
				.sorted(
						comparing(Person::getAge)
						.thenComparing(Person::getName)
				)
				.collect(toList());
	}

	@Test
	public void using_collect_method_and_collectors_class() {
//		List<Person> olderThan20 = new ArrayList<>();
//		people.stream()
//				.filter(person -> person.getAge() > 20)
//				.forEach(person -> olderThan20.add(person));

//		List<Person> olderThan20 = people.stream()
//				.filter(person -> person.getAge() > 20)
//				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
		List<Person> olderThan20 = people.stream()
				.filter(person -> person.getAge() > 20)
				.collect(toList());
	}

	@Test
	public void grouping_by_age() {
		Map<Integer, List<Person>> peopleByAge = people.stream()
				.collect(groupingBy(Person::getAge));
		System.out.println(peopleByAge);

		Map<Integer, List<String>> map = people.stream()
				.collect(groupingBy(Person::getAge, mapping(Person::getName, toList())));
		System.out.println(map);
	}

	@Test
	public void group_names_by_first_character__get_oldes_in_each_group() {
//		Comparator<Person> byAge = Comparator.comparing(Person::getAge);
//		people.stream()
//				.collect(
//						groupingBy(
//								person -> person.getName().charAt(0),
//								reducing(BinaryOperator.maxBy(byAge))
//						)
//				);
		// group by first character of name
		// and
		// reduce to the person with maximum age.
		Map<Character, Optional<Person>> oldestPersonInEachAlphabet = people.stream()
				.collect(
						groupingBy(
								person -> person.getName().charAt(0),
								reducing(maxBy(comparing(Person::getAge)))
						)
				);
		System.out.println(oldestPersonInEachAlphabet);
	}

	@Test
	public void listing_all_files_in_a_directory() throws IOException {
		Files.list(Paths.get("."))
				.forEach(System.out::println);

		System.out.println("--------------------");

		Files.list(Paths.get("."))
				.filter(Files::isDirectory)
				.forEach(System.out::println);
	}

	@Test
	public void listing_selected_files_in_a_directory() throws IOException {
		String[] javaFiles = new File("src/main/java").list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".java");
			}
		});

//		Arrays.stream(javaFiles)
//				.forEach(System.out::println);

		Files.newDirectoryStream(
				Paths.get("src/main/java"),
				path -> path.toString().endsWith(".java")
		)
				.forEach(System.out::println);
	}

	@Test
	public void list_using_file_properties() {
//		Arrays.stream(new File(".").listFiles(file -> file.isHidden()))
//				.forEach(System.out::println);
		Arrays.stream(new File(".").listFiles(File::isHidden))
				.forEach(System.out::println);
	}

	@Test
	public void listing_immediate_subdirectories_using_flatMap() {
		List<File> files = new ArrayList<>();
		File[] javaFiles = new File("src/test/java").listFiles();
		for (File file : javaFiles) {
			File[] filesInSubDir = file.listFiles();
			if(filesInSubDir != null)
				files.addAll(Arrays.asList(filesInSubDir));
			else
				files.add(file);
		}
		files.stream().forEach(System.out::println);
		System.out.printf("Count: %d\n", files.size());

		files = Stream.of(new File("src/test/java").listFiles())
				.flatMap(file -> file.listFiles() == null ? Stream.of(file) : Stream.of(file.listFiles()))
				.collect(toList());
		System.out.printf("Count: %d\n", files.size());
	}

	@Test
	public void watching_a_file_change() throws IOException, InterruptedException {
		Path path = Paths.get(".");
		WatchService watchService = path.getFileSystem().newWatchService();
		path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

		System.out.println("report any file changed within next 1 minutes...");

		WatchKey watchKey = watchService.poll(1, TimeUnit.MINUTES);
		if(watchKey != null) {
			watchKey.pollEvents().stream().forEach(event -> System.out.println(event.context()));
		}
	}
}
