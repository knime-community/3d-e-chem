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

public class FragmentsBySimilarityWorkflowTest {
	private static final String SIMILARITIES_H5 = "./similarities.h5";
	@Rule
	public ErrorCollector collector = new ErrorCollector();
	private TestFlowRunner runner;
	private static File similarity_matrix = new File(SIMILARITIES_H5);

	private void runTestWorkflow(String wfDir) throws IOException, InvalidSettingsException, CanceledExecutionException,
			UnsupportedWorkflowVersionException, LockFailedException, InterruptedException {
		File workflowDir = new File(wfDir);
		runner.runTestWorkflow(workflowDir);
	}

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
		FileUtils.copyURLToFile(new URL("https://github.com/3D-e-Chem/kripodb/raw/master/data/similarities.h5"),
				similarity_matrix);
	}

	@AfterClass
	public static void cleanupDatafiles() {
		similarity_matrix.delete();
	}

	@Test
	public void test_usinlocalfile() throws IOException, InvalidSettingsException, CanceledExecutionException,
			UnsupportedWorkflowVersionException, LockFailedException, InterruptedException {
		String wfDir = "src/knime/kripo-similar-fragments-test-localmatrix";
		runTestWorkflow(wfDir);
	}

	@Test
	public void test_invalidsettings() throws IOException, InvalidSettingsException, CanceledExecutionException,
			UnsupportedWorkflowVersionException, LockFailedException, InterruptedException {
		String wfDir = "src/knime/kripo-similar-fragments-test-invalidsettings";
		runTestWorkflow(wfDir);
	}
}
