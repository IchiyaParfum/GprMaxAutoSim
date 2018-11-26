package autoSim;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BScanExecuter extends Thread implements Constants{
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
		super("BScan Executer: " + filename);
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
		 	int exitCode = proc.waitFor();
		 	in.finish();
		 	err.finish();
		 	if(exitCode != 0) {
		 		return;
		 	}
		 		 	
			//Try to merge BScans
		 	command = exeMerge + " " + dir.getAbsolutePath() + "\\" + filename + " --remove";
		 	proc = Runtime.getRuntime().exec(command);
		 	in = new StreamExhauster(proc.getInputStream(), 256);
		 	err = new StreamExhauster(proc.getErrorStream(), 256);
		 	new Thread(in).start();
		 	new Thread(err).start();
		 	exitCode = proc.waitFor();
		 	in.finish();
		 	err.finish();
		 	if(exitCode != 0) {
		 		return;
		 	}
		 	
		 	/*Try to create figure
		 	 * Because matlab interpreter does not find function even if the process' directory is set to
		 	 * the correct path, a batch file is created which first changes the path to the script path 
		 	 * and then executes the matlab script
		 	 */
		 	
		 	File batch = new File(dir.getAbsolutePath() + "\\" + filename + ".bat");
		 	command = exePlot + " plot_Bscan('" + dir.getAbsolutePath() + "\\" + filename + "_merged.out')";
			writeBatchFile(batch, command);
			command = batch.getAbsolutePath();
		 	ProcessBuilder pb = new ProcessBuilder(command);
		 	proc = pb.start();
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
	
	private void writeBatchFile(File file, String command) throws IOException {
		if(command != null) {
			BufferedWriter wr = new BufferedWriter(new FileWriter(file));
			//Change to correct drive
			String [] token = file.getAbsolutePath().split("\\\\");
			if(token != null) {
				wr.write(token[0] + "\n");
				
				//Change directory to script location
				wr.write("cd " + appDir + "\\scripts\n"); //TODO Get working dir
			
				//Write command
				wr.write(command);
				wr.flush();
				wr.close();
			}
		}		
	}
}
