package bernhard.scharrer.netapi.server;

public class LinuxConsole extends Console {

	private static final String RESET = "\u001B[0m";
	private static final String RED = "\u001B[31m";
	private static final String GREEN = "\u001B[32m";
	private static final String YELLOW = "\u001B[33m";
	private static final String LIGHT_YELLOW = "\u001B[93m";
	
	protected static final String INFO = RESET+GREEN+"[INFO] " + RESET;
	protected static final String ERROR = RESET+RED+"[ERROR] " + RESET;
	protected static final String WARN = RESET+YELLOW+"[WARN] " + RESET;
	protected static final String DEBUG = RESET+LIGHT_YELLOW+"[DEBUG] " + RESET;
	
	protected static final String API = RESET+"[NetAPI]" + RESET + " ";

	public LinuxConsole(boolean debug) {
		super(debug);
	}
	
	@Override
	public void debug(String debug) {
		System.out.println(DEBUG+debug);
	}

	@Override
	public void info(String info) {
		System.out.println(INFO+info);
	}

	@Override
	public void warn(String warn) {
		System.out.println(WARN+warn);
	}

	@Override
	public void error(String error) {
		System.out.println(ERROR+error);
	}
	
}
