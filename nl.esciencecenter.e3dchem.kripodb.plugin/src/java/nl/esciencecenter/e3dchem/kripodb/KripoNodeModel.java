package nl.esciencecenter.e3dchem.kripodb;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.port.PortType;

import nl.esciencecenter.e3dchem.python.PythonWrapperNodeConfig;
import nl.esciencecenter.e3dchem.python.PythonWrapperNodeModel;

public abstract class KripoNodeModel<C extends PythonWrapperNodeConfig> extends PythonWrapperNodeModel<C> {
	public KripoNodeModel(PortType[] inPortTypes, PortType[] outPortTypes) {
		super(inPortTypes, outPortTypes);
	}

	@Override
	public void validPython() throws InvalidSettingsException {
		try {
			super.validPython();
		} catch (InvalidSettingsException e) {
			if (e.getMessage().contains("install")) {
				throw new InvalidSettingsException(
						"'kripodb' Python package not found, please install or correct Python executable, see the description of a KripoDB node for help",
						e);
			}
			throw e;
		}
	
	}
}