package nl.esciencecenter.e3dchem.modifiedtanimoto;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.util.CheckUtils;
import org.knime.core.node.util.ViewUtils;
import org.knime.distance.category.DistanceCategorySubPanel;

@SuppressWarnings("serial")
public class ModifiedTanimotoDistanceSubPanel extends
		DistanceCategorySubPanel<ModifiedTanimotoDistanceConfig> {

	private JTextField meanBitDensity;

	/**
	 * Constructor
	 */
	public ModifiedTanimotoDistanceSubPanel() {
		setLayout(new BorderLayout());
		add(settingsLayout(), BorderLayout.NORTH);
	}

	private Component settingsLayout() {
		meanBitDensity = new JTextField(5);
		JPanel settings = new JPanel(new GridLayout(0, 1));
		settings.setBorder(BorderFactory.createTitledBorder("Configuration"));
		settings.add(ViewUtils.getInFlowLayout(FlowLayout.LEFT, 0, 0,
				new JLabel("mean bit density:"), meanBitDensity));
		return settings;
	}

	@Override
	protected void loadConfig(ModifiedTanimotoDistanceConfig config) {
		ModifiedTanimotoDistanceConfig realConfig = config == null ? new ModifiedTanimotoDistanceConfig()
				: config;
		meanBitDensity.setText(Double.toString(realConfig.getMeanBitDensity()));
	}

	@Override
	protected ModifiedTanimotoDistanceConfig getConfig()
			throws InvalidSettingsException {
		ModifiedTanimotoDistanceConfig config = new ModifiedTanimotoDistanceConfig();
		config.setMeanBitDensity(getDouble(meanBitDensity,
				"Custom beta value must be a positive double value"));
		return config;
	}

	/**
	 * @param maxAmountDomainValues
	 * @return
	 * @throws InvalidSettingsException
	 */
	private static double getDouble(final JTextField maxAmountDomainValues,
			final String exceptionText) throws InvalidSettingsException {
		String text = maxAmountDomainValues.getText();
		try {
			double toReturn = Double.valueOf(text);
			CheckUtils.checkSetting(toReturn > 0, exceptionText);
			return toReturn;
		} catch (Exception e) {
			throw new InvalidSettingsException(exceptionText);
		}
	}

}
