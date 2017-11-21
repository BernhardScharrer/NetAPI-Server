package bernhard.scharrer.netapi.server;

public class WindowsConsole extends Console {

	public WindowsConsole(boolean debug) {
		super(debug);
	}

	@Override
	protected void debug(String debug) {
		System.out.println("[DEBUG] "+debug);
	}

	@Override
	protected void info(String info) {
		System.out.println("[INFO] "+info);
	}

	@Override
	protected void warn(String warn) {
		System.out.println("[WARN] "+warn);
	}

	@Override
	protected void error(String error) {
		System.err.println("[ERROR] "+error);
	}
	
}
