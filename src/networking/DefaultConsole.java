package networking;

public class DefaultConsole implements Console {

	@Override
	public void debug(String msg) {
		System.out.println("[DEBUG] "+msg);
	}

	@Override
	public void info(String msg) {
		System.out.println("[INFO] "+msg);
	}

	@Override
	public void warn(String msg) {
		System.out.println("[WARN] "+msg);
	}

	@Override
	public void error(String msg) {
		System.out.println("[ERROR] "+msg);
	}

}
