/**
 * 
 */
package pawel.sopremo.operator;

import org.junit.Test;

import pawel.utils.JsonConverter;
import eu.stratosphere.sopremo.expressions.ConstantExpression;
import eu.stratosphere.sopremo.io.Source;
import eu.stratosphere.sopremo.testing.SopremoTestPlan;
import eu.stratosphere.sopremo.type.IJsonNode;

/**
 * @author ptondryk
 * 
 */
public class EventSopremoOperatorTest {

	@Test
	public void testExtractEvents() {
		final SopremoTestPlan sopremoPlan = new SopremoTestPlan(1, 1);

		final EventSopremoOperator eventOperator = new EventSopremoOperator();
		eventOperator.setInputs(new Source("file://"
				+ System.getProperty("user.dir")
				+ "/src/test/resources/test_operators/time_output.json"));
		eventOperator.setNewer(new ConstantExpression("false"));

		sopremoPlan.getOutputOperator(0).setInputs(eventOperator);

		// prepare expected output
		IJsonNode input1 = JsonConverter
				.string2JsonNode("{\"analyzedText\":\"Taiwan Semiconductor Manufacturing Co, Taiwan's largest semiconductor maker, said on Wednesday its board had approved overseas issuance of up to US$350 million in convertible bonds to raise funds for capital expenditures.   Taiwan Semiconductor issued a statement saying the bond issue would raise funds for the company's long-term investments over the coming two years. It did not elaborate. Taiwan Semiconductor has embarked on an ambitious expansion. In mid-April, it announced it would build six advanced wafer-fabrication plants as part of a T$400 billion investment at southern Taiwan's Tainan Scienced-based industrial park. The Tainan investment would be worth T$400 billion over 10 years, making it Taiwan's largest single industrial project -- bigger even than Formosa group's huge T$240 billion petrochemical plant at central Yunlin county.   The chipmaker's board had approved the promotion of Rick Tsai as executive vic-president, Gary Tseng, Quincy Lin and Stephen T. Tso as senior vice-presidents, and J.B. Chen as vice-president, the statement said. The company has become the world's largest custom maker of integrated circuits, a strategy that has attracted many imitators because of its success. The firm's foundry business does not design and sell its own IC products, but custom makes them according to customers' specifications. -- Taipei Newsroom (5080815)\",\"events\":[{\"event\": \"Taiwan Semiconductor Manufacturing Co said\", \"end\": \"1997-05-14 23,59,59\", \"is_personal\": \"false\", \"start\": \"1997-05-14 00,00,00\", \"text\": \"Taiwan Semiconductor Manufacturing Co, Taiwan's largest semiconductor maker, said on Wednesday its board had approved overseas issuance of up to US$350 million in convertible bonds to raise funds for capital expenditures.\", \"is_timespan\": \"false\", \"range_end\": \"221\", \"range_start\": \"0\"}],\"timestamp\":\"19970514000000\"}");

		sopremoPlan.getExpectedOutput(0).add(input1);

		sopremoPlan.run();

	}
}
