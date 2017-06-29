package nl.esciencecenter.e3dchem.kripodb.ws.pharmacophores.align;

import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataTableSpecCreator;
import org.knime.core.data.StringValue;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.vector.doublevector.DoubleVectorCellFactory;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import nl.esciencecenter.e3dchem.kripodb.ws.WsNodeModel;
import nl.esciencecenter.e3dchem.kripodb.ws.client.model.AlignedPharmacophore;

/**
 * This is the model implementation of AlignModel.
 *
 */
public class AlignModel extends WsNodeModel<AlignConfig> {
	static final int QUERY_PORT = 0;
	static final int REFERENCE_PORT = 1;

	private static final DataTableSpec outputSpec = new DataTableSpec(
			new DataColumnSpecCreator("Aligned pharmacophore", StringCell.TYPE).createSpec(),
			new DataColumnSpecCreator("Transformation matrix", DoubleVectorCellFactory.TYPE).createSpec(),
			new DataColumnSpecCreator("RMSD", DoubleCell.TYPE).createSpec()
	);

	/**
	 * Constructor for the node model.
	 */
	protected AlignModel() {
		super(2, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {
		SettingsModelString queryColumn = getConfig().getQueryColumn();

		String referencePharmacophore = getReferencePharmacophore(inData);
		Aligner aligner = new Aligner(referencePharmacophore, getConfig().getPharmacophoresApi());
		
		BufferedDataTable queryData = inData[QUERY_PORT];
		DataTableSpec outSpec = new DataTableSpecCreator(queryData.getSpec()).addColumns(outputSpec).createSpec();
		BufferedDataContainer container = exec.createDataContainer(outSpec);
		int queryIndex = queryData.getSpec().findColumnIndex(queryColumn.getStringValue());
		
		String current;
		AlignedPharmacophore aligned;
		long currentRow = 0;
		long size = queryData.size();
		for (DataRow queryRow : queryData) {
			current = ((StringValue) queryRow.getCell(queryIndex)).getStringValue();
			aligned = aligner.align(current);
			double[] matrix = aligned.getTransformationMatrix().stream().flatMap(c -> c.stream()).mapToDouble(Double::doubleValue).toArray();
			container.addRowToTable(
					new DefaultRow(
							queryRow.getKey(),
							new StringCell(aligned.getPhar()),
							DoubleVectorCellFactory.createCell(matrix),
							new DoubleCell(aligned.getRmsd())
					)
			);
			
			exec.setProgress((double) currentRow++ / size);
			exec.checkCanceled();
		}

		// once we are done, we close the container and return its table
		container.close();
		BufferedDataTable out = container.getTable();
		return new BufferedDataTable[] { out };
	}

	
	
	private String getReferencePharmacophore(final BufferedDataTable[] inData) {
		SettingsModelString referenceColumn = getConfig().getReferenceColumn();
		String referencePharmacophore = null;
		BufferedDataTable referenceData = inData[REFERENCE_PORT];
		int referenceIndex = referenceData.getSpec().findColumnIndex(referenceColumn.getStringValue());
		for (DataRow referenceRow : referenceData) {
			referencePharmacophore = ((StringValue) referenceRow.getCell(referenceIndex)).getStringValue();
		}
		// TODO throw exception when not found
		return referencePharmacophore;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {

		// TODO: check if user settings are available, fit to the incoming
		// table structure, and the incoming types are feasible for the node
		// to execute. If the node can execute in its current state return
		// the spec of its output data table(s) (if you can, otherwise an array
		// with null elements), or throw an exception with a useful user message

		DataTableSpec outSpec = new DataTableSpecCreator(inSpecs[QUERY_PORT]).addColumns(outputSpec).createSpec();
		return new DataTableSpec[] { outSpec };
	}


	@Override
	public AlignConfig createConfig() {
		return new AlignConfig();
	}

}
