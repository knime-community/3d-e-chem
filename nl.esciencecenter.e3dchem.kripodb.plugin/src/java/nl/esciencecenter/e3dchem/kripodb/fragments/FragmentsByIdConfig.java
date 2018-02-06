package nl.esciencecenter.e3dchem.kripodb.fragments;

import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.workflow.FlowVariable;

import nl.esciencecenter.e3dchem.kripodb.KripoPythonConfig;

public class FragmentsByIdConfig extends KripoPythonConfig {
	private static final String DEFAULT_IDTYPE = "fragment";
	private static final String CFG_IDCOLNAME = "id_column";
	public static final Set<String> LIST_IDENTIFIERTYPES = Stream.of(DEFAULT_IDTYPE, "pdb").collect(Collectors.toSet());
	private static final String CFG_IDTYPE = "id_type";
	private static final String CFG_FRAGMENTSDB = "fragmentsdb";
	private static final String DEFAULT_FRAGMENTSDB = "http://3d-e-chem.vu-compmedchem.nl/kripodb";

	private SettingsModelString m_idColumn = new SettingsModelString(CFG_IDCOLNAME, null);
	private SettingsModelString m_fragmentsDB = new SettingsModelString(CFG_FRAGMENTSDB, DEFAULT_FRAGMENTSDB);
	private SettingsModelString m_idType = new SettingsModelString(CFG_IDTYPE, DEFAULT_IDTYPE);

	@Override
	public void saveTo(final NodeSettingsWO settings) {
		super.saveTo(settings);
		m_idColumn.saveSettingsTo(settings);
		m_fragmentsDB.saveSettingsTo(settings);
		m_idType.saveSettingsTo(settings);
	}

	@Override
	public void loadFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		super.loadFrom(settings);
		m_idColumn.loadSettingsFrom(settings);
		m_fragmentsDB.loadSettingsFrom(settings);
		if (m_fragmentsDB.getStringValue() == null || m_fragmentsDB.getStringValue().isEmpty()) {
			throw new InvalidSettingsException("Fragments database file or ws url cannot be empty");
		} else {
			String fragmentsDB = m_fragmentsDB.getStringValue();
			if (fragmentsDB.startsWith("http")) {
				// TODO test if webservice is online, using a HEAD request.
			} else {
				File fragmentsdb_file = new File(fragmentsDB);
				if (!fragmentsdb_file.canRead()) {
					throw new InvalidSettingsException("Unable to read fragments database file");
				}
			}
		}
		m_idType.loadSettingsFrom(settings);
	}

	@Override
	public Set<FlowVariable> getOptionsValues() {
		Set<FlowVariable> variables = super.getOptionsValues();
		variables.add(new FlowVariable(CFG_IDCOLNAME, m_idColumn.getStringValue()));
		variables.add(new FlowVariable(CFG_FRAGMENTSDB, m_fragmentsDB.getStringValue()));
		variables.add(new FlowVariable(CFG_IDTYPE, m_idType.getStringValue()));
		return variables;
	}

	public SettingsModelString getIdColumn() {
		return m_idColumn;
	}

	public SettingsModelString getFragmentsDB() {
		return m_fragmentsDB;
	}

	public SettingsModelString getIdType() {
		return m_idType;
	}
}
