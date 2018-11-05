package autoSim;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**Because some native platforms only provide limited buffer size for standard input and output streams,
 * failure to promptly write the input stream or read the output stream of the subprocess may cause 
 * the subprocess to block, and even deadlock. This class prevents that from happen by emptying the streams
 * consecutively.
 * 
 * @author mstieger
 *
 */
class StreamExhauster extends Thread
{
    InputStream is;
    char[] buffer;
    boolean running;
    
    StreamExhauster(InputStream is, int bufferSize)
    {
        this.is = is;
        buffer = new char[bufferSize];
        running = false;
    }
    
    @Override
    public void run()
    {
    	running = true;
    	
    	//Reads from stream
		InputStreamReader isr = new InputStreamReader(is);
    	try {
            while(running){
            	isr.read(buffer);
            }
    	}catch(IOException e) {
    		
    	}finally {
    		try {
				isr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}          
                     
    }
    
    public void finish() {
    	running = false;
    }
}

