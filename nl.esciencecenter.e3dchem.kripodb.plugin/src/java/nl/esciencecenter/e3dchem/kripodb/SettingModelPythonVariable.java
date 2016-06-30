package nl.esciencecenter.e3dchem.kripodb;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public class SettingModelPythonVariable extends SettingsModelString {
	private Boolean emptyAllowed = false;

	public SettingModelPythonVariable(String configName, String defaultValue) {
		this(configName, defaultValue, false);
	}

	public SettingModelPythonVariable(String configName, String defaultValue, Boolean emptyAllowed) {
		super(configName, defaultValue);
		this.emptyAllowed = emptyAllowed;

		// the actual value is the specified default value
		try {
			checkPythonic(defaultValue);
		} catch (IllegalArgumentException iae) {
			throw new IllegalArgumentException("InitialValue:" + iae.getMessage());
		}
	}

	@Override
	protected void validateSettingsForModel(NodeSettingsRO settings) throws InvalidSettingsException {
		super.validateSettingsForModel(settings);
		String value = settings.getString(getConfigName());
		try {
			checkPythonic(value);
		} catch (IllegalArgumentException iae) {
			throw new InvalidSettingsException(iae.getMessage());
		}
	}

	protected void checkPythonic(final String value) throws IllegalArgumentException {
		if (!emptyAllowed && value.length() == 0) {
			throw new IllegalArgumentException("Can not be empty");
		}

		// value can not start with numberic char.
		if (value.length() > 0 && Character.isDigit(value.charAt(0))) {
			throw new IllegalArgumentException("Python variable can not start with a digit 0 through 9");
		}

		// value can not contain non alpha/numeric or _ chars.
		String allowed_chars = "^[a-zA-Z0-9_]*$";
		if (!value.matches(allowed_chars)) {
			throw new IllegalArgumentException(
					"Python variable can only contain uppercase and lowercase letters A through Z, the underscore _ and the digits 0 through 9");
		}

		// TODO optionally check for reserver words from
		// https://docs.python.org/3.5/reference/lexical_analysis.html#keywords
	}

}
