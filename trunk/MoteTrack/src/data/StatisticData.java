package data;

import java.util.Iterator;
import java.util.LinkedList;

public class StatisticData {
        private final LinkedList<Position> positions;
        private Position  mean=null, stddev=null;
        private volatile boolean valid=false;
        
        public StatisticData() {
                positions = new LinkedList<Position>();
        }
        public void addPosition(Position newPos) {
                this.positions.add(newPos);
                this.valid = false;
        }
        public Position getMeans() {
                if (this.valid) return this.mean;
                
                Position means = new Position(0, 0, 0);
                Position t;
                Iterator<Position> it = positions.iterator();
                while (it.hasNext()) {
                        t = it.next();
                        means.setX(t.getX()+means.getX());
                        means.setY(t.getY()+means.getY());
                        means.setZ(t.getZ()+means.getZ());
                }
                means.setX(means.getX()/positions.size());
                means.setY(means.getY()/positions.size());
                means.setZ(means.getZ()/positions.size());
                
                this.mean = means;
                return means;
        }
        public Position getStdDeviation() {
                if (this.valid) return this.stddev;
                
                Position means = this.getMeans();
                Position stddev = new Position(0,0,0);
                Position t;
                Iterator<Position> it = positions.iterator();
                while (it.hasNext()) {
                        t = it.next();
                        stddev.setX(stddev.getX()+Math.pow(t.getX()-means.getX(),2));
                        stddev.setY(stddev.getY()+Math.pow(t.getY()-means.getY(),2));
                        stddev.setZ(stddev.getZ()+Math.pow(t.getZ()-means.getZ(),2));
                }
                stddev.setX(Math.sqrt(stddev.getX()/(positions.size()-1)));
                stddev.setY(Math.sqrt(stddev.getY()/(positions.size()-1)));
                stddev.setZ(Math.sqrt(stddev.getZ()/(positions.size()-1)));
                this.stddev = stddev;
                this.valid = true;
                return stddev;
        }
        public int getCount() {
                return this.positions.size();
        }
        public String toString() {
        	return this.getStdDeviation().toString();
        }
}
