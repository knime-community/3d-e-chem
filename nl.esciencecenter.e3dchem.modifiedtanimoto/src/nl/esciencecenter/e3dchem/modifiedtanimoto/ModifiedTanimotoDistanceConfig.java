package nl.esciencecenter.e3dchem.modifiedtanimoto;

import org.knime.base.util.flowvariable.FlowVariableProvider;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.util.CheckUtils;
import org.knime.distance.category.DistanceCategoryConfig;
import org.knime.distance.util.propertyresolver.Configuration;
import org.knime.distance.util.propertyresolver.Property;

@Configuration
public final class ModifiedTanimotoDistanceConfig extends
		DistanceCategoryConfig<ModifiedTanimotoDistance> {

	@Property("meanBitDensity")
	private double meanBitDensity = 0.01;

	/**
	 * Framework constructor.
	 */
	ModifiedTanimotoDistanceConfig() {

	}

	public ModifiedTanimotoDistanceConfig(final double meanBitDensity,
			String column) throws InvalidSettingsException {
		super(column);
		this.meanBitDensity = meanBitDensity;
		CheckUtils.checkSetting(meanBitDensity >= 0,
				"mean bit density is not positive: %f ", meanBitDensity);
	}

	@Override
	protected DistanceCategoryConfig<?> clone(String... columns)
			throws InvalidSettingsException {
		CheckUtils.checkSetting(columns != null && columns.length == 1,
				"Exactly one column must be selected.");
		return new ModifiedTanimotoDistanceConfig(meanBitDensity, columns[0]);
	}

	@Override
	public String getFactoryId() {
		return ModifiedTanimotoDistanceFactory.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ModifiedTanimotoDistance createDistanceMeasure(DataTableSpec spec,
			FlowVariableProvider flowVariableProvider)
			throws InvalidSettingsException {
		return new ModifiedTanimotoDistance(this, spec);
	}

	public double getMeanBitDensity() {
		return meanBitDensity;
	}

	public void setMeanBitDensity(final double meanBitDensity) {
		this.meanBitDensity = meanBitDensity;
	}

}
