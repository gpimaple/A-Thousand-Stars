package mainPackage;

public class Shield {
			public String Name;
			public double DamageMultiplier;// the amount the shield will multiply damage taken by. O.7 is 70%. values grater than one actually increase damage 
			public double MaxHitpoints;//the max amount of health a shield will have
			public double CurrentHitpoints;//the current amount of health a shield has 
			public double Regeneration;//points regenerated each tick
			public boolean Failed = false;
			public int SetupTime;//how long shields take to fix
			public int countdown;
			public Shield(String name, double damagemultiplier, double maxhitpoints, double regeneration, int setuptime)
			{
				Name = name;
				DamageMultiplier = damagemultiplier;
				MaxHitpoints = maxhitpoints;
				CurrentHitpoints = maxhitpoints;
				Regeneration = regeneration;
				SetupTime = setuptime;
				countdown = setuptime;
				
			}
		}