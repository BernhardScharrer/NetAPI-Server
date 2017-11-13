package bernhard.scharrer.netapi.server;

public class DefaultConsole extends Console {
	
	public DefaultConsole(boolean debug) {
		super(debug);
	}

	@Override
	public void debug(String debug) {
		System.out.println("[DEBUG] "+debug);
	}

	@Override
	public void info(String info) {
		System.out.println("[INFO] "+info);
	}

	@Override
	public void warn(String warn) {
		System.out.println("[WARN] "+warn);
	}

	@Override
	public void error(String error) {
		System.out.println("[ERROR] "+error);
	}
	
}
