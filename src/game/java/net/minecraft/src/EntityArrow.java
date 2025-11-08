package net.minecraft.src;

import java.util.List;

public class EntityArrow extends Entity {
	private int xTile = -1;
	private int yTile = -1;
	private int zTile = -1;
	private int inTile = 0;
	private int field_28019_h = 0;
	private boolean inGround = false;
	public boolean field_28020_a = false;
	public int arrowShake = 0;
	public EntityLiving owner;
	private int ticksInGround;
	private int ticksInAir = 0;

	public EntityArrow(World var1) {
		super(var1);
		this.setSize(0.5F, 0.5F);
	}

	public EntityArrow(World var1, double var2, double var4, double var6) {
		super(var1);
		this.setSize(0.5F, 0.5F);
		this.setPosition(var2, var4, var6);
		this.be = 0.0F;
	}

	public EntityArrow(World var1, EntityLiving var2) {
		super(var1);
		this.owner = var2;
		this.field_28020_a = var2 instanceof EntityPlayer;
		this.setSize(0.5F, 0.5F);
		this.setLocationAndAngles(var2.prevPosZ, var2.posX + (double)var2.getEyeHeight(), var2.posY, var2.aR, var2.rotationYaw);
		this.prevPosZ -= (double)(MathHelper.cos(this.aR / 180.0F * (float)Math.PI) * 0.16F);
		this.posX -= (double)0.1F;
		this.posY -= (double)(MathHelper.sin(this.aR / 180.0F * (float)Math.PI) * 0.16F);
		this.setPosition(this.prevPosZ, this.posX, this.posY);
		this.be = 0.0F;
		this.posZ = (double)(-MathHelper.sin(this.aR / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI));
		this.motionY = (double)(MathHelper.cos(this.aR / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI));
		this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI));
		this.setArrowHeading(this.posZ, this.motionX, this.motionY, 1.5F, 1.0F);
	}

	protected void entityInit() {
	}

	public void setArrowHeading(double var1, double var3, double var5, float var7, float var8) {
		float var9 = MathHelper.sqrt_double(var1 * var1 + var3 * var3 + var5 * var5);
		var1 /= (double)var9;
		var3 /= (double)var9;
		var5 /= (double)var9;
		var1 += this.br.nextGaussian() * (double)0.0075F * (double)var8;
		var3 += this.br.nextGaussian() * (double)0.0075F * (double)var8;
		var5 += this.br.nextGaussian() * (double)0.0075F * (double)var8;
		var1 *= (double)var7;
		var3 *= (double)var7;
		var5 *= (double)var7;
		this.posZ = var1;
		this.motionX = var3;
		this.motionY = var5;
		float var10 = MathHelper.sqrt_double(var1 * var1 + var5 * var5);
		this.rotationPitch = this.aR = (float)(Math.atan2(var1, var5) * 180.0D / (double)((float)Math.PI));
		this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(var3, (double)var10) * 180.0D / (double)((float)Math.PI));
		this.ticksInGround = 0;
	}

	public void setVelocity(double var1, double var3, double var5) {
		this.posZ = var1;
		this.motionX = var3;
		this.motionY = var5;
		if(this.prevRotationYaw == 0.0F && this.rotationPitch == 0.0F) {
			float var7 = MathHelper.sqrt_double(var1 * var1 + var5 * var5);
			this.rotationPitch = this.aR = (float)(Math.atan2(var1, var5) * 180.0D / (double)((float)Math.PI));
			this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(var3, (double)var7) * 180.0D / (double)((float)Math.PI));
			this.prevRotationYaw = this.rotationYaw;
			this.rotationPitch = this.aR;
			this.setLocationAndAngles(this.prevPosZ, this.posX, this.posY, this.aR, this.rotationYaw);
			this.ticksInGround = 0;
		}

	}

	public void onUpdate() {
		super.onUpdate();
		if(this.prevRotationYaw == 0.0F && this.rotationPitch == 0.0F) {
			float var1 = MathHelper.sqrt_double(this.posZ * this.posZ + this.motionY * this.motionY);
			this.rotationPitch = this.aR = (float)(Math.atan2(this.posZ, this.motionY) * 180.0D / (double)((float)Math.PI));
			this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(this.motionX, (double)var1) * 180.0D / (double)((float)Math.PI));
		}

		int var15 = this.aH.getBlockId(this.xTile, this.yTile, this.zTile);
		if(var15 > 0) {
			Block.blocksList[var15].setBlockBoundsBasedOnState(this.aH, this.xTile, this.yTile, this.zTile);
			if(Block.blocksList[var15].getCollisionBoundingBoxFromPool(this.aH, this.xTile, this.yTile, this.zTile).isVecInside(Vec3D.createVector(this.prevPosZ, this.posX, this.posY))) {
				this.inGround = true;
			}
		}

		if(this.arrowShake > 0) {
			--this.arrowShake;
		}

		if(this.inGround) {
			var15 = this.aH.getBlockId(this.xTile, this.yTile, this.zTile);
			int var17 = this.aH.getBlockMetadata(this.xTile, this.yTile, this.zTile);
			if(var15 == this.inTile && var17 == this.field_28019_h) {
				++this.ticksInGround;
				if(this.ticksInGround == 1200) {
					this.setEntityDead();
				}

			} else {
				this.inGround = false;
				this.posZ *= (double)(this.br.nextFloat() * 0.2F);
				this.motionX *= (double)(this.br.nextFloat() * 0.2F);
				this.motionY *= (double)(this.br.nextFloat() * 0.2F);
				this.ticksInGround = 0;
				this.ticksInAir = 0;
			}
		} else {
			++this.ticksInAir;
			Vec3D var16 = Vec3D.createVector(this.prevPosZ, this.posX, this.posY);
			Vec3D var2 = Vec3D.createVector(this.prevPosZ + this.posZ, this.posX + this.motionX, this.posY + this.motionY);
			MovingObjectPosition var3 = this.aH.func_28105_a(var16, var2, false, true);
			var16 = Vec3D.createVector(this.prevPosZ, this.posX, this.posY);
			var2 = Vec3D.createVector(this.prevPosZ + this.posZ, this.posX + this.motionX, this.posY + this.motionY);
			if(var3 != null) {
				var2 = Vec3D.createVector(var3.hitVec.xCoord, var3.hitVec.yCoord, var3.hitVec.zCoord);
			}

			Entity var4 = null;
			List var5 = this.aH.getEntitiesWithinAABBExcludingEntity(this, this.aV.addCoord(this.posZ, this.motionX, this.motionY).expand(1.0D, 1.0D, 1.0D));
			double var6 = 0.0D;

			float var10;
			for(int var8 = 0; var8 < var5.size(); ++var8) {
				Entity var9 = (Entity)var5.get(var8);
				if(var9.canBeCollidedWith() && (var9 != this.owner || this.ticksInAir >= 5)) {
					var10 = 0.3F;
					AxisAlignedBB var11 = var9.aV.expand((double)var10, (double)var10, (double)var10);
					MovingObjectPosition var12 = var11.func_1169_a(var16, var2);
					if(var12 != null) {
						double var13 = var16.distanceTo(var12.hitVec);
						if(var13 < var6 || var6 == 0.0D) {
							var4 = var9;
							var6 = var13;
						}
					}
				}
			}

			if(var4 != null) {
				var3 = new MovingObjectPosition(var4);
			}

			float var18;
			if(var3 != null) {
				if(var3.entityHit != null) {
					if(var3.entityHit.attackEntityFrom(this.owner, 4)) {
						this.aH.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.br.nextFloat() * 0.2F + 0.9F));
						this.setEntityDead();
					} else {
						this.posZ *= (double)-0.1F;
						this.motionX *= (double)-0.1F;
						this.motionY *= (double)-0.1F;
						this.aR += 180.0F;
						this.rotationPitch += 180.0F;
						this.ticksInAir = 0;
					}
				} else {
					this.xTile = var3.blockX;
					this.yTile = var3.blockY;
					this.zTile = var3.blockZ;
					this.inTile = this.aH.getBlockId(this.xTile, this.yTile, this.zTile);
					this.field_28019_h = this.aH.getBlockMetadata(this.xTile, this.yTile, this.zTile);
					this.posZ = (double)((float)(var3.hitVec.xCoord - this.prevPosZ));
					this.motionX = (double)((float)(var3.hitVec.yCoord - this.posX));
					this.motionY = (double)((float)(var3.hitVec.zCoord - this.posY));
					var18 = MathHelper.sqrt_double(this.posZ * this.posZ + this.motionX * this.motionX + this.motionY * this.motionY);
					this.prevPosZ -= this.posZ / (double)var18 * (double)0.05F;
					this.posX -= this.motionX / (double)var18 * (double)0.05F;
					this.posY -= this.motionY / (double)var18 * (double)0.05F;
					this.aH.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.br.nextFloat() * 0.2F + 0.9F));
					this.inGround = true;
					this.arrowShake = 7;
				}
			}

			this.prevPosZ += this.posZ;
			this.posX += this.motionX;
			this.posY += this.motionY;
			var18 = MathHelper.sqrt_double(this.posZ * this.posZ + this.motionY * this.motionY);
			this.aR = (float)(Math.atan2(this.posZ, this.motionY) * 180.0D / (double)((float)Math.PI));

			for(this.rotationYaw = (float)(Math.atan2(this.motionX, (double)var18) * 180.0D / (double)((float)Math.PI)); this.rotationYaw - this.prevRotationYaw < -180.0F; this.prevRotationYaw -= 360.0F) {
			}

			while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
				this.prevRotationYaw += 360.0F;
			}

			while(this.aR - this.rotationPitch < -180.0F) {
				this.rotationPitch -= 360.0F;
			}

			while(this.aR - this.rotationPitch >= 180.0F) {
				this.rotationPitch += 360.0F;
			}

			this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
			this.aR = this.rotationPitch + (this.aR - this.rotationPitch) * 0.2F;
			float var19 = 0.99F;
			var10 = 0.03F;
			if(this.isInWater()) {
				for(int var20 = 0; var20 < 4; ++var20) {
					float var21 = 0.25F;
					this.aH.spawnParticle("bubble", this.prevPosZ - this.posZ * (double)var21, this.posX - this.motionX * (double)var21, this.posY - this.motionY * (double)var21, this.posZ, this.motionX, this.motionY);
				}

				var19 = 0.8F;
			}

			this.posZ *= (double)var19;
			this.motionX *= (double)var19;
			this.motionY *= (double)var19;
			this.motionX -= (double)var10;
			this.setPosition(this.prevPosZ, this.posX, this.posY);
		}
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		var1.setShort("xTile", (short)this.xTile);
		var1.setShort("yTile", (short)this.yTile);
		var1.setShort("zTile", (short)this.zTile);
		var1.setByte("inTile", (byte)this.inTile);
		var1.setByte("inData", (byte)this.field_28019_h);
		var1.setByte("shake", (byte)this.arrowShake);
		var1.setByte("inGround", (byte)(this.inGround ? 1 : 0));
		var1.setBoolean("player", this.field_28020_a);
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		this.xTile = var1.getShort("xTile");
		this.yTile = var1.getShort("yTile");
		this.zTile = var1.getShort("zTile");
		this.inTile = var1.getByte("inTile") & 255;
		this.field_28019_h = var1.getByte("inData") & 255;
		this.arrowShake = var1.getByte("shake") & 255;
		this.inGround = var1.getByte("inGround") == 1;
		this.field_28020_a = var1.getBoolean("player");
	}

	public void onCollideWithPlayer(EntityPlayer var1) {
		if(!this.aH.multiplayerWorld) {
			if(this.inGround && this.field_28020_a && this.arrowShake <= 0 && var1.inventory.addItemStackToInventory(new ItemStack(Item.arrow, 1))) {
				this.aH.playSoundAtEntity(this, "random.pop", 0.2F, ((this.br.nextFloat() - this.br.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				var1.onItemPickup(this, 1);
				this.setEntityDead();
			}

		}
	}

	public float getShadowSize() {
		return 0.0F;
	}
}
