package autoSim;

public interface Constants {
	public final static String workingDir = "/";	//Has to end with a /
	public final static String config = "GprMax.config";
	public final static String exeBScan = "python C:\\Program Files\\gprMax\\gprMax\\gprMax.py";
	public final static String exeMerge = "python C:\\Program Files\\gprMax\\tools\\outputfiles_merge.py";
	public final static String exePlot = "matlab -nodisplay -nosplash -nodesktop -noFigureWindow -wait -r \"try, "
			+ "run('C:\\Program Files\\gprMax\\tools\\MATLAB_scripts\\plot_Bscan.m'), catch me, fprintf('%s / %s\\n',me.identifier,me.message), end, exit\"";
	
}
