package nl.esciencecenter.e3dchem.kripodb.fragmentsbysimilarity;

import org.knime.core.data.DataTableSpec;
import org.knime.core.data.StringValue;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObjectSpec;

import nl.esciencecenter.e3dchem.python.PythonOptionsPanel;

/**
 * <code>NodeDialog</code> for the "FragmentBySimilarity" Node.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 */
public class FragmentBySimilarityDialog extends DefaultNodeSettingsPane {
	private PythonOptionsPanel<FragmentsBySimilarityConfig> pythonOptions;

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

		SettingsModelString matrix = config.getMatrix();
		String historyID = "kripodb-matrix";
		// TODO DialogComponentFileChooser uses a border with 'Selected File' as
		// hardcoded title, replace with 'Matrix'.
		addDialogComponent(new DialogComponentFileChooser(matrix, historyID, ".h5"));

		SettingsModelDoubleBounded cutoff = config.getCutoff();
		addDialogComponent(new DialogComponentNumber(cutoff, "Cutoff", 0.01));

		SettingsModelIntegerBounded limit = config.getLimit();
		addDialogComponent(new DialogComponentNumber(limit, "Limit", 1));

		pythonOptions = new PythonOptionsPanel<FragmentsBySimilarityConfig>();
		addTab("Python options", pythonOptions);
	}

	@Override
	public void loadAdditionalSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs)
			throws NotConfigurableException {
		super.loadAdditionalSettingsFrom(settings, specs);
		FragmentsBySimilarityConfig config = new FragmentsBySimilarityConfig();
		try {
			config.loadFrom(settings);
			pythonOptions.loadSettingsFrom(config);
		} catch (InvalidSettingsException e) {
			// fall back to config defaults
		}
	}

	@Override
	public void loadAdditionalSettingsFrom(NodeSettingsRO settings, DataTableSpec[] specs)
			throws NotConfigurableException {
		super.loadAdditionalSettingsFrom(settings, specs);
		FragmentsBySimilarityConfig config = new FragmentsBySimilarityConfig();
		try {
			config.loadFrom(settings);
			pythonOptions.loadSettingsFrom(config);
		} catch (InvalidSettingsException e) {
			// fall back to config defaults
		}
	}

	@Override
	public void saveAdditionalSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
		super.saveAdditionalSettingsTo(settings);
		FragmentsBySimilarityConfig config = new FragmentsBySimilarityConfig();
		pythonOptions.saveSettingsTo(config);
		config.saveTo(settings);
	}
}
