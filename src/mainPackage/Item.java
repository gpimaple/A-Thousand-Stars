package mainPackage;

import java.awt.Image;

public class Item {
	public String Name = null;
	public String Description = null;
	public int Value;
	public String Type = null;
	public Image Sprite = null;
	
	//generator
	//generators generate power for the ship. They are needed by weapons, thrusters, and shields
	public boolean IsGenerator = false;
	public double PowerGenerated;//the points of power generated each tick
	public double CurrentPower;//the current amount of power stored
	public double MaxPower;//the max amount of power stored by the generator
	
	//shield
	//shields absorb some percentage of the damage. When the shields' own health drops below zero, they fail for a period of time
	public boolean IsShield = false;
	public double DamageMultiplier;//the number the damage is multiplied by. Lower is better
	public double MaxHitpoints;//the max amount of health the shield can have
	public double CurrentHitpoints;//the current amount of health the shield has
	public double RegenPerTick; //the amount of regen per tick
	public boolean Failed = false; //whether the shield has failed
	public double FixTime;//the amount of time that the shield will fail. Influenced by regen
	public double CurrentFix; //the current fix level
	public double PowerConsumptionPerRegenTick;//when regenerating health, this is the amount of power it will draw per tick
	
	//Thruster
	//thrusters propel the ship, drawing power
	public boolean IsThruster = false;
	public double Acceleration;//the acceleration, affected by mass
	public double PowerConsumptionPerThrust;
	
	//Weapon
	//weapons allow the ship to fire at enemies or asteroids
	public boolean IsWeapon = false;
	public String EntityFired;//the actual object launched
	public double Velocity; // the velocity of the entity
	public double Spread; // the spread in radians
	public double DamageBoost;// the damage multiplier
	public int    NumberFiredAtOnce;//bullets fired at once
	public double MaxWeaponReload;//Max reload level
	public double CurrentWeaponReload;//the current reload
	public double WeaponRegeneration; //how fast it reloads
	public double PowerConsumptionWhenFired;//how much energy is consumed
	
	public void InitializeGenerator(double powergen, double max)
	{
		IsGenerator = true;
		PowerGenerated = powergen;
		MaxPower = max;
		CurrentPower = max;
	}
	
	public void InitializeShield(double dammult, double max, double regen, double fixtime, double powersump)
	{
		IsShield = true;
		DamageMultiplier = dammult;
		MaxHitpoints = max;
		CurrentHitpoints = max;
		RegenPerTick = regen;
		FixTime = fixtime;
		CurrentFix = 0;
		PowerConsumptionPerRegenTick = powersump;
		
	}
	
	public void InitializeThruster(double accel, double powersump)
	{
		IsThruster = true;
		Acceleration = accel;
		PowerConsumptionPerThrust = powersump;
	}
	
	public void InitializeWeapon(String type, double vel, double spread, double boost, int atonce, double maxrel, double regen, double powersump)
	{
		IsWeapon = true;
		EntityFired = type;
		Velocity = vel;
		Spread = spread;
		DamageBoost = boost;
		NumberFiredAtOnce= atonce;
		MaxWeaponReload = maxrel;
		CurrentWeaponReload = maxrel;
		WeaponRegeneration = regen;
		PowerConsumptionWhenFired = powersump;
	}
	public Item(String name, String description, String type, int value, Image sprite)
	{
		Name = name;
		Description = description;
		Value = value;
		Type = type;
		Sprite = sprite;
	}
}
