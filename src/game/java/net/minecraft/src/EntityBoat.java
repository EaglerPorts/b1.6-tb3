package net.minecraft.src;

import java.util.List;

public class EntityBoat extends Entity {
	public int boatCurrentDamage;
	public int boatTimeSinceHit;
	public int boatRockDirection;
	private int field_9394_d;
	private double field_9393_e;
	private double field_9392_f;
	private double field_9391_g;
	private double field_9390_h;
	private double field_9389_i;
	private double field_9388_j;
	private double field_9387_k;
	private double field_9386_l;

	public EntityBoat(World var1) {
		super(var1);
		this.boatCurrentDamage = 0;
		this.boatTimeSinceHit = 0;
		this.boatRockDirection = 1;
		this.aE = true;
		this.setSize(1.5F, 0.6F);
		this.be = this.width / 2.0F;
	}

	protected boolean canTriggerWalking() {
		return false;
	}

	protected void entityInit() {
	}

	public AxisAlignedBB getCollisionBox(Entity var1) {
		return var1.aV;
	}

	public AxisAlignedBB getBoundingBox() {
		return this.aV;
	}

	public boolean canBePushed() {
		return true;
	}

	public EntityBoat(World var1, double var2, double var4, double var6) {
		this(var1);
		this.setPosition(var2, var4 + (double)this.be, var6);
		this.posZ = 0.0D;
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.aI = var2;
		this.prevPosX = var4;
		this.prevPosY = var6;
	}

	public double getMountedYOffset() {
		return (double)this.width * 0.0D - (double)0.3F;
	}

	public boolean attackEntityFrom(Entity var1, int var2) {
		if(!this.aH.multiplayerWorld && !this.field_9293_aM) {
			this.boatRockDirection = -this.boatRockDirection;
			this.boatTimeSinceHit = 10;
			this.boatCurrentDamage += var2 * 10;
			this.setBeenAttacked();
			if(this.boatCurrentDamage > 40) {
				int var3;
				for(var3 = 0; var3 < 3; ++var3) {
					this.dropItemWithOffset(Block.planks.blockID, 1, 0.0F);
				}

				for(var3 = 0; var3 < 2; ++var3) {
					this.dropItemWithOffset(Item.stick.shiftedIndex, 1, 0.0F);
				}

				this.setEntityDead();
			}

			return true;
		} else {
			return true;
		}
	}

	public void performHurtAnimation() {
		this.boatRockDirection = -this.boatRockDirection;
		this.boatTimeSinceHit = 10;
		this.boatCurrentDamage += this.boatCurrentDamage * 10;
	}

	public boolean canBeCollidedWith() {
		return !this.field_9293_aM;
	}

	public void setPositionAndRotation2(double var1, double var3, double var5, float var7, float var8, int var9) {
		this.field_9393_e = var1;
		this.field_9392_f = var3;
		this.field_9391_g = var5;
		this.field_9390_h = (double)var7;
		this.field_9389_i = (double)var8;
		this.field_9394_d = var9 + 4;
		this.posZ = this.field_9388_j;
		this.motionX = this.field_9387_k;
		this.motionY = this.field_9386_l;
	}

	public void setVelocity(double var1, double var3, double var5) {
		this.field_9388_j = this.posZ = var1;
		this.field_9387_k = this.motionX = var3;
		this.field_9386_l = this.motionY = var5;
	}

	public void onUpdate() {
		super.onUpdate();
		if(this.boatTimeSinceHit > 0) {
			--this.boatTimeSinceHit;
		}

		if(this.boatCurrentDamage > 0) {
			--this.boatCurrentDamage;
		}

		this.aI = this.prevPosZ;
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		byte var1 = 5;
		double var2 = 0.0D;

		for(int var4 = 0; var4 < var1; ++var4) {
			double var5 = this.aV.minY + (this.aV.maxY - this.aV.minY) * (double)(var4 + 0) / (double)var1 - 0.125D;
			double var7 = this.aV.minY + (this.aV.maxY - this.aV.minY) * (double)(var4 + 1) / (double)var1 - 0.125D;
			AxisAlignedBB var9 = AxisAlignedBB.getBoundingBoxFromPool(this.aV.minX, var5, this.aV.minZ, this.aV.maxX, var7, this.aV.maxZ);
			if(this.aH.isAABBInMaterial(var9, Material.water)) {
				var2 += 1.0D / (double)var1;
			}
		}

		double var6;
		double var8;
		double var10;
		double var21;
		if(this.aH.multiplayerWorld) {
			if(this.field_9394_d > 0) {
				var21 = this.prevPosZ + (this.field_9393_e - this.prevPosZ) / (double)this.field_9394_d;
				var6 = this.posX + (this.field_9392_f - this.posX) / (double)this.field_9394_d;
				var8 = this.posY + (this.field_9391_g - this.posY) / (double)this.field_9394_d;

				for(var10 = this.field_9390_h - (double)this.aR; var10 < -180.0D; var10 += 360.0D) {
				}

				while(var10 >= 180.0D) {
					var10 -= 360.0D;
				}

				this.aR = (float)((double)this.aR + var10 / (double)this.field_9394_d);
				this.rotationYaw = (float)((double)this.rotationYaw + (this.field_9389_i - (double)this.rotationYaw) / (double)this.field_9394_d);
				--this.field_9394_d;
				this.setPosition(var21, var6, var8);
				this.setRotation(this.aR, this.rotationYaw);
			} else {
				var21 = this.prevPosZ + this.posZ;
				var6 = this.posX + this.motionX;
				var8 = this.posY + this.motionY;
				this.setPosition(var21, var6, var8);
				if(this.aW) {
					this.posZ *= 0.5D;
					this.motionX *= 0.5D;
					this.motionY *= 0.5D;
				}

				this.posZ *= (double)0.99F;
				this.motionX *= (double)0.95F;
				this.motionY *= (double)0.99F;
			}

		} else {
			if(var2 < 1.0D) {
				var21 = var2 * 2.0D - 1.0D;
				this.motionX += (double)0.04F * var21;
			} else {
				this.motionX += (double)0.001F;
			}

			if(this.aF != null) {
				this.posZ += this.aF.posZ * 0.2D;
				this.motionY += this.aF.motionY * 0.2D;
			}

			var21 = 0.4D;
			if(this.posZ < -var21) {
				this.posZ = -var21;
			}

			if(this.posZ > var21) {
				this.posZ = var21;
			}

			if(this.motionY < -var21) {
				this.motionY = -var21;
			}

			if(this.motionY > var21) {
				this.motionY = var21;
			}

			if(this.aW) {
				this.posZ *= 0.5D;
				this.motionX *= 0.5D;
				this.motionY *= 0.5D;
			}

			this.moveEntity(this.posZ, this.motionX, this.motionY);
			var6 = Math.sqrt(this.posZ * this.posZ + this.motionY * this.motionY);
			if(var6 > 0.15D) {
				var8 = Math.cos((double)this.aR * Math.PI / 180.0D);
				var10 = Math.sin((double)this.aR * Math.PI / 180.0D);

				for(int var12 = 0; (double)var12 < 1.0D + var6 * 60.0D; ++var12) {
					double var13 = (double)(this.br.nextFloat() * 2.0F - 1.0F);
					double var15 = (double)(this.br.nextInt(2) * 2 - 1) * 0.7D;
					double var17;
					double var19;
					if(this.br.nextBoolean()) {
						var17 = this.prevPosZ - var8 * var13 * 0.8D + var10 * var15;
						var19 = this.posY - var10 * var13 * 0.8D - var8 * var15;
						this.aH.spawnParticle("splash", var17, this.posX - 0.125D, var19, this.posZ, this.motionX, this.motionY);
					} else {
						var17 = this.prevPosZ + var8 + var10 * var13 * 0.7D;
						var19 = this.posY + var10 - var8 * var13 * 0.7D;
						this.aH.spawnParticle("splash", var17, this.posX - 0.125D, var19, this.posZ, this.motionX, this.motionY);
					}
				}
			}

			if(this.onGround && var6 > 0.15D) {
				if(!this.aH.multiplayerWorld) {
					this.setEntityDead();

					int var22;
					for(var22 = 0; var22 < 3; ++var22) {
						this.dropItemWithOffset(Block.planks.blockID, 1, 0.0F);
					}

					for(var22 = 0; var22 < 2; ++var22) {
						this.dropItemWithOffset(Item.stick.shiftedIndex, 1, 0.0F);
					}
				}
			} else {
				this.posZ *= (double)0.99F;
				this.motionX *= (double)0.95F;
				this.motionY *= (double)0.99F;
			}

			this.rotationYaw = 0.0F;
			var8 = (double)this.aR;
			var10 = this.aI - this.prevPosZ;
			double var23 = this.prevPosY - this.posY;
			if(var10 * var10 + var23 * var23 > 0.001D) {
				var8 = (double)((float)(Math.atan2(var23, var10) * 180.0D / Math.PI));
			}

			double var14;
			for(var14 = var8 - (double)this.aR; var14 >= 180.0D; var14 -= 360.0D) {
			}

			while(var14 < -180.0D) {
				var14 += 360.0D;
			}

			if(var14 > 20.0D) {
				var14 = 20.0D;
			}

			if(var14 < -20.0D) {
				var14 = -20.0D;
			}

			this.aR = (float)((double)this.aR + var14);
			this.setRotation(this.aR, this.rotationYaw);
			List var16 = this.aH.getEntitiesWithinAABBExcludingEntity(this, this.aV.expand((double)0.2F, 0.0D, (double)0.2F));
			int var24;
			if(var16 != null && var16.size() > 0) {
				for(var24 = 0; var24 < var16.size(); ++var24) {
					Entity var18 = (Entity)var16.get(var24);
					if(var18 != this.aF && var18.canBePushed() && var18 instanceof EntityBoat) {
						var18.applyEntityCollision(this);
					}
				}
			}

			for(var24 = 0; var24 < 4; ++var24) {
				int var25 = MathHelper.floor_double(this.prevPosZ + ((double)(var24 % 2) - 0.5D) * 0.8D);
				int var26 = MathHelper.floor_double(this.posX);
				int var20 = MathHelper.floor_double(this.posY + ((double)(var24 / 2) - 0.5D) * 0.8D);
				if(this.aH.getBlockId(var25, var26, var20) == Block.snow.blockID) {
					this.aH.setBlockWithNotify(var25, var26, var20, 0);
				}
			}

			if(this.aF != null && this.aF.field_9293_aM) {
				this.aF = null;
			}

		}
	}

	public void updateRiderPosition() {
		if(this.aF != null) {
			double var1 = Math.cos((double)this.aR * Math.PI / 180.0D) * 0.4D;
			double var3 = Math.sin((double)this.aR * Math.PI / 180.0D) * 0.4D;
			this.aF.setPosition(this.prevPosZ + var1, this.posX + this.getMountedYOffset() + this.aF.getYOffset(), this.posY + var3);
		}
	}

	protected void writeEntityToNBT(NBTTagCompound var1) {
	}

	protected void readEntityFromNBT(NBTTagCompound var1) {
	}

	public float getShadowSize() {
		return 0.0F;
	}

	public boolean interact(EntityPlayer var1) {
		if(this.aF != null && this.aF instanceof EntityPlayer && this.aF != var1) {
			return true;
		} else {
			if(!this.aH.multiplayerWorld) {
				var1.mountEntity(this);
			}

			return true;
		}
	}
}
