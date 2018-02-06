package nl.esciencecenter.e3dchem.kripodb;

import java.io.IOException;

import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.port.PortType;

import nl.esciencecenter.e3dchem.python.PythonWrapperNodeConfig;
import nl.esciencecenter.e3dchem.python.PythonWrapperNodeModel;

public abstract class KripoNodeModel<C extends PythonWrapperNodeConfig> extends PythonWrapperNodeModel<C> {
	public KripoNodeModel(PortType[] inPortTypes, PortType[] outPortTypes) {
		super(inPortTypes, outPortTypes);
	}

	@Override
	public BufferedDataTable[] execute(BufferedDataTable[] inData, ExecutionContext exec) throws Exception {
		try {
			return super.execute(inData, exec);
		} catch (IOException e) {
			if (e.getMessage().contains("kripodb")) {
				throw new IOException(e.getMessage() + " See node description how to resolve.");
			} else {
				throw e;
			}
		}
	}

}