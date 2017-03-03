package nl.esciencecenter.e3dchem.kripodb.ws;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.workflow.UnsupportedWorkflowVersionException;
import org.knime.core.util.LockFailedException;
import org.knime.testing.core.TestrunConfiguration;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import nl.esciencecenter.e3dchem.knime.testing.TestFlowRunner;
import nl.esciencecenter.e3dchem.python.PythonWrapperTestUtils;

/**
 * KNIME test workflows which call a web service which is mocked using WireMock.
 */
public class JavaWsWorkflowTest {
	private TestFlowRunner runner;

	@Rule
	public ErrorCollector collector = new ErrorCollector();

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8089);

	@Before
	public void setUp() {
		TestrunConfiguration runConfiguration = new TestrunConfiguration();
		runConfiguration.setTestDialogs(true);
		runConfiguration.setLoadSaveLoad(false);
		runner = new TestFlowRunner(collector, runConfiguration);
	}

	@BeforeClass
	public static void setUpDatafiles() throws MalformedURLException, IOException {
		PythonWrapperTestUtils.materializeKNIMEPythonUtils();
	}

	@Test
	public void test_similarFragments() throws IOException, InvalidSettingsException, CanceledExecutionException,
			UnsupportedWorkflowVersionException, LockFailedException, InterruptedException {
		File workflowDir = new File("src/knime/kripo-java-similar-fragments-test");
		runner.runTestWorkflow(workflowDir);
	}

	@Test
	public void test_fragmentInformation() throws IOException, InvalidSettingsException, CanceledExecutionException,
			UnsupportedWorkflowVersionException, LockFailedException, InterruptedException {
		File workflowDir = new File("src/knime/kripo-java-fragment-information-test");
		runner.runTestWorkflow(workflowDir);
	}
}
