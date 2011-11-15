package net.yapbam.junit;

import static org.junit.Assert.*;

import net.yapbam.util.StringUtils;

import org.junit.Test;

/** Tests for StringUtils class.
 * @see StringUtils
 */
public class StringUtilsTest {

	@Test
	public void testSplit() {
		// Tests empty imput string
		assertArrayEquals(new String[]{""}, StringUtils.split("", ','));
		// Tests string which not contains the delimiter
		assertArrayEquals(new String[]{"ABC"}, StringUtils.split("ABC", ','));
		// Tests standard use
		assertArrayEquals(new String[]{"A","B","C"}, StringUtils.split("A,B,C", ','));
		// Tests string beginning and ending by the delimiter
		assertArrayEquals(new String[]{"","A","","B","C",""}, StringUtils.split(",A,,B,C,", ','));
		// Test exotic delimiter
		assertArrayEquals(new String[]{"A","B","C"}, StringUtils.split("A\\B\\C", '\\'));
	}
}
