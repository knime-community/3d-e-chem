package nl.esciencecenter.e3dchem.modifiedtanimoto;

import org.knime.distance.category.DistanceCategorySubPanel;
import org.knime.distance.measure.bitvector.BitVectorDistanceMeasureFactory;

public class ModifiedTanimotoDistanceFactory extends
		BitVectorDistanceMeasureFactory<ModifiedTanimotoDistanceConfig> {

	public static final String ID = ModifiedTanimotoDistanceFactory.class
			.getName();

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DistanceCategorySubPanel<ModifiedTanimotoDistanceConfig> createComponent() {
		return new ModifiedTanimotoDistanceSubPanel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ModifiedTanimotoDistanceConfig createConfiguration() {
		return new ModifiedTanimotoDistanceConfig();
	}

}
