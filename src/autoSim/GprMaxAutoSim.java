package autoSim;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GprMaxAutoSim implements ThreadListener{
	private ArrayList<Thread> inputFileCreators;
	
	public GprMaxAutoSim(){
		
		inputFileCreators = new ArrayList<>();	
		try {
			deployInputFileCreators();
		} catch (IOException | ParserConfigurationException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void deployInputFileCreators() throws IOException, ParserConfigurationException, SAXException{
		File f = new File(AppProperties.runFile);
		if(f.exists()) {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String filename;
			//Read simulations
			while((filename = br.readLine()) != null) {
				if(!inputFileCreators.contains(filename)) {
					//Dispatch thread to create input files
					Thread t = new InputFileCreator(filename, this);				
					inputFileCreators.add(t);
					t.start();
				}
			}
		}else {
			throw new IOException("There has to be a config file at location: " + f.getAbsolutePath());
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
