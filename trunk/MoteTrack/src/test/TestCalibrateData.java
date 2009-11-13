package test;

import java.util.Observable;
import java.util.Observer;

import misc.DataCalibrator;
import misc.ServerLogger;
//import misc.TestObserver;
import network.ServerDataReader;

public class TestCalibrateData {
	public static void main(String[] args) {
		System.out.println("Starting client");
		DataCalibrator calib = new DataCalibrator(70);
//		calib.addObserver(new TestObserver());
		ServerDataReader reader = new ServerDataReader("localhost",5000,"020000136187");
		reader.addObserver(calib);
		reader.startReader();
	}
}
