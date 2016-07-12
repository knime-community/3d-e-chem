package nl.esciencecenter.e3dchem.sygma;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;

/**
 * <code>NodeDialog</code> for the "PredictMetabolites" Node.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 */
public class PredictMetabolitesDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring PredictMetabolites node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected PredictMetabolitesDialog() {
        super();
        PredictMetabolitesConfig config = new PredictMetabolitesConfig();

        SettingsModelIntegerBounded count = config.getCount();
        addDialogComponent(new DialogComponentNumber(count, "Counter:", 1, 5));
    }
}
