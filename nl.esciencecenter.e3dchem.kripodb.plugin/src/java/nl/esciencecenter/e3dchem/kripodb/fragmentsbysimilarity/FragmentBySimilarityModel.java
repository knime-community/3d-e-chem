package nl.esciencecenter.e3dchem.kripodb.fragmentsbysimilarity;

import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.port.PortType;

import nl.esciencecenter.e3dchem.kripodb.PythonNodeModel;

/**
 * This is the model implementation of FragmentBySimilarity.
 *
 */
public class FragmentBySimilarityModel extends PythonNodeModel<FragmentsBySimilarityConfig> {
	public FragmentBySimilarityModel() {
		super(new PortType[] { BufferedDataTable.TYPE }, new PortType[] { BufferedDataTable.TYPE });
		python_code_filename = "fragment_by_similarity.py";
	}

	@Override
	protected FragmentsBySimilarityConfig createConfig() {
		return new FragmentsBySimilarityConfig();
	}

	@Override
	protected DataTableSpec[] getOutputSpecs(DataTableSpec[] inSpecs) {
		DataTableSpec outputSpec = new DataTableSpec(
				new DataColumnSpecCreator("hit_frag_id", StringCell.TYPE).createSpec(),
				new DataColumnSpecCreator("query_frag_id", StringCell.TYPE).createSpec(),
				new DataColumnSpecCreator("score", DoubleCell.TYPE).createSpec());
		return new DataTableSpec[] { outputSpec };
	}

}
