package mainPackage;
//What Thruster the entity has. Every entry will have a name and acceleration

public class Thruster {
	public String Name;
	public double Acceleration;
	public Thruster(String name, double acceleration)
	{
		Name = name; 
		Acceleration = acceleration;
	}
}
