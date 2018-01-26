package nl.esciencecenter.e3dchem.sygma;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.rdkit.knime.types.RDKitMolValue;

import nl.esciencecenter.e3dchem.python.PythonOptionsPanel;

/**
 * <code>NodeDialog</code> for the "PredictMetabolites" Node.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows creation of a simple dialog with standard
 * components. If you need a more complex dialog please derive directly from {@link org.knime.core.node.NodeDialogPane}.
 */
public class PredictMetabolitesDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring PredictMetabolites node dialog. This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    @SuppressWarnings("unchecked")
	protected PredictMetabolitesDialog() {
        super();
        PredictMetabolitesConfig config = new PredictMetabolitesConfig();

        addDialogComponent(new DialogComponentColumnNameSelection(config.getParentsColumnName(), "Parents molecule column", 0,
                RDKitMolValue.class));

        addDialogComponent(new DialogComponentNumber(config.getPhase1cycles(), "Phase 1 cycles:", 1, 5));
        addDialogComponent(new DialogComponentNumber(config.getPhase2cycles(), "Phase 2 cycles:", 1, 5));
        addTab("Python options", new PythonOptionsPanel<PredictMetabolitesConfig>(config));
    }
}
