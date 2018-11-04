package autoSim;

import java.io.File;
import java.io.IOException;

public class BScanExecuter implements Runnable, Constants{
	private ThreadListener owner;
	private File dir;
	private String filename;
	private int iterations;
	
	/**
	 * @param dir directory in which input file with filename lies
	 * @param filename filename of the input file
	 * @param iterations iterations to run bscan
	 * @param owner owns this thread
	 */
	public BScanExecuter(File dir, String filename, int iterations, ThreadListener owner) {
		this.dir = dir;
		this.filename = filename;
		this.iterations = iterations;
		this.owner = owner;
	}
	
	public void run() {
		try {
			//Try to execute BScan
			Runtime.getRuntime().exec(exeBScan + dir.getPath() + filename + ".in" + " -n " + iterations);
			//Try to merge output files from bscan
			Runtime.getRuntime().exec(exeMerge + dir.getPath() + filename + ".out");
		} catch (IOException e) {
			//Tell owner that operation failed
			owner.threadFinished(new ThreadState(e.getMessage(), false));
			e.printStackTrace();
		}
		owner.threadFinished(new ThreadState("", true));	//Tell owner that operation succeded
	}
	
}
