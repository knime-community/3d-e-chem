package nl.esciencecenter.e3dchem.kripodb;

import nl.esciencecenter.e3dchem.python.PythonWrapperNodeConfig;

public class KripoPythonConfig extends PythonWrapperNodeConfig {

	public KripoPythonConfig() {
		super();
		addRequiredModule("kripodb.canned");
	}

	public KripoPythonConfig(String[] inputTables, String[] outputTables) {
		super(inputTables, outputTables);
		addRequiredModule("kripodb.canned");
	}

}
