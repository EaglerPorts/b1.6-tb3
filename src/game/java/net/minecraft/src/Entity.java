package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Entity {
	private static int nextEntityID = 0;
	public int aC = nextEntityID++;
	public double aD = 1.0D;
	public boolean aE = false;
	public Entity aF;
	public Entity riddenByEntity;
	public World aH;
	public double aI;
	public double prevPosX;
	public double prevPosY;
	public double prevPosZ;
	public double posX;
	public double posY;
	public double posZ;
	public double motionX;
	public double motionY;
	public float aR;
	public float rotationYaw;
	public float rotationPitch;
	public float prevRotationYaw;
	public final AxisAlignedBB aV = AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
	public boolean aW = false;
	public boolean onGround;
	public boolean isCollidedHorizontally;
	public boolean isCollidedVertically = false;
	public boolean isCollided = false;
	public boolean beenAttacked;
	public boolean isInWeb = true;
	public boolean field_9293_aM = false;
	public float be = 0.0F;
	public float yOffset = 0.6F;
	public float width = 1.8F;
	public float height = 0.0F;
	public float prevDistanceWalkedModified = 0.0F;
	protected float distanceWalkedModified = 0.0F;
	private int nextStepDistance = 1;
	public double bk;
	public double lastTickPosX;
	public double lastTickPosY;
	public float bn = 0.0F;
	public float ySize = 0.0F;
	public boolean bp = false;
	public float bq = 0.0F;
	protected Random br = new Random();
	public int bs = 0;
	public int ticksExisted = 1;
	public int fireResistance = 0;
	protected int fire = 300;
	protected boolean bw = false;
	public int bx = 0;
	public int field_9306_bj = 300;
	private boolean isFirstUpdate = true;
	public String bz;
	public String skinUrl;
	protected boolean bB = false;
	protected DataWatcher bC = new DataWatcher();
	private double entityRiderPitchDelta;
	private double entityRiderYawDelta;
	public boolean bD = false;
	public int bE;
	public int chunkCoordX;
	public int chunkCoordY;
	public int chunkCoordZ;
	public int serverPosX;
	public int serverPosY;
	public boolean bK;
	private ArrayList f = new ArrayList();

	public Entity(World var1) {
		this.aH = var1;
		this.setPosition(0.0D, 0.0D, 0.0D);
		this.bC.addObject(0, Byte.valueOf((byte)0));
		this.entityInit();
	}

	protected abstract void entityInit();

	public DataWatcher getDataWatcher() {
		return this.bC;
	}

	public boolean equals(Object var1) {
		return var1 instanceof Entity ? ((Entity)var1).aC == this.aC : false;
	}

	public int hashCode() {
		return this.aC;
	}

	protected void preparePlayerToSpawn() {
		if(this.aH != null) {
			while(this.posX > 0.0D) {
				this.setPosition(this.prevPosZ, this.posX, this.posY);
				if(this.aH.getCollidingBoundingBoxes(this, this.aV).size() == 0) {
					break;
				}

				++this.posX;
			}

			this.posZ = this.motionX = this.motionY = 0.0D;
			this.rotationYaw = 0.0F;
		}
	}

	public void setEntityDead() {
		this.field_9293_aM = true;
	}

	protected void setSize(float var1, float var2) {
		this.yOffset = var1;
		this.width = var2;
	}

	protected void setRotation(float var1, float var2) {
		this.aR = var1 % 360.0F;
		this.rotationYaw = var2 % 360.0F;
	}

	public void setPosition(double var1, double var3, double var5) {
		this.prevPosZ = var1;
		this.posX = var3;
		this.posY = var5;
		float var7 = this.yOffset / 2.0F;
		float var8 = this.width;
		this.aV.setBounds(var1 - (double)var7, var3 - (double)this.be + (double)this.bn, var5 - (double)var7, var1 + (double)var7, var3 - (double)this.be + (double)this.bn + (double)var8, var5 + (double)var7);
	}

	public void func_346_d(float var1, float var2) {
		float var3 = this.rotationYaw;
		float var4 = this.aR;
		this.aR = (float)((double)this.aR + (double)var1 * 0.15D);
		this.rotationYaw = (float)((double)this.rotationYaw - (double)var2 * 0.15D);
		if(this.rotationYaw < -90.0F) {
			this.rotationYaw = -90.0F;
		}

		if(this.rotationYaw > 90.0F) {
			this.rotationYaw = 90.0F;
		}

		this.prevRotationYaw += this.rotationYaw - var3;
		this.rotationPitch += this.aR - var4;
	}

	public void onUpdate() {
		this.onEntityUpdate();
	}

	public void onEntityUpdate() {
		if(this.riddenByEntity != null && this.riddenByEntity.field_9293_aM) {
			this.riddenByEntity = null;
		}

		++this.bs;
		this.height = this.prevDistanceWalkedModified;
		this.aI = this.prevPosZ;
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevRotationYaw = this.rotationYaw;
		this.rotationPitch = this.aR;
		if(this.handleWaterMovement()) {
			if(!this.bw && !this.isFirstUpdate) {
				float var1 = MathHelper.sqrt_double(this.posZ * this.posZ * (double)0.2F + this.motionX * this.motionX + this.motionY * this.motionY * (double)0.2F) * 0.2F;
				if(var1 > 1.0F) {
					var1 = 1.0F;
				}

				this.aH.playSoundAtEntity(this, "random.splash", var1, 1.0F + (this.br.nextFloat() - this.br.nextFloat()) * 0.4F);
				float var2 = (float)MathHelper.floor_double(this.aV.minY);

				int var3;
				float var4;
				float var5;
				for(var3 = 0; (float)var3 < 1.0F + this.yOffset * 20.0F; ++var3) {
					var4 = (this.br.nextFloat() * 2.0F - 1.0F) * this.yOffset;
					var5 = (this.br.nextFloat() * 2.0F - 1.0F) * this.yOffset;
					this.aH.spawnParticle("bubble", this.prevPosZ + (double)var4, (double)(var2 + 1.0F), this.posY + (double)var5, this.posZ, this.motionX - (double)(this.br.nextFloat() * 0.2F), this.motionY);
				}

				for(var3 = 0; (float)var3 < 1.0F + this.yOffset * 20.0F; ++var3) {
					var4 = (this.br.nextFloat() * 2.0F - 1.0F) * this.yOffset;
					var5 = (this.br.nextFloat() * 2.0F - 1.0F) * this.yOffset;
					this.aH.spawnParticle("splash", this.prevPosZ + (double)var4, (double)(var2 + 1.0F), this.posY + (double)var5, this.posZ, this.motionX, this.motionY);
				}
			}

			this.distanceWalkedModified = 0.0F;
			this.bw = true;
			this.fireResistance = 0;
		} else {
			this.bw = false;
		}

		if(this.aH.multiplayerWorld) {
			this.fireResistance = 0;
		} else if(this.fireResistance > 0) {
			if(this.bB) {
				this.fireResistance -= 4;
				if(this.fireResistance < 0) {
					this.fireResistance = 0;
				}
			} else {
				if(this.fireResistance % 20 == 0) {
					this.attackEntityFrom((Entity)null, 1);
				}

				--this.fireResistance;
			}
		}

		if(this.handleLavaMovement()) {
			this.setOnFireFromLava();
		}

		if(this.posX < -64.0D) {
			this.kill();
		}

		if(!this.aH.multiplayerWorld) {
			this.setEntityFlag(0, this.fireResistance > 0);
			this.setEntityFlag(2, this.riddenByEntity != null);
		}

		this.isFirstUpdate = false;
	}

	protected void setOnFireFromLava() {
		if(!this.bB) {
			this.attackEntityFrom((Entity)null, 4);
			this.fireResistance = 600;
		}

	}

	protected void kill() {
		this.setEntityDead();
	}

	public boolean isOffsetPositionInLiquid(double var1, double var3, double var5) {
		AxisAlignedBB var7 = this.aV.getOffsetBoundingBox(var1, var3, var5);
		List var8 = this.aH.getCollidingBoundingBoxes(this, var7);
		return var8.size() > 0 ? false : !this.aH.getIsAnyLiquid(var7);
	}

	public void moveEntity(double var1, double var3, double var5) {
		if(this.bp) {
			this.aV.offset(var1, var3, var5);
			this.prevPosZ = (this.aV.minX + this.aV.maxX) / 2.0D;
			this.posX = this.aV.minY + (double)this.be - (double)this.bn;
			this.posY = (this.aV.minZ + this.aV.maxZ) / 2.0D;
		} else {
			double var7 = this.prevPosZ;
			double var9 = this.posY;
			if(this.beenAttacked) {
				this.beenAttacked = false;
				var1 *= 0.25D;
				var3 *= (double)0.05F;
				var5 *= 0.25D;
				this.posZ = 0.0D;
				this.motionX = 0.0D;
				this.motionY = 0.0D;
			}

			double var11 = var1;
			double var13 = var3;
			double var15 = var5;
			AxisAlignedBB var17 = this.aV.copy();
			boolean var18 = this.aW && this.isSneaking();
			if(var18) {
				double var19;
				for(var19 = 0.05D; var1 != 0.0D && this.aH.getCollidingBoundingBoxes(this, this.aV.getOffsetBoundingBox(var1, -1.0D, 0.0D)).size() == 0; var11 = var1) {
					if(var1 < var19 && var1 >= -var19) {
						var1 = 0.0D;
					} else if(var1 > 0.0D) {
						var1 -= var19;
					} else {
						var1 += var19;
					}
				}

				for(; var5 != 0.0D && this.aH.getCollidingBoundingBoxes(this, this.aV.getOffsetBoundingBox(0.0D, -1.0D, var5)).size() == 0; var15 = var5) {
					if(var5 < var19 && var5 >= -var19) {
						var5 = 0.0D;
					} else if(var5 > 0.0D) {
						var5 -= var19;
					} else {
						var5 += var19;
					}
				}
			}

			List var35 = this.aH.getCollidingBoundingBoxes(this, this.aV.addCoord(var1, var3, var5));

			for(int var20 = 0; var20 < var35.size(); ++var20) {
				var3 = ((AxisAlignedBB)var35.get(var20)).calculateYOffset(this.aV, var3);
			}

			this.aV.offset(0.0D, var3, 0.0D);
			if(!this.isInWeb && var13 != var3) {
				var5 = 0.0D;
				var3 = var5;
				var1 = var5;
			}

			boolean var36 = this.aW || var13 != var3 && var13 < 0.0D;

			int var21;
			for(var21 = 0; var21 < var35.size(); ++var21) {
				var1 = ((AxisAlignedBB)var35.get(var21)).calculateXOffset(this.aV, var1);
			}

			this.aV.offset(var1, 0.0D, 0.0D);
			if(!this.isInWeb && var11 != var1) {
				var5 = 0.0D;
				var3 = var5;
				var1 = var5;
			}

			for(var21 = 0; var21 < var35.size(); ++var21) {
				var5 = ((AxisAlignedBB)var35.get(var21)).calculateZOffset(this.aV, var5);
			}

			this.aV.offset(0.0D, 0.0D, var5);
			if(!this.isInWeb && var15 != var5) {
				var5 = 0.0D;
				var3 = var5;
				var1 = var5;
			}

			double var23;
			int var28;
			double var37;
			if(this.ySize > 0.0F && var36 && (var18 || this.bn < 0.05F) && (var11 != var1 || var15 != var5)) {
				var37 = var1;
				var23 = var3;
				double var25 = var5;
				var1 = var11;
				var3 = (double)this.ySize;
				var5 = var15;
				AxisAlignedBB var27 = this.aV.copy();
				this.aV.setBB(var17);
				var35 = this.aH.getCollidingBoundingBoxes(this, this.aV.addCoord(var11, var3, var15));

				for(var28 = 0; var28 < var35.size(); ++var28) {
					var3 = ((AxisAlignedBB)var35.get(var28)).calculateYOffset(this.aV, var3);
				}

				this.aV.offset(0.0D, var3, 0.0D);
				if(!this.isInWeb && var13 != var3) {
					var5 = 0.0D;
					var3 = var5;
					var1 = var5;
				}

				for(var28 = 0; var28 < var35.size(); ++var28) {
					var1 = ((AxisAlignedBB)var35.get(var28)).calculateXOffset(this.aV, var1);
				}

				this.aV.offset(var1, 0.0D, 0.0D);
				if(!this.isInWeb && var11 != var1) {
					var5 = 0.0D;
					var3 = var5;
					var1 = var5;
				}

				for(var28 = 0; var28 < var35.size(); ++var28) {
					var5 = ((AxisAlignedBB)var35.get(var28)).calculateZOffset(this.aV, var5);
				}

				this.aV.offset(0.0D, 0.0D, var5);
				if(!this.isInWeb && var15 != var5) {
					var5 = 0.0D;
					var3 = var5;
					var1 = var5;
				}

				if(!this.isInWeb && var13 != var3) {
					var5 = 0.0D;
					var3 = var5;
					var1 = var5;
				} else {
					var3 = (double)(-this.ySize);

					for(var28 = 0; var28 < var35.size(); ++var28) {
						var3 = ((AxisAlignedBB)var35.get(var28)).calculateYOffset(this.aV, var3);
					}

					this.aV.offset(0.0D, var3, 0.0D);
				}

				if(var37 * var37 + var25 * var25 >= var1 * var1 + var5 * var5) {
					var1 = var37;
					var3 = var23;
					var5 = var25;
					this.aV.setBB(var27);
				} else {
					double var41 = this.aV.minY - (double)((int)this.aV.minY);
					if(var41 > 0.0D) {
						this.bn = (float)((double)this.bn + var41 + 0.01D);
					}
				}
			}

			this.prevPosZ = (this.aV.minX + this.aV.maxX) / 2.0D;
			this.posX = this.aV.minY + (double)this.be - (double)this.bn;
			this.posY = (this.aV.minZ + this.aV.maxZ) / 2.0D;
			this.onGround = var11 != var1 || var15 != var5;
			this.isCollidedHorizontally = var13 != var3;
			this.aW = var13 != var3 && var13 < 0.0D;
			this.isCollidedVertically = this.onGround || this.isCollidedHorizontally;
			this.updateFallState(var3, this.aW);
			if(var11 != var1) {
				this.posZ = 0.0D;
			}

			if(var13 != var3) {
				this.motionX = 0.0D;
			}

			if(var15 != var5) {
				this.motionY = 0.0D;
			}

			var37 = this.prevPosZ - var7;
			var23 = this.posY - var9;
			int var26;
			int var38;
			int var39;
			if(this.canTriggerWalking() && !var18 && this.riddenByEntity == null) {
				this.prevDistanceWalkedModified = (float)((double)this.prevDistanceWalkedModified + (double)MathHelper.sqrt_double(var37 * var37 + var23 * var23) * 0.6D);
				var38 = MathHelper.floor_double(this.prevPosZ);
				var26 = MathHelper.floor_double(this.posX - (double)0.2F - (double)this.be);
				var39 = MathHelper.floor_double(this.posY);
				var28 = this.aH.getBlockId(var38, var26, var39);
				if(this.aH.getBlockId(var38, var26 - 1, var39) == Block.fence.blockID) {
					var28 = this.aH.getBlockId(var38, var26 - 1, var39);
				}

				if(this.prevDistanceWalkedModified > (float)this.nextStepDistance && var28 > 0) {
					++this.nextStepDistance;
					StepSound var29 = Block.blocksList[var28].stepSound;
					if(this.aH.getBlockId(var38, var26 + 1, var39) == Block.snow.blockID) {
						var29 = Block.snow.stepSound;
						this.aH.playSoundAtEntity(this, var29.func_1145_d(), var29.getVolume() * 0.15F, var29.getPitch());
					} else if(!Block.blocksList[var28].blockMaterial.getIsLiquid()) {
						this.aH.playSoundAtEntity(this, var29.func_1145_d(), var29.getVolume() * 0.15F, var29.getPitch());
					}

					Block.blocksList[var28].onEntityWalking(this.aH, var38, var26, var39, this);
				}
			}

			var38 = MathHelper.floor_double(this.aV.minX + 0.001D);
			var26 = MathHelper.floor_double(this.aV.minY + 0.001D);
			var39 = MathHelper.floor_double(this.aV.minZ + 0.001D);
			var28 = MathHelper.floor_double(this.aV.maxX - 0.001D);
			int var40 = MathHelper.floor_double(this.aV.maxY - 0.001D);
			int var30 = MathHelper.floor_double(this.aV.maxZ - 0.001D);
			if(this.aH.checkChunksExist(var38, var26, var39, var28, var40, var30)) {
				for(int var31 = var38; var31 <= var28; ++var31) {
					for(int var32 = var26; var32 <= var40; ++var32) {
						for(int var33 = var39; var33 <= var30; ++var33) {
							int var34 = this.aH.getBlockId(var31, var32, var33);
							if(var34 > 0) {
								Block.blocksList[var34].onEntityCollidedWithBlock(this.aH, var31, var32, var33, this);
							}
						}
					}
				}
			}

			this.bn *= 0.4F;
			boolean var42 = this.isWet();
			if(this.aH.isBoundingBoxBurning(this.aV.func_28195_e(0.001D, 0.001D, 0.001D))) {
				this.dealFireDamage(1);
				if(!var42) {
					++this.fireResistance;
					if(this.fireResistance == 0) {
						this.fireResistance = 300;
					}
				}
			} else if(this.fireResistance <= 0) {
				this.fireResistance = -this.ticksExisted;
			}

			if(var42 && this.fireResistance > 0) {
				this.aH.playSoundAtEntity(this, "random.fizz", 0.7F, 1.6F + (this.br.nextFloat() - this.br.nextFloat()) * 0.4F);
				this.fireResistance = -this.ticksExisted;
			}

		}
	}

	protected boolean canTriggerWalking() {
		return true;
	}

	protected void updateFallState(double var1, boolean var3) {
		if(var3) {
			if(this.distanceWalkedModified > 0.0F) {
				this.fall(this.distanceWalkedModified);
				this.distanceWalkedModified = 0.0F;
			}
		} else if(var1 < 0.0D) {
			this.distanceWalkedModified = (float)((double)this.distanceWalkedModified - var1);
		}

	}

	public AxisAlignedBB getBoundingBox() {
		return null;
	}

	protected void dealFireDamage(int var1) {
		if(!this.bB) {
			this.attackEntityFrom((Entity)null, var1);
		}

	}

	protected void fall(float var1) {
		if(this.aF != null) {
			this.aF.fall(var1);
		}

	}

	public boolean isWet() {
		return this.bw || this.aH.canBlockBeRainedOn(MathHelper.floor_double(this.prevPosZ), MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY));
	}

	public boolean isInWater() {
		return this.bw;
	}

	public boolean handleWaterMovement() {
		return this.aH.handleMaterialAcceleration(this.aV.expand(0.0D, (double)-0.4F, 0.0D).func_28195_e(0.001D, 0.001D, 0.001D), Material.water, this);
	}

	public boolean isInsideOfMaterial(Material var1) {
		double var2 = this.posX + (double)this.getEyeHeight();
		int var4 = MathHelper.floor_double(this.prevPosZ);
		int var5 = MathHelper.floor_float((float)MathHelper.floor_double(var2));
		int var6 = MathHelper.floor_double(this.posY);
		int var7 = this.aH.getBlockId(var4, var5, var6);
		if(var7 != 0 && Block.blocksList[var7].blockMaterial == var1) {
			float var8 = BlockFluid.getPercentAir(this.aH.getBlockMetadata(var4, var5, var6)) - 1.0F / 9.0F;
			float var9 = (float)(var5 + 1) - var8;
			return var2 < (double)var9;
		} else {
			return false;
		}
	}

	public float getEyeHeight() {
		return 0.0F;
	}

	public boolean handleLavaMovement() {
		return this.aH.isMaterialInBB(this.aV.expand((double)-0.1F, (double)-0.4F, (double)-0.1F), Material.lava);
	}

	public void moveFlying(float var1, float var2, float var3) {
		float var4 = MathHelper.sqrt_float(var1 * var1 + var2 * var2);
		if(var4 >= 0.01F) {
			if(var4 < 1.0F) {
				var4 = 1.0F;
			}

			var4 = var3 / var4;
			var1 *= var4;
			var2 *= var4;
			float var5 = MathHelper.sin(this.aR * (float)Math.PI / 180.0F);
			float var6 = MathHelper.cos(this.aR * (float)Math.PI / 180.0F);
			this.posZ += (double)(var1 * var6 - var2 * var5);
			this.motionY += (double)(var2 * var6 + var1 * var5);
		}
	}

	public float getEntityBrightness(float var1) {
		int var2 = MathHelper.floor_double(this.prevPosZ);
		double var3 = (this.aV.maxY - this.aV.minY) * 0.66D;
		int var5 = MathHelper.floor_double(this.posX - (double)this.be + var3);
		int var6 = MathHelper.floor_double(this.posY);
		return this.aH.checkChunksExist(MathHelper.floor_double(this.aV.minX), MathHelper.floor_double(this.aV.minY), MathHelper.floor_double(this.aV.minZ), MathHelper.floor_double(this.aV.maxX), MathHelper.floor_double(this.aV.maxY), MathHelper.floor_double(this.aV.maxZ)) ? this.aH.getLightBrightness(var2, var5, var6) : 0.0F;
	}

	public void setWorld(World var1) {
		this.aH = var1;
	}

	public void setPositionAndRotation(double var1, double var3, double var5, float var7, float var8) {
		this.aI = this.prevPosZ = var1;
		this.prevPosX = this.posX = var3;
		this.prevPosY = this.posY = var5;
		this.rotationPitch = this.aR = var7;
		this.prevRotationYaw = this.rotationYaw = var8;
		this.bn = 0.0F;
		double var9 = (double)(this.rotationPitch - var7);
		if(var9 < -180.0D) {
			this.rotationPitch += 360.0F;
		}

		if(var9 >= 180.0D) {
			this.rotationPitch -= 360.0F;
		}

		this.setPosition(this.prevPosZ, this.posX, this.posY);
		this.setRotation(var7, var8);
	}

	public void setLocationAndAngles(double var1, double var3, double var5, float var7, float var8) {
		this.bk = this.aI = this.prevPosZ = var1;
		this.lastTickPosX = this.prevPosX = this.posX = var3 + (double)this.be;
		this.lastTickPosY = this.prevPosY = this.posY = var5;
		this.aR = var7;
		this.rotationYaw = var8;
		this.setPosition(this.prevPosZ, this.posX, this.posY);
	}

	public float getDistanceToEntity(Entity var1) {
		float var2 = (float)(this.prevPosZ - var1.prevPosZ);
		float var3 = (float)(this.posX - var1.posX);
		float var4 = (float)(this.posY - var1.posY);
		return MathHelper.sqrt_float(var2 * var2 + var3 * var3 + var4 * var4);
	}

	public double getDistanceSq(double var1, double var3, double var5) {
		double var7 = this.prevPosZ - var1;
		double var9 = this.posX - var3;
		double var11 = this.posY - var5;
		return var7 * var7 + var9 * var9 + var11 * var11;
	}

	public double getDistance(double var1, double var3, double var5) {
		double var7 = this.prevPosZ - var1;
		double var9 = this.posX - var3;
		double var11 = this.posY - var5;
		return (double)MathHelper.sqrt_double(var7 * var7 + var9 * var9 + var11 * var11);
	}

	public double getDistanceSqToEntity(Entity var1) {
		double var2 = this.prevPosZ - var1.prevPosZ;
		double var4 = this.posX - var1.posX;
		double var6 = this.posY - var1.posY;
		return var2 * var2 + var4 * var4 + var6 * var6;
	}

	public void onCollideWithPlayer(EntityPlayer var1) {
	}

	public void applyEntityCollision(Entity var1) {
		if(var1.aF != this && var1.riddenByEntity != this) {
			double var2 = var1.prevPosZ - this.prevPosZ;
			double var4 = var1.posY - this.posY;
			double var6 = MathHelper.abs_max(var2, var4);
			if(var6 >= (double)0.01F) {
				var6 = (double)MathHelper.sqrt_double(var6);
				var2 /= var6;
				var4 /= var6;
				double var8 = 1.0D / var6;
				if(var8 > 1.0D) {
					var8 = 1.0D;
				}

				var2 *= var8;
				var4 *= var8;
				var2 *= (double)0.05F;
				var4 *= (double)0.05F;
				var2 *= (double)(1.0F - this.bq);
				var4 *= (double)(1.0F - this.bq);
				this.addVelocity(-var2, 0.0D, -var4);
				var1.addVelocity(var2, 0.0D, var4);
			}

		}
	}

	public void addVelocity(double var1, double var3, double var5) {
		this.posZ += var1;
		this.motionX += var3;
		this.motionY += var5;
	}

	protected void setBeenAttacked() {
		this.isCollided = true;
	}

	public boolean attackEntityFrom(Entity var1, int var2) {
		this.setBeenAttacked();
		return false;
	}

	public boolean canBeCollidedWith() {
		return false;
	}

	public boolean canBePushed() {
		return false;
	}

	public void addToPlayerScore(Entity var1, int var2) {
	}

	public boolean isInRangeToRenderVec3D(Vec3D var1) {
		double var2 = this.prevPosZ - var1.xCoord;
		double var4 = this.posX - var1.yCoord;
		double var6 = this.posY - var1.zCoord;
		double var8 = var2 * var2 + var4 * var4 + var6 * var6;
		return this.isInRangeToRenderDist(var8);
	}

	public boolean isInRangeToRenderDist(double var1) {
		double var3 = this.aV.getAverageEdgeLength();
		var3 *= 64.0D * this.aD;
		return var1 < var3 * var3;
	}

	public String getEntityTexture() {
		return null;
	}

	public boolean addEntityID(NBTTagCompound var1) {
		String var2 = this.getEntityString();
		if(!this.field_9293_aM && var2 != null) {
			var1.setString("id", var2);
			this.writeToNBT(var1);
			return true;
		} else {
			return false;
		}
	}

	public void writeToNBT(NBTTagCompound var1) {
		var1.setTag("Pos", this.newDoubleNBTList(new double[]{this.prevPosZ, this.posX + (double)this.be - (double)this.bn, this.posY}));
		var1.setTag("Motion", this.newDoubleNBTList(new double[]{this.posZ, this.motionX, this.motionY}));
		var1.setTag("Rotation", this.newFloatNBTList(new float[]{this.aR, this.rotationYaw}));
		var1.setFloat("FallDistance", this.distanceWalkedModified);
		var1.setShort("Fire", (short)this.fireResistance);
		var1.setShort("Air", (short)this.field_9306_bj);
		var1.setBoolean("OnGround", this.aW);
		this.writeEntityToNBT(var1);
	}

	public void readFromNBT(NBTTagCompound var1) {
		NBTTagList var2 = var1.getTagList("Pos");
		NBTTagList var3 = var1.getTagList("Motion");
		NBTTagList var4 = var1.getTagList("Rotation");
		this.setPosition(0.0D, 0.0D, 0.0D);
		this.posZ = ((NBTTagDouble)var3.tagAt(0)).doubleValue;
		this.motionX = ((NBTTagDouble)var3.tagAt(1)).doubleValue;
		this.motionY = ((NBTTagDouble)var3.tagAt(2)).doubleValue;
		if(Math.abs(this.posZ) > 10.0D) {
			this.posZ = 0.0D;
		}

		if(Math.abs(this.motionX) > 10.0D) {
			this.motionX = 0.0D;
		}

		if(Math.abs(this.motionY) > 10.0D) {
			this.motionY = 0.0D;
		}

		this.aI = this.bk = this.prevPosZ = ((NBTTagDouble)var2.tagAt(0)).doubleValue;
		this.prevPosX = this.lastTickPosX = this.posX = ((NBTTagDouble)var2.tagAt(1)).doubleValue;
		this.prevPosY = this.lastTickPosY = this.posY = ((NBTTagDouble)var2.tagAt(2)).doubleValue;
		this.rotationPitch = this.aR = ((NBTTagFloat)var4.tagAt(0)).floatValue;
		this.prevRotationYaw = this.rotationYaw = ((NBTTagFloat)var4.tagAt(1)).floatValue;
		this.distanceWalkedModified = var1.getFloat("FallDistance");
		this.fireResistance = var1.getShort("Fire");
		this.field_9306_bj = var1.getShort("Air");
		this.aW = var1.getBoolean("OnGround");
		this.setPosition(this.prevPosZ, this.posX, this.posY);
		this.setRotation(this.aR, this.rotationYaw);
		this.readEntityFromNBT(var1);
	}

	protected final String getEntityString() {
		return EntityList.getEntityString(this);
	}

	protected abstract void readEntityFromNBT(NBTTagCompound var1);

	protected abstract void writeEntityToNBT(NBTTagCompound var1);

	protected NBTTagList newDoubleNBTList(double... var1) {
		NBTTagList var2 = new NBTTagList();
		double[] var3 = var1;
		int var4 = var1.length;

		for(int var5 = 0; var5 < var4; ++var5) {
			double var6 = var3[var5];
			var2.setTag(new NBTTagDouble(var6));
		}

		return var2;
	}

	protected NBTTagList newFloatNBTList(float... var1) {
		NBTTagList var2 = new NBTTagList();
		float[] var3 = var1;
		int var4 = var1.length;

		for(int var5 = 0; var5 < var4; ++var5) {
			float var6 = var3[var5];
			var2.setTag(new NBTTagFloat(var6));
		}

		return var2;
	}

	public float getShadowSize() {
		return this.width / 2.0F;
	}

	public EntityItem dropItem(int var1, int var2) {
		return this.dropItemWithOffset(var1, var2, 0.0F);
	}

	public EntityItem dropItemWithOffset(int var1, int var2, float var3) {
		return this.entityDropItem(new ItemStack(var1, var2, 0), var3);
	}

	public EntityItem entityDropItem(ItemStack var1, float var2) {
		EntityItem var3 = new EntityItem(this.aH, this.prevPosZ, this.posX + (double)var2, this.posY, var1);
		var3.delayBeforeCanPickup = 10;
		this.aH.entityJoinedWorld(var3);
		return var3;
	}

	public boolean isEntityAlive() {
		return !this.field_9293_aM;
	}

	public boolean isEntityInsideOpaqueBlock() {
		for(int var1 = 0; var1 < 8; ++var1) {
			float var2 = ((float)((var1 >> 0) % 2) - 0.5F) * this.yOffset * 0.9F;
			float var3 = ((float)((var1 >> 1) % 2) - 0.5F) * 0.1F;
			float var4 = ((float)((var1 >> 2) % 2) - 0.5F) * this.yOffset * 0.9F;
			int var5 = MathHelper.floor_double(this.prevPosZ + (double)var2);
			int var6 = MathHelper.floor_double(this.posX + (double)this.getEyeHeight() + (double)var3);
			int var7 = MathHelper.floor_double(this.posY + (double)var4);
			if(this.aH.func_28100_h(var5, var6, var7)) {
				return true;
			}
		}

		return false;
	}

	public boolean interact(EntityPlayer var1) {
		return false;
	}

	public AxisAlignedBB getCollisionBox(Entity var1) {
		return null;
	}

	public void updateRidden() {
		if(this.riddenByEntity.field_9293_aM) {
			this.riddenByEntity = null;
		} else {
			this.posZ = 0.0D;
			this.motionX = 0.0D;
			this.motionY = 0.0D;
			this.onUpdate();
			if(this.riddenByEntity != null) {
				this.riddenByEntity.updateRiderPosition();
				this.entityRiderYawDelta += (double)(this.riddenByEntity.aR - this.riddenByEntity.rotationPitch);

				for(this.entityRiderPitchDelta += (double)(this.riddenByEntity.rotationYaw - this.riddenByEntity.prevRotationYaw); this.entityRiderYawDelta >= 180.0D; this.entityRiderYawDelta -= 360.0D) {
				}

				while(this.entityRiderYawDelta < -180.0D) {
					this.entityRiderYawDelta += 360.0D;
				}

				while(this.entityRiderPitchDelta >= 180.0D) {
					this.entityRiderPitchDelta -= 360.0D;
				}

				while(this.entityRiderPitchDelta < -180.0D) {
					this.entityRiderPitchDelta += 360.0D;
				}

				double var1 = this.entityRiderYawDelta * 0.5D;
				double var3 = this.entityRiderPitchDelta * 0.5D;
				float var5 = 10.0F;
				if(var1 > (double)var5) {
					var1 = (double)var5;
				}

				if(var1 < (double)(-var5)) {
					var1 = (double)(-var5);
				}

				if(var3 > (double)var5) {
					var3 = (double)var5;
				}

				if(var3 < (double)(-var5)) {
					var3 = (double)(-var5);
				}

				this.entityRiderYawDelta -= var1;
				this.entityRiderPitchDelta -= var3;
				this.aR = (float)((double)this.aR + var1);
				this.rotationYaw = (float)((double)this.rotationYaw + var3);
			}
		}
	}

	public void updateRiderPosition() {
		this.aF.setPosition(this.prevPosZ, this.posX + this.getMountedYOffset() + this.aF.getYOffset(), this.posY);
	}

	public double getYOffset() {
		return (double)this.be;
	}

	public double getMountedYOffset() {
		return (double)this.width * 0.75D;
	}

	public void mountEntity(Entity var1) {
		this.entityRiderPitchDelta = 0.0D;
		this.entityRiderYawDelta = 0.0D;
		if(var1 == null) {
			if(this.riddenByEntity != null) {
				this.setLocationAndAngles(this.riddenByEntity.prevPosZ, this.riddenByEntity.aV.minY + (double)this.riddenByEntity.width, this.riddenByEntity.posY, this.aR, this.rotationYaw);
				this.riddenByEntity.aF = null;
			}

			this.riddenByEntity = null;
		} else if(this.riddenByEntity == var1) {
			this.riddenByEntity.aF = null;
			this.riddenByEntity = null;
			this.setLocationAndAngles(var1.prevPosZ, var1.aV.minY + (double)var1.width, var1.posY, this.aR, this.rotationYaw);
		} else {
			if(this.riddenByEntity != null) {
				this.riddenByEntity.aF = null;
			}

			if(var1.aF != null) {
				var1.aF.riddenByEntity = null;
			}

			this.riddenByEntity = var1;
			var1.aF = this;
		}
	}

	public void setPositionAndRotation2(double var1, double var3, double var5, float var7, float var8, int var9) {
		this.setPosition(var1, var3, var5);
		this.setRotation(var7, var8);
	}

	public float getCollisionBorderSize() {
		return 0.1F;
	}

	public Vec3D getLookVec() {
		return null;
	}

	public void setInPortal() {
	}

	public void setVelocity(double var1, double var3, double var5) {
		this.posZ = var1;
		this.motionX = var3;
		this.motionY = var5;
	}

	public void handleHealthUpdate(byte var1) {
	}

	public void performHurtAnimation() {
	}

	public void updateCloak() {
	}

	public void outfitWithItem(int var1, int var2, int var3) {
	}

	public boolean isBurning() {
		return this.fireResistance > 0 || this.getEntityFlag(0);
	}

	public boolean isRiding() {
		return this.riddenByEntity != null || this.getEntityFlag(2);
	}

	public boolean isSneaking() {
		return this.getEntityFlag(1);
	}

	protected boolean getEntityFlag(int var1) {
		return (this.bC.getWatchableObjectByte(0) & 1 << var1) != 0;
	}

	protected void setEntityFlag(int var1, boolean var2) {
		byte var3 = this.bC.getWatchableObjectByte(0);
		if(var2) {
			this.bC.updateObject(0, Byte.valueOf((byte)(var3 | 1 << var1)));
		} else {
			this.bC.updateObject(0, Byte.valueOf((byte)(var3 & ~(1 << var1))));
		}

	}

	public void onStruckByLightning(EntityLightningBolt var1) {
		this.dealFireDamage(5);
		++this.fireResistance;
		if(this.fireResistance == 0) {
			this.fireResistance = 300;
		}

	}

	public void onKillEntity(EntityLiving var1) {
	}

	private boolean a(int var1, int var2, int var3) {
		int var4 = this.aH.getBlockId(var1, var2, var3);
		if(var4 == 0) {
			return false;
		} else {
			this.f.clear();
			Block.blocksList[var4].getCollidingBoundingBoxes(this.aH, var1, var2, var3, this.aV, this.f);
			return this.f.size() > 0;
		}
	}

	protected boolean func_28014_c(double var1, double var3, double var5) {
		int var7 = MathHelper.floor_double(var1);
		int var8 = MathHelper.floor_double(var3);
		int var9 = MathHelper.floor_double(var5);
		double var10 = var1 - (double)var7;
		double var12 = var3 - (double)var8;
		double var14 = var5 - (double)var9;
		if(this.a(var7, var8, var9)) {
			boolean var16 = !this.a(var7 - 1, var8, var9);
			boolean var17 = !this.a(var7 + 1, var8, var9);
			boolean var18 = !this.a(var7, var8 - 1, var9);
			boolean var19 = !this.a(var7, var8 + 1, var9);
			boolean var20 = !this.a(var7, var8, var9 - 1);
			boolean var21 = !this.a(var7, var8, var9 + 1);
			byte var22 = -1;
			double var23 = 9999.0D;
			if(var16 && var10 < var23) {
				var23 = var10;
				var22 = 0;
			}

			if(var17 && 1.0D - var10 < var23) {
				var23 = 1.0D - var10;
				var22 = 1;
			}

			if(var18 && var12 < var23) {
				var23 = var12;
				var22 = 2;
			}

			if(var19 && 1.0D - var12 < var23) {
				var23 = 1.0D - var12;
				var22 = 3;
			}

			if(var20 && var14 < var23) {
				var23 = var14;
				var22 = 4;
			}

			if(var21 && 1.0D - var14 < var23) {
				var23 = 1.0D - var14;
				var22 = 5;
			}

			float var25 = this.br.nextFloat() * 0.2F + 0.1F;
			if(var22 == 0) {
				this.posZ = (double)(-var25);
			}

			if(var22 == 1) {
				this.posZ = (double)var25;
			}

			if(var22 == 2) {
				this.motionX = (double)(-var25);
			}

			if(var22 == 3) {
				this.motionX = (double)var25;
			}

			if(var22 == 4) {
				this.motionY = (double)(-var25);
			}

			if(var22 == 5) {
				this.motionY = (double)var25;
			}
		}

		return false;
	}
}
