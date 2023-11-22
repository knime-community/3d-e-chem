package nl.esciencecenter.e3dchem.sygma;

import java.io.BufferedReader;
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
import org.knime.core.node.port.PortType;

import nl.esciencecenter.e3dchem.python.PythonWrapperNodeModel;

/**
 * This is the model implementation of PredictMetabolites.
 *
 */
public class PredictMetabolitesModel extends PythonWrapperNodeModel<PredictMetabolitesConfig> {
	/**
	 * Constructor for the node model.
	 */
	protected PredictMetabolitesModel() {
		// TODO one incoming port and one outgoing port is assumed
		super(new PortType[] { BufferedDataTable.TYPE }, new PortType[] { BufferedDataTable.TYPE });
		python_code_filename = "predict_metabolite.py";
	}

	@Override
	protected PredictMetabolitesConfig createConfig() {
		return new PredictMetabolitesConfig();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(DataTableSpec[] inSpecs) throws InvalidSettingsException {

		// TODO: check if user settings are available, fit to the incoming
		// table structure, and the incoming types are feasible for the node
		// to execute. If the node can execute in its current state return
		// the spec of its output data table(s) (if you can, otherwise an array
		// with null elements), or throw an exception with a useful user message

		return super.configure(inSpecs);
	}

	public List<String> generateForSingleParent(String parent) {
		PredictMetabolitesConfig config = getConfig();
		// TODO add path to sygma cli to config
		String cliPath = "sygma";

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
		// When copying inData to output table using a Python pandas dataframe
		// copy(),
		// the smiles input column has changed to string type
		// Fix by finding column in smile type in inData input and applying cast
		// on output table

		// Find columns of smile type in input
		List<String> smileColNames = new ArrayList<>();
		DataTableSpec colSspec = inData[0].getSpec();
		for (DataColumnSpec colSpec : colSspec) {
			if (colSpec.getType().equals(SmilesCell.TYPE)) {
				smileColNames.add(colSpec.getName());
			}
		}

		// Run actual Python code
		BufferedDataTable[] pytables;
		try {
			pytables = super.execute(inData, exec);
		} catch (IOException e) {
			if (e.getMessage().contains("sygma")) {
				throw new IOException(e.getMessage() + " See node description how to resolve.");
			} else {
				throw e;
			}
		}

		// Create a rearranger which will perform replace
		DataTableSpec pyOutSpec = pytables[0].getSpec();
		ColumnRearranger rearranger = new ColumnRearranger(pyOutSpec);
		for (String smileColName : smileColNames) {
			int index = pyOutSpec.findColumnIndex(smileColName);
			rearranger.replace(new SingleCellFactory(colSspec.getColumnSpec(smileColName)) {

				@Override
				public DataCell getCell(DataRow row) {
					DataCell cell = row.getCell(index);
					if (cell.getType().isCompatible(SmilesCell.class)) {
						return cell;
					} else {
						return SmilesCellFactory.create(((StringCell) cell).getStringValue());
					}
				}
			}, smileColName);
		}

		// Run string>smiles rearranger on Python output
		BufferedDataTable outTable = exec.createColumnRearrangeTable(pytables[0], rearranger, exec);
		return new BufferedDataTable[] { outTable };
	}
}
