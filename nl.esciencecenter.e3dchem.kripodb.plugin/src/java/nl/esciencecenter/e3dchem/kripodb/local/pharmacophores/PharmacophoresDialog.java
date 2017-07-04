package nl.esciencecenter.e3dchem.kripodb.local.pharmacophores;

import org.knime.core.data.StringValue;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import nl.esciencecenter.e3dchem.kripodb.ws.WsNodeDialog;

public class PharmacophoresDialog extends WsNodeDialog {

	@SuppressWarnings("unchecked")
	public PharmacophoresDialog() {
		super();
		PharmacophoresConfig config = new PharmacophoresConfig();

		SettingsModelString idColumn = config.getIdColumn();
		addDialogComponent(
				new DialogComponentColumnNameSelection(idColumn, "Kripo fragment identifiers", 0, StringValue.class));

		SettingsModelString pharsdb = config.getPharmacophoresDB();
		String historyID = "kripodb-pharsdb";
		addDialogComponent(new DialogComponentFileChooser(pharsdb, historyID, ".h5"));
	}

}
