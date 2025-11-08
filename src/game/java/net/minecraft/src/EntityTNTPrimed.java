package net.minecraft.src;

public class EntityTNTPrimed extends Entity {
	public int fuse;

	public EntityTNTPrimed(World var1) {
		super(var1);
		this.fuse = 0;
		this.aE = true;
		this.setSize(0.98F, 0.98F);
		this.be = this.width / 2.0F;
	}

	public EntityTNTPrimed(World var1, double var2, double var4, double var6) {
		this(var1);
		this.setPosition(var2, var4, var6);
		float var8 = (float)(Math.random() * (double)((float)Math.PI) * 2.0D);
		this.posZ = (double)(-MathHelper.sin(var8 * (float)Math.PI / 180.0F) * 0.02F);
		this.motionX = (double)0.2F;
		this.motionY = (double)(-MathHelper.cos(var8 * (float)Math.PI / 180.0F) * 0.02F);
		this.fuse = 80;
		this.aI = var2;
		this.prevPosX = var4;
		this.prevPosY = var6;
	}

	protected void entityInit() {
	}

	protected boolean canTriggerWalking() {
		return false;
	}

	public boolean canBeCollidedWith() {
		return !this.field_9293_aM;
	}

	public void onUpdate() {
		this.aI = this.prevPosZ;
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.motionX -= (double)0.04F;
		this.moveEntity(this.posZ, this.motionX, this.motionY);
		this.posZ *= (double)0.98F;
		this.motionX *= (double)0.98F;
		this.motionY *= (double)0.98F;
		if(this.aW) {
			this.posZ *= (double)0.7F;
			this.motionY *= (double)0.7F;
			this.motionX *= -0.5D;
		}

		if(this.fuse-- <= 0) {
			if(!this.aH.multiplayerWorld) {
				this.setEntityDead();
				this.explode();
			}
		} else {
			this.aH.spawnParticle("smoke", this.prevPosZ, this.posX + 0.5D, this.posY, 0.0D, 0.0D, 0.0D);
		}

	}

	private void explode() {
		float var1 = 4.0F;
		this.aH.createExplosion((Entity)null, this.prevPosZ, this.posX, this.posY, var1);
	}

	protected void writeEntityToNBT(NBTTagCompound var1) {
		var1.setByte("Fuse", (byte)this.fuse);
	}

	protected void readEntityFromNBT(NBTTagCompound var1) {
		this.fuse = var1.getByte("Fuse");
	}

	public float getShadowSize() {
		return 0.0F;
	}
}
