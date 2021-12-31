package net.yapbam.gui.dialogs.export;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import net.yapbam.export.CsvExportParameters;
import net.yapbam.gui.YapbamState;

public class ExporterParametersTest {

	@Test
	public void test() {
		final ExporterParameters<Void> p = new ExporterParameters<Void>(null);
		
		p.setFormatParams(new CsvExportParameters(StandardCharsets.UTF_16BE,'\t'));
		YapbamState.INSTANCE.save("net.yapbam.gui.dialogs.export.ExporterParametersTest", p);

		ExporterParameters<Void> other = (ExporterParameters<Void>) YapbamState.INSTANCE.restore("net.yapbam.gui.dialogs.export.ExporterParametersTest");
		
		assertEquals(p.getFormatParams(),other.getFormatParams());
	}

}
