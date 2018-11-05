package autoSim;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GprMaxAutoSim implements ThreadListener, Constants{
	private ArrayList<Thread> inputFileCreators;
	private HashMap<String, Integer> configs;
	
	public GprMaxAutoSim(){
		configs = new HashMap<>();
		inputFileCreators = new ArrayList<>();
		
		try {
			readFilenamesFromConfig();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			createInputFileCreators();
		} catch (InvalidParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void readFilenamesFromConfig() throws IOException{
		File file = new File(workingDir + config); 
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		
		//Read filenames from file
		String st;

		while ((st = br.readLine()) != null) {
			if(st.matches("\\w+\\s+\\d+")) {
				//Setup scanner
				Scanner scanner = new Scanner(st);	
				scanner.useDelimiter(" ");
				
				//Ensure that name only exists once
				String name = scanner.next();
				if(!configs.containsKey(name)) {
					configs.put(name, scanner.nextInt());
				}
				scanner.close();
			}				
		}
		
		br.close();
	}	
	
	private void createInputFileCreators() throws InvalidParameterException, IOException{
		BufferedReader br;
		Scanner scanner;
		ArrayList<InputFileConfig> ifc = new ArrayList<>();
		
		//Read parameter files (.param) and layout files (.lay) and create input files (.in)
		for(Map.Entry<String, Integer> e: configs.entrySet()) {
			br = new BufferedReader(new FileReader(new File(workingDir + e.getKey() + ".csv")));	
	        
	        //Read parameter file as csv line by line     
			String line = null;

			while ((line = br.readLine()) != null) {
				//Read if not a comment
				if(!line.startsWith("#")) {
					//Setup scanner
					scanner = new Scanner(line);
					scanner.useDelimiter("\\s*\\,\\s*");
					
					double[] params = new double[InputFileConfig.nOfParam];
					int i = 0;
					
					while (scanner.hasNext()) {
						if(i < params.length) {
							params[i++] = scanner.nextDouble();
						}else {
							throw new InvalidParameterException();
						}
					}
					ifc.add(new InputFileConfig(e.getValue(), params));
					scanner.close();
				}		
			}
			br.close();
			
			//Dispatch thread to create input files
			Thread t = new Thread(new InputFileCreator(e.getKey(), ifc, this));
			t.setName(e.getKey());
			inputFileCreators.add(t);
			t.start();
		}
	}		
			
	
	@Override
	public void threadFinished(ThreadState state) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		new GprMaxAutoSim();
	}
}
