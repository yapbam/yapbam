package net.yapbam.junit;

import static org.junit.Assert.*;

import net.yapbam.util.ArrayUtils;

import org.junit.Test;

public class ArrayUtilsTest {

	@Test
	public void testToStringStringArray() {
		String[] array = new String[0];
		String string = ArrayUtils.toString(array);
		assertEquals(string, "");
		assertEquals(ArrayUtils.parseStringArray(string).length, 0);
		
		array = new String[]{"echo test", "&é+\"'],%t", "X"};
		string = ArrayUtils.toString(array);
		//System.out.println (string);
		assertArrayEquals(array, ArrayUtils.parseStringArray(string));

		array = new String[]{"A", "", "B", ""};
		string = ArrayUtils.toString(array);
		//System.out.println (string);
		assertArrayEquals(array, ArrayUtils.parseStringArray(string));

		array = new String[]{"A", "", "", ""};
		string = ArrayUtils.toString(array);
		//System.out.println (string);
		assertArrayEquals(array, ArrayUtils.parseStringArray(string));
}
}
