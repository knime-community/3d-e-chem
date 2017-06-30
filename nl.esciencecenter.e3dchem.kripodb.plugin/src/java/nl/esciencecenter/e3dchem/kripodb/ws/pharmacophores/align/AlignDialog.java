package nl.esciencecenter.e3dchem.kripodb.ws.pharmacophores.align;

import org.knime.core.data.StringValue;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;

import nl.esciencecenter.e3dchem.kripodb.ws.WsNodeDialog;

public class AlignDialog extends WsNodeDialog {

	@SuppressWarnings("unchecked")
	protected AlignDialog() {
		super();

		AlignConfig config = new AlignConfig();
		addDialogComponent(new DialogComponentColumnNameSelection(config.getQueryColumn(),
				"Query Pharmacophore column (table 1)", AlignModel.QUERY_PORT, StringValue.class));

		addDialogComponent(new DialogComponentColumnNameSelection(config.getReferenceColumn(),
				"Reference Pharmacophore column (table 2)", AlignModel.REFERENCE_PORT, StringValue.class));

		createNewTab("Advanced");

		addDialogComponent(new DialogComponentNumber(config.getCutoff(),
				"Tolerance threshold for considering two distances to be equivalent", 0.1));

		addDialogComponent(
				new DialogComponentNumber(config.getBreakNumCliques(), "Break when set number of cliques is found", 1));

		addAdvancedSettings(config);
	}
}
