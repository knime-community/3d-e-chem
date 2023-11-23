package nl.esciencecenter.e3dchem.sygma;

import java.util.Set;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.workflow.FlowVariable;

public class PredictMetabolitesConfig {
	private SettingsModelString sygmaPath = new SettingsModelString("sygmaPath", "sygma");
	private SettingsModelString parentsColumnName = new SettingsModelString("parents_column_name", null);
	private SettingsModelIntegerBounded phase1cycles = new SettingsModelIntegerBounded("phase1_cycles",
			2, 0, 100);
	private SettingsModelIntegerBounded phase2cycles = new SettingsModelIntegerBounded("phase2_cycles",
			1, 0, 100);

	public void saveSettingsTo(final NodeSettingsWO settings) {
		phase1cycles.saveSettingsTo(settings);
		phase2cycles.saveSettingsTo(settings);
		parentsColumnName.saveSettingsTo(settings);
		sygmaPath.saveSettingsTo(settings);
	}

	public void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		phase1cycles.loadSettingsFrom(settings);
		phase2cycles.loadSettingsFrom(settings);
		parentsColumnName.loadSettingsFrom(settings);
		sygmaPath.loadSettingsFrom(settings);
	}

	public void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		phase1cycles.validateSettings(settings);
		phase2cycles.validateSettings(settings);
		parentsColumnName.validateSettings(settings);
		sygmaPath.validateSettings(settings);
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

	public SettingsModelString getSygmaPath() {
		return sygmaPath;
	}
}
