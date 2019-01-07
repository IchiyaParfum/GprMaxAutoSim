package autoSim;

import java.io.IOException;
import java.util.Arrays;

/**Contains the parameters for a input file
 * 
 * @see <a href="GprMax online documentation">http://docs.gprmax.com/en/latest/input.html</a> 
 * @author mstieger
 *
 */
public class TargetCreator {
	public String getTarget(String target) throws IOException {
		String s = "";
		if(target != null) {
			if(target.startsWith("#cylinder:")) {
				return target;	//No change in cylinder targets
			}
			else if(target.startsWith("#box:")) {
				String[] command = target.split(" ");
				if(command.length == 9) {
					//Valid command
					if(Double.parseDouble(command[7]) == 1) {
						//No density necessary => plain box
						command[7] = "";
						return getStringFromArray(command);
					}else {
						//Make density with cylinders
						return getDensityBox(Double.parseDouble(command[1]), Double.parseDouble(command[2]), Double.parseDouble(command[3]), 
								Double.parseDouble(command[4]), Double.parseDouble(command[5]), Double.parseDouble(command[6]), 
								Double.parseDouble(command[7]), command[8]);
					}
				}else {
					throw new IOException("Target definition invalid. Too many or not enough parameters.");
				}
			}
		
		}
		return s;
	}
	
	private String getStringFromArray(String[] array) {
		StringBuilder builder = new StringBuilder();
		for(String s : array) {
			builder.append(s);
			builder.append(" ");
		}
		return builder.toString();
	}
	
	private String getDensityBox(double x1, double y1, double z1, double x2, double y2, double z2, double density, String material) {
		
		double minSide = Math.abs(Math.min(x2-x1, y2-y1));
		double raster = minSide/16;
		double nOfCyl = 15 * (Math.abs((Math.max(x2-x1, y2-y1)/raster)) -1);
		double r = Math.sqrt(Math.abs((x2-x1)*(y2-y1)*density/(nOfCyl*Math.PI)));
		
		String box = new String();
		String ws = " ";
		for(double dx = r; dx <= x2 - r; dx += raster) {
			for(double dy = r; dy <= y2 - r; dy += raster) {
				box += "#cylinder: " + ws +
						(x1+dx) + ws +
						(y1+dy) + ws +
						z1 + ws +
						(x1+dx) + ws +
						(y1+dy) + ws +
						z2 + ws +
						r + ws +
						material + "\n";						
			}
		}	
		return box;
	}
	
	public String getGeometryView(String filename) {
		return "#geometry_view: 0 0 0 1 1 0.003 0.003 0.003 0.003 " + filename + " n";
	}
}
