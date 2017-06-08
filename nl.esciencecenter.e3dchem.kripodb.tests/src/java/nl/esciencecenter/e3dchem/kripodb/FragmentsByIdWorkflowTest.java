package nl.esciencecenter.e3dchem.kripodb;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
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

import nl.esciencecenter.e3dchem.knime.testing.TestFlowRunner;
import nl.esciencecenter.e3dchem.python.PythonWrapperTestUtils;

public class FragmentsByIdWorkflowTest {
	private static final String FRAGMENTS_SQLITE = "./fragments.sqlite";
	@Rule
	public ErrorCollector collector = new ErrorCollector();
	private TestFlowRunner runner;
	private static File fragments = new File(FRAGMENTS_SQLITE);

	@Before
	public void setUp() {
		TestrunConfiguration runConfiguration = new TestrunConfiguration();
		runConfiguration.setTestDialogs(true);
		runConfiguration.setAllowedMemoryIncrease(4096000);
		runConfiguration.setLoadSaveLoad(false);
		runner = new TestFlowRunner(collector, runConfiguration);
	}

	private void runTestWorkflow(String wfDir) throws IOException, InvalidSettingsException, CanceledExecutionException,
			UnsupportedWorkflowVersionException, LockFailedException, InterruptedException {
		File workflowDir = new File(wfDir);
		runner.runTestWorkflow(workflowDir);
	}

	@BeforeClass
	public static void setUpDatafiles() throws MalformedURLException, IOException {
		PythonWrapperTestUtils.materializeKNIMEPythonUtils();
		FileUtils.copyURLToFile(new URL("https://github.com/3D-e-Chem/kripodb/raw/master/data/fragments.sqlite"),
				fragments);
	}

	@AfterClass
	public static void cleanupDatafiles() {
		fragments.delete();
	}

	@Test
	public void test_default() throws IOException, InvalidSettingsException, CanceledExecutionException,
			UnsupportedWorkflowVersionException, LockFailedException, InterruptedException {
		runTestWorkflow("src/knime/kripo-fragment-information-test-default");
	}

	@Test
	public void test_pdb() throws IOException, InvalidSettingsException, CanceledExecutionException,
			UnsupportedWorkflowVersionException, LockFailedException, InterruptedException {
		runTestWorkflow("src/knime/kripo-fragment-information-test-pdb");
	}

	@Test
	public void test_invalidsettings() throws IOException, InvalidSettingsException, CanceledExecutionException,
			UnsupportedWorkflowVersionException, LockFailedException, InterruptedException {
		runTestWorkflow("src/knime/kripo-fragment-information-test-invalidsettings");
	}

	@Test
	public void test_notfound() throws IOException, InvalidSettingsException, CanceledExecutionException,
			UnsupportedWorkflowVersionException, LockFailedException, InterruptedException {
		File workflowDir = new File("src/knime/kripo-fragment-information-test-notfound");
		runner.runTestWorkflow(workflowDir);
	}
}
