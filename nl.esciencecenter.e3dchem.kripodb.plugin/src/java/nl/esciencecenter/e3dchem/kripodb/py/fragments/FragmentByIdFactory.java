package nl.esciencecenter.e3dchem.kripodb.py.fragments;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "FragmentBySimilarity" Node.
 *
 */
public class FragmentByIdFactory extends NodeFactory<FragmentByIdModel> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FragmentByIdModel createNodeModel() {
		return new FragmentByIdModel();
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
	public boolean hasDialog() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NodeDialogPane createNodeDialogPane() {
		return new FragmentByIdDialog();
	}

	@Override
	public NodeView<FragmentByIdModel> createNodeView(int viewIndex, FragmentByIdModel nodeModel) {
		return null;
	}

}
