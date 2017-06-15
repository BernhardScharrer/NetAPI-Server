package networking.channels;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Packet implements Serializable {

	private static final long serialVersionUID = -691405110077995221L;
	
	private String name;
	private Map<String, Object> entries;
	
	public Packet(String name) {
		this.name = name;
		this.entries = new HashMap<>();
	}
	
    protected void addEntry(String name, Object value) {
        entries.put(name, value);
    }

    public Object getEntry(String name) {
        return entries.get(name);
    }
	
	public String getName() {
		return name;
	}
	
	
}
