package nl.esciencecenter.e3dchem.kripodb.ws.pharmacophores.align;

import org.knime.core.data.StringValue;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;

import nl.esciencecenter.e3dchem.kripodb.ws.WsNodeDialog;

public class AlignDialog extends WsNodeDialog {

    @SuppressWarnings("unchecked")
	protected AlignDialog() {
        super();
        
        AlignConfig config = new AlignConfig();
        addDialogComponent(new DialogComponentColumnNameSelection(
        		config.getQueryColumn(), 
        		"Query Pharmacophore column (table 1)", AlignModel.QUERY_PORT, StringValue.class
        ));

        addDialogComponent(new DialogComponentColumnNameSelection(
        		config.getReferenceColumn(), 
        		"Reference Pharmacophore column (table 2)", AlignModel.REFERENCE_PORT, StringValue.class
        ));
    }
}
