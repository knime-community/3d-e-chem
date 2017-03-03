package nl.esciencecenter.e3dchem.kripodb.ws.fragments;

import static java.net.HttpURLConnection.HTTP_NOT_FOUND;

import java.util.ArrayList;
import java.util.List;

import org.knime.chem.types.Mol2Cell;
import org.knime.chem.types.Mol2CellFactory;
import org.knime.chem.types.SmilesCell;
import org.knime.chem.types.SmilesCellFactory;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.RowKey;
import org.knime.core.data.StringValue;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.google.gson.JsonParseException;

import nl.esciencecenter.e3dchem.kripodb.ws.WsNodeModel;
import nl.esciencecenter.e3dchem.kripodb.ws.client.ApiException;
import nl.esciencecenter.e3dchem.kripodb.ws.client.FragmentsApi;
import nl.esciencecenter.e3dchem.kripodb.ws.client.JSON;
import nl.esciencecenter.e3dchem.kripodb.ws.client.StringUtil;
import nl.esciencecenter.e3dchem.kripodb.ws.client.model.Fragment;
import nl.esciencecenter.e3dchem.kripodb.ws.client.model.FragmentsNotFound;

/**
 * This is the model implementation of FragmentById.
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

		List<String> absentIdentifiers = new ArrayList<>();
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
				fetchFragments(chunk, container, absentIdentifiers);
				chunk.clear();

				exec.setProgress((double) currentChunk / chunkCount,
						" processing chunk " + currentChunk + " of " + chunkCount);
				currentChunk++;
			}

			exec.checkCanceled();
		}
		if (!absentIdentifiers.isEmpty()) {
			this.getLogger().warn("Following identifier(s) could not be found: "
					+ StringUtil.join(absentIdentifiers.toArray(new String[] {}), ","));
			setWarningMessage("Some identifiers could not be found");
		}

		// once we are done, we close the container and return its table
		container.close();
		BufferedDataTable out = container.getTable();
		return new BufferedDataTable[] { out };
	}

	private void fetchFragments(List<String> ids, BufferedDataContainer container, List<String> absentIdentifiers)
			throws ApiException {
		String idType = getConfig().getIdType().getStringValue();
		try {
			if (idType == "pdb") {
				fetchFragmentsByPdbCode(ids, container);
			} else {
				fetchFragmentsById(ids, container);
			}
		} catch (ApiException e) {
			if (e.getCode() == HTTP_NOT_FOUND && e.getResponseHeaders().containsKey("Content-Type")
					&& !e.getResponseHeaders().get("Content-Type").isEmpty()
					&& e.getResponseHeaders().get("Content-Type").get(0) == "application/problem+json") {
				JSON json = new JSON(getConfig().getApiClient());
				try {
					FragmentsNotFound notFound = json.deserialize(e.getResponseBody(), FragmentsNotFound.class);
					absentIdentifiers.addAll(notFound.getAbsentIdentifiers());
				} catch (JsonParseException e2) {
					handleApiException(e, String.join(" or ", ids));
				}
			} else {
				handleApiException(e, String.join(" or ", ids));
			}
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
			DataCell[] cells = new DataCell[17];
			cells[0] = SmilesCellFactory.create(fragment.getSmiles());
			cells[1] = new StringCell(fragment.getPdbTitle());
			cells[2] = new IntCell(fragment.getHetSeqNr());
			cells[3] = new StringCell(fragment.getHetCode());
			cells[4] = new StringCell(fragment.getFragId());
			cells[5] = Mol2CellFactory.create(fragment.getMol());
			cells[6] = new StringCell(fragment.getUniprotName());
			cells[7] = new IntCell(fragment.getNrRGroups());
			cells[8] = new StringCell(fragment.getPdbCode());
			cells[9] = new StringCell(fragment.getAtomCodes());
			cells[10] = new StringCell(fragment.getProtChain());
			cells[11] = new StringCell(fragment.getUniprotAcc());
			cells[12] = new StringCell(fragment.getEcNumber());
			cells[13] = new StringCell(fragment.getProtName());
			cells[14] = new IntCell(fragment.getFragNr());
			cells[15] = new StringCell(fragment.getHetChain());
			cells[16] = new StringCell(fragment.getHashCode());
			DataRow row = new DefaultRow(rowKey, cells);
			container.addRowToTable(row);
		}
	}

	@Override
	protected DataTableSpec[] configure(DataTableSpec[] inSpecs) throws InvalidSettingsException {
		FragmentsByIdConfig config = getConfig();

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
		return new DataTableSpec[] { createOutputColumnSpec() };
	}

	private DataTableSpec createOutputColumnSpec() {
		// TODO reorder columns
		String[] names = { "smiles", "pdb_title", "het_seq_nr", "het_code", "frag_id", "mol", "uniprot_name",
				"nr_r_groups", "pdb_code", "atom_codes", "prot_chain", "uniprot_acc", "ec_number", "prot_name",
				"frag_nr", "het_chain", "hash_code" };
		DataType[] types = { SmilesCell.TYPE, StringCell.TYPE, IntCell.TYPE, StringCell.TYPE, Mol2Cell.TYPE,
				StringCell.TYPE, StringCell.TYPE, IntCell.TYPE, StringCell.TYPE, StringCell.TYPE, StringCell.TYPE,
				StringCell.TYPE, StringCell.TYPE, StringCell.TYPE, IntCell.TYPE, StringCell.TYPE, StringCell.TYPE, };
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
