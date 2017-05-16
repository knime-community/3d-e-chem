package nl.esciencecenter.e3dchem.kripodb.ws.pharmacophores;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import nl.esciencecenter.e3dchem.kripodb.ws.WsNodeConfig;

public class PharmacophoresConfig extends WsNodeConfig {
	private static final String CFG_IDCOLNAME = "id_column";
	
	private final SettingsModelString idColumn = new SettingsModelString(CFG_IDCOLNAME, null);

	@Override
	public void saveSettingsTo(NodeSettingsWO settings) {
		super.saveSettingsTo(settings);
		idColumn.saveSettingsTo(settings);
	}

	@Override
	public void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		super.loadValidatedSettingsFrom(settings);
		idColumn.loadSettingsFrom(settings);
	}

	@Override
	public void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		super.validateSettings(settings);
		idColumn.validateSettings(settings);
	}

	public SettingsModelString getIdColumn() {
		return idColumn;
	}

}
