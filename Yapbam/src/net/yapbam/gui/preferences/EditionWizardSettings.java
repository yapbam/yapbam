package net.yapbam.gui.preferences;

public class EditionWizardSettings {
	public enum Mode {
		NEVER, WHEN_NULL, ALWAYS
	}
	public enum Source {
		MOST_PROBABLE, LAST
	}
	
	private Mode mode;
	private Source source;


	public EditionWizardSettings(Mode mode, Source source) {
		super();
		this.mode = mode;
		this.source = source;
	}

	public Mode getMode() {
		return mode;
	}

	public Source getSource() {
		return source;
	}

	void setMode(Mode mode) {
		this.mode = mode;
	}

	void setSource(Source source) {
		this.source = source;
	}
}
