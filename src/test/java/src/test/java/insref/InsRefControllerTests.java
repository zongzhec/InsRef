package src.test.java.insref;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import insref.InsRefController;
import model.Concepts;

class InsRefControllerTests {

	static List<HashMap<String, String>> instrumentsMock = new ArrayList<HashMap<String, String>>();
	static List<HashMap<String, String>> instrumentsLmeMock = new ArrayList<HashMap<String, String>>();
	static List<HashMap<String, String>> instrumentsPrimeMock = new ArrayList<HashMap<String, String>>();
	static HashMap<String, String> instrumentMock = new HashMap<String, String>();
	static HashMap<String, String> instrumentPrimeMock = new HashMap<String, String>();

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		// Mock instruments
		instrumentMock.put(Concepts.TRADABLE, Concepts.FALSE);
		instrumentMock.put(Concepts.MAPPING_KEY, Concepts.LME_CODE);
		instrumentMock.put(Concepts.LME_CODE, "map_key_01");
		instrumentsLmeMock.add(instrumentMock);
		instrumentPrimeMock.put(Concepts.TRADABLE, Concepts.FALSE);
		instrumentPrimeMock.put(Concepts.MAPPING_KEY, Concepts.EXCH_CODE);
		instrumentPrimeMock.put(Concepts.EXCH_CODE, "map_key_01");
		instrumentsPrimeMock.add(instrumentPrimeMock);
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void parseCommandsShouldReturnOneIfInputIsShowTest() {
		assertEquals(1, InsRefController.parseCommands("show"));
	}

	@Test
	void parseCommandsShouldReturnZeroIfInputIsExitTest() {
		assertEquals(0, InsRefController.parseCommands("exit"));
	}

	@Test
	void parseCommandsShouldReturnFourHandredIfInputIsNotValidTest() {
		assertEquals(400, InsRefController.parseCommands("command_not_valid"));
	}

	@Test
	void applyRulePublishShouldNotUpdateTradableIfInsIsNotPublishedTest() {
		instrumentMock = instrumentsLmeMock.get(0);
		instrumentMock.put(Concepts.PUBLISH, Concepts.FALSE);
		instrumentMock.put(Concepts.TRADABLE, Concepts.FALSE);
		instrumentsLmeMock.set(0, instrumentMock);
		instrumentsLmeMock = InsRefController.applyRulePublish(instrumentsLmeMock);
		assertEquals(Concepts.FALSE, instrumentsLmeMock.get(0).get(Concepts.TRADABLE));
	}

	@Test
	void applyRulePublishShouldUpdateTradableIfInsIsPublishedTest() {
		instrumentMock = instrumentsLmeMock.get(0);
		instrumentMock.put(Concepts.PUBLISH, Concepts.TRUE);
		instrumentMock.put(Concepts.TRADABLE, Concepts.FALSE);
		instrumentsLmeMock.set(0, instrumentMock);
		instrumentsLmeMock = InsRefController.applyRulePublish(instrumentsLmeMock);
		assertEquals(Concepts.TRUE, instrumentsLmeMock.get(0).get(Concepts.TRADABLE));
	}

	@Test
	void applyRuleMappingShouldNotUpdateTradableIfPrimeIsNotPublishedTest() {
		instrumentPrimeMock = instrumentsPrimeMock.get(0);
		instrumentPrimeMock.put(Concepts.PUBLISH, Concepts.FALSE);
		instrumentPrimeMock.put(Concepts.TRADABLE, Concepts.FALSE);
		instrumentMock = instrumentsLmeMock.get(0);
		instrumentMock.put(Concepts.PUBLISH, Concepts.FALSE);
		instrumentMock.put(Concepts.TRADABLE, Concepts.FALSE);
		instrumentsLmeMock.set(0, instrumentMock);
		instrumentsLmeMock = InsRefController.applyRuleMapping(instrumentsLmeMock, instrumentsPrimeMock);
		assertEquals(Concepts.FALSE, instrumentsLmeMock.get(0).get(Concepts.TRADABLE));
	}

	@Test
	void applyRuleMappingShouldNotUpdateTradableIfPrimeIsNotPublishedTest2() {
		instrumentPrimeMock = instrumentsPrimeMock.get(0);
		instrumentPrimeMock.put(Concepts.PUBLISH, Concepts.FALSE);
		instrumentPrimeMock.put(Concepts.TRADABLE, Concepts.FALSE);
		instrumentMock = instrumentsLmeMock.get(0);
		instrumentMock.put(Concepts.PUBLISH, Concepts.TRUE);
		instrumentMock.put(Concepts.TRADABLE, Concepts.TRUE);
		instrumentsLmeMock.set(0, instrumentMock);
		instrumentsLmeMock = InsRefController.applyRuleMapping(instrumentsLmeMock, instrumentsPrimeMock);
		assertEquals(Concepts.TRUE, instrumentsLmeMock.get(0).get(Concepts.TRADABLE));
	}

	@Test
	void applyRuleMappingShouldUpdateTradableIfPrimeIsPublishedTest() {
		instrumentPrimeMock = instrumentsPrimeMock.get(0);
		instrumentPrimeMock.put(Concepts.PUBLISH, Concepts.TRUE);
		instrumentPrimeMock.put(Concepts.TRADABLE, Concepts.TRUE);
		instrumentsPrimeMock.set(0, instrumentPrimeMock);
		instrumentMock = instrumentsLmeMock.get(0);
		instrumentMock.put(Concepts.PUBLISH, Concepts.FALSE);
		instrumentMock.put(Concepts.TRADABLE, Concepts.FALSE);
		instrumentsLmeMock.set(0, instrumentMock);
		instrumentsLmeMock = InsRefController.applyRuleMapping(instrumentsLmeMock, instrumentsPrimeMock);
		assertEquals(Concepts.TRUE, instrumentsLmeMock.get(0).get(Concepts.TRADABLE));
	}

}
