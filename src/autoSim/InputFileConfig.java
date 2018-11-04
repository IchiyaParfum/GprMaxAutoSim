package autoSim;

/**Contains the parameters for a input file
 * 
 * @see <a href="GprMax online documentation">http://docs.gprmax.com/en/latest/input.html</a> 
 * @author mstieger
 *
 */
public class InputFileConfig {
	public final static int nOfParam = 11;	//4 for material, 7 for cylinder
	private final String ws = " ";
	private int iterations;
	private double[] parameters;
	
	public InputFileConfig(int iterations, double[] parameters) throws InvalidParameterException{
		if(parameters == null || parameters.length != 11) {
			throw new InvalidParameterException();
		}
		this.iterations = iterations;
		this.parameters = parameters;
	}
	
	/**Puts the parameters together to a valid material command
	 * 
	 * Syntax: #material: f1 f2 f3 f4 str1
	 * f1 is the relative permittivity, epsylon-r
	 * f2 is the conductivity (Siemens/metre), sigma
	 * f3 is the relative permeability, micro-r
	 * f4 is the magnetic loss (Ohms/metre), sigma*
	 * str1 is an identifier for the material.
	 */
	public String getMaterial() {
		return "#material: " + 
				parameters[0] + ws +
				parameters[1] + ws +
				parameters[2] + ws +
				parameters[3] + ws +
				"myCyl";
	};
	
	/**Puts the parameters together to a valid cylinder command
	 * 
	 * #cylinder: f1 f2 f3 f4 f5 f6 f7 str1 [c1]
	 * f1 f2 f3 are the coordinates (x,y,z) of the centre of one face of the cylinder, and f4 f5 f6 are the coordinates (x,y,z) of the centre of the other face.
	 * f7 is the radius of the cylinder.
	 * str1 is a material identifier that must correspond to material that has already been defined in the input file, or is one of the builtin materials pec or free_space.
	 */
	public String getCylinder() {
		return "#cylinder: " + 
				parameters[4] + ws +
				parameters[5] + ws +
				parameters[6] + ws +
				parameters[7] + ws +
				parameters[8] + ws +
				parameters[9] + ws +
				parameters[10] + ws +
				"myCyl";
		
	};
	
	@Override
	public String toString() {
		return (getMaterial() + "\n" + getCylinder());
	}
}
