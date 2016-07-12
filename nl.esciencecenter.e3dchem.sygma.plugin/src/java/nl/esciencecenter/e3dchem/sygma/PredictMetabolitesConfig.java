package nl.esciencecenter.e3dchem.sygma;

import java.util.Set;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.workflow.FlowVariable;

import nl.esciencecenter.e3dchem.python.PythonWrapperNodeConfig;

public class PredictMetabolitesConfig extends PythonWrapperNodeConfig {
    private static final String CFGKEY_PHASE1CYCLES = "phase1_cycles";
    private static final int DEFAULT_PHASE1CYCLES = 2;
    private static final String CFGKEY_PHASE2CYCLES = "phase2_cycles";
    private static final int DEFAULT_PHASE2CYCLES = 1;
    private static final String CFG_PARENTSCOLNAME = "parents_column_name";

    private final SettingsModelString parentsColumnName = new SettingsModelString(CFG_PARENTSCOLNAME, null);

    private final SettingsModelIntegerBounded phase1cycles = new SettingsModelIntegerBounded(CFGKEY_PHASE1CYCLES,
            DEFAULT_PHASE1CYCLES, 0, 100);

    private final SettingsModelIntegerBounded phase2cycles = new SettingsModelIntegerBounded(CFGKEY_PHASE2CYCLES,
            DEFAULT_PHASE2CYCLES, 0, 100);

    @Override
    public void saveTo(final NodeSettingsWO settings) {
        phase1cycles.saveSettingsTo(settings);
        phase2cycles.saveSettingsTo(settings);
        parentsColumnName.saveSettingsTo(settings);
    }

    @Override
    public void loadFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        phase1cycles.loadSettingsFrom(settings);
        phase2cycles.loadSettingsFrom(settings);
        parentsColumnName.loadSettingsFrom(settings);
    }

    @Override
    public Set<FlowVariable> getOptionsValues() {
        Set<FlowVariable> variables = super.getOptionsValues();
        variables.add(new FlowVariable(CFGKEY_PHASE1CYCLES, phase1cycles.getIntValue()));
        variables.add(new FlowVariable(CFGKEY_PHASE2CYCLES, phase2cycles.getIntValue()));
        variables.add(new FlowVariable(CFG_PARENTSCOLNAME, parentsColumnName.getStringValue()));
        return variables;
    }

    public SettingsModelIntegerBounded getPhase1cycles() {
        return phase1cycles;
    }

    public SettingsModelIntegerBounded getPhase2cycles() {
        return phase2cycles;
    }

    public SettingsModelString getParentsColumnName() {
        return parentsColumnName;
    }
}
