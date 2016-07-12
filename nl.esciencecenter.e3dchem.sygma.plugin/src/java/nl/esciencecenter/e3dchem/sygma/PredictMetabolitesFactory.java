package nl.esciencecenter.e3dchem.sygma;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "PredictMetabolites" Node.
 *
 */
public class PredictMetabolitesFactory extends NodeFactory<PredictMetabolitesModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public PredictMetabolitesModel createNodeModel() {
        return new PredictMetabolitesModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNrNodeViews() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeView<PredictMetabolitesModel> createNodeView(final int viewIndex, final PredictMetabolitesModel nodeModel) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDialog() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeDialogPane createNodeDialogPane() {
        return new PredictMetabolitesDialog();
    }

}
