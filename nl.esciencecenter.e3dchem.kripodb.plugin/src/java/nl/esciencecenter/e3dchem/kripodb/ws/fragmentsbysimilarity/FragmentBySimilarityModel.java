package nl.esciencecenter.e3dchem.kripodb.ws.fragmentsbysimilarity;

import static java.net.HttpURLConnection.HTTP_NOT_FOUND;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.RowKey;
import org.knime.core.data.StringValue;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;

import com.google.gson.JsonParseException;

import nl.esciencecenter.e3dchem.kripodb.ws.WsNodeModel;
import nl.esciencecenter.e3dchem.kripodb.ws.client.ApiException;
import nl.esciencecenter.e3dchem.kripodb.ws.client.FragmentsApi;
import nl.esciencecenter.e3dchem.kripodb.ws.client.JSON;
import nl.esciencecenter.e3dchem.kripodb.ws.client.StringUtil;
import nl.esciencecenter.e3dchem.kripodb.ws.client.model.FragmentNotFound;
import nl.esciencecenter.e3dchem.kripodb.ws.client.model.Hit;

/**
 * This is the model implementation of FragmentBySimilarity.
 *
 */
public class FragmentBySimilarityModel extends WsNodeModel<FragmentsBySimilarityConfig> {
	protected FragmentBySimilarityModel() {
		super(1, 1);
	}

	@Override
	public FragmentsBySimilarityConfig createConfig() {
		return new FragmentsBySimilarityConfig();
	}

	@Override
	protected BufferedDataTable[] execute(BufferedDataTable[] inData, ExecutionContext exec) throws Exception {
		DataTableSpec outputSpec = createOutputColumnSpec();
		BufferedDataContainer container = exec.createDataContainer(outputSpec);
		BufferedDataTable table = inData[0];
		int columnIndex = table.getSpec().findColumnIndex(getConfig().getFragmentIdColumn().getStringValue());

		List<String> absentIdentifiers = new ArrayList<>();
		long rowCount = table.size();
		long currentRow = 0;
		for (DataRow row : table) {
			if (row.getCell(columnIndex).isMissing()) {
				setWarningMessage("Skipped missing value");
				continue;
			}
			String queryFragmentId = ((StringCell) row.getCell(columnIndex)).getStringValue();

			try {
				fetchSimilarFragments(queryFragmentId, container);
			} catch (ApiException e) {
				if (e.getCode() == HTTP_NOT_FOUND && e.getResponseHeaders().containsKey("Content-Type")
						&& !e.getResponseHeaders().get("Content-Type").isEmpty()
						&& "application/problem+json".equals(e.getResponseHeaders().get("Content-Type").get(0))) {
					JSON json = new JSON(getConfig().getApiClient());
					try {
						FragmentNotFound notFound = json.deserialize(e.getResponseBody(), FragmentNotFound.class);
						if (queryFragmentId.equals(notFound.getIdentifier())) {
							absentIdentifiers.add(queryFragmentId);
						} else {
							handleApiException(e, queryFragmentId);
						}
					} catch (JsonParseException e2) {
						handleApiException(e, queryFragmentId);
					}
				} else {
					handleApiException(e, queryFragmentId);
				}
			}

			exec.checkCanceled();
			exec.setProgress((double) currentRow / rowCount, " processing row " + currentRow);
			currentRow++;
		}
		if (!absentIdentifiers.isEmpty()) {
			this.getLogger().warn("Following fragment identifier(s) could not be found: "
					+ StringUtil.join(absentIdentifiers.toArray(new String[] {}), ","));
			setWarningMessage("Some query fragment identifiers could not be found");
		}

		// once we are done, we close the container and return its table
		container.close();
		BufferedDataTable out = container.getTable();
		return new BufferedDataTable[] { out };
	}

	private void fetchSimilarFragments(String fragmentId, BufferedDataContainer container) throws ApiException {
		FragmentsApi api = getConfig().getFragmentsApi();
		double cutoff = getConfig().getCutoff().getDoubleValue();
		int limit = getConfig().getLimit().getIntValue();
		List<Hit> hits = api.getSimilarFragments(fragmentId, cutoff, limit);
		for (Hit hit : hits) {
			RowKey rowKey = RowKey.createRowKey(container.size());
			DataRow row = new DefaultRow(rowKey, new StringCell(hit.getQueryFragId()),
					new StringCell(hit.getHitFragId()), new DoubleCell(hit.getScore()));
			container.addRowToTable(row);
		}
	}

	@Override
	protected DataTableSpec[] configure(DataTableSpec[] inSpecs) throws InvalidSettingsException {
		FragmentsBySimilarityConfig config = getConfig();

		String fragmentIdColumn = config.getFragmentIdColumn().getStringValue();
		int fragmentIdColumnIndex = inSpecs[0].findColumnIndex(fragmentIdColumn);
		if (fragmentIdColumnIndex == -1) {
			// if no column has been selected then use first string like column
			int i = 0;
			for (DataColumnSpec spec : inSpecs[0]) {
				if (spec.getType().isCompatible(StringValue.class)) {
					setWarningMessage(
							"Column '" + spec.getName() + "' automatically chosen as fragment identifier column");
					config.getFragmentIdColumn().setStringValue(spec.getName());
					fragmentIdColumnIndex = i;
					break;
				}
				i++;
			}
			if (fragmentIdColumnIndex == -1) {
				throw new InvalidSettingsException("No valid identifier column available, require a String column");
			}
		}
		if (!inSpecs[0].getColumnSpec(fragmentIdColumnIndex).getType().isCompatible(StringValue.class)) {
			throw new InvalidSettingsException("Column '" + fragmentIdColumn + "' does not contain String cells");
		}
		return new DataTableSpec[] { createOutputColumnSpec() };
	}

	private DataTableSpec createOutputColumnSpec() {
		String[] names = { "query_frag_id", "hit_frag_id", "score" };
		DataType[] types = { StringCell.TYPE, StringCell.TYPE, DoubleCell.TYPE };
		return new DataTableSpec(names, types);
	}
}
