package net.minecraft.src;

public class BiomeGenHell extends BiomeGenBase {
	public BiomeGenHell() {
		this.spawnableMonsterList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableWaterCreatureList.clear();
		this.spawnableMonsterList.add(new SpawnListEntry(EntityGhast::new, 10));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityPigZombie::new, 10));
	}
}
