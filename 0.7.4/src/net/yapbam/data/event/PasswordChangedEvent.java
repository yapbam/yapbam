package net.yapbam.data.event;

public class PasswordChangedEvent extends DataEvent {
	private String old;
	private String password;

	public PasswordChangedEvent(Object source, String old, String password) {
		super(source);
		this.old = old;
		this.password = password;
	}

	public String getOldPassword() {
		return old;
	}

	public String getPassword() {
		return password;
	}
}
