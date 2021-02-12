package nl.esciencecenter.e3dchem.sygma;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.port.PortObjectSpec;
import org.rdkit.knime.types.RDKitMolValue;

import nl.esciencecenter.e3dchem.python.PythonOptionsPanel;

/**
 * <code>NodeDialog</code> for the "PredictMetabolites" Node.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 */
public class PredictMetabolitesDialog extends DefaultNodeSettingsPane {

	private PythonOptionsPanel<PredictMetabolitesConfig> pythonOptions;
	private PredictMetabolitesConfig config;

	/**
	 * New pane for configuring PredictMetabolites node dialog. This is just a
	 * suggestion to demonstrate possible default dialog components.
	 */
	@SuppressWarnings("unchecked")
	protected PredictMetabolitesDialog() {
		super();
		config = new PredictMetabolitesConfig();

		addDialogComponent(new DialogComponentColumnNameSelection(config.getParentsColumnName(),
				"Parents molecule column", 0, RDKitMolValue.class));

		addDialogComponent(new DialogComponentNumber(config.getPhase1cycles(), "Phase 1 cycles:", 1, 5));
		addDialogComponent(new DialogComponentNumber(config.getPhase2cycles(), "Phase 2 cycles:", 1, 5));

		pythonOptions = new PythonOptionsPanel<PredictMetabolitesConfig>();
		addTab("Python options", pythonOptions);
	}

	@Override
	public void loadAdditionalSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs)
			throws NotConfigurableException {
		super.loadAdditionalSettingsFrom(settings, specs);
		try {
			config.loadFromInDialog(settings);
		} catch (InvalidSettingsException e) {
			// swallow error
		}
		pythonOptions.loadSettingsFrom(config);
	}

	@Override
	public void loadAdditionalSettingsFrom(NodeSettingsRO settings, DataTableSpec[] specs)
			throws NotConfigurableException {
		super.loadAdditionalSettingsFrom(settings, specs);
		try {
			config.loadFromInDialog(settings);
		} catch (InvalidSettingsException e) {
			// swallow error
		}
		pythonOptions.loadSettingsFrom(config);
	}

	@Override
	public void saveAdditionalSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
		super.saveAdditionalSettingsTo(settings);
		pythonOptions.saveSettingsTo(config);
		config.saveToInDialog(settings);
	}
}
