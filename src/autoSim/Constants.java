package autoSim;

public interface Constants {
	public final static String workingDir = "sim/";	//Has to end with a /
	public final static String config = "GprMax.config";
	public final static String exeBScan = "C:\\Users\\mstieger\\Anaconda3\\envs\\gprMax\\python.exe -m gprMax";
	public final static String exeMerge = "C:\\Users\\mstieger\\Anaconda3\\envs\\gprMax\\python.exe -m "
			+ "tools.outputfiles_merge";
	public final static String exePlot = "matlab -nodisplay -nosplash -nodesktop -noFigureWindow -wait -r \"try, "
			+ "run('C:\\Program Files\\gprMax\\tools\\MATLAB_scripts\\plot_Bscan.m'), catch me, fprintf('%s / %s\\n',me.identifier,me.message), end, exit\"";
	
}
