package net.minecraft.src;

public class EntityFallingSand extends Entity {
	public int blockID;
	public int fallTime = 0;

	public EntityFallingSand(World var1) {
		super(var1);
	}

	public EntityFallingSand(World var1, double var2, double var4, double var6, int var8) {
		super(var1);
		this.blockID = var8;
		this.aE = true;
		this.setSize(0.98F, 0.98F);
		this.be = this.width / 2.0F;
		this.setPosition(var2, var4, var6);
		this.posZ = 0.0D;
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.aI = var2;
		this.prevPosX = var4;
		this.prevPosY = var6;
	}

	protected boolean canTriggerWalking() {
		return false;
	}

	protected void entityInit() {
	}

	public boolean canBeCollidedWith() {
		return !this.field_9293_aM;
	}

	public void onUpdate() {
		if(this.blockID == 0) {
			this.setEntityDead();
		} else {
			this.aI = this.prevPosZ;
			this.prevPosX = this.posX;
			this.prevPosY = this.posY;
			++this.fallTime;
			this.motionX -= (double)0.04F;
			this.moveEntity(this.posZ, this.motionX, this.motionY);
			this.posZ *= (double)0.98F;
			this.motionX *= (double)0.98F;
			this.motionY *= (double)0.98F;
			int var1 = MathHelper.floor_double(this.prevPosZ);
			int var2 = MathHelper.floor_double(this.posX);
			int var3 = MathHelper.floor_double(this.posY);
			if(this.aH.getBlockId(var1, var2, var3) == this.blockID) {
				this.aH.setBlockWithNotify(var1, var2, var3, 0);
			}

			if(this.aW) {
				this.posZ *= (double)0.7F;
				this.motionY *= (double)0.7F;
				this.motionX *= -0.5D;
				this.setEntityDead();
				if((!this.aH.canBlockBePlacedAt(this.blockID, var1, var2, var3, true, 1) || BlockSand.canFallBelow(this.aH, var1, var2 - 1, var3) || !this.aH.setBlockWithNotify(var1, var2, var3, this.blockID)) && !this.aH.multiplayerWorld) {
					this.dropItem(this.blockID, 1);
				}
			} else if(this.fallTime > 100 && !this.aH.multiplayerWorld) {
				this.dropItem(this.blockID, 1);
				this.setEntityDead();
			}

		}
	}

	protected void writeEntityToNBT(NBTTagCompound var1) {
		var1.setByte("Tile", (byte)this.blockID);
	}

	protected void readEntityFromNBT(NBTTagCompound var1) {
		this.blockID = var1.getByte("Tile") & 255;
	}

	public float getShadowSize() {
		return 0.0F;
	}

	public World getWorld() {
		return this.aH;
	}
}
