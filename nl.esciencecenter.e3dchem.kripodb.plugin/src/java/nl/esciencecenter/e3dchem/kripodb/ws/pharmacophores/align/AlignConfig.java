package nl.esciencecenter.e3dchem.kripodb.ws.pharmacophores.align;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelDouble;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import nl.esciencecenter.e3dchem.kripodb.ws.WsNodeConfig;

public class AlignConfig extends WsNodeConfig {
	static final String CFGKEY_QUERY = "queryColumn";
	private final SettingsModelString queryColumn = new SettingsModelString(CFGKEY_QUERY, "");

	static final String CFGKEY_REFERENCE = "referenceColumn";
	private final SettingsModelString referenceColumn = new SettingsModelString(CFGKEY_REFERENCE, "");

	static final String CFGKEY_CUTOFF = "cutoff";
	private final SettingsModelDouble cutoff = new SettingsModelDoubleBounded(CFGKEY_CUTOFF, 1.0, 0.0, 1000.0);

	static final String CFGKEY_BREAKNUMCLIQUES = "breakNumCliques";
	private final SettingsModelInteger breakNumCliques = new SettingsModelIntegerBounded(CFGKEY_BREAKNUMCLIQUES, 3000, 0, 5000);

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

	public SettingsModelString getProbeIdColumn() {
		return queryColumn;
	}

	public SettingsModelString getReferenceIdColumn() {
		return referenceColumn;
	}

	public SettingsModelDouble getCutoff() {
		return cutoff;
	}

	public SettingsModelInteger getBreakNumCliques() {
		return breakNumCliques;
	}
}
