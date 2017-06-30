package nl.esciencecenter.e3dchem.kripodb.ws.pharmacophores;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.StringValue;
import org.knime.core.data.container.CellFactory;
import org.knime.core.data.container.ColumnRearranger;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;

import nl.esciencecenter.e3dchem.knime.pharmacophore.PharCell;
import nl.esciencecenter.e3dchem.kripodb.ws.WsNodeModel;

public class PharmacophoresModel extends WsNodeModel<PharmacophoresConfig> {

	public PharmacophoresModel() {
		super(1, 1);
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

		DataColumnSpec newColumnSpec = createOutputColumnSpec();
		// and the DataTableSpec for the appended part
		DataTableSpec appendedSpec = new DataTableSpec(newColumnSpec);
		DataTableSpec outputSpec = new DataTableSpec(inSpecs[0], appendedSpec);
		return new DataTableSpec[] { outputSpec };
	}

	@Override
	public PharmacophoresConfig createConfig() {
		return new PharmacophoresConfig();
	}

	@Override
	protected BufferedDataTable[] execute(BufferedDataTable[] inData, ExecutionContext exec) throws Exception {
		int colIndex = inData[0].getDataTableSpec().findColumnIndex(getConfig().getIdColumn().getStringValue());
		ColumnRearranger outputTable = new ColumnRearranger(inData[0].getDataTableSpec());
		// append the new column
		CellFactory cellFactory = new PharmacophoreCellFactory(createOutputColumnSpec(),
				getConfig().getPharmacophoresApi(), colIndex);
		outputTable.append(cellFactory);
		// and create the actual output table
		BufferedDataTable bufferedOutput = exec.createColumnRearrangeTable(inData[0], outputTable, exec);
		// return it
		return new BufferedDataTable[] { bufferedOutput };
	}

	private DataColumnSpec createOutputColumnSpec() {
		DataColumnSpecCreator colSpecCreator = new DataColumnSpecCreator("Pharmacophore", PharCell.TYPE);
		return colSpecCreator.createSpec();
	}
}
