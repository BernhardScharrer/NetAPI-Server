package bernhard.scharrer.netapi.server;

public abstract class Console {
	
	private boolean debug;

	/**
	 * @param should console debug?
	 */
	public Console(boolean debug) {
		this.debug = debug;
	}
	
	/**
	 * print debug message to console or somewhere else.
	 * 
	 * @param debug
	 */
	public abstract void debug(String debug);
	
	/**
	 * print info message to console or somewhere else.
	 * 
	 * @param info
	 */
	public abstract void info(String info);
	
	/**
	 * print warn message to console or somewhere else.
	 * 
	 * @param warn
	 */
	public abstract void warn(String warn);
	
	/**
	 * print error message to console or somewhere else.
	 * 
	 * @param error
	 */
	public abstract void error(String error);
	
	/**
	 * @return returns if console is debugging
	 */
	boolean isDebugging() {
		return debug;
	}
	
}
