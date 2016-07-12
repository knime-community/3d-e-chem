package nl.esciencecenter.e3dchem.sygma;

import java.util.Arrays;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.port.PortType;

import nl.esciencecenter.e3dchem.python.PythonWrapperNodeModel;

/**
 * This is the model implementation of PredictMetabolites.
 *
 */
public class PredictMetabolitesModel extends PythonWrapperNodeModel<PredictMetabolitesConfig> {
    /**
     * Constructor for the node model.
     */
    protected PredictMetabolitesModel() {
        // TODO one incoming port and one outgoing port is assumed
        super(new PortType[] { BufferedDataTable.TYPE }, new PortType[] { BufferedDataTable.TYPE });
        python_code_filename = "predict_metabolite.py";
        required_python_packages = Arrays.asList("sygma");
    }

    @Override
    protected PredictMetabolitesConfig createConfig() {
        return new PredictMetabolitesConfig();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(DataTableSpec[] inSpecs) throws InvalidSettingsException {

        // TODO: check if user settings are available, fit to the incoming
        // table structure, and the incoming types are feasible for the node
        // to execute. If the node can execute in its current state return
        // the spec of its output data table(s) (if you can, otherwise an array
        // with null elements), or throw an exception with a useful user message

        return super.configure(inSpecs);
    }
}
