package nl.esciencecenter.e3dchem.modifiedtanimoto;

import static org.junit.Assert.*;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;

public class ModifiedTanimotoDistanceConfigTest {
	@Test
	public void getMeanBitDensity() throws InvalidSettingsException {
		double meanBitDensity = 0.023;
		ModifiedTanimotoDistanceConfig config = new ModifiedTanimotoDistanceConfig(meanBitDensity, "BitVector");
		
		double actual = config.getMeanBitDensity();
		assertEquals(meanBitDensity, actual, 0.001);
	}

	@Test(expected=InvalidSettingsException.class)
	public void construct_negativedensity_invalidsettingsexception() throws InvalidSettingsException {
		double meanBitDensity = -0.023;
		new ModifiedTanimotoDistanceConfig(meanBitDensity, "BitVector");
	}
	
	@Test
	public void getFactoryId() throws InvalidSettingsException {
		double meanBitDensity = 0.023;
		ModifiedTanimotoDistanceConfig config = new ModifiedTanimotoDistanceConfig(meanBitDensity, "BitVector");

		String factoryId = config.getFactoryId();
		
		String expected = "nl.esciencecenter.e3dchem.modifiedtanimoto.ModifiedTanimotoDistanceFactory";
		assertEquals(expected, factoryId);
	}
}
