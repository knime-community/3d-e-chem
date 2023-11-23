package nl.esciencecenter.e3dchem.sygma;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.knime.chem.types.SmilesCell;
import org.knime.chem.types.SmilesCellFactory;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.container.ColumnRearranger;
import org.knime.core.data.container.SingleCellFactory;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.port.PortType;

/**
 * This is the model implementation of PredictMetabolites.
 *
 */
public class PredictMetabolitesModel extends NodeModel {
	private PredictMetabolitesConfig config = new PredictMetabolitesConfig();

	public PredictMetabolitesModel() {
		super(1, 1);
	}

	private DataTableSpec createOutputSpec() {
        return new DataTableSpec(new DataColumnSpecCreator("metabolite", StringCell.TYPE).createSpec());
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(DataTableSpec[] inSpecs) throws InvalidSettingsException {
        DataTableSpec appendedSpec = createOutputSpec();
        DataTableSpec outputSpec = new DataTableSpec(inSpecs[0], appendedSpec);
        return new DataTableSpec[] { outputSpec };
	}

	public List<String> processParent(String parent) {
		String cliPath = config.getSygmaPath().getStringValue();

		ProcessBuilder processBuilder = new ProcessBuilder(cliPath,
			cliPath,
			"--phase1_cycles", Integer.toString(config.getPhase1cycles().getIntValue()),
			"--phase2_cycles", Integer.toString(config.getPhase2cycles().getIntValue()),
			parent
		);
		Process process = processBuilder.start();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		StringBuilder output = new StringBuilder();
		int read;
		char[] buffer = new char[4096];
		while ((read = reader.read(buffer)) != -1) {
			output.append(buffer, 0, read);
		}
		List<String> records = Arrays.asList(output.toString().split("\\$\\$\\$\\$"));
		return records;
	}

	@Override
	public BufferedDataTable[] execute(BufferedDataTable[] inData, ExecutionContext exec) throws Exception {
		BufferedDataTable inTable = inData[0];
		DataTableSpec inSpec = inTable.getSpec();
        int parentIndex = inSpec.findColumnIndex(config.getParentsColumnName());
		BufferedDataContainer container = exec.createDataContainer(createOutputSpec());
		long rowCount = inTable.size();
		long currentRow = 0;
		for (DataRow inRow : inTable) {
			StringCell parentCell = ((StringCell) inRow.getCell(parentIndex));
			if (parentCell.isMissing()) {
				continue;
			}
			String parent = parentCell.getStringValue();
			List<String> records = processParent(parent);
			for (String record : records) {
				// TODO from mol2 block parse id, pathway and score
				container.addRowToTable(new DefaultRow(parent, record));
			}
			exec.checkCanceled();
			exec.setProgress(currentRow / (double) rowCount, "Processing row " + currentRow);
			currentRow++;
		}
		container.close();
		BufferedDataTable outTable = container.getTable();
		BufferedDataTable out = exec.createJoinedTable(inTable, outTable, exec.createSubProgress(0.1));
		return new BufferedDataTable[] { out };
	}

	@Override
    protected void loadInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {
        // No internals to load
    }

    @Override
    protected void saveInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {
        // No internals to save
    }

    @Override
    protected void saveSettingsTo(NodeSettingsWO settings) {
        config.saveSettingsTo(settings);
    }

    @Override
    protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
        config.validateSettings(settings);
    }

    @Override
    protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
        config.loadValidatedSettingsFrom(settings);
    }

    @Override
    protected void reset() {
        // No internals to reset
    }
}
