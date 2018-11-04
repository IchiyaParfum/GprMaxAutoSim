package autoSim;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

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
		Pattern p = Pattern.compile("\\w\\s\\W"); //Word followed by whitespace followed by non-word
		
		while ((st = br.readLine()) != null) {
			Scanner scanner = new Scanner(st);			
			if(scanner.hasNext(p)) {
				String name = scanner.next();
				//Ensure that name only exists once
				if(!configs.containsKey(name)) {
					configs.put(name, scanner.nextInt());
				}
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
			int index = 0;

			while ((line = br.readLine()) != null) {
				scanner = new Scanner(line);
				scanner.useDelimiter(",");
				double[] params = new double[InputFileConfig.nOfParam];
				int i = 0;
				
				while (scanner.hasNext()) {
					if(i < params.length) {
						params[i] = scanner.nextDouble();
					}else {
						throw new InvalidParameterException();
					}
				}
				ifc.add(new InputFileConfig(e.getValue(), params));
				scanner.close();
			}
			br.close();
			
			//Dispatch thread to create input files
			Thread t = new Thread(new InputFileCreator(e.getKey(), ifc, this));
			t.setName(e.getKey());
			inputFileCreators.add(t);
			t.start();
		}
	}		
			
	
	public void threadFinished(ThreadState state) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		new GprMaxAutoSim();
	}
}
