package autoSim;

import java.io.File;

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
	
	@Override
	public void run() {
		 try{			 
			 //Try to execute BScan
			 String command = exeBScan + " " + dir.getAbsolutePath() + "\\" + filename + ".in" + " -n " + iterations;
		 	Process proc = Runtime.getRuntime().exec(command);
		 	StreamExhauster in = new StreamExhauster(proc.getInputStream(), 256);
		 	StreamExhauster err = new StreamExhauster(proc.getErrorStream(), 256);
		 	new Thread(in).start();
		 	new Thread(err).start();
		 	proc.waitFor();
		 	in.finish();
		 	err.finish();
		 	
			//Try to merge BScans
		 	command = exeMerge + " " + dir.getAbsolutePath() + "\\" + filename + " --remove";
		 	proc = Runtime.getRuntime().exec(command);
		 	in = new StreamExhauster(proc.getInputStream(), 256);
		 	err = new StreamExhauster(proc.getErrorStream(), 256);
		 	new Thread(in).start();
		 	new Thread(err).start();
		 	proc.waitFor();
		 	in.finish();
		 	err.finish();
		 	
		 	owner.threadFinished(new ThreadState("", true));	//Tell owner that operation succeded
	        } catch (Throwable t){
	        	owner.threadFinished(new ThreadState(t.getMessage(), false));	//Tell owner that operation failed
	            t.printStackTrace();
	        }		
	}
	
}
