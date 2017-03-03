package nl.esciencecenter.e3dchem.kripodb;

import static org.junit.Assert.*;

import org.junit.Test;

import nl.esciencecenter.e3dchem.kripodb.fragments.FragmentByIdFactory;

public class FragmentByIdFactoryTest {
	
	@Test
	public void getNrNodeViewsTest() {
		FragmentByIdFactory factory = new FragmentByIdFactory();
		assertEquals(factory.getNrNodeViews(), 0);
	}
}
