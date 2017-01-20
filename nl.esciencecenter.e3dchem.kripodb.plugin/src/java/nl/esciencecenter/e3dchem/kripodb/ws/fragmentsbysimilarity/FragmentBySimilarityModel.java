package nl.esciencecenter.e3dchem.kripodb.ws.fragmentsbysimilarity;

import java.util.List;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;

import nl.esciencecenter.e3dchem.kripodb.ws.WsNodeModel;
import nl.esciencecenter.e3dchem.kripodb.ws.client.ApiException;
import nl.esciencecenter.e3dchem.kripodb.ws.client.FragmentsApi;
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

		long rowCount = table.size();
		long currentRow = 0;
		for (DataRow row : table) {
			String queryFragmentId = ((StringCell) row.getCell(columnIndex)).getStringValue();

			try {
				fetchSimilarFragments(queryFragmentId, container);
			} catch (ApiException e) {
				handleApiException(e, queryFragmentId);
			}

			exec.checkCanceled();
			exec.setProgress((double) currentRow / rowCount, " processing row " + currentRow);
			currentRow++;
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
		// TODO validate and try column selection
		return new DataTableSpec[] { createOutputColumnSpec() };
	}

	private DataTableSpec createOutputColumnSpec() {
		String[] names = { "query_frag_id", "hit_frag_id", "score" };
		DataType[] types = { StringCell.TYPE, StringCell.TYPE, DoubleCell.TYPE };
		return new DataTableSpec(names, types);
	}
}
