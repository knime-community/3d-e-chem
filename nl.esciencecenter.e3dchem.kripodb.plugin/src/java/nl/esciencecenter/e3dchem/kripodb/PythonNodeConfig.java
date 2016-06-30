package nl.esciencecenter.e3dchem.kripodb;

import java.util.HashSet;
import java.util.Set;

import org.knime.code.generic.VariableNames;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.workflow.FlowVariable;

public abstract class PythonNodeConfig {
	private static final String PYTHON_OPTIONS = "options";
	private static final VariableNames VARIABLE_NAMES = new VariableNames("flow_variables",
			new String[] { "input_table" }, new String[] { "output_table" }, null, null, null);

	static VariableNames getVariableNames() {
		return VARIABLE_NAMES;
	}

	static String getOptionsName() {
		return PYTHON_OPTIONS;
	}

	/**
	 * Save configuration to the given node settings.
	 * 
	 * @param settings
	 *            The settings to save to
	 */
	public abstract void saveTo(final NodeSettingsWO settings);

	/**
	 * Load configuration from the given node settings.
	 * 
	 * @param settings
	 *            The settings to load from
	 * @throws InvalidSettingsException
	 *             If the settings are invalid
	 */
	public abstract void loadFrom(final NodeSettingsRO settings) throws InvalidSettingsException;

	public Set<FlowVariable> toFlowVariables() {
		Set<FlowVariable> variables = new HashSet<FlowVariable>();
		return variables;
	}
}
