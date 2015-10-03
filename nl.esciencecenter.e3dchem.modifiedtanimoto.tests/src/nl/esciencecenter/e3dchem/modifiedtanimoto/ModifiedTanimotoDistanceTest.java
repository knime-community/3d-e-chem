package nl.esciencecenter.e3dchem.modifiedtanimoto;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.vector.bitvector.SparseBitVectorCell;
import org.knime.core.data.vector.bitvector.SparseBitVectorCellFactory;
import org.knime.core.node.InvalidSettingsException;
import org.knime.distance.DistanceMeasurementException;

public class ModifiedTanimotoDistanceTest {

	private ModifiedTanimotoDistance measure;

	@Before
	public void setUp() throws InvalidSettingsException {
		ModifiedTanimotoDistanceConfig config = new ModifiedTanimotoDistanceConfig(0.023, "BitVector");
		DataColumnSpec columnSpecs = new DataColumnSpecCreator("BitVector", SparseBitVectorCell.TYPE).createSpec();
		DataTableSpec spec = new DataTableSpec(columnSpecs);
		measure = new ModifiedTanimotoDistance(config, spec);
	}
	
	@Test
	public void computeDistance() throws DistanceMeasurementException {
		SparseBitVectorCellFactory factory = new SparseBitVectorCellFactory(10);
		factory.set(2);
		factory.set(4);
		factory.set(6);
		factory.set(7);
		SparseBitVectorCell bitvector1 = factory.createDataCell();
		DataRow row1 = new DefaultRow("bitvector1", bitvector1);
		factory.set(3);
		factory.set(5);
		factory.clear(7);
		factory.clear(4);
		SparseBitVectorCell bitvector2 = factory.createDataCell();
		DataRow row2 = new DefaultRow("bitvector2", bitvector2);
		
		double distance = measure.computeDistance(row1, row2);

		assertEquals(0.3902, distance, 0.0001);
	}

	@Test(expected=DistanceMeasurementException.class)
	public void computeDistance_DiffLength_DistanceMeasurementException() throws DistanceMeasurementException {
		SparseBitVectorCellFactory factory1 = new SparseBitVectorCellFactory(11);
		SparseBitVectorCell bitvector1 = factory1.createDataCell();
		DataRow row1 = new DefaultRow("bitvector1", bitvector1);
		SparseBitVectorCellFactory factory2 = new SparseBitVectorCellFactory(22);
		SparseBitVectorCell bitvector2 = factory2.createDataCell();
		DataRow row2 = new DefaultRow("bitvector2", bitvector2);
		
		measure.computeDistance(row1, row2);
	}
}
