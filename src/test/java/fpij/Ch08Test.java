package fpij;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.joining;

class Tickers {
	public static final List<String> symbols = Arrays.asList("AMD", "HPQ", "IBM", "TXN", "VMW", "XRX", "AAPL", "ADBE", "AMZN", "CRAY", "CSCO",
			"DELL", "GOOG", "INTC", "INTU", "MSFT", "ORCL", "TIBX", "VRSN", "YHOO");
}

public class Ch08Test {
	@Test
	public void sorted_tickers_over_100_dollars() {
		YahooFinance finance = new YahooFinance();

		System.out.println(//
				Tickers.symbols.stream() //
						.filter(symbol -> finance.getPrice(symbol).compareTo(new BigDecimal("100")) > 0) //
						.sorted() //
						.collect(joining(",")));
	}

	static class StockInfo {
		public final String ticker;
		public final BigDecimal price;

		public StockInfo(String ticker, BigDecimal price) {
			this.ticker = ticker;
			this.price = price;
		}

		@Override
		public String toString() {
			return String.format("ticker: %s price %g", ticker, price);
		}
	}

	static class StockUtil {
		public static StockInfo getPrice(final String ticker) {
			YahooFinance finance = new YahooFinance();
			return new StockInfo(ticker, finance.getPrice(ticker));
		}

		public static Predicate<StockInfo> isPriceLessThan(final int price) {
			return stockInfo -> stockInfo.price.compareTo(BigDecimal.valueOf(price)) < 0;
		}

		public static StockInfo pickHigh(final StockInfo stockInfo1, final StockInfo stockInfo2) {
			return stockInfo1.price.compareTo(stockInfo2.price) > 0 ? stockInfo1 : stockInfo2;
		}
	}

	@Test
	public void pick_the_highest_priced_stock_whose_value_is_less_than_$500__imperative() {
		//
		final List<StockInfo> stocks = new ArrayList<>();
		final Predicate<StockInfo> isPriceLessThan500 = StockUtil.isPriceLessThan(500);
		StockInfo highPriced = new StockInfo("", BigDecimal.ZERO);

		for (String symbol : Tickers.symbols) {
			StockInfo stockInfo = StockUtil.getPrice(symbol);
			if (isPriceLessThan500.test(stockInfo))
				highPriced = StockUtil.pickHigh(highPriced, stockInfo);
		}

		System.out.println("High priced under $500 is " + highPriced); // 8.079s
	}

	@Test
	public void pick_the_highest_priced_stock_whose_value_is_less_than_$500__functional() {
		System.out.println( //
				Tickers.symbols.parallelStream() //
						.map(StockUtil::getPrice) //
						.filter(StockUtil.isPriceLessThan(500)) //
						.reduce(StockUtil::pickHigh).get());
		// straem:          8.059
		// parallel straem: 0.676
	}
}
