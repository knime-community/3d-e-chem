package nl.esciencecenter.e3dchem.kripodb.ws;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentString;

public class WsNodeDialog extends DefaultNodeSettingsPane {
	protected void addAdvancedSettings(WsNodeConfig config) {
		addDialogComponent(new DialogComponentString(config.getBasePathSettings(), "Base path"));
		addDialogComponent(new DialogComponentNumber(config.getTimeoutSettings(), "Timeout (s)", 1));
	}
}
