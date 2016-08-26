package fpij;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class Ch01Test {
	@Test
	public void filter_discount_and_sum_up_prices() {
		List<BigDecimal> prices = Arrays.asList(
				new BigDecimal("10"),
				new BigDecimal("30"),
				new BigDecimal("17"),
				new BigDecimal("20"),
				new BigDecimal("15"),
				new BigDecimal("18"),
				new BigDecimal("45"),
				new BigDecimal("12")
		);

		BigDecimal totalOfDiscountedPrcies = BigDecimal.ZERO;
		for (BigDecimal price : prices) {
			if (price.compareTo(BigDecimal.valueOf(20)) > 0)
				totalOfDiscountedPrcies = totalOfDiscountedPrcies.add(price.multiply(BigDecimal.valueOf(0.9)));
		}

		assertThat(totalOfDiscountedPrcies.toString(), is("67.5"));

		totalOfDiscountedPrcies = prices.stream()
				.filter(price -> price.compareTo(BigDecimal.valueOf(20)) > 0)
				.map(price -> price.multiply(BigDecimal.valueOf(0.9)))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		assertThat(totalOfDiscountedPrcies.toString(), is("67.5"));
	}
}
