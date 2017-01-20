package nl.esciencecenter.e3dchem.kripodb.ws.fragmentsbysimilarity;

import java.io.File;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import nl.esciencecenter.e3dchem.kripodb.ws.WsNodeConfig;

public class FragmentsBySimilarityConfig extends WsNodeConfig {
    private static final String CFG_LIMIT = "limit";
    private static final int DEFAULT_LIMIT = 1000;
    private static final String CFG_FRAGIDCOLNAME = "fragment_id_column";
    private static final double DEFAULT_CUTOFF = 0.55;
    private static final String CFG_CUTOFF = "cutoff";
    private static final String CFG_MATRIX = "matrix";
    public static final String DEFAULT_MATRIX = "http://3d-e-chem.vu-compmedchem.nl/kripodb";
    private SettingsModelIntegerBounded m_limit = new SettingsModelIntegerBounded(CFG_LIMIT, DEFAULT_LIMIT, 0, Integer.MAX_VALUE);
    private SettingsModelString m_fragmentIdColumn = new SettingsModelString(CFG_FRAGIDCOLNAME, null);
    private SettingsModelDoubleBounded m_cutoff = new SettingsModelDoubleBounded(CFG_CUTOFF, DEFAULT_CUTOFF, 0.45, 1.0);
    private SettingsModelString m_matrix = new SettingsModelString(CFG_MATRIX, DEFAULT_MATRIX);

    @Override
	public void saveSettingsTo(NodeSettingsWO settings) {
		super.saveSettingsTo(settings);
        m_fragmentIdColumn.saveSettingsTo(settings);
        m_matrix.saveSettingsTo(settings);
        m_cutoff.saveSettingsTo(settings);
        m_limit.saveSettingsTo(settings);
	}

	@Override
	public void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		super.loadValidatedSettingsFrom(settings);
        m_fragmentIdColumn.loadSettingsFrom(settings);
        m_matrix.loadSettingsFrom(settings);
        String matrix = m_matrix.getStringValue();
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
        m_cutoff.loadSettingsFrom(settings);
        m_limit.loadSettingsFrom(settings);
	}

	@Override
	public void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		super.validateSettings(settings);
        m_fragmentIdColumn.validateSettings(settings);
        m_matrix.validateSettings(settings);
        m_cutoff.validateSettings(settings);
        m_limit.validateSettings(settings);
	}

	public void saveTo(final NodeSettingsWO settings) {
    }

    public void loadFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
    }


    public SettingsModelIntegerBounded getLimit() {
        return m_limit;
    }

    public SettingsModelString getFragmentIdColumn() {
        return m_fragmentIdColumn;
    }

    public SettingsModelDoubleBounded getCutoff() {
        return m_cutoff;
    }

    public SettingsModelString getMatrix() {
        return m_matrix;
    }
}
