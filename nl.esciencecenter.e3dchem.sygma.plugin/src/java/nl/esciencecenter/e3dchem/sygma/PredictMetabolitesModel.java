package nl.esciencecenter.e3dchem.sygma;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

/**
 * This is the model implementation of PredictMetabolites.
 *
 */
public class PredictMetabolitesModel extends NodeModel {
	private PredictMetabolitesConfig config = new PredictMetabolitesConfig();

    private static final NodeLogger logger = NodeLogger.getLogger(PredictMetabolitesModel.class);

	public PredictMetabolitesModel() {
		super(1, 1);
	}

	private DataTableSpec createOutputSpec() {
		return new DataTableSpec(
				new DataColumnSpecCreator("parent", StringCell.TYPE).createSpec(),
				new DataColumnSpecCreator("metabolite", StringCell.TYPE).createSpec()
		);
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(DataTableSpec[] inSpecs) throws InvalidSettingsException {
        DataTableSpec appendedSpec = createOutputSpec();
        return new DataTableSpec[] { appendedSpec };
	}

	public List<String> processParent(String parent) throws Exception {
		String cliPath = config.getSygmaPath().getStringValue();
		String phase1cycles = Integer.toString(config.getPhase1cycles().getIntValue());
		String phase2cycles = Integer.toString(config.getPhase2cycles().getIntValue());

		List<String> commands = Arrays.asList(
			cliPath,
			"--phase1",
			phase1cycles,
			"--phase2",
			phase2cycles,
			parent
		);
		ProcessBuilder processBuilder = new ProcessBuilder(commands);
		Process process = processBuilder.start();
		StreamCollector stdout = new StreamCollector(process.getInputStream());
        Thread stdoutT = new Thread(stdout);
        stdoutT.start();
        StreamCollector stderr = new StreamCollector(process.getErrorStream());
        Thread stderrT = new Thread(stderr);
		stderrT.start();
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            process.destroy();
            throw e;
        }
		int exitCode = process.exitValue();
		
        if (exitCode != 0) {
            setWarningMessage("Some rows failed to run correctly" + Integer.toString(exitCode) + stderr.getContent());
        }
		stdoutT.join();
        stderrT.join();

		List<String> metabolites = Arrays.asList(stdout.getContent().split("\\$\\$\\$\\$" + System.lineSeparator()));
		return metabolites;
	}

	@Override
	public BufferedDataTable[] execute(BufferedDataTable[] inData, ExecutionContext exec) throws Exception {
		BufferedDataTable inTable = inData[0];
		DataTableSpec inSpec = inTable.getSpec();
        int parentIndex = inSpec.findColumnIndex(config.getParentsColumnName().getStringValue());
		if (parentIndex < 0) {
			// TODO better selection, when no column is selected
			parentIndex = 0;
		}
		BufferedDataContainer container = exec.createDataContainer(createOutputSpec());
		long rowCount = inTable.size();
		long currentRow = 0;
		for (DataRow inRow : inTable) {
			StringCell parentCell = ((StringCell) inRow.getCell(parentIndex));
			if (parentCell.isMissing()) {
				continue;
			}
			String parent = parentCell.getStringValue();
			logger.error(parentCell.toString());
			List<String> records = processParent(parent);
			for (String record : records) {
				if (record.equals(System.lineSeparator())) {
					continue;
				}
				// TODO from mol2 block parse id, pathway and score
				RowKey rowKey = RowKey.createRowKey(container.size());
				logger.error("Adding row with parent " + parent + " and metabolite " + record);
				DataCell[] cells = new DataCell[2];
				cells[0] = parentCell;
				cells[1] = new StringCell(record);
				container.addRowToTable(new DefaultRow(rowKey, cells));
			}
			exec.checkCanceled();
			exec.setProgress(currentRow / (double) rowCount, "Processing row " + currentRow);
			currentRow++;
		}
		container.close();
		BufferedDataTable outTable = container.getTable();
		// TODO join inTable and outTable on the parent column
		return new BufferedDataTable[] { outTable };
	}

    @Override
    protected void reset() {
        // No internals to reset
    }

	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// TODO Auto-generated method stub
		
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
}
