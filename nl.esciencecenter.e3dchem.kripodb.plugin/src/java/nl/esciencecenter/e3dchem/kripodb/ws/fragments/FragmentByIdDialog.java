package nl.esciencecenter.e3dchem.kripodb.ws.fragments;

import java.util.Set;

import org.knime.core.data.StringValue;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import nl.esciencecenter.e3dchem.kripodb.ws.WsNodeDialog;

/**
 * <code>NodeDialog</code> for the "FragmentBySimilarity" Node.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows creation of a simple dialog with standard
 * components. If you need a more complex dialog please derive directly from {@link org.knime.core.node.NodeDialogPane}.
 */
public class FragmentByIdDialog extends WsNodeDialog {
    /**
     * New pane for configuring FragmentBySimilarity node dialog. This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected FragmentByIdDialog() {
        super();
        FragmentsByIdConfig config = new FragmentsByIdConfig();

        SettingsModelString idColumn = config.getIdColumn();
        addDialogComponent(new DialogComponentColumnNameSelection(idColumn, "Identifiers", 0, StringValue.class));

        SettingsModelString idType = config.getIdType();
        Set<String> idTypeChoices = FragmentsByIdConfig.LIST_IDENTIFIERTYPES;
        addDialogComponent(new DialogComponentStringSelection(idType, "Type of identifier", idTypeChoices));

        createNewTab("Advanced");
        SettingsModelInteger chunkSize = config.getChunkSize();
        addDialogComponent(new DialogComponentNumber(chunkSize, "Chunk Size", 1));
        addAdvancedSettings();
    }
}
