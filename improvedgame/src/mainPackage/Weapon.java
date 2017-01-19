package mainPackage;
//What weapon the entity has. Every entry will have a name, velocity, spread, damage boost, amount fired at once, how long to reload, and current reload
		
public class Weapon {
	public String Name; 
	public double Velocity; // muzzle velocity
	public double Spread; // the spread in radians
	public double DamageBoost;// the damage multiplier
	public int NumberAtOnce;//bullets fired at once
	public int MaxReload;//Max
	public int CurrentReload;
	public int Regeneration;
	public Weapon(String name, double velocity, double spread, double damageboost, int numberatonce, int maxreload, int regeneration)
	{
		Name = name;
		Velocity = velocity;
		Spread = spread;
		DamageBoost = damageboost;
		NumberAtOnce = numberatonce;
		MaxReload = maxreload;
		CurrentReload = maxreload;
		Regeneration = regeneration;
	}
}
