package test;

import data.Position;
import network.NormalizedServerDataReader;
import network.ServerDataReader;

public class TestNormalizationReader {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//new Position(0.002612373011961519, 0.010537201740807226, 0.010896639502163345)
		System.out.println("Starting client");
		NormalizedServerDataReader obs = new NormalizedServerDataReader(new Position(0.15,0.15,0.15),1);
		ServerDataReader reader = new ServerDataReader("localhost",5000);//,"020000136187");
		reader.addObserver(obs);
		reader.startReader();
	}

}
