package network;

import java.util.HashMap;

import data.SensorData;

public class ServerDataReader extends ServerReader {
	String ids;
	HashMap<String, Object> map;
	
	public ServerDataReader(String srv, int port, String ids) {
		super(srv, port);
		String[] splitIds = ids.split(";");
		map = new HashMap<String, Object>();
		for (int i=0;i<splitIds.length;i++) {
			map.put(splitIds[i], " ");
		}
	}

	protected void updateObserver(String msg) {
		String[] sMsg = msg.split(" ");
		if (!map.containsKey(sMsg[0])) return;
		System.out.println(msg);
		SensorData data = new SensorData(sMsg[0],Long.parseLong(sMsg[1]),Double.parseDouble(sMsg[2]),Double.parseDouble(sMsg[3]),Double.parseDouble(sMsg[4]));
		this.setChanged();
		this.notifyObservers(data);
	}
}
