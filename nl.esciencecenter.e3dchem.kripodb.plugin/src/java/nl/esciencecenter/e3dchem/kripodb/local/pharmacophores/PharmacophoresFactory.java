package nl.esciencecenter.e3dchem.kripodb.local.pharmacophores;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

@Deprecated
public class PharmacophoresFactory extends NodeFactory<PharmacophoresModel> {

	@Override
	public PharmacophoresModel createNodeModel() {
		return new PharmacophoresModel();
	}

	@Override
	protected int getNrNodeViews() {
		return 0;
	}

	@Override
	public NodeView<PharmacophoresModel> createNodeView(int viewIndex, PharmacophoresModel nodeModel) {
		return null;
	}

	@Override
	protected boolean hasDialog() {
		return true;
	}

	@Override
	protected NodeDialogPane createNodeDialogPane() {
		return new PharmacophoresDialog();
	}

}
