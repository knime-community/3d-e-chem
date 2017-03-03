package nl.esciencecenter.e3dchem.kripodb.ws.fragmentsbysimilarity;

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
    private SettingsModelIntegerBounded m_limit = new SettingsModelIntegerBounded(CFG_LIMIT, DEFAULT_LIMIT, 0, Integer.MAX_VALUE);
    private SettingsModelString m_fragmentIdColumn = new SettingsModelString(CFG_FRAGIDCOLNAME, null);
    private SettingsModelDoubleBounded m_cutoff = new SettingsModelDoubleBounded(CFG_CUTOFF, DEFAULT_CUTOFF, 0.45, 1.0);

    @Override
	public void saveSettingsTo(NodeSettingsWO settings) {
		super.saveSettingsTo(settings);
        m_fragmentIdColumn.saveSettingsTo(settings);
        m_cutoff.saveSettingsTo(settings);
        m_limit.saveSettingsTo(settings);
	}

	@Override
	public void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		super.loadValidatedSettingsFrom(settings);
        m_fragmentIdColumn.loadSettingsFrom(settings);
        m_cutoff.loadSettingsFrom(settings);
        m_limit.loadSettingsFrom(settings);
	}

	@Override
	public void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		super.validateSettings(settings);
        m_fragmentIdColumn.validateSettings(settings);
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
}
