package nl.esciencecenter.e3dchem.kripodb;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

public class SettingModelPythonVariableTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void constructor_filled_valid() {
		new SettingModelPythonVariable("mykey", "myval");
	}

	@Test
	public void constructor_allowempty_valid() {
		new SettingModelPythonVariable("mykey", "", true);
	}

	@Test
	public void constructor_disallowempty_valid() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("InitialValue:Can not be empty");

		new SettingModelPythonVariable("mykey", "");
	}

	@Test
	public void constructor_startswithdigit() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("InitialValue:Python variable can not start with a digit 0 through 9");

		new SettingModelPythonVariable("mykey", "0myval");
	}

	@Test
	public void constructor_weirdchar() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(
				"InitialValue:Python variable can only contain uppercase and lowercase letters A through Z, the underscore _ and the digits 0 through 9");

		new SettingModelPythonVariable("mykey", "my&val");

	}

	@Test
	public void validateSettingsForModel_valid() throws InvalidSettingsException {
		SettingModelPythonVariable setting = new SettingModelPythonVariable("mykey", "myval");
		NodeSettings settings = new NodeSettings("mykey");
		settings.addString("mykey", "myvar2");

		setting.validateSettings(settings);
	}

	@Test
	public void validateSettingsForModel_startswithdigit() throws InvalidSettingsException {
		thrown.expect(InvalidSettingsException.class);
		thrown.expectMessage("Python variable can not start with a digit 0 through 9");

		SettingModelPythonVariable setting = new SettingModelPythonVariable("mykey", "myval");
		NodeSettings settings = new NodeSettings("mykey");
		settings.addString("mykey", "0myvar2");

		setting.validateSettings(settings);
	}

	@Test
	public void validateSettingsForModel_weirdchar() throws InvalidSettingsException {
		thrown.expect(InvalidSettingsException.class);
		thrown.expectMessage(
				"Python variable can only contain uppercase and lowercase letters A through Z, the underscore _ and the digits 0 through 9");

		SettingModelPythonVariable setting = new SettingModelPythonVariable("mykey", "myval");
		NodeSettings settings = new NodeSettings("somekey");
		settings.addString("mykey", "my&val2");

		setting.validateSettings(settings);
	}

}
