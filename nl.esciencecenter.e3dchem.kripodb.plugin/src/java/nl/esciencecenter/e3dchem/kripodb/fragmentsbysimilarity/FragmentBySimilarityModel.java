package nl.esciencecenter.e3dchem.kripodb.fragmentsbysimilarity;

import java.io.File;
import java.util.Arrays;

import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.port.PortType;

import nl.esciencecenter.e3dchem.kripodb.PythonWrapperNodeModel;

/**
 * This is the model implementation of FragmentBySimilarity.
 *
 */
public class FragmentBySimilarityModel extends PythonWrapperNodeModel<FragmentsBySimilarityConfig> {
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
        DataTableSpec outputSpec = new DataTableSpec(new DataColumnSpecCreator("hit_frag_id", StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator("query_frag_id", StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator("score", DoubleCell.TYPE).createSpec());
        return new DataTableSpec[] { outputSpec };
    }

    @Override
    protected DataTableSpec[] configure(DataTableSpec[] inSpecs) throws InvalidSettingsException {
        FragmentsBySimilarityConfig config = getConfig();
        int idColumnIndex = inSpecs[0].findColumnIndex(config.getFragmentIdColumn().getStringValue());
        if (idColumnIndex < 0) {
            throw new InvalidSettingsException("No valid fragment identifier column selected, require a String column");
        }

        String matrix = config.getMatrix().getStringValue();
        if (matrix == "" || matrix == null) {
            config.getMatrix().setStringValue(FragmentsBySimilarityConfig.DEFAULT_MATRIX);
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
