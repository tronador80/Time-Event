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
 * Test class for class {@link TemporalSummariesSopremoOperator}.
 * 
 * @author ptondryk
 * 
 */
public class TemporalSummariesSopremoOperatorTest {

	@Test
	public void testTemporalSummaries() {
		final SopremoTestPlan sopremoPlan = new SopremoTestPlan(1, 1);

		final TemporalSummariesSopremoOperator temporalSummariesTaggerOperator = new TemporalSummariesSopremoOperator();
		temporalSummariesTaggerOperator.setInputs(new Source("file://"
				+ System.getProperty("user.dir")
				+ "/src/test/resources/test_operators/event_output.json"));

		sopremoPlan.getOutputOperator(0).setInputs(
				temporalSummariesTaggerOperator);

		// prepare expected output
		IJsonNode output1 = JsonConverter
				.string2JsonNode("{\"analyzedText\" : \"Taiwan Semiconductor Manufacturing Co, Taiwan's largest semiconductor maker, said on Wednesday its board had approved overseas issuance of up to US$350 million in convertible bonds to raise funds for capital expenditures.   Taiwan Semiconductor issued a statement saying the bond issue would raise funds for the company's long-term investments over the coming two years. It did not elaborate. Taiwan Semiconductor has embarked on an ambitious expansion. In mid-April, it announced it would build six advanced wafer-fabrication plants as part of a T$400 billion investment at southern Taiwan's Tainan Scienced-based industrial park. The Tainan investment would be worth T$400 billion over 10 years, making it Taiwan's largest single industrial project -- bigger even than Formosa group's huge T$240 billion petrochemical plant at central Yunlin county.   The chipmaker's board had approved the promotion of Rick Tsai as executive vic-president, Gary Tseng, Quincy Lin and Stephen T. Tso as senior vice-presidents, and J.B. Chen as vice-president, the statement said. The company has become the world's largest custom maker of integrated circuits, a strategy that has attracted many imitators because of its success. The firm's foundry business does not design and sell its own IC products, but custom makes them according to customers' specifications. -- Taipei Newsroom (5080815)\",\"summary\" : \"\"}");
		IJsonNode output2 = JsonConverter
				.string2JsonNode("{\"analyzedText\" : \"Suomen Optioporssi (SOP), a Finnish options exchange, on Monday put the value of northern bleached softwood pulp at $554.90 per tonne, up from $549.56 last week. Information for calculation of the benchmark pulp price is supplied by more than 30 companies from 11 different countries, SOP says. The benchmark Pulp Indicator Index (PIX) is released by SOP at the start of every week, usually on Monday. In May, the PIX mean was $541.51, up from $518.12 in April. The PIX mean was $522.28 in March, $546.59 in February and $556.86 in January. -- Helsinki newsroom +358-9-680 50 245, e-mail news@reuters.fi\",\"summary\" : \"The PIX mean was $522.28 in March, $546.59 in February and $556.86 in January. In May, the PIX mean was $541.51, up from $518.12 in April.\"}");

		sopremoPlan.getExpectedOutput(0).add(output1).add(output2);

		sopremoPlan.run();

	}

	@Test
	public void testTemporalSummariesWithParameter() {
		final SopremoTestPlan sopremoPlan = new SopremoTestPlan(1, 1);

		final TemporalSummariesSopremoOperator temporalSummariesTaggerOperator = new TemporalSummariesSopremoOperator();
		temporalSummariesTaggerOperator.setInputs(new Source("file://"
				+ System.getProperty("user.dir")
				+ "/src/test/resources/test_operators/event_output.json"));
		temporalSummariesTaggerOperator
				.setSentenceNum(new ConstantExpression(1));

		sopremoPlan.getOutputOperator(0).setInputs(
				temporalSummariesTaggerOperator);

		// prepare expected output
		IJsonNode output1 = JsonConverter
				.string2JsonNode("{\"analyzedText\" : \"Taiwan Semiconductor Manufacturing Co, Taiwan's largest semiconductor maker, said on Wednesday its board had approved overseas issuance of up to US$350 million in convertible bonds to raise funds for capital expenditures.   Taiwan Semiconductor issued a statement saying the bond issue would raise funds for the company's long-term investments over the coming two years. It did not elaborate. Taiwan Semiconductor has embarked on an ambitious expansion. In mid-April, it announced it would build six advanced wafer-fabrication plants as part of a T$400 billion investment at southern Taiwan's Tainan Scienced-based industrial park. The Tainan investment would be worth T$400 billion over 10 years, making it Taiwan's largest single industrial project -- bigger even than Formosa group's huge T$240 billion petrochemical plant at central Yunlin county.   The chipmaker's board had approved the promotion of Rick Tsai as executive vic-president, Gary Tseng, Quincy Lin and Stephen T. Tso as senior vice-presidents, and J.B. Chen as vice-president, the statement said. The company has become the world's largest custom maker of integrated circuits, a strategy that has attracted many imitators because of its success. The firm's foundry business does not design and sell its own IC products, but custom makes them according to customers' specifications. -- Taipei Newsroom (5080815)\",\"summary\" : \"\"}");
		IJsonNode output2 = JsonConverter
				.string2JsonNode("{\"analyzedText\" : \"Suomen Optioporssi (SOP), a Finnish options exchange, on Monday put the value of northern bleached softwood pulp at $554.90 per tonne, up from $549.56 last week. Information for calculation of the benchmark pulp price is supplied by more than 30 companies from 11 different countries, SOP says. The benchmark Pulp Indicator Index (PIX) is released by SOP at the start of every week, usually on Monday. In May, the PIX mean was $541.51, up from $518.12 in April. The PIX mean was $522.28 in March, $546.59 in February and $556.86 in January. -- Helsinki newsroom +358-9-680 50 245, e-mail news@reuters.fi\",\"summary\" : \"In May, the PIX mean was $541.51, up from $518.12 in April.\"}");

		sopremoPlan.getExpectedOutput(0).add(output1).add(output2);

		sopremoPlan.run();

	}
}
