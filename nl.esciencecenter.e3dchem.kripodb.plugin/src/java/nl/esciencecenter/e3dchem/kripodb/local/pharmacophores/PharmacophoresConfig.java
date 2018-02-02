package nl.esciencecenter.e3dchem.kripodb.local.pharmacophores;

import java.io.File;
import java.util.Set;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.workflow.FlowVariable;

import nl.esciencecenter.e3dchem.kripodb.KripoPythonConfig;

public class PharmacophoresConfig extends KripoPythonConfig {
	private static final String CFG_IDCOLNAME = "id_column";
	private static final String CFG_PHARMACOPHORESDB = "pharmacophoresdb";

	private final SettingsModelString idColumn = new SettingsModelString(CFG_IDCOLNAME, null);
	private final SettingsModelString pharmacophoresDB = new SettingsModelString(CFG_PHARMACOPHORESDB, null);

	@Override
	public void loadFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		idColumn.loadSettingsFrom(settings);
		pharmacophoresDB.loadSettingsFrom(settings);
		String pharmacophoresDB_fn = pharmacophoresDB.getStringValue();
		if (pharmacophoresDB_fn == null || pharmacophoresDB_fn.isEmpty()) {
			throw new InvalidSettingsException("Pharmacophores database file cannot be empty");
		} else {
			File pharmacophoresDB_file = new File(pharmacophoresDB_fn);
			if (!pharmacophoresDB_file.canRead()) {
				throw new InvalidSettingsException("Unable to read pharmacophores database file");
			}
		}
	}

	@Override
	public void saveTo(NodeSettingsWO settings) {
		idColumn.saveSettingsTo(settings);
		pharmacophoresDB.saveSettingsTo(settings);
	}

	@Override
	public Set<FlowVariable> getOptionsValues() {
		Set<FlowVariable> variables = super.getOptionsValues();
		variables.add(new FlowVariable(CFG_IDCOLNAME, idColumn.getStringValue()));
		variables.add(new FlowVariable(CFG_PHARMACOPHORESDB, pharmacophoresDB.getStringValue()));
		return variables;
	}

	public SettingsModelString getIdColumn() {
		return idColumn;
	}

	public SettingsModelString getPharmacophoresDB() {
		return pharmacophoresDB;
	}

}
