package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

public class EntityWolf extends EntityAnimal {
	private boolean looksWithInterest = false;
	private float field_25048_b;
	private float field_25054_c;
	private boolean isWolfShaking;
	private boolean field_25052_g;
	private float timeWolfIsShaking;
	private float prevTimeWolfIsShaking;

	public EntityWolf(World var1) {
		super(var1);
		this.N = "/mob/wolf.png";
		this.setSize(0.8F, 0.8F);
		this.defaultPitch = 1.1F;
		this.X = 8;
	}

	protected void entityInit() {
		super.entityInit();
		this.bC.addObject(16, Byte.valueOf((byte)0));
		this.bC.addObject(17, "");
		this.bC.addObject(18, Integer.valueOf(this.X));
	}

	protected boolean canTriggerWalking() {
		return false;
	}

	public String getEntityTexture() {
		return this.isWolfTamed() ? "/mob/wolf_tame.png" : (this.isWolfAngry() ? "/mob/wolf_angry.png" : super.getEntityTexture());
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
		var1.setBoolean("Angry", this.isWolfAngry());
		var1.setBoolean("Sitting", this.isWolfSitting());
		if(this.getWolfOwner() == null) {
			var1.setString("Owner", "");
		} else {
			var1.setString("Owner", this.getWolfOwner());
		}

	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
		this.setWolfAngry(var1.getBoolean("Angry"));
		this.setWolfSitting(var1.getBoolean("Sitting"));
		String var2 = var1.getString("Owner");
		if(var2.length() > 0) {
			this.setWolfOwner(var2);
			this.setWolfTamed(true);
		}

	}

	protected boolean canDespawn() {
		return !this.isWolfTamed();
	}

	protected String getLivingSound() {
		return this.isWolfAngry() ? "mob.wolf.growl" : (this.br.nextInt(3) == 0 ? (this.isWolfTamed() && this.bC.getWatchableObjectInt(18) < 10 ? "mob.wolf.whine" : "mob.wolf.panting") : "mob.wolf.bark");
	}

	protected String getHurtSound() {
		return "mob.wolf.hurt";
	}

	protected String getDeathSound() {
		return "mob.wolf.death";
	}

	protected float getSoundVolume() {
		return 0.4F;
	}

	protected int getDropItemId() {
		return -1;
	}

	protected void updatePlayerActionState() {
		super.updatePlayerActionState();
		if(!this.hasAttacked && !this.hasPath() && this.isWolfTamed() && this.riddenByEntity == null) {
			EntityPlayer var3 = this.aH.getPlayerEntityByName(this.getWolfOwner());
			if(var3 != null) {
				float var2 = var3.getDistanceToEntity(this);
				if(var2 > 5.0F) {
					this.getPathOrWalkableBlock(var3, var2);
				}
			} else if(!this.isInWater()) {
				this.setWolfSitting(true);
			}
		} else if(this.playerToAttack == null && !this.hasPath() && !this.isWolfTamed() && this.aH.rand.nextInt(100) == 0) {
			List var1 = this.aH.getEntitiesWithinAABB(EntitySheep.class, AxisAlignedBB.getBoundingBoxFromPool(this.prevPosZ, this.posX, this.posY, this.prevPosZ + 1.0D, this.posX + 1.0D, this.posY + 1.0D).expand(16.0D, 4.0D, 16.0D));
			if(!var1.isEmpty()) {
				this.setTarget((Entity)var1.get(this.aH.rand.nextInt(var1.size())));
			}
		}

		if(this.isInWater()) {
			this.setWolfSitting(false);
		}

		if(!this.aH.multiplayerWorld) {
			this.bC.updateObject(18, Integer.valueOf(this.X));
		}

	}

	public void onLivingUpdate() {
		super.onLivingUpdate();
		this.looksWithInterest = false;
		if(this.hasCurrentTarget() && !this.hasPath() && !this.isWolfAngry()) {
			Entity var1 = this.getCurrentTarget();
			if(var1 instanceof EntityPlayer) {
				EntityPlayer var2 = (EntityPlayer)var1;
				ItemStack var3 = var2.inventory.getCurrentItem();
				if(var3 != null) {
					if(!this.isWolfTamed() && var3.itemID == Item.bone.shiftedIndex) {
						this.looksWithInterest = true;
					} else if(this.isWolfTamed() && Item.itemsList[var3.itemID] instanceof ItemFood) {
						this.looksWithInterest = ((ItemFood)Item.itemsList[var3.itemID]).getIsWolfsFavoriteMeat();
					}
				}
			}
		}

		if(!this.U && this.isWolfShaking && !this.field_25052_g && !this.hasPath() && this.aW) {
			this.field_25052_g = true;
			this.timeWolfIsShaking = 0.0F;
			this.prevTimeWolfIsShaking = 0.0F;
			this.aH.func_9425_a(this, (byte)8);
		}

	}

	public void onUpdate() {
		super.onUpdate();
		this.field_25054_c = this.field_25048_b;
		if(this.looksWithInterest) {
			this.field_25048_b += (1.0F - this.field_25048_b) * 0.4F;
		} else {
			this.field_25048_b += (0.0F - this.field_25048_b) * 0.4F;
		}

		if(this.looksWithInterest) {
			this.aB = 10;
		}

		if(this.isWet()) {
			this.isWolfShaking = true;
			this.field_25052_g = false;
			this.timeWolfIsShaking = 0.0F;
			this.prevTimeWolfIsShaking = 0.0F;
		} else if((this.isWolfShaking || this.field_25052_g) && this.field_25052_g) {
			if(this.timeWolfIsShaking == 0.0F) {
				this.aH.playSoundAtEntity(this, "mob.wolf.shake", this.getSoundVolume(), (this.br.nextFloat() - this.br.nextFloat()) * 0.2F + 1.0F);
			}

			this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
			this.timeWolfIsShaking += 0.05F;
			if(this.prevTimeWolfIsShaking >= 2.0F) {
				this.isWolfShaking = false;
				this.field_25052_g = false;
				this.prevTimeWolfIsShaking = 0.0F;
				this.timeWolfIsShaking = 0.0F;
			}

			if(this.timeWolfIsShaking > 0.4F) {
				float var1 = (float)this.aV.minY;
				int var2 = (int)(MathHelper.sin((this.timeWolfIsShaking - 0.4F) * (float)Math.PI) * 7.0F);

				for(int var3 = 0; var3 < var2; ++var3) {
					float var4 = (this.br.nextFloat() * 2.0F - 1.0F) * this.yOffset * 0.5F;
					float var5 = (this.br.nextFloat() * 2.0F - 1.0F) * this.yOffset * 0.5F;
					this.aH.spawnParticle("splash", this.prevPosZ + (double)var4, (double)(var1 + 0.8F), this.posY + (double)var5, this.posZ, this.motionX, this.motionY);
				}
			}
		}

	}

	public boolean getWolfShaking() {
		return this.isWolfShaking;
	}

	public float getShadingWhileShaking(float var1) {
		return 12.0F / 16.0F + (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * var1) / 2.0F * 0.25F;
	}

	public float getShakeAngle(float var1, float var2) {
		float var3 = (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * var1 + var2) / 1.8F;
		if(var3 < 0.0F) {
			var3 = 0.0F;
		} else if(var3 > 1.0F) {
			var3 = 1.0F;
		}

		return MathHelper.sin(var3 * (float)Math.PI) * MathHelper.sin(var3 * (float)Math.PI * 11.0F) * 0.15F * (float)Math.PI;
	}

	public float getInterestedAngle(float var1) {
		return (this.field_25054_c + (this.field_25048_b - this.field_25054_c) * var1) * 0.15F * (float)Math.PI;
	}

	public float getEyeHeight() {
		return this.width * 0.8F;
	}

	protected int func_25026_x() {
		return this.isWolfSitting() ? 20 : super.func_25026_x();
	}

	private void getPathOrWalkableBlock(Entity var1, float var2) {
		PathEntity var3 = this.aH.getPathToEntity(this, var1, 16.0F);
		if(var3 == null && var2 > 12.0F) {
			int var4 = MathHelper.floor_double(var1.prevPosZ) - 2;
			int var5 = MathHelper.floor_double(var1.posY) - 2;
			int var6 = MathHelper.floor_double(var1.aV.minY);

			for(int var7 = 0; var7 <= 4; ++var7) {
				for(int var8 = 0; var8 <= 4; ++var8) {
					if((var7 < 1 || var8 < 1 || var7 > 3 || var8 > 3) && this.aH.func_28100_h(var4 + var7, var6 - 1, var5 + var8) && !this.aH.func_28100_h(var4 + var7, var6, var5 + var8) && !this.aH.func_28100_h(var4 + var7, var6 + 1, var5 + var8)) {
						this.setLocationAndAngles((double)((float)(var4 + var7) + 0.5F), (double)var6, (double)((float)(var5 + var8) + 0.5F), this.aR, this.rotationYaw);
						return;
					}
				}
			}
		} else {
			this.setPathToEntity(var3);
		}

	}

	protected boolean isMovementCeased() {
		return this.isWolfSitting() || this.field_25052_g;
	}

	public boolean attackEntityFrom(Entity var1, int var2) {
		this.setWolfSitting(false);
		if(var1 != null && !(var1 instanceof EntityPlayer) && !(var1 instanceof EntityArrow)) {
			var2 = (var2 + 1) / 2;
		}

		if(!super.attackEntityFrom((Entity)var1, var2)) {
			return false;
		} else {
			if(!this.isWolfTamed() && !this.isWolfAngry()) {
				if(var1 instanceof EntityPlayer) {
					this.setWolfAngry(true);
					this.playerToAttack = (Entity)var1;
				}

				if(var1 instanceof EntityArrow && ((EntityArrow)var1).owner != null) {
					var1 = ((EntityArrow)var1).owner;
				}

				if(var1 instanceof EntityLiving) {
					List var3 = this.aH.getEntitiesWithinAABB(EntityWolf.class, AxisAlignedBB.getBoundingBoxFromPool(this.prevPosZ, this.posX, this.posY, this.prevPosZ + 1.0D, this.posX + 1.0D, this.posY + 1.0D).expand(16.0D, 4.0D, 16.0D));
					Iterator var4 = var3.iterator();

					while(var4.hasNext()) {
						Entity var5 = (Entity)var4.next();
						EntityWolf var6 = (EntityWolf)var5;
						if(!var6.isWolfTamed() && var6.playerToAttack == null) {
							var6.playerToAttack = (Entity)var1;
							if(var1 instanceof EntityPlayer) {
								var6.setWolfAngry(true);
							}
						}
					}
				}
			} else if(var1 != this && var1 != null) {
				if(this.isWolfTamed() && var1 instanceof EntityPlayer && ((EntityPlayer)var1).username.equalsIgnoreCase(this.getWolfOwner())) {
					return true;
				}

				this.playerToAttack = (Entity)var1;
			}

			return true;
		}
	}

	protected Entity findPlayerToAttack() {
		return this.isWolfAngry() ? this.aH.getClosestPlayerToEntity(this, 16.0D) : null;
	}

	protected void attackEntity(Entity var1, float var2) {
		if(var2 > 2.0F && var2 < 6.0F && this.br.nextInt(10) == 0) {
			if(this.aW) {
				double var8 = var1.prevPosZ - this.prevPosZ;
				double var5 = var1.posY - this.posY;
				float var7 = MathHelper.sqrt_double(var8 * var8 + var5 * var5);
				this.posZ = var8 / (double)var7 * 0.5D * (double)0.8F + this.posZ * (double)0.2F;
				this.motionY = var5 / (double)var7 * 0.5D * (double)0.8F + this.motionY * (double)0.2F;
				this.motionX = (double)0.4F;
			}
		} else if((double)var2 < 1.5D && var1.aV.maxY > this.aV.minY && var1.aV.minY < this.aV.maxY) {
			this.deathTime = 20;
			byte var3 = 2;
			if(this.isWolfTamed()) {
				var3 = 4;
			}

			var1.attackEntityFrom(this, var3);
		}

	}

	public boolean interact(EntityPlayer var1) {
		ItemStack var2 = var1.inventory.getCurrentItem();
		if(!this.isWolfTamed()) {
			if(var2 != null && var2.itemID == Item.bone.shiftedIndex && !this.isWolfAngry()) {
				--var2.stackSize;
				if(var2.stackSize <= 0) {
					var1.inventory.setInventorySlotContents(var1.inventory.currentItem, (ItemStack)null);
				}

				if(!this.aH.multiplayerWorld) {
					if(this.br.nextInt(3) == 0) {
						this.setWolfTamed(true);
						this.setPathToEntity((PathEntity)null);
						this.setWolfSitting(true);
						this.X = 20;
						this.setWolfOwner(var1.username);
						this.showHeartsOrSmokeFX(true);
						this.aH.func_9425_a(this, (byte)7);
					} else {
						this.showHeartsOrSmokeFX(false);
						this.aH.func_9425_a(this, (byte)6);
					}
				}

				return true;
			}
		} else {
			if(var2 != null && Item.itemsList[var2.itemID] instanceof ItemFood) {
				ItemFood var3 = (ItemFood)Item.itemsList[var2.itemID];
				if(var3.getIsWolfsFavoriteMeat() && this.bC.getWatchableObjectInt(18) < 20) {
					--var2.stackSize;
					if(var2.stackSize <= 0) {
						var1.inventory.setInventorySlotContents(var1.inventory.currentItem, (ItemStack)null);
					}

					this.heal(((ItemFood)Item.porkRaw).getHealAmount());
					return true;
				}
			}

			if(var1.username.equalsIgnoreCase(this.getWolfOwner())) {
				if(!this.aH.multiplayerWorld) {
					this.setWolfSitting(!this.isWolfSitting());
					this.ay = false;
					this.setPathToEntity((PathEntity)null);
				}

				return true;
			}
		}

		return false;
	}

	void showHeartsOrSmokeFX(boolean var1) {
		String var2 = "heart";
		if(!var1) {
			var2 = "smoke";
		}

		for(int var3 = 0; var3 < 7; ++var3) {
			double var4 = this.br.nextGaussian() * 0.02D;
			double var6 = this.br.nextGaussian() * 0.02D;
			double var8 = this.br.nextGaussian() * 0.02D;
			this.aH.spawnParticle(var2, this.prevPosZ + (double)(this.br.nextFloat() * this.yOffset * 2.0F) - (double)this.yOffset, this.posX + 0.5D + (double)(this.br.nextFloat() * this.width), this.posY + (double)(this.br.nextFloat() * this.yOffset * 2.0F) - (double)this.yOffset, var4, var6, var8);
		}

	}

	public void handleHealthUpdate(byte var1) {
		if(var1 == 7) {
			this.showHeartsOrSmokeFX(true);
		} else if(var1 == 6) {
			this.showHeartsOrSmokeFX(false);
		} else if(var1 == 8) {
			this.field_25052_g = true;
			this.timeWolfIsShaking = 0.0F;
			this.prevTimeWolfIsShaking = 0.0F;
		} else {
			super.handleHealthUpdate(var1);
		}

	}

	public float setTailRotation() {
		return this.isWolfAngry() ? (float)Math.PI * 0.49F : (this.isWolfTamed() ? (0.55F - (float)(20 - this.bC.getWatchableObjectInt(18)) * 0.02F) * (float)Math.PI : (float)Math.PI * 0.2F);
	}

	public int getMaxSpawnedInChunk() {
		return 8;
	}

	public String getWolfOwner() {
		return this.bC.getWatchableObjectString(17);
	}

	public void setWolfOwner(String var1) {
		this.bC.updateObject(17, var1);
	}

	public boolean isWolfSitting() {
		return (this.bC.getWatchableObjectByte(16) & 1) != 0;
	}

	public void setWolfSitting(boolean var1) {
		byte var2 = this.bC.getWatchableObjectByte(16);
		if(var1) {
			this.bC.updateObject(16, Byte.valueOf((byte)(var2 | 1)));
		} else {
			this.bC.updateObject(16, Byte.valueOf((byte)(var2 & -2)));
		}

	}

	public boolean isWolfAngry() {
		return (this.bC.getWatchableObjectByte(16) & 2) != 0;
	}

	public void setWolfAngry(boolean var1) {
		byte var2 = this.bC.getWatchableObjectByte(16);
		if(var1) {
			this.bC.updateObject(16, Byte.valueOf((byte)(var2 | 2)));
		} else {
			this.bC.updateObject(16, Byte.valueOf((byte)(var2 & -3)));
		}

	}

	public boolean isWolfTamed() {
		return (this.bC.getWatchableObjectByte(16) & 4) != 0;
	}

	public void setWolfTamed(boolean var1) {
		byte var2 = this.bC.getWatchableObjectByte(16);
		if(var1) {
			this.bC.updateObject(16, Byte.valueOf((byte)(var2 | 4)));
		} else {
			this.bC.updateObject(16, Byte.valueOf((byte)(var2 & -5)));
		}

	}
}
