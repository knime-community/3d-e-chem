package nl.esciencecenter.e3dchem.kripodb.py;

import static org.junit.Assert.*;

import org.junit.Test;

import nl.esciencecenter.e3dchem.kripodb.py.fragments.FragmentByIdFactory;

public class FragmentByIdFactoryTest {
	
	@Test
	public void getNrNodeViewsTest() {
		FragmentByIdFactory factory = new FragmentByIdFactory();
		assertEquals(factory.getNrNodeViews(), 0);
	}
}
