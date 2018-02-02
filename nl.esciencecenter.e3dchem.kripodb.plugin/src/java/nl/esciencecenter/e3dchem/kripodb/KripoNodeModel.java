package nl.esciencecenter.e3dchem.kripodb;

import org.knime.core.node.port.PortType;

import nl.esciencecenter.e3dchem.python.PythonWrapperNodeConfig;
import nl.esciencecenter.e3dchem.python.PythonWrapperNodeModel;

public abstract class KripoNodeModel<C extends PythonWrapperNodeConfig> extends PythonWrapperNodeModel<C> {
	public KripoNodeModel(PortType[] inPortTypes, PortType[] outPortTypes) {
		super(inPortTypes, outPortTypes);
	}
}