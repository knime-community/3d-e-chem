package nl.esciencecenter.e3dchem.kripodb.local.pharmacophores;

import java.io.File;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.StringValue;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.port.PortType;

import nl.esciencecenter.e3dchem.kripodb.KripoNodeModel;

public class PharmacophoresModel extends KripoNodeModel<PharmacophoresConfig> {

	public PharmacophoresModel() {
		super(new PortType[] { BufferedDataTable.TYPE }, new PortType[] { BufferedDataTable.TYPE });
		python_code_filename = "pharmacophores.py";
	}

	@Override
	protected DataTableSpec[] configure(DataTableSpec[] inSpecs) throws InvalidSettingsException {
		PharmacophoresConfig config = getConfig();

		String idColumn = config.getIdColumn().getStringValue();
		int idColumnIndex = inSpecs[0].findColumnIndex(idColumn);
		if (idColumnIndex == -1) {
			int i = 0;
			for (DataColumnSpec spec : inSpecs[0]) {
				if (spec.getType().isCompatible(StringValue.class)) {
					setWarningMessage("Column '" + spec.getName() + "' automatically chosen as identifier column");
					config.getIdColumn().setStringValue(spec.getName());
					idColumnIndex = i;
					break;
				}
				i++;
			}
			if (idColumnIndex == -1) {
				throw new InvalidSettingsException(
						"No valid fragment identifier column available, require a String column");
			}
		}
		if (!inSpecs[0].getColumnSpec(idColumnIndex).getType().isCompatible(StringValue.class)) {
			throw new InvalidSettingsException("Column '" + idColumn + "' does not contain String cells");
		}

		String pharmacophoresDB_fn = config.getPharmacophoresDB().getStringValue();
		if (pharmacophoresDB_fn == null || pharmacophoresDB_fn.isEmpty()) {
			throw new InvalidSettingsException("Pharmacophores database file cannot be empty");
		} else {
			File pharmacophoresDB_file = new File(pharmacophoresDB_fn);
			if (!pharmacophoresDB_file.canRead()) {
				throw new InvalidSettingsException("Unable to read pharmacophores database file");
			}
		}

		return super.configure(inSpecs);
	}

	@Override
	protected PharmacophoresConfig createConfig() {
		return new PharmacophoresConfig();
	}
}
