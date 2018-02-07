package nl.esciencecenter.e3dchem.kripodb.local.pharmacophores;

import org.knime.core.data.DataTableSpec;
import org.knime.core.data.StringValue;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObjectSpec;

import nl.esciencecenter.e3dchem.kripodb.ws.WsNodeDialog;
import nl.esciencecenter.e3dchem.python.PythonOptionsPanel;

public class PharmacophoresDialog extends WsNodeDialog {
	private PythonOptionsPanel<PharmacophoresConfig> pythonOptions;

	@SuppressWarnings("unchecked")
	public PharmacophoresDialog() {
		super();
		PharmacophoresConfig config = new PharmacophoresConfig();

		SettingsModelString idColumn = config.getIdColumn();
		addDialogComponent(
				new DialogComponentColumnNameSelection(idColumn, "Kripo fragment identifiers", 0, StringValue.class));

		SettingsModelString pharsdb = config.getPharmacophoresDB();
		String historyID = "kripodb-pharsdb";
		addDialogComponent(new DialogComponentFileChooser(pharsdb, historyID, ".h5"));

		pythonOptions = new PythonOptionsPanel<PharmacophoresConfig>();
		addTab("Python options", pythonOptions);
	}

	@Override
	public void loadAdditionalSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs)
			throws NotConfigurableException {
		super.loadAdditionalSettingsFrom(settings, specs);
		PharmacophoresConfig config = new PharmacophoresConfig();
		config.loadFromInDialog(settings);
		pythonOptions.loadSettingsFrom(config);
	}

	@Override
	public void loadAdditionalSettingsFrom(NodeSettingsRO settings, DataTableSpec[] specs)
			throws NotConfigurableException {
		super.loadAdditionalSettingsFrom(settings, specs);
		PharmacophoresConfig config = new PharmacophoresConfig();
		config.loadFromInDialog(settings);
		pythonOptions.loadSettingsFrom(config);
	}

	@Override
	public void saveAdditionalSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
		super.saveAdditionalSettingsTo(settings);
		PharmacophoresConfig config = new PharmacophoresConfig();
		pythonOptions.saveSettingsTo(config);
		config.saveToInDialog(settings);
	}
}
