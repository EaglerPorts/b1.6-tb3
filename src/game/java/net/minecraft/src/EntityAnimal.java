package net.minecraft.src;

public abstract class EntityAnimal extends EntityCreature {
	public EntityAnimal(World var1) {
		super(var1);
	}

	protected float getBlockPathWeight(int var1, int var2, int var3) {
		return this.aH.getBlockId(var1, var2 - 1, var3) == Block.grass.blockID ? 10.0F : this.aH.getLightBrightness(var1, var2, var3) - 0.5F;
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
	}

	public boolean getCanSpawnHere() {
		int var1 = MathHelper.floor_double(this.prevPosZ);
		int var2 = MathHelper.floor_double(this.aV.minY);
		int var3 = MathHelper.floor_double(this.posY);
		return this.aH.getBlockId(var1, var2 - 1, var3) == Block.grass.blockID && this.aH.getFullBlockLightValue(var1, var2, var3) > 8 && super.getCanSpawnHere();
	}

	public int getTalkInterval() {
		return 120;
	}
}
