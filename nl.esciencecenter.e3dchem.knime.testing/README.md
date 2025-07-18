Test Knime workflows from a Junit test.

[![Java CI with Maven](https://github.com/3D-e-Chem/knime-testflow/actions/workflows/ci.yml/badge.svg)](https://github.com/3D-e-Chem/knime-testflow/actions/workflows/ci.yml)
![Coverage](.github/badges/jacoco.svg)
[![DOI](https://zenodo.org/badge/doi/10.5281/zenodo.55805.svg)](http://dx.doi.org/10.5281/zenodo.55805)

The Knime Testing Framework can run a test workflow either:
* Inside Knime, if you right-click on a workflow in your local workspace, you can select "Run as workflow test".
* From the command line, using `knime -application org.knime.testing.NGTestflowRunner -root <workflow dir>`.

This repo gives you another option run a test workflow inside of a Junit @Test method declaration.

This project uses [Eclipse Tycho](https://www.eclipse.org/tycho/) to perform build steps.

# Usage

Using the plugin requires several steps.

## 1. Add repository

This plugin is available in the `https://update.knime.com/community-contributions/${knime.version}` update site.

To make use of in a Tycho based project add to the `<repositories>` tag of the `pom.xml` file the following:
```
<repository>
    <id>p2-knime-community</id>
    <layout>p2</layout>
    <url>https://update.knime.com/community-contributions/trusted/${knime.version}</url>
</repository>
```

## 2. Add dependency to tests

In the `Require-Bundle` attribute of the `META-INF/MANIFEST.MF` of the tests module add
```
nl.esciencecenter.e3dchem.knime.testing.plugin;bundle-version="[1.0.0,2.0.0)",
org.knime.testing;bundle-version="[4.0.0,6.0.0)",
```

## 3. Add test workflow

Create a test workflow as described in the ["Testing Framework" manual](https://github.com/knime/knime-core/blob/bf6f8c378694d5a435ef29cb469a7ced26ffca9f/org.knime.testing/doc/Regression%20Tests.pdf).

Place the workflow as a directory inside the `src/knime/` directory of the tests module.

## 4. Add test

Create a new test class and inside the class put the following:
```
@Rule
public ErrorCollector collector = new ErrorCollector();
private TestFlowRunner runner;

@Before
public void setUp() {
    TestrunConfiguration runConfiguration = new TestrunConfiguration();
    runner = new TestFlowRunner(collector, runConfiguration);
}

@Test
public void test_simple() throws IOException, InvalidSettingsException, CanceledExecutionException,
        UnsupportedWorkflowVersionException, LockFailedException, InterruptedException {
    File workflowDir = new File("src/knime/my-workflow-test");
    runner.runTestWorkflow(workflowDir);
}
```

This will test the workflow put in `src/knime/my-workflow-test` in the previous step.

This will run minimal checks, to check more configure `runConfiguration` object.  
For example add some more checks by adding 
```
runConfiguration.setTestDialogs(true);
runConfiguration.setReportDeprecatedNodes(true);
runConfiguration.setCheckMemoryLeaks(true);
```

## 5. Run tests

```
mvn verify
```

The test results can be found in the `T E S T S` section of the standard output.

## 6. Add GUI testing on GitHub actions.

As you might have noticed during the previouse step, running test will quickly show some dialogs and windows.
To show graphical user elements an X-server is required, sadly GitHub actions does not run an X-server.
A temporary X-server can be run with Xvfb, which is luckily available on all GitHub actions environments.

Prepend `xvfb-run` before the `mvn verify` command in the `.github/workflows/*.yml` file.

For example
```
script: xvfb-run mvn verify -B
```

# Build

```
mvn verify
```

An Eclipse update site will be made in `p2/target/repository/` repository.
The update site can be used to perform a local installation.
By default this will compile against KNIME AP v5.1, using the [KNIME-AP-5.1](targetPlatform/KNIME-AP-5.1.target) file.
To build instead for KNIME AP v4.7, use:
```
mvn verify -Dknime.version=4.7
```
