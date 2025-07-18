package nl.esciencecenter.e3dchem.kripodb.ws.fragments;

import static java.net.HttpURLConnection.HTTP_NOT_FOUND;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.RDKit.RDKFuncs;
import org.RDKit.RWMol;
import org.knime.chem.types.SmilesCell;
import org.knime.chem.types.SmilesCellFactory;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.MissingCell;
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
import org.rdkit.knime.types.RDKitMolCellFactory;

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
		List<String> chunk = new ArrayList<String>(chunkSize);
		long currentRow = 1;
		for (DataRow row : table) {
			if (row.getCell(columnIndex).isMissing()) {
				setWarningMessage("Skipped missing value");
				continue;
			}
			String id = ((StringCell) row.getCell(columnIndex)).getStringValue();

			chunk.add(id);
			// chunk full or last row
			if (chunk.size() == chunkSize || currentRow >= table.size()) {
				fetchFragments(chunk, container, absentIdentifiers);
				chunk.clear();

				exec.setProgress((double) currentRow / table.size(),
						" processing row " + currentRow + " of " + table.size());
			}
			currentRow++;
			exec.checkCanceled();
		}
		if (!absentIdentifiers.isEmpty()) {
			if (absentIdentifiers.size() == table.size()) {
				throw new ApiException("Could not find any fragment identifiers, check input and base path option");
			} else {
				this.getLogger().warn("Following identifier(s) could not be found: "
						+ StringUtil.join(absentIdentifiers.toArray(new String[] {}), ","));
				setWarningMessage("Some identifiers could not be found");
			}
		}

		// once we are done, we close the container and return its table
		container.close();
		BufferedDataTable out = container.getTable();
		return new BufferedDataTable[] { out };
	}

	private void fetchFragments(List<String> ids, BufferedDataContainer container, List<String> absentIdentifiers)
			throws ApiException, InvalidSettingsException {
		String idType = getConfig().getIdType().getStringValue();
		try {
			if ("pdb".equals(idType)) {
				fetchFragmentsByPdbCode(ids, container);
			} else if ("fragment".equals(idType)) {
				fetchFragmentsById(ids, container);
			} else {
				throw new InvalidSettingsException("Invalid type of identifier");
			}
		} catch (ApiException e) {
			if (e.getCode() == HTTP_NOT_FOUND && e.getResponseHeaders().containsKey("Content-Type")
					&& !e.getResponseHeaders().get("Content-Type").isEmpty()
					&& "application/problem+json".equals(e.getResponseHeaders().get("Content-Type").get(0))) {
				JSON json = new JSON(getConfig().getApiClient());
				try {
					FragmentsNotFound notFound = json.deserialize(e.getResponseBody(), FragmentsNotFound.class);
					absentIdentifiers.addAll(notFound.getAbsentIdentifiers());
					fillContainer(notFound.getFragments(), container);
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
		List<String> ids = pdbCodes.stream().map(String::toLowerCase).collect(Collectors.toList());
		List<Fragment> fragments = api.getFragments(new ArrayList<String>(), ids);
		fillContainer(fragments, container);
	}

	private void fetchFragmentsById(List<String> fragmentIds, BufferedDataContainer container) throws ApiException {
		FragmentsApi api = getConfig().getFragmentsApi();
		List<Fragment> fragments = api.getFragments(fragmentIds, new ArrayList<String>());
		fillContainer(fragments, container);
	}

	private DataTableSpec createOutputColumnSpec() {
		// TODO reorder columns
		String[] names = { "smiles", "pdb_title", "het_seq_nr", "het_code", "frag_id", "mol", "uniprot_name",
				"nr_r_groups", "pdb_code", "atom_codes", "prot_chain", "uniprot_acc", "ec_number", "prot_name",
				"frag_nr", "het_chain", "hash_code" };
		DataType[] types = { SmilesCell.TYPE, StringCell.TYPE, IntCell.TYPE, StringCell.TYPE, StringCell.TYPE,
				RDKitMolCellFactory.TYPE, StringCell.TYPE, IntCell.TYPE, StringCell.TYPE, StringCell.TYPE,
				StringCell.TYPE, StringCell.TYPE, StringCell.TYPE, StringCell.TYPE, IntCell.TYPE, StringCell.TYPE,
				StringCell.TYPE, };
		return new DataTableSpec(names, types);
	}

	private DataCell fragStringCell(String value, String missingMessage) {
		if (value == null) {
			return new MissingCell(missingMessage);
		}
		return new StringCell(value);
	}

	private void fillContainer(List<Fragment> fragments, BufferedDataContainer container) {
		for (Fragment fragment : fragments) {

			DataCell ecNumber = fragStringCell(fragment.getEcNumber(), "Fragment has no ec number");
			DataCell protName = fragStringCell(fragment.getProtName(), "Fragment has no protein name");
			DataCell uniprotName = fragStringCell(fragment.getUniprotName(), "Fragment has no uniprot name");
			DataCell uniprotAcc = fragStringCell(fragment.getUniprotAcc(), "Fragment has no uniprot accession");
			DataCell smiles = new MissingCell("Fragment has no smiles");
			if (fragment.getSmiles() != null) {
				smiles = SmilesCellFactory.create(fragment.getSmiles());
			}
			DataCell mol = new MissingCell("Fragment has no molblock");
			if (fragment.getMol() != null) {
				RWMol rdkitmol = RDKFuncs.MolBlockToMol(fragment.getMol());
				mol = RDKitMolCellFactory.createRDKitMolCell(rdkitmol);
			}

			DataRow row = new DefaultRow(RowKey.createRowKey(container.size()), smiles,
					new StringCell(fragment.getPdbTitle()), new IntCell(fragment.getHetSeqNr()),
					new StringCell(fragment.getHetCode()), new StringCell(fragment.getFragId()), mol, uniprotName,
					new IntCell(fragment.getNrRGroups()), new StringCell(fragment.getPdbCode()),
					new StringCell(fragment.getAtomCodes()), new StringCell(fragment.getProtChain()), uniprotAcc,
					ecNumber, protName, new IntCell(fragment.getFragNr()), new StringCell(fragment.getHetChain()),
					new StringCell(fragment.getHashCode()));
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
