package autoSim;

public interface Constants {
	public final static String appDir = System.getProperty("user.dir");
	public final static String simDir = "sim/";	//Has to end with a slash /
	public final static String scriptDir = "scripts/"; //Has to end with a slash /
	public final static String configFile = "GprMax.config";
	public final static String exeBScan = "C:\\Users\\mstieger\\Anaconda3\\envs\\gprMax\\python.exe -m gprMax";
	public final static String exeMerge = "C:\\Users\\mstieger\\Anaconda3\\envs\\gprMax\\python.exe -m "
			+ "tools.outputfiles_merge";
	public final static String exePlot = "matlab -nodisplay -nosplash -nodesktop -noFigureWindow -wait -r";
}
