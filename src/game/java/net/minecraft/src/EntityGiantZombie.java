package net.minecraft.src;

public class EntityGiantZombie extends EntityMob {
	public EntityGiantZombie(World var1) {
		super(var1);
		this.N = "/mob/zombie.png";
		this.defaultPitch = 0.5F;
		this.attackStrength = 50;
		this.X *= 10;
		this.be *= 6.0F;
		this.setSize(this.yOffset * 6.0F, this.width * 6.0F);
	}

	protected float getBlockPathWeight(int var1, int var2, int var3) {
		return this.aH.getLightBrightness(var1, var2, var3) - 0.5F;
	}
}
