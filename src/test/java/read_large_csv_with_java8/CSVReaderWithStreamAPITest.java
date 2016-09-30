package read_large_csv_with_java8;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

//https://dzone.com/articles/how-to-read-a-big-csv-file-with-java-8-and-stream
class YourJavaItem {
	private String itemNumber;
	private String someProeprty;

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	public void setSomeProeprty(String someProeprty) {
		this.someProeprty = someProeprty;
	}
}

public class CSVReaderWithStreamAPITest {
	private List<YourJavaItem> processInputFile(String inputFilePath) {
		List<YourJavaItem> inputList = new ArrayList<YourJavaItem>();
		try {
			File inputF = new File(inputFilePath);
			InputStream inputFS = new FileInputStream(inputF);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
			// skip the header of the csv
			inputList = br.lines().skip(1).map(mapToItem).collect(Collectors.toList());
			br.close();
			// } catch (FileNotFoundException | IOException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inputList;
	}

	private Function<String, YourJavaItem> mapToItem = (line) -> {
		String[] p = line.split(",");// a CSV has comma separated lines
		YourJavaItem item = new YourJavaItem();
		item.setItemNumber(p[0]);// <-- this is the first column in the csv file
		if (p[3] != null && p[3].trim().length() > 0) {
			item.setSomeProeprty(p[3]);
		}
		// more initialization goes here
		return item;
	};
}
