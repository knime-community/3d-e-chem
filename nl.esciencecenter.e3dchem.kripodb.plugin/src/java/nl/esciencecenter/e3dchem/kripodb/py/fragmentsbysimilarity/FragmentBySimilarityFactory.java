package nl.esciencecenter.e3dchem.kripodb.py.fragmentsbysimilarity;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "FragmentBySimilarity" Node.
 *
 */
public class FragmentBySimilarityFactory extends NodeFactory<FragmentBySimilarityModel> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FragmentBySimilarityModel createNodeModel() {
		return new FragmentBySimilarityModel();
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
		return new FragmentBySimilarityDialog();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NodeView<FragmentBySimilarityModel> createNodeView(int viewIndex, FragmentBySimilarityModel nodeModel) {
		return null;
	}

}
