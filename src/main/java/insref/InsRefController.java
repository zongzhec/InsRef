package insref;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dnl.utils.text.table.TextTable;
import model.Concepts;
import untils.FileProcessUtil;
import untils.LogUtil;

public class InsRefController {

	static List<HashMap<String, String>> instruments = new ArrayList<HashMap<String, String>>();
	static List<HashMap<String, String>> instrumentsLme = new ArrayList<HashMap<String, String>>();
	static List<HashMap<String, String>> instrumentsPrime = new ArrayList<HashMap<String, String>>();
	static HashMap<String, String> instrument = new HashMap<String, String>();
	static HashMap<String, String> instrumentPrime = new HashMap<String, String>();

	public static void main(String[] args) throws IOException {
		initialize();
		runProcess();
	}

	public static void runProcess() throws IOException {
		// run process and continuously retrieve user input.
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String input = "";
		int returnCode = 1;
		while (returnCode > 0) {
			input = reader.readLine();
			returnCode = parseCommands(input);
		}
		LogUtil.info("Exiting, bye.");
		// System.exit(0);

	}

	public static int parseCommands(String input) {
		int returnCode = 0;
		// Parse different commands from user.
		switch (input.toLowerCase()) {
		case "show":
			getInsInfo();
			applyRules();
			showRefInfo();
			returnCode = 1;
			break;
		case "exit":
			returnCode = 0;
			break;
		default:
			LogUtil.warning("Unrecognized command, please try again");
			returnCode = 400;
			break;
		}
		return returnCode;
	}

	public static void showRefInfo() {
		// Show instruments info as a table.
		String[] columnNames = instrumentsLme.get(0).keySet().toArray(new String[0]);
		String[][] data = new String[instrumentsLme.size()][instrumentsLme.get(0).size()];
		for (int i = 0; i < instrumentsLme.size(); i++) {
			data[i] = instrumentsLme.get(i).values().toArray(new String[0]);
		}

		TextTable textTable = new TextTable(columnNames, data);
		textTable.printTable();
	}

	public static void applyRules() {
		// Rules are applied here and can be added with further rules.
		instrumentsLme = applyRulePublish(instrumentsLme);
		instrumentsLme = applyRuleMapping(instrumentsLme, instrumentsPrime);

	}

	public static List<HashMap<String, String>> applyRuleMapping(List<HashMap<String, String>> instrumentsLme, List<HashMap<String, String>> instrumentsPrime) {
		// Map LME instruments with prime instruments
		for (int i = 0; i < instrumentsLme.size(); i++) {
			instrument = instrumentsLme.get(i);
			// Mapping key is flexible via input field "MAPPING_KEY"
			String mappingKeyLme = instrument.get(Concepts.MAPPING_KEY);
			for (int j = 0; j < instrumentsPrime.size(); j++) {
				// Start to scan prime instruments and link with lme instruments.
				instrumentPrime = instrumentsPrime.get(j);
				String mappingKeyPrime = instrumentPrime.get(Concepts.MAPPING_KEY);
				if (instrument.get(mappingKeyLme).equalsIgnoreCase(instrumentPrime.get(mappingKeyPrime))) {
					// linked instruments with prime, now enriching tradable field.
					if (instrumentPrime.get(Concepts.PUBLISH).equalsIgnoreCase(Concepts.TRUE)) {
						instrument.put(Concepts.TRADABLE, Concepts.TRUE);
					}
				}
			}
			instrumentsLme.set(i, instrument);
		}
		return instrumentsLme;

	}

	public static List<HashMap<String, String>> applyRulePublish(List<HashMap<String, String>> instruments) {
		// If a LME instrument is published, the tradable flag should be set as true.
		for (int i = 0; i < instruments.size(); i++) {
			instrument = instruments.get(i);
			if (instrument.get(Concepts.PUBLISH).equalsIgnoreCase(Concepts.TRUE)) {
				instrument.put(Concepts.TRADABLE, Concepts.TRUE);
			}
			instruments.set(i, instrument);
		}
		return instruments;
	}

	public static void getInsInfo() {
		// get instruments info from input file.
		clearIns();
		FileProcessUtil fp = new FileProcessUtil();
		instruments = fp.getInsFromFile("C:\\AppData\\ins.txt");
		for (int i = 0; i < instruments.size(); i++) {
			instrument = instruments.get(i);
			switch (instrument.get(Concepts.CATEGORY)) {
			case Concepts.LME:
				instrumentsLme.add(instrument);
				break;
			case Concepts.PRIME:
				instrumentsPrime.add(instrument);
				break;
			default:
				break;
			}
		}

	}

	public static void clearIns() {
		// clear out current instruments before loading.
		instruments.clear();
		instrumentPrime.clear();
		instrumentsLme.clear();
	}

	public static void initialize() {
		// Initialize and print welcome message.
		LogUtil.info(
				"Welcome to use Instrument Reference System, for any questions please contact zongzhe_chen@sina.com");
		LogUtil.info("Please choose available commands and press enter");
		LogUtil.info("You can modify source file C:\\AppData\\ins.txt at any time before typing new commands");
		LogUtil.info("Current available commands are: 1)show  2)exit");

	}

}
