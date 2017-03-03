package nl.esciencecenter.e3dchem.kripodb.py.fragmentsbysimilarity;

import java.io.File;
import java.util.Arrays;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.StringValue;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.port.PortType;

import nl.esciencecenter.e3dchem.kripodb.KripoNodeModel;

/**
 * This is the model implementation of FragmentBySimilarity.
 *
 */
public class FragmentBySimilarityModel extends KripoNodeModel<FragmentsBySimilarityConfig> {
	public FragmentBySimilarityModel() {
		super(new PortType[] { BufferedDataTable.TYPE }, new PortType[] { BufferedDataTable.TYPE });
		python_code_filename = "fragment_by_similarity.py";
		required_python_packages = Arrays.asList("kripodb.canned");
	}

	@Override
	protected FragmentsBySimilarityConfig createConfig() {
		return new FragmentsBySimilarityConfig();
	}

	@Override
	protected DataTableSpec[] getOutputSpecs(DataTableSpec[] inSpecs) {
		DataTableSpec outputSpec = new DataTableSpec(
				new DataColumnSpecCreator("query_frag_id", StringCell.TYPE).createSpec(),
				new DataColumnSpecCreator("hit_frag_id", StringCell.TYPE).createSpec(),
				new DataColumnSpecCreator("score", DoubleCell.TYPE).createSpec());
		return new DataTableSpec[] { outputSpec };
	}

	@Override
	protected DataTableSpec[] configure(DataTableSpec[] inSpecs) throws InvalidSettingsException {
		FragmentsBySimilarityConfig config = getConfig();

		String fragmentIdColumn = config.getFragmentIdColumn().getStringValue();
		int fragmentIdColumnIndex = inSpecs[0].findColumnIndex(fragmentIdColumn);
		if (fragmentIdColumnIndex == -1) {
			// if no column has been selected then use first string like column
			int i = 0;
			for (DataColumnSpec spec : inSpecs[0]) {
				if (spec.getType().isCompatible(StringValue.class)) {
					setWarningMessage(
							"Column '" + spec.getName() + "' automatically chosen as fragment identifier column");
					config.getFragmentIdColumn().setStringValue(spec.getName());
					fragmentIdColumnIndex = i;
					break;
				}
				i++;
			}
			if (fragmentIdColumnIndex == -1) {
				throw new InvalidSettingsException("No valid identifier column available, require a String column");
			}
		}
		if (!inSpecs[0].getColumnSpec(fragmentIdColumnIndex).getType().isCompatible(StringValue.class)) {
			throw new InvalidSettingsException("Column '" + fragmentIdColumn + "' does not contain String cells");
		}

		String matrix = config.getMatrix().getStringValue();
		if ("".equals(matrix) || matrix == null) {
			throw new InvalidSettingsException("Matrix file or ws url can not be empty");
		} else {
			if (matrix.startsWith("http")) {
				// TODO test if webservice is online, using a HEAD request.
			} else {
				File fragmentsdb = new File(matrix);
				if (!fragmentsdb.canRead()) {
					throw new InvalidSettingsException("Unable to read matrix file");
				}
			}
		}
		return super.configure(inSpecs);
	}

}
