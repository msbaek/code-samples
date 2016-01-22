package nameinverter;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NameInverter {
	public String invert(String name) {
		if (name == null || name.isEmpty())
			return "";
		else
			return formatName(removeHonorifics(splitNames(name)));
	}

	private ArrayList<String> splitNames(String name) {
		return new ArrayList<>(Arrays.asList(name.trim().split("\\s+")));
	}

	private List<String> removeHonorifics(List<String> names) {
		if (names.size() > 1 && isHonorific(names.get(0)))
			names.remove(0);
		return names;
	}

	private boolean isHonorific(String name) {
		return name.matches("Mr\\.|Mrs\\.");
	}

	private String formatName(List<String> names) {
		if (names.size() == 1)
			return names.get(0);
		else {
			return formatMultiElementName(names);
		}
	}

	private String formatMultiElementName(List<String> names) {
		String postNominal = "";
		if (names.size() > 2)
			postNominal = getPostNominals(names);
		return String.format("%s, %s %s", names.get(1), names.get(0), postNominal).trim();
	}

	private String getPostNominals(List<String> names) {
		List<String> postNominals = names.subList(2, names.size());
		return Joiner.on(" ").join(postNominals);
	}
}
