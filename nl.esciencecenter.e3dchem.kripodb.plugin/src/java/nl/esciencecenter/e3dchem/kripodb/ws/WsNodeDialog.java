package nl.esciencecenter.e3dchem.kripodb.ws;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentString;

public class WsNodeDialog extends DefaultNodeSettingsPane {
	protected void addAdvancedSettings() {
		addDialogComponent(new DialogComponentString(WsNodeConfig.basePathSettings(), "Base path"));
		addDialogComponent(new DialogComponentNumber(WsNodeConfig.timeoutSettings(), "Timeout (s)", 1));
	}
}
