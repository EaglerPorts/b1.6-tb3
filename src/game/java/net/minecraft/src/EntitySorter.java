package net.minecraft.src;

import java.util.Comparator;

public class EntitySorter implements Comparator {
	private Entity a;

	public EntitySorter(Entity var1) {
		this.a = var1;
	}

	public int sortByDistanceToEntity(WorldRenderer var1, WorldRenderer var2) {
		double var3 = (double)var1.distanceToEntitySquared(this.a);
		double var5 = (double)var2.distanceToEntitySquared(this.a);
		if(!var1.worldObj.worldProvider.field_6478_e) {
			if(var3 > 1024.0D && var1.posY < 64) {
				var3 *= 10.0D;
			}

			if(var5 > 1024.0D && var2.posY < 64) {
				var5 *= 10.0D;
			}
		}

		return var3 < var5 ? -1 : 1;
	}

	public int compare(Object var1, Object var2) {
		return this.sortByDistanceToEntity((WorldRenderer)var1, (WorldRenderer)var2);
	}
}
