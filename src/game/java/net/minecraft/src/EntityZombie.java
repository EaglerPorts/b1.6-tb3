package net.minecraft.src;

public class EntityZombie extends EntityMob {
	public EntityZombie(World var1) {
		super(var1);
		this.N = "/mob/zombie.png";
		this.defaultPitch = 0.5F;
		this.attackStrength = 5;
	}

	public void onLivingUpdate() {
		if(this.aH.isDaytime()) {
			float var1 = this.getEntityBrightness(1.0F);
			if(var1 > 0.5F && this.aH.canBlockSeeTheSky(MathHelper.floor_double(this.prevPosZ), MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY)) && this.br.nextFloat() * 30.0F < (var1 - 0.4F) * 2.0F) {
				this.fireResistance = 300;
			}
		}

		super.onLivingUpdate();
	}

	protected String getLivingSound() {
		return "mob.zombie";
	}

	protected String getHurtSound() {
		return "mob.zombiehurt";
	}

	protected String getDeathSound() {
		return "mob.zombiedeath";
	}

	protected int getDropItemId() {
		return Item.feather.shiftedIndex;
	}
}
