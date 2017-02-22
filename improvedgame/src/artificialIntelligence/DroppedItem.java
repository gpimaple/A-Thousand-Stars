package artificialIntelligence;

import mainPackage.*;
import usefulMethods.UsefulParticleMethods;

public class DroppedItem implements AI {
	
	public Item Itemheld;
	public Entity Host;
	
	public DroppedItem(Item item, Entity host)
	{
		Itemheld = item;
		Host = host;
	}
	
	@Override//will give the player the item represented
	public void onClick() {
		
		Entity playerentity = Main.EntityMap.get(Main.ParticleList.get(Main.PlayerIndex));
		
		double distance = UsefulParticleMethods.GetDistance(playerentity.Host, Host.Host);
		
		System.out.println("hi");
		if(distance < 200)
		{
			Item[] inventory = playerentity.Inventory;
			
			for(int i = 0; i < inventory.length; i++)
			{
				if(inventory[i] == null)
				{
					inventory[i] = Itemheld;
					Host.CurrentHealth = -50000;
				}
			}
		}
		
	}

	@Override
	public void onTick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDamagedbyEntity(Entity damager) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeath() {
		// TODO Auto-generated method stub
		
	}
	
}
