package misc;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import data.Position;

public class MoteTrackConfiguration {
	private Configurator config;
	public MoteTrackConfiguration(String fileName) {
		config = new Configurator(fileName);
	}
	public void loadConfiguration() {
		try {
			config.loadConfiguration();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public void saveConfiguration() {
		try {
			config.saveConfiguration();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Position getCalibrationData() {
		String confVal=config.get("calibration");
		if (confVal==null) return new Position(0,0,0);
		String[] split = confVal.split(";");
		return new Position(Double.parseDouble(split[0]),Double.parseDouble(split[1]),Double.parseDouble(split[2]));
	}
	public void setCalibrationData(Position calib) {
		config.put("calibration",calib.getX()+";"+calib.getY()+";"+calib.getZ());
	}
	public void setConnectedTags(HashMap<String, ArrayList<String>> list) {
		String whole="", key;
		Iterator<String> itMap = list.keySet().iterator();
		Iterator<String> itArr;
		while (itMap.hasNext()) {
			key = itMap.next();
			itArr=list.get(key).iterator();
			whole+=";"+key;
			while(itArr.hasNext()) {
				whole+=","+itArr.next();
			}
		}
		config.put("connections", whole.substring(1));
	}
	public HashMap<String, ArrayList<String>> getConnectedTags() {
		HashMap<String, ArrayList<String>> list = new HashMap<String, ArrayList<String>>();
		ArrayList<String> arr;
		String[] split;
		try {
			split = config.get("connections").split(";");
		} catch (Exception e) {
			return list;
		}
		String[] line;
		for (int i=0;i<split.length;i++) {
			line = split[i].split(",");
			arr = new ArrayList<String>();
			for (int j=1;j<line.length;j++) {
				arr.add(line[j]);
			}
			list.put(line[0], arr);
		}
		return list;
	}
	public String getServer() {
            String server = config.get("server");
            if (server != null) {
                server = server.replace('@', ':');
            } else { server="localhost:5000"; }
            return server;
	}
	public void setServer(String server) {
		config.put("server", server.replace(':', '@'));
	}
	public String[] getTagFilter() {
		try {
			return config.get("filter").split(";");
		} catch (Exception e){
			return null;
		}
	}
	public void setTagFilter(String[] tags) {
		String whole="";
		for (int i=0;i<tags.length;i++) {
			whole+=";"+tags[i];
		}
		config.put("filter", whole.substring(1));
	}

        // REPALY FILE
	public String getReplayFile() {
		return config.get("replayFile");
	}
	public void setReplayFile(String replayFile) {
		config.put("replayFile", replayFile);
	}


        // REPLAY RATE
	public int getReplayRate() {
		try {
			return Integer.parseInt(config.get("replayRate"));
		} catch (Exception e) {
			return 100;
		}
	}
	public void setReplayRate(int rate) {
		config.put("replayRate", rate+"");
	}


        // REPLAY PORT
	public int getReplayPort() {
		try {
			return Integer.parseInt(config.get("replayPort"));
		} catch (Exception  e) {
			return 5000;
		}
	}
	public void setReplayPort(int port) {
		config.put("replayPort", port+"");
	}


        // IS NORMALIZED
	public boolean isNormalized() {
            String conf = config.get("normalized");
            boolean normalized = false;
            if (conf != null) {
                normalized = conf.equalsIgnoreCase("true");
            }
            return normalized;
	}
	public void setNormalized(boolean b) {
		config.put("normalized", String.valueOf(b));
	}


        // MAX HISTORY READINGS
	public int getHistoryReadings() {
		try {
			return Integer.parseInt(config.get("maxHistory"));
		} catch(Exception e) {
			return 20;
		}
	}
	public void setHistoryReadings(int readings) {
		config.put("maxHistory", readings+"");
	}


        // DRAW HISTORY
        public boolean drawHistory() {
            String conf = config.get("history");
            boolean history = false;
            if (conf != null) {
                history = conf.equalsIgnoreCase("true");
            }
            return history;
        }

        public void setDrawhistory(boolean drawHistory) {
            config.put("history", String.valueOf(drawHistory));
        }


        // MAX VELOCITY
        public int getMaxVelocity() {
            try {
            	return Integer.parseInt(config.get("maxVelocity"));
            } catch(Exception e) {
            	return 10;
            }
        }

        public void setMaxVelocity(int maxVelocity) {
            config.put("maxVelocity", String.valueOf(maxVelocity));
        }


        // VELOCITY OBSERVER
        public boolean velocityObs() {
            String conf = config.get("velocityObs");
            boolean velocityObs = false;
            if (conf != null) {
                velocityObs = conf.equalsIgnoreCase("true");
            }
            return velocityObs;
        }

        public void setVelocityObs(boolean enabled) {
            config.put("velocityObs", String.valueOf(enabled));
        }


        // LOG DATA
        public boolean log() {
            String conf = config.get("log");
            boolean log = false;
            if (conf != null) {
                log = conf.equalsIgnoreCase("true");
            }
            return log;
        }

        public void setLog(boolean enabled) {
            config.put("log", String.valueOf(enabled));
        }


        // LOG FILE
	public String getLogFile() {
		return config.get("logFile");
	}
	public void setLogFile(String logFile) {
		config.put("logFile", logFile);
	}


        // LOG PATH
	public String getlogPath() {
		return config.get("logPath");
	}
	public void setlogPath(String logPath) {
		config.put("logPath", logPath);
	}


        // autoStartServer
        public boolean autoStartServer() {
            String conf = config.get("autoStartServer");
            boolean autoStartServer = false;
            if (conf != null) {
                autoStartServer = conf.equalsIgnoreCase("true");
            }
            return autoStartServer;
        }

        public void autoStartServer(boolean enabled) {
            config.put("autoStartServer", String.valueOf(enabled));
        }
        
        public void setPatternFiles(String[] fileNames) {
        	String conf ="";
        	for (int i=0;i<fileNames.length;i++) {
        		conf+=";"+fileNames[i];
        	}
        	config.put("patternFiles", conf.substring(1));
        }
        public String[] getPatternFiles() {
        	try {
        		return config.get("patternFiles").split(";");
        	} catch (Exception e) {
        		return new String[0];
        	}
        }
}