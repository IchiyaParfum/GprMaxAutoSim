package autoSim;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CopyFile{

	public void copy(File src, File dest) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(src));
			PrintWriter pwr = new PrintWriter(new BufferedWriter(new FileWriter(dest, true)));
			String line;
			while((line = br.readLine()) != null) {
				pwr.println(line);
			}
			br.close();
			pwr.flush();
			pwr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
