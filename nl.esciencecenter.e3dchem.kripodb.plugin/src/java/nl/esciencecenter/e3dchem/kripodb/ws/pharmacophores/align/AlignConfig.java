package nl.esciencecenter.e3dchem.kripodb.ws.pharmacophores.align;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import nl.esciencecenter.e3dchem.kripodb.ws.WsNodeConfig;

public class AlignConfig extends WsNodeConfig {
	static final String CFGKEY_QUERY = "queryColumn";
	private final SettingsModelString queryColumn = new SettingsModelString(CFGKEY_QUERY, "");

	static final String CFGKEY_REFERENCE = "referenceColumn";
	private final SettingsModelString referenceColumn = new SettingsModelString(CFGKEY_REFERENCE, "");

	@Override
	public void saveSettingsTo(NodeSettingsWO settings) {
		super.saveSettingsTo(settings);
		queryColumn.saveSettingsTo(settings);
		referenceColumn.saveSettingsTo(settings);
	}

	@Override
	public void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		super.loadValidatedSettingsFrom(settings);
		queryColumn.loadSettingsFrom(settings);
		referenceColumn.loadSettingsFrom(settings);
	}

	@Override
	public void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		super.validateSettings(settings);
		queryColumn.validateSettings(settings);
		referenceColumn.validateSettings(settings);
	}

	public SettingsModelString getQueryColumn() {
		return queryColumn;
	}

	public SettingsModelString getReferenceColumn() {
		return referenceColumn;
	}
	
}
