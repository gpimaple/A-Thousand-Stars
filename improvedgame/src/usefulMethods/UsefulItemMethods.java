package usefulMethods;

import java.awt.Image;
import java.awt.Toolkit;

import mainPackage.Item;

public class UsefulItemMethods {
	
	
	
	public static Item GlorbroxNuclearReactor()
	{
		Image sprite =  Toolkit.getDefaultToolkit().getImage("Resources/Images/ItemSprite/Generators/GlorboxNuclearReactor.png");
		Item generator = new Item(
				"Glorbrox Nuclear Reactor", 
				"An alien nuclear reactor barely held together with glue and caution tape.",
				"shield",
				10,
				sprite
				);
		generator.InitializeGenerator(
				1.0,                 //how many power points it generates each tick
				400.0                 //max amount of power
				);          
		
		return generator;
	}
	
	public static Item ZetaCorpDefenseShield()
	{
		Image sprite =  Toolkit.getDefaultToolkit().getImage("Resources/Images/ItemSprite/Shields/ZetaCorpStandardDefenseShield.png");
		Item shield = new Item(
				"ZetaCorp Defense Shield", 
				"A standard shield produced by the massive Zetacorp corporation.",
				"shield",
				10,
				sprite
				);
		shield.InitializeShield(
				0.5,            //damage multiplier
				600,            //max health
				0.005,          //regeneration
				9000,           //time to fix
				0.05);          //power consumption
		
		return shield;
	}
	
	public static Item StandardGalacticRocketryBasicThruster()
	{
		Image sprite =  Toolkit.getDefaultToolkit().getImage("Resources/Images/ItemSprite/Thrusters/StandardGalacticRocketryBasicThruster.png");
		Item thruster = new Item(
				"Standard Galactic Rocketry Basic Thruster", 
				"An old fashioned basic thruster.",
				"thruster",
				10,
				sprite
				);
		thruster.InitializeThruster(
				0.5,
				1);
		return thruster;
	}
	
	public static Item ZetaCorpAsteroidHarvester()
	{
		Image sprite =  Toolkit.getDefaultToolkit().getImage("Resources/Images/ItemSprite/Weapons/ZetaCorpAsteroidHarvester.png");
		//Weapon weapon = new Weapon("ZetaCorp Asteroid Harvester", 1, 0.0, 5, 1, 50, 1);
		Item weapon = new Item(
				"ZetaCorp Asteroid Harvester", 
				"An electricity powered weapon that is used to harvest asteroids.",
				"weapon",
				10,
				sprite
				);
		weapon.InitializeWeapon(
				"electron torpedo",//type of particle/entity created
				1.0,               //velocity
				0.1,               //spread of bullets
				5.0,               //damageboost
				1,                 //number of bullets fired at once
				50.0,              //max reload                  
				1.0,               //reload rate
				100                //power consumption per shot fired
				);
		return weapon;
	}
}
