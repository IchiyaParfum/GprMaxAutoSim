package autoSim;

import java.util.ArrayList;

public class InputFileCreator implements Runnable, ThreadListener{
	private ThreadListener owner;
	private ArrayList<Thread> BScanExecuters;
	private ArrayList<InputFileConfig> configs;
	
	public InputFileCreator(ThreadListener owner) {
		
	}
	
	public void threadFinished(ThreadState state) {
		// TODO Auto-generated method stub
		
	}

	public void run() {
		// TODO Auto-generated method stub		
	}

}
