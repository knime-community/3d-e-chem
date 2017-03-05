package nl.esciencecenter.e3dchem.kripodb.ws.fragments;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import nl.esciencecenter.e3dchem.kripodb.ws.WsNodeConfig;

public class FragmentsByIdConfig extends WsNodeConfig {
	private static final String DEFAULT_IDTYPE = "fragment";
	private static final String CFG_IDCOLNAME = "id_column";
	public static final Set<String> LIST_IDENTIFIERTYPES = Stream.of(DEFAULT_IDTYPE, "pdb").collect(Collectors.toSet());
	private static final String CFG_IDTYPE = "id_type";
	public static final String CFGKEY_CHUNKSIZE = "Chunk Size";
	
	private final SettingsModelString m_idColumn = new SettingsModelString(CFG_IDCOLNAME, null);
	private final SettingsModelString m_idType = new SettingsModelString(CFG_IDTYPE, DEFAULT_IDTYPE);
	private final SettingsModelInteger m_chunksize = new SettingsModelIntegerBounded(CFGKEY_CHUNKSIZE, 20, 1, 100);

	@Override
	public void saveSettingsTo(final NodeSettingsWO settings) {
		super.saveSettingsTo(settings);
		m_idColumn.saveSettingsTo(settings);
		m_idType.saveSettingsTo(settings);
		m_chunksize.saveSettingsTo(settings);
	}

	@Override
	public void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		super.validateSettings(settings);
		m_idColumn.validateSettings(settings);
		m_idType.validateSettings(settings);
		m_chunksize.validateSettings(settings);
	}

	@Override
	public void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		super.loadValidatedSettingsFrom(settings);
		m_idColumn.loadSettingsFrom(settings);
		m_idType.loadSettingsFrom(settings);
		m_chunksize.loadSettingsFrom(settings);
	}

	public SettingsModelString getIdColumn() {
		return m_idColumn;
	}

	public SettingsModelString getIdType() {
		return m_idType;
	}

	public SettingsModelInteger getChunkSize() {
		return m_chunksize;
	}
}
