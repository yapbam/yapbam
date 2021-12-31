package net.yapbam.export;

import java.io.Serializable;

public interface FormatParams extends Serializable {
	ExportFormatType getType();
	String format(Object obj);
}
