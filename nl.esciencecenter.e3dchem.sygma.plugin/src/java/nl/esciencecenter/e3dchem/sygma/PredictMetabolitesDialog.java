package nl.esciencecenter.e3dchem.sygma;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.data.StringValue;

/**
 * <code>NodeDialog</code> for the "PredictMetabolites" Node.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 */
public class PredictMetabolitesDialog extends DefaultNodeSettingsPane {
	@SuppressWarnings("unchecked")
	public PredictMetabolitesDialog() {
		super();
		PredictMetabolitesConfig config = new PredictMetabolitesConfig();

		addDialogComponent(new DialogComponentColumnNameSelection(config.getParentsColumnName(),
				"Parents molecule column", 0, StringValue.class));
		// TODO also allow smiles or strings column

		addDialogComponent(new DialogComponentNumber(config.getPhase1cycles(), "Phase 1 cycles:", 1, 5));
		addDialogComponent(new DialogComponentNumber(config.getPhase2cycles(), "Phase 2 cycles:", 1, 5));

		addDialogComponent(new DialogComponentString(config.getSygmaPath(), "Sygma path", true, 60));
	}
}
