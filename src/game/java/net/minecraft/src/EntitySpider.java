package net.minecraft.src;

public class EntitySpider extends EntityMob {
	public EntitySpider(World var1) {
		super(var1);
		this.N = "/mob/spider.png";
		this.setSize(1.4F, 0.9F);
		this.defaultPitch = 0.8F;
	}

	public double getMountedYOffset() {
		return (double)this.width * 0.75D - 0.5D;
	}

	protected boolean canTriggerWalking() {
		return false;
	}

	protected Entity findPlayerToAttack() {
		float var1 = this.getEntityBrightness(1.0F);
		if(var1 < 0.5F) {
			double var2 = 16.0D;
			return this.aH.getClosestPlayerToEntity(this, var2);
		} else {
			return null;
		}
	}

	protected String getLivingSound() {
		return "mob.spider";
	}

	protected String getHurtSound() {
		return "mob.spider";
	}

	protected String getDeathSound() {
		return "mob.spiderdeath";
	}

	protected void attackEntity(Entity var1, float var2) {
		float var3 = this.getEntityBrightness(1.0F);
		if(var3 > 0.5F && this.br.nextInt(100) == 0) {
			this.playerToAttack = null;
		} else {
			if(var2 > 2.0F && var2 < 6.0F && this.br.nextInt(10) == 0) {
				if(this.aW) {
					double var4 = var1.prevPosZ - this.prevPosZ;
					double var6 = var1.posY - this.posY;
					float var8 = MathHelper.sqrt_double(var4 * var4 + var6 * var6);
					this.posZ = var4 / (double)var8 * 0.5D * (double)0.8F + this.posZ * (double)0.2F;
					this.motionY = var6 / (double)var8 * 0.5D * (double)0.8F + this.motionY * (double)0.2F;
					this.motionX = (double)0.4F;
				}
			} else {
				super.attackEntity(var1, var2);
			}

		}
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
	}

	protected int getDropItemId() {
		return Item.silk.shiftedIndex;
	}

	public boolean isOnLadder() {
		return this.onGround;
	}
}
