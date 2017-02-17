package mainPackage;

public class Entity 
{
	public String Type; // has no effect. For identification in other
	public String Info; // info that is viewable
	public double MaxHealth;//maximum health level
	public double CurrentHealth;//current health level
	public double HealthRegen;//health regained each tick
	public double EntityDamageSelfOnContact = 0;
	public double EntityDamageOnContact;
	public Item[] Inventory;//the inventory of the entity.
	public Item   Generator = null;
	public Item   Shield = null;
	public Item   Weapon = null;
	public Item   Thruster = null;
	public Object Miscellaneous = null;
	public Particle Host;
	//TODO need to add entity tags 
	// these tags tell what to do when damaged, dead, or born. 
	int wealth;
	public Entity(Particle host, String type, String info,//basic info 
			Item[] inventory,//the inventory
			double maxhealth, double healthregen, //health
			double entitydamageoncontact)//contactbehavior
	{
		Host = host;
		Type = type;
		Info = info;
		
		Inventory = inventory;
		
		//get generator, Shield, Weapon, thruster
		for(int i = 0; i < Inventory.length; i++)
		{
			if(Inventory[i].IsGenerator == true)
			{
				Generator = Inventory[i];
			}
			if(Inventory[i].IsShield == true)
			{
				Shield = Inventory[i];
			}
			if(Inventory[i].IsWeapon == true)
			{
				Weapon = Inventory[i];
			}
			if(Inventory[i].IsThruster == true)
			{
				Thruster = Inventory[i];
			}
		}
		
		
		MaxHealth = maxhealth;
		CurrentHealth = maxhealth;//defaults to full health. Can be modified if this is not needed
		HealthRegen = healthregen;
		EntityDamageOnContact = entitydamageoncontact;
	}



}

