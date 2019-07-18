package artificialIntelligence;

import mainPackage.*;
import usefulMethods.UsefulParticleMethods;
import usefulMethods.UsefulSoundImageMethods;

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
		if(Main.PlayerIndex != -1)
		{
			Entity playerentity = Main.EntityMap.get(Main.ParticleList.get(Main.PlayerIndex));
			double distance = UsefulParticleMethods.GetDistance(playerentity.Host, Host.Host);

			if(distance < 20000)
			{
				Item[] inventory = playerentity.Inventory;


				int selectedanswer = UsefulSoundImageMethods.DrawPopup("Pickup Item Confirmation", 
						new String[] {"Pickup Item: " + Itemheld.Name + "?"}, 
						new String[] {"Yes", "No"});
				if(selectedanswer == 0)
				{
					for(int i = 0; i < inventory.length; i++)
					{
						if(inventory[i] == null)
						{
							inventory[i] = Itemheld;
							Host.CurrentHealth = -50000;
							break;
						}
					}
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
