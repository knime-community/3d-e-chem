package nl.esciencecenter.e3dchem.modifiedtanimoto;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.vector.bitvector.BitVectorUtil;
import org.knime.core.data.vector.bitvector.BitVectorValue;
import org.knime.core.node.InvalidSettingsException;
import org.knime.distance.DistanceMeasurementException;
import org.knime.distance.SingleColumnDistanceMeasure;

public final class ModifiedTanimotoDistance extends
		SingleColumnDistanceMeasure<ModifiedTanimotoDistanceConfig> {

	/**
	 * @param config
	 *            distance configuration
	 * @param spec
	 *            input data table specification
	 * @throws InvalidSettingsException
	 *             thrown if the table specification does not fit to the
	 *             configured columns, either name or types
	 */
	public ModifiedTanimotoDistance(
			final ModifiedTanimotoDistanceConfig config,
			final DataTableSpec spec) throws InvalidSettingsException {
		super(config, spec);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double computeDistance(final DataRow row1, final DataRow row2)
			throws DistanceMeasurementException {
		BitVectorValue av = checkNotMissing(row1, getIndex(),
				BitVectorValue.class);
		BitVectorValue bv = checkNotMissing(row2, getIndex(),
				BitVectorValue.class);
		if (av.length() != bv.length()) {
			throw new DistanceMeasurementException("Cell in row '"
					+ row1.getKey() + " and cell in row '" + row1.getKey()
					+ " do not have same length");
		}

		double p0 = getConfig().getMeanBitDensity();
		double corr_st = (2 - p0) / 3;
		double corr_sto = (1 + p0) / 3;
		double n = av.length();
		double a = av.cardinality();
		double b = bv.cardinality();
		double c = BitVectorUtil.cardinalityOfIntersection(av, bv);

		double st = c / (a + b - c);
		double st0 = (n - a - b + c) / (n - c);
		double smt = corr_st * st + corr_sto * st0;

		return smt;
	}
}
