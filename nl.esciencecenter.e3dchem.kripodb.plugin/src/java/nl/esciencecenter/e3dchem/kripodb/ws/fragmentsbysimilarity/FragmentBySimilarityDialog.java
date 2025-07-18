package nl.esciencecenter.e3dchem.kripodb.ws.fragmentsbysimilarity;

import org.knime.core.data.StringValue;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import nl.esciencecenter.e3dchem.kripodb.ws.WsNodeDialog;

/**
 * <code>NodeDialog</code> for the "FragmentBySimilarity" Node.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 */
public class FragmentBySimilarityDialog extends WsNodeDialog {
	/**
	 * New pane for configuring FragmentBySimilarity node dialog. This is just a
	 * suggestion to demonstrate possible default dialog components.
	 */
	@SuppressWarnings("unchecked")
	protected FragmentBySimilarityDialog() {
		super();
		FragmentsBySimilarityConfig config = new FragmentsBySimilarityConfig();

		SettingsModelString fragmentIdColumn = config.getFragmentIdColumn();
		addDialogComponent(
				new DialogComponentColumnNameSelection(fragmentIdColumn, "Fragment identifiers", 0, StringValue.class));

		SettingsModelDoubleBounded cutoff = config.getCutoff();
		addDialogComponent(new DialogComponentNumber(cutoff, "Cutoff", 0.01));

		SettingsModelIntegerBounded limit = config.getLimit();
		addDialogComponent(new DialogComponentNumber(limit, "Limit", 1));

		createNewTab("Advanced");
		addAdvancedSettings(config);
	}
}
