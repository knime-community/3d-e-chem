package nl.esciencecenter.e3dchem.kripodb.fragmentsbysimilarity;

import java.io.File;
import java.util.Set;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.workflow.FlowVariable;

import nl.esciencecenter.e3dchem.kripodb.KripoPythonConfig;

public class FragmentsBySimilarityConfig extends KripoPythonConfig {
    private static final String CFG_LIMIT = "limit";
    private static final int DEFAULT_LIMIT = 1000;
    private static final String CFG_FRAGIDCOLNAME = "fragment_id_column";
    private static final double DEFAULT_CUTOFF = 0.55;
    private static final String CFG_CUTOFF = "cutoff";
    private static final String CFG_MATRIX = "matrix";
    public static final String DEFAULT_MATRIX = "";
    private SettingsModelIntegerBounded m_limit = new SettingsModelIntegerBounded(CFG_LIMIT, DEFAULT_LIMIT, 0, Integer.MAX_VALUE);
    private SettingsModelString m_fragmentIdColumn = new SettingsModelString(CFG_FRAGIDCOLNAME, null);
    private SettingsModelDoubleBounded m_cutoff = new SettingsModelDoubleBounded(CFG_CUTOFF, DEFAULT_CUTOFF, 0.45, 1.0);
    private SettingsModelString m_matrix = new SettingsModelString(CFG_MATRIX, DEFAULT_MATRIX);

    @Override
    public void saveTo(final NodeSettingsWO settings) {
        m_fragmentIdColumn.saveSettingsTo(settings);
        m_matrix.saveSettingsTo(settings);
        m_cutoff.saveSettingsTo(settings);
        m_limit.saveSettingsTo(settings);
    }

    @Override
    public void loadFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        m_fragmentIdColumn.loadSettingsFrom(settings);
        m_matrix.loadSettingsFrom(settings);
        String matrix = m_matrix.getStringValue();
        if ("".equals(matrix) || matrix == null) {
            throw new InvalidSettingsException("Matrix file can not be empty");
        } else {
            File fragmentsdb = new File(matrix);
            if (!fragmentsdb.canRead()) {
                throw new InvalidSettingsException("Unable to read matrix file");
            }
        }
        m_cutoff.loadSettingsFrom(settings);
        m_limit.loadSettingsFrom(settings);
    }

    @Override
    public Set<FlowVariable> getOptionsValues() {
        Set<FlowVariable> variables = super.getOptionsValues();
        variables.add(new FlowVariable(CFG_FRAGIDCOLNAME, m_fragmentIdColumn.getStringValue()));
        variables.add(new FlowVariable(CFG_MATRIX, m_matrix.getStringValue()));
        variables.add(new FlowVariable(CFG_CUTOFF, m_cutoff.getDoubleValue()));
        variables.add(new FlowVariable(CFG_LIMIT, m_limit.getIntValue()));
        return variables;
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
