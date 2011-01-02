package net.yapbam.junit;

import static org.junit.Assert.*;

import net.yapbam.util.CheckSum;

import org.junit.Test;

public class CheckSumTest {

	@Test
	public void test() {
		test (new byte[] {0,127});
		test (new byte[] {127,32});
		test (new byte[] {-1,32});
		test (new byte[] {7,32});
	}

	private void test(byte[] bytes) {
		String toString = CheckSum.toString(bytes);
		System.out.println (toString);
		byte[] toBytes = CheckSum.toBytes(toString);
		assertArrayEquals(bytes, toBytes);
	}

}
