package nl.esciencecenter.e3dchem.kripodb.ws.fragments;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import nl.esciencecenter.e3dchem.kripodb.ws.WsNodeModel;
import nl.esciencecenter.e3dchem.kripodb.ws.client.ApiException;
import nl.esciencecenter.e3dchem.kripodb.ws.client.FragmentsApi;
import nl.esciencecenter.e3dchem.kripodb.ws.client.model.Fragment;

/**
 * This is the model implementation of FragmentBySimilarity.
 *
 */
public class FragmentByIdModel extends WsNodeModel<FragmentsByIdConfig> {
	protected FragmentByIdModel() {
		super(1, 1);
	}

	@Override
	protected BufferedDataTable[] execute(BufferedDataTable[] inData, ExecutionContext exec) throws Exception {
		DataTableSpec outputSpec = createOutputColumnSpec();
		BufferedDataContainer container = exec.createDataContainer(outputSpec);
		BufferedDataTable table = inData[0];
		int columnIndex = table.getSpec().findColumnIndex(getConfig().getIdColumn().getStringValue());
		
		int chunkSize = getConfig().getChunkSize().getIntValue(); 
		long chunkCount = table.size() / chunkSize;
		if (table.size() < chunkSize) {
			chunkCount = 1;
		}
		long currentChunk = 1;
		List<String> chunk = new ArrayList<String>(chunkSize);
		for (DataRow row : table) {
			String id = ((StringCell) row.getCell(columnIndex)).getStringValue();
			
			chunk.add(id);
			// chunk full or last chunk
			if (chunk.size() == chunkSize || currentChunk == chunkCount) {
				fetchFragments(chunk, container);
				chunk.clear();

				exec.setProgress((double) currentChunk / chunkCount, " processing chunk " + currentChunk + " of " + chunkCount);
				currentChunk++;
			}

			exec.checkCanceled();
		}

		// once we are done, we close the container and return its table
		container.close();
		BufferedDataTable out = container.getTable();
		return new BufferedDataTable[] { out };
	}
	
	
	private void fetchFragments(List<String> ids, BufferedDataContainer container) throws ApiException {
		String idType = getConfig().getIdType().getStringValue();
		try {
			if (idType == "pdb") {
				fetchFragmentsByPdbCode(ids, container);
			} else {
				fetchFragmentsById(ids, container);
			} 
		} catch (ApiException e) {
			handleApiException(e, String.join(" or ", ids));
		}
	}
	
	private void fetchFragmentsByPdbCode(List<String> pdbCodes, BufferedDataContainer container) throws ApiException {
		FragmentsApi api = getConfig().getFragmentsApi();
		List<Fragment> fragments = api.getFragments(new ArrayList<String>(), pdbCodes);
		fillContainer(fragments, container);
	}

	private void fetchFragmentsById(List<String> fragmentIds, BufferedDataContainer container) throws ApiException {
		FragmentsApi api = getConfig().getFragmentsApi();
		List<Fragment> fragments = api.getFragments(fragmentIds, new ArrayList<String>());
		fillContainer(fragments, container);
	}

	private void fillContainer(List<Fragment> fragments, BufferedDataContainer container) {
		for (Fragment fragment : fragments) {
			RowKey rowKey = RowKey.createRowKey(container.size());
			DataCell[] cells = new DataCell[18];
			cells[0] = new StringCell(fragment.getSmiles());
			cells[1] = new StringCell(fragment.getPdbTitle());
			// TODO add other cells
			DataRow row = new DefaultRow(rowKey, cells);
			container.addRowToTable(row);
		}
	}

	@Override
	protected DataTableSpec[] configure(DataTableSpec[] inSpecs) throws InvalidSettingsException {
		// TODO validate and try column selection
		return new DataTableSpec[] { createOutputColumnSpec() };
	}

	private DataTableSpec createOutputColumnSpec() {
		// TODO reorder columns
		String[] names = { "smiles", "pdb_title", "het_seq_nr", "het_code", "frag_id", "rowid", "mol", "uniprot_name",
				"nr_r_groups", "pdb_code", "atom_codes", "prot_chain", "uniprot_acc", "ec_number", "prot_name",
				"frag_nr", "het_chain", "hash_code" };
		// TODO make smiles and mol into Smile and RDKIT type resp.
		DataType[] types = { StringCell.TYPE, StringCell.TYPE, IntCell.TYPE, StringCell.TYPE, StringCell.TYPE,
				IntCell.TYPE, StringCell.TYPE, StringCell.TYPE, IntCell.TYPE, StringCell.TYPE, StringCell.TYPE,
				StringCell.TYPE, StringCell.TYPE, StringCell.TYPE, StringCell.TYPE, IntCell.TYPE, StringCell.TYPE,
				StringCell.TYPE, };
		return new DataTableSpec(names, types);
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		super.saveSettingsTo(settings);
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		super.loadValidatedSettingsFrom(settings);
	}

	@Override
	public FragmentsByIdConfig createConfig() {
		return new FragmentsByIdConfig();
	}
}
