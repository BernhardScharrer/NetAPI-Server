package bernhard.scharrer.netapi.server;

public class RawConsole extends Console {

	@Override
	public void debug(String debug) {
		if (super.isDebugging()) System.out.println("[DEBUG] "+debug);
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
