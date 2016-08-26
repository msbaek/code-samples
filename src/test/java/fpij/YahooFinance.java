package fpij;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;

public class YahooFinance {
	public BigDecimal getPrice(String ticker) {
		try {
			URL url = new URL("http://ichart.finance.yahoo.com/table.csv?s=" + ticker);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String data = reader.lines().skip(1).findFirst().get();
			String[] dataItems = data.split(",");
			return new BigDecimal(dataItems[dataItems.length - 1]);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
