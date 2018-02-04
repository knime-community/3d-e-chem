package nl.esciencecenter.e3dchem.kripodb.fragments;

import java.util.Set;

import org.knime.core.data.DataTableSpec;
import org.knime.core.data.StringValue;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObjectSpec;

import nl.esciencecenter.e3dchem.python.PythonOptionsPanel;

/**
 * <code>NodeDialog</code> for the "FragmentBySimilarity" Node.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 */
public class FragmentByIdDialog extends DefaultNodeSettingsPane {
	private PythonOptionsPanel<FragmentsByIdConfig> pythonOptions;

	/**
	 * New pane for configuring FragmentBySimilarity node dialog. This is just a
	 * suggestion to demonstrate possible default dialog components.
	 */
	@SuppressWarnings("unchecked")
	protected FragmentByIdDialog() {
		super();
		FragmentsByIdConfig config = new FragmentsByIdConfig();

		SettingsModelString idColumn = config.getIdColumn();
		addDialogComponent(new DialogComponentColumnNameSelection(idColumn, "Identifiers", 0, StringValue.class));

		SettingsModelString fragmentsdb = config.getFragmentsDB();
		String historyID = "kripodb-fragmentsdb";
		// TODO DialogComponentFileChooser uses a border with 'Selected File' as
		// hardcoded title, replace with 'Matrix'.
		addDialogComponent(new DialogComponentFileChooser(fragmentsdb, historyID, ".sqlite"));

		SettingsModelString idType = config.getIdType();
		Set<String> idTypeChoices = FragmentsByIdConfig.LIST_IDENTIFIERTYPES;
		addDialogComponent(new DialogComponentStringSelection(idType, "Type of identifier", idTypeChoices));

		pythonOptions = new PythonOptionsPanel<FragmentsByIdConfig>();
		addTab("Python options", pythonOptions);
	}

	@Override
	public void loadAdditionalSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs)
			throws NotConfigurableException {
		super.loadAdditionalSettingsFrom(settings, specs);
		FragmentsByIdConfig config = new FragmentsByIdConfig();
		try {
			config.loadFrom(settings);
			pythonOptions.loadSettingsFrom(config);
		} catch (InvalidSettingsException e) {
			// fall back to config defaults
		}
	}

	@Override
	public void loadAdditionalSettingsFrom(NodeSettingsRO settings, DataTableSpec[] specs)
			throws NotConfigurableException {
		super.loadAdditionalSettingsFrom(settings, specs);
		FragmentsByIdConfig config = new FragmentsByIdConfig();
		try {
			config.loadFrom(settings);
			pythonOptions.loadSettingsFrom(config);
		} catch (InvalidSettingsException e) {
			// fall back to config defaults
		}
	}

	@Override
	public void saveAdditionalSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
		super.saveAdditionalSettingsTo(settings);
		FragmentsByIdConfig config = new FragmentsByIdConfig();
		pythonOptions.saveSettingsTo(config);
		config.saveTo(settings);
	}
}
