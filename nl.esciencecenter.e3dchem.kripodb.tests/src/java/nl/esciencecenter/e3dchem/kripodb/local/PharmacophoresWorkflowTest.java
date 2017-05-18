package nl.esciencecenter.e3dchem.kripodb.local;

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

public class PharmacophoresWorkflowTest {
	private static final String PHARMACOPHORES_H5 = "./pharmacophores.h5";
	@Rule
	public ErrorCollector collector = new ErrorCollector();
	private TestFlowRunner runner;
	private static File pharmacophoresdb = new File(PHARMACOPHORES_H5);

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
		FileUtils.copyURLToFile(new URL("https://github.com/3D-e-Chem/kripodb/raw/master/data/pharmacophores.h5"),
				pharmacophoresdb);
	}

	@AfterClass
	public static void cleanupDatafiles() {
		pharmacophoresdb.delete();
	}

// TODO implement workflow
//	@Test
//	public void test_default() throws IOException, InvalidSettingsException, CanceledExecutionException,
//			UnsupportedWorkflowVersionException, LockFailedException, InterruptedException {
//		runTestWorkflow("src/knime/kripo-python-pharmacophores-test-default");
//	}
}
