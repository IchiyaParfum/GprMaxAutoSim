package autoSim;

/**Encapsules user-defined states of a thread process
 * 
 * @author mstieger
 *
 */
public class ThreadState {
	private String msg;
	private boolean done;
	
	public ThreadState(String msg, boolean done) {
		this.setMsg(msg);
		this.setDone(done);
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
