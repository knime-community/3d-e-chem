package nl.esciencecenter.e3dchem.kripodb.ws;

import static java.net.HttpURLConnection.HTTP_NOT_FOUND;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;

import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import nl.esciencecenter.e3dchem.kripodb.ws.client.ApiException;

public abstract class WsNodeModel<C extends WsNodeConfig> extends NodeModel {
	private C config;

	protected WsNodeModel(int nrInDataPorts, int nrOutDataPorts) {
		super(nrInDataPorts, nrOutDataPorts);
		config = createConfig();
	}

	public C getConfig() {
		return config;
	}

	public abstract C createConfig();

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		config.validateSettings(settings);
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		config.loadValidatedSettingsFrom(settings);

	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		config.saveSettingsTo(settings);
	}

	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// nothing to do as node has no internals

	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// nothing to do as node has no internals
	}

	@Override
	protected void reset() {
		// nothing to do as node has no internals
	}
	
	protected void handleApiException(ApiException e, String thing) throws ApiException {
		Throwable cause = e.getCause();
		if (e.getCode() == HTTP_NOT_FOUND) {
			// TODO parse JSON body for detail message
			throw new ApiException("Item '" + thing + "' could not be found on KripoDB server, please check if item is correct");
		}
		if (cause == null) {
			throw e;
		}
		if (cause instanceof SocketTimeoutException) {
			throw new ApiException("KripoDB webservice server timed out: Increase timeout in advanced tab of options dialog or try again later when server is less busy");
		}
		throw e;
	}

}
