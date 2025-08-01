package nl.esciencecenter.e3dchem.kripodb.ws.pharmacophores;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.data.MissingCell;
import org.knime.core.data.container.SingleCellFactory;
import org.knime.core.data.def.StringCell;

import nl.esciencecenter.e3dchem.knime.pharmacophore.PharCell;
import nl.esciencecenter.e3dchem.kripodb.ws.client.ApiException;
import nl.esciencecenter.e3dchem.kripodb.ws.client.FragmentsApi;

public class PharmacophoreCellFactory extends SingleCellFactory {
	private final FragmentsApi api;
	private final int colIndex;

	public PharmacophoreCellFactory(final DataColumnSpec createOutputColumnSpec, final FragmentsApi pharmacophoresApi,
			final int colIndex) {
		super(createOutputColumnSpec);
		this.api = pharmacophoresApi;
		this.colIndex = colIndex;
	}

	@Override
	public DataCell getCell(final DataRow row) {
		DataCell currCell = row.getCell(colIndex);
		// check the cell for missing value
		if (currCell.isMissing()) {
			return new MissingCell("Missing fragment identifier has missing pharmacophore");
		}
		String fragmentId = ((StringCell) currCell).getStringValue();
		try {
			File pharfile = api.getFragmentPhar(fragmentId);
			Charset encoding = Charset.forName("UTF-8");
			String phar = Files.readString(pharfile.toPath(), encoding);
			return new PharCell(phar);
		} catch (ApiException | IOException e) {
			return new MissingCell(e.getMessage());
		}
	}

}
