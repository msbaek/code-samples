package fpij;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class Ch04Test {
	final List<Asset> assets = Arrays.asList(//
			new Asset(Asset.AssetType.BOND, 1000),//
			new Asset(Asset.AssetType.BOND, 2000),//
			new Asset(Asset.AssetType.STOCK, 3000),//
			new Asset(Asset.AssetType.STOCK, 4000)//
			);

	@Test
	public void total_asset_values() {
		System.out.println(//
				assets.stream()//
						.mapToInt(Asset::getValue)//
						.sum());

		System.out.println(//
				assets.stream() //
						.mapToInt(a -> a.getType() == Asset.AssetType.BOND ? a.getValue() : 0) //
						.sum());

		System.out.println(//
				assets.stream() //
						.mapToInt(a -> a.getType() == Asset.AssetType.STOCK ? a.getValue() : 0) //
						.sum());
	}

	@Test
	public void refactoring_to_separate_key_concern() {
		Predicate<Asset> assetSelector = asset -> asset.getType() == Asset.AssetType.BOND;

		System.out.println(//
				assets.stream() //
						.filter(assetSelector).mapToInt(Asset::getValue).sum());
	}

	@Test
	public void decorator_pattern_using_consumer_chains() {
		// camera, filter
		// reduce: merge filters
	}

	@Test
	public void fluent_interface_using_lambda() {
		Mailer mailer = new Mailer();
		mailer.from("build@agiledeveloper.com");
		mailer.to("venkats@agiledeveloper.com");
		mailer.subject("build notification");
		mailer.body("...your code sucks...");
		mailer.send();

		// method chaining
		new MailBuilder() //
				.from("build@agiledeveloper.com") //
				.to("venkats@agiledeveloper.com") //
				.subject("build notification") //
				.body("...it sucks less...") //
				.send();

		// fluent using consumer
		FluentMailer.send(mailer1 -> //
				mailer1.from("build@agiledeveloper.com") //
						.to("venkats@agiledeveloper.com") //
						.subject("build notification") //
						.body("...much better..."));
	}

	@Test
	public void dealing_with_exceptions() {
		List<String> paths = Arrays.asList("/usr", "/tmp");
		paths.stream() //
				.map(path -> {
					try {
						return new File(path).getCanonicalPath();
					} catch (IOException e) {
						return e.getMessage();
					}
				}) //
				.forEach(System.out::println);
		// Error, this code will not compile
	}
}
