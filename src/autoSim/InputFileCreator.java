package autoSim;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class InputFileCreator extends Thread implements ThreadListener, Constants{
	private ThreadListener owner;
	private ArrayList<Thread> bScanExecuters;
	private String filename;
	
	public InputFileCreator(String filename, ThreadListener owner) {
		super("InputFileCreator: " + filename);
		this.owner = owner;
		bScanExecuters = new ArrayList<>();
		this.filename = filename;
	}
	
	@Override
	public synchronized void threadFinished(ThreadState state) {
		//synchronized => To ensure thread safety during access on thread array
		//TODO
	}

	@Override
	public void run() {
		//Create new directory to store files in (<filename>_yyyy-MM-dd_HH-mm-ss)
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		File dir = new File(AppProperties.simDir + "\\" + filename + "_" + sdf.format(new Date()));	
		
		if(!dir.exists() && dir.mkdir()) {
			try {
				int iterations = 0;
				String layout = new String();
				String material = new String();
				
				//Read general settings	
				XmlReader2 xml = new XmlReader2(new File(AppProperties.simDir + "\\" + filename + ".sim"));		
				NodeList nodes = xml.getNodeList("iterations");
				System.out.println(nodes.getLength());
				if(nodes != null) {
					iterations = Integer.parseInt(nodes.item(0).getTextContent());
				}
				nodes = xml.getNodeList("layout");
				if(nodes != null) {
					layout = nodes.item(0).getTextContent();
				}
				nodes = xml.getNodeList("material");
				if(nodes != null) {
					material = nodes.item(0).getTextContent();
				}
				File layFile = new File(AppProperties.tempDir + "\\" + layout);
				if(!layFile.exists()) {
					throw new IOException("File does not exist: " + layFile.getAbsolutePath());
				}
				File matfile = new File(AppProperties.tempDir + "\\" + material);
				if(!matfile.exists()) {
					throw new IOException("File does not exist: " + matfile.getAbsolutePath());
				}
				
				//Read targets
				nodes = xml.getNodeList("run");
				if(nodes != null) {
					TargetCreator tar = new TargetCreator();
					for(int i = 0; i < nodes.getLength(); i++) {
						//Create input file
						String filenameMod = filename + i + "SEQ";				
						CopyFile cf = new CopyFile();
						cf.copy(layFile, new File(dir.getPath() + "/" + filenameMod + ".in"));
						cf.copy(matfile, new File(dir.getPath() + "/" + filenameMod + ".in"));
												
						//Write targets
						Element e = (Element) nodes.item(i);
						NodeList tars = e.getElementsByTagName("tar");
						PrintWriter pwr = new PrintWriter(new BufferedWriter(new FileWriter(dir.getPath() + "/" + filenameMod + ".in", true)));
						for(int j = 0; j < tars.getLength(); j++) {
							pwr.println(tar.getTarget(tars.item(j).getTextContent()) + "\n");
						}
						pwr.flush();
						pwr.close();
				        
				        //Execute BScan
				        Thread t = new BScanExecuter(filenameMod, dir, iterations, this);
				        bScanExecuters.add(t);
				        t.start();
					}
				}
				
			}catch(IOException e) {
				owner.threadFinished(new ThreadState(e.getMessage(), false));
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
		}
	}

}
