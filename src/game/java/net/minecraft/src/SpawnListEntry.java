package net.minecraft.src;

import net.peyton.eagler.minecraft.suppliers.EntitySupplier;

public class SpawnListEntry {
	public EntitySupplier entityClass;
	public int spawnRarityRate;

	public SpawnListEntry(EntitySupplier var1, int var2) {
		this.entityClass = var1;
		this.spawnRarityRate = var2;
	}
}
