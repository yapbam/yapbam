package net.yapbam.export;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class JsonFormatWriterTest {

	@Test
	public void test() throws IOException {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final JsonFormatWriter writer = new JsonFormatWriter(out);
		writer.addValue("a string with a \" and 	tab");
		writer.close();
		assertEquals("\"a string with a \\\" and \\ttab\"",out.toString(StandardCharsets.UTF_8));
	}

}
