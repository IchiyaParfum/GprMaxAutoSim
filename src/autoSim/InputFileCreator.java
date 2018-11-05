package autoSim;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InputFileCreator implements Runnable, ThreadListener, Constants{
	private ThreadListener owner;
	private ArrayList<Thread> bScanExecuters;
	private ArrayList<InputFileConfig> configs;
	private String filename;
	
	public InputFileCreator(String filename, ArrayList<InputFileConfig> configs, ThreadListener owner) {
		this.owner = owner;
		bScanExecuters = new ArrayList<>();
		this.configs = configs;
		this.filename = filename;
	}
	
	@Override
	public synchronized void threadFinished(ThreadState state) {
		//synchronized => To ensure thread safety during access on thread array
		//TODO
	}

	@Override
	public void run() {
		//Create new directory to store files in (<filename>_yyyy-MM-dd_HH-mm)
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
		File dir = new File(workingDir + filename + "_" + sdf.format(new Date()));	
		
		if(dir.mkdir()) {
			try {
				//Create input files for each config
				int seq = 0;
				
				for(InputFileConfig c : configs) {	
					String genFileName = filename + "_" + seq++ +"SEQ";
					BufferedReader br = new BufferedReader(new FileReader(workingDir + filename + ".templ"));
					BufferedWriter wr = new BufferedWriter(new FileWriter(dir.getPath() + "/" + genFileName + ".in"));
			        
					//Copy template		
			        String line;
			        while((line = br.readLine()) != null) {
			        	wr.write(line);
			        	wr.write("\n");
			        }
					
				    //Append config
				    wr.write(c.toString());
			        
			        //Close streams
			        br.close();
			        wr.flush();
			        wr.close();
			        
			        //Execute BScan
			        Thread t = new Thread(new BScanExecuter(dir, genFileName, c.getIterations(), this));
			        t.setName(genFileName);
			        bScanExecuters.add(t);
			        t.start();			       			        
				}
				owner.threadFinished(new ThreadState("", true));
				
			}catch(IOException e) {
				owner.threadFinished(new ThreadState(e.getMessage(), false));
				e.printStackTrace();
			}
						
		}
	}

}
