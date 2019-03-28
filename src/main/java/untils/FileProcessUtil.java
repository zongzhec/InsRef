package untils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileProcessUtil {

	public List<HashMap<String, String>> getInsFromFile(String fileName) {
		List<HashMap<String, String>> instruments = new ArrayList<HashMap<String, String>>();
		List<String> lineContent = new ArrayList<String>();

		BufferedReader br = null;
		FileReader fr = null;

		try {
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);
			lineContent = Files.readAllLines(Paths.get(fileName));
			// Enable dynamic fields from source file.
			String[] keys = lineContent.get(0).split("\\|");
			for (int i = 1; i < lineContent.size(); i++) {
				String[] values = lineContent.get(i).split("\\|");
				HashMap<String, String> instrument = new HashMap<String, String>();
				for (int j = 0; j < keys.length; j++) {
					instrument.put(keys[j].trim().toLowerCase(), values[j].trim().toLowerCase());
				}
				instruments.add(instrument);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
					if (fr != null) {
						fr.close();
					}
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return instruments;
	}
}
