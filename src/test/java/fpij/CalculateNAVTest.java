package fpij;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CalculateNAVTest {
	@Test
	public void computeStockWorth() {
		CalculateNAV calculateNAV = new CalculateNAV(s -> new BigDecimal("6.01"));

		BigDecimal expected = new BigDecimal("6010.00");
		assertThat(calculateNAV.computeStockWorth("GOOG", 1000), is(expected));
	}

	@Test
	public void integrate_with_web_service() {
		String ticker = "GOOG";
		BigDecimal price = yahooFinance.getPrice(ticker);
		assertThat(price, is(new BigDecimal("775.419983")));
	}

	YahooFinance yahooFinance = new YahooFinance();
}