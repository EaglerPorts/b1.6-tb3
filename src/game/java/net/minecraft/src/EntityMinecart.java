package net.minecraft.src;

import java.util.List;

public class EntityMinecart extends Entity implements IInventory {
	private ItemStack[] cargoItems;
	public int minecartCurrentDamage;
	public int minecartTimeSinceHit;
	public int minecartRockDirection;
	private boolean field_856_i;
	public int minecartType;
	public int fuel;
	public double pushX;
	public double pushZ;
	private static final int[][][] field_855_j = new int[][][]{{{0, 0, -1}, {0, 0, 1}}, {{-1, 0, 0}, {1, 0, 0}}, {{-1, -1, 0}, {1, 0, 0}}, {{-1, 0, 0}, {1, -1, 0}}, {{0, 0, -1}, {0, -1, 1}}, {{0, -1, -1}, {0, 0, 1}}, {{0, 0, 1}, {1, 0, 0}}, {{0, 0, 1}, {-1, 0, 0}}, {{0, 0, -1}, {-1, 0, 0}}, {{0, 0, -1}, {1, 0, 0}}};
	private int field_9415_k;
	private double field_9414_l;
	private double field_9413_m;
	private double field_9412_n;
	private double field_9411_o;
	private double field_9410_p;
	private double field_9409_q;
	private double field_9408_r;
	private double field_9407_s;

	public EntityMinecart(World var1) {
		super(var1);
		this.cargoItems = new ItemStack[36];
		this.minecartCurrentDamage = 0;
		this.minecartTimeSinceHit = 0;
		this.minecartRockDirection = 1;
		this.field_856_i = false;
		this.aE = true;
		this.setSize(0.98F, 0.7F);
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
		return null;
	}

	public boolean canBePushed() {
		return true;
	}

	public EntityMinecart(World var1, double var2, double var4, double var6, int var8) {
		this(var1);
		this.setPosition(var2, var4 + (double)this.be, var6);
		this.posZ = 0.0D;
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.aI = var2;
		this.prevPosX = var4;
		this.prevPosY = var6;
		this.minecartType = var8;
	}

	public double getMountedYOffset() {
		return (double)this.width * 0.0D - (double)0.3F;
	}

	public boolean attackEntityFrom(Entity var1, int var2) {
		if(!this.aH.multiplayerWorld && !this.field_9293_aM) {
			this.minecartRockDirection = -this.minecartRockDirection;
			this.minecartTimeSinceHit = 10;
			this.setBeenAttacked();
			this.minecartCurrentDamage += var2 * 10;
			if(this.minecartCurrentDamage > 40) {
				if(this.aF != null) {
					this.aF.mountEntity(this);
				}

				this.setEntityDead();
				this.dropItemWithOffset(Item.minecartEmpty.shiftedIndex, 1, 0.0F);
				if(this.minecartType == 1) {
					EntityMinecart var3 = this;

					for(int var4 = 0; var4 < var3.getSizeInventory(); ++var4) {
						ItemStack var5 = var3.getStackInSlot(var4);
						if(var5 != null) {
							float var6 = this.br.nextFloat() * 0.8F + 0.1F;
							float var7 = this.br.nextFloat() * 0.8F + 0.1F;
							float var8 = this.br.nextFloat() * 0.8F + 0.1F;

							while(var5.stackSize > 0) {
								int var9 = this.br.nextInt(21) + 10;
								if(var9 > var5.stackSize) {
									var9 = var5.stackSize;
								}

								var5.stackSize -= var9;
								EntityItem var10 = new EntityItem(this.aH, this.prevPosZ + (double)var6, this.posX + (double)var7, this.posY + (double)var8, new ItemStack(var5.itemID, var9, var5.getItemDamage()));
								float var11 = 0.05F;
								var10.posZ = (double)((float)this.br.nextGaussian() * var11);
								var10.motionX = (double)((float)this.br.nextGaussian() * var11 + 0.2F);
								var10.motionY = (double)((float)this.br.nextGaussian() * var11);
								this.aH.entityJoinedWorld(var10);
							}
						}
					}

					this.dropItemWithOffset(Block.chest.blockID, 1, 0.0F);
				} else if(this.minecartType == 2) {
					this.dropItemWithOffset(Block.stoneOvenIdle.blockID, 1, 0.0F);
				}
			}

			return true;
		} else {
			return true;
		}
	}

	public void performHurtAnimation() {
		System.out.println("Animating hurt");
		this.minecartRockDirection = -this.minecartRockDirection;
		this.minecartTimeSinceHit = 10;
		this.minecartCurrentDamage += this.minecartCurrentDamage * 10;
	}

	public boolean canBeCollidedWith() {
		return !this.field_9293_aM;
	}

	public void setEntityDead() {
		for(int var1 = 0; var1 < this.getSizeInventory(); ++var1) {
			ItemStack var2 = this.getStackInSlot(var1);
			if(var2 != null) {
				float var3 = this.br.nextFloat() * 0.8F + 0.1F;
				float var4 = this.br.nextFloat() * 0.8F + 0.1F;
				float var5 = this.br.nextFloat() * 0.8F + 0.1F;

				while(var2.stackSize > 0) {
					int var6 = this.br.nextInt(21) + 10;
					if(var6 > var2.stackSize) {
						var6 = var2.stackSize;
					}

					var2.stackSize -= var6;
					EntityItem var7 = new EntityItem(this.aH, this.prevPosZ + (double)var3, this.posX + (double)var4, this.posY + (double)var5, new ItemStack(var2.itemID, var6, var2.getItemDamage()));
					float var8 = 0.05F;
					var7.posZ = (double)((float)this.br.nextGaussian() * var8);
					var7.motionX = (double)((float)this.br.nextGaussian() * var8 + 0.2F);
					var7.motionY = (double)((float)this.br.nextGaussian() * var8);
					this.aH.entityJoinedWorld(var7);
				}
			}
		}

		super.setEntityDead();
	}

	public void onUpdate() {
		if(this.minecartTimeSinceHit > 0) {
			--this.minecartTimeSinceHit;
		}

		if(this.minecartCurrentDamage > 0) {
			--this.minecartCurrentDamage;
		}

		double var7;
		if(this.aH.multiplayerWorld && this.field_9415_k > 0) {
			if(this.field_9415_k > 0) {
				double var46 = this.prevPosZ + (this.field_9414_l - this.prevPosZ) / (double)this.field_9415_k;
				double var47 = this.posX + (this.field_9413_m - this.posX) / (double)this.field_9415_k;
				double var5 = this.posY + (this.field_9412_n - this.posY) / (double)this.field_9415_k;

				for(var7 = this.field_9411_o - (double)this.aR; var7 < -180.0D; var7 += 360.0D) {
				}

				while(var7 >= 180.0D) {
					var7 -= 360.0D;
				}

				this.aR = (float)((double)this.aR + var7 / (double)this.field_9415_k);
				this.rotationYaw = (float)((double)this.rotationYaw + (this.field_9410_p - (double)this.rotationYaw) / (double)this.field_9415_k);
				--this.field_9415_k;
				this.setPosition(var46, var47, var5);
				this.setRotation(this.aR, this.rotationYaw);
			} else {
				this.setPosition(this.prevPosZ, this.posX, this.posY);
				this.setRotation(this.aR, this.rotationYaw);
			}

		} else {
			this.aI = this.prevPosZ;
			this.prevPosX = this.posX;
			this.prevPosY = this.posY;
			this.motionX -= (double)0.04F;
			int var1 = MathHelper.floor_double(this.prevPosZ);
			int var2 = MathHelper.floor_double(this.posX);
			int var3 = MathHelper.floor_double(this.posY);
			if(BlockRail.isRailBlockAt(this.aH, var1, var2 - 1, var3)) {
				--var2;
			}

			double var4 = 0.4D;
			boolean var6 = false;
			var7 = 1.0D / 128.0D;
			int var9 = this.aH.getBlockId(var1, var2, var3);
			if(BlockRail.isRailBlock(var9)) {
				Vec3D var10 = this.func_514_g(this.prevPosZ, this.posX, this.posY);
				int var11 = this.aH.getBlockMetadata(var1, var2, var3);
				this.posX = (double)var2;
				boolean var12 = false;
				boolean var13 = false;
				if(var9 == Block.railPowered.blockID) {
					var12 = (var11 & 8) != 0;
					var13 = !var12;
				}

				if(((BlockRail)Block.blocksList[var9]).getIsPowered()) {
					var11 &= 7;
				}

				if(var11 >= 2 && var11 <= 5) {
					this.posX = (double)(var2 + 1);
				}

				if(var11 == 2) {
					this.posZ -= var7;
				}

				if(var11 == 3) {
					this.posZ += var7;
				}

				if(var11 == 4) {
					this.motionY += var7;
				}

				if(var11 == 5) {
					this.motionY -= var7;
				}

				int[][] var14 = field_855_j[var11];
				double var15 = (double)(var14[1][0] - var14[0][0]);
				double var17 = (double)(var14[1][2] - var14[0][2]);
				double var19 = Math.sqrt(var15 * var15 + var17 * var17);
				double var21 = this.posZ * var15 + this.motionY * var17;
				if(var21 < 0.0D) {
					var15 = -var15;
					var17 = -var17;
				}

				double var23 = Math.sqrt(this.posZ * this.posZ + this.motionY * this.motionY);
				this.posZ = var23 * var15 / var19;
				this.motionY = var23 * var17 / var19;
				double var25;
				if(var13) {
					var25 = Math.sqrt(this.posZ * this.posZ + this.motionY * this.motionY);
					if(var25 < 0.03D) {
						this.posZ *= 0.0D;
						this.motionX *= 0.0D;
						this.motionY *= 0.0D;
					} else {
						this.posZ *= 0.5D;
						this.motionX *= 0.0D;
						this.motionY *= 0.5D;
					}
				}

				var25 = 0.0D;
				double var27 = (double)var1 + 0.5D + (double)var14[0][0] * 0.5D;
				double var29 = (double)var3 + 0.5D + (double)var14[0][2] * 0.5D;
				double var31 = (double)var1 + 0.5D + (double)var14[1][0] * 0.5D;
				double var33 = (double)var3 + 0.5D + (double)var14[1][2] * 0.5D;
				var15 = var31 - var27;
				var17 = var33 - var29;
				double var35;
				double var37;
				double var39;
				if(var15 == 0.0D) {
					this.prevPosZ = (double)var1 + 0.5D;
					var25 = this.posY - (double)var3;
				} else if(var17 == 0.0D) {
					this.posY = (double)var3 + 0.5D;
					var25 = this.prevPosZ - (double)var1;
				} else {
					var35 = this.prevPosZ - var27;
					var37 = this.posY - var29;
					var39 = (var35 * var15 + var37 * var17) * 2.0D;
					var25 = var39;
				}

				this.prevPosZ = var27 + var15 * var25;
				this.posY = var29 + var17 * var25;
				this.setPosition(this.prevPosZ, this.posX + (double)this.be, this.posY);
				var35 = this.posZ;
				var37 = this.motionY;
				if(this.aF != null) {
					var35 *= 0.75D;
					var37 *= 0.75D;
				}

				if(var35 < -var4) {
					var35 = -var4;
				}

				if(var35 > var4) {
					var35 = var4;
				}

				if(var37 < -var4) {
					var37 = -var4;
				}

				if(var37 > var4) {
					var37 = var4;
				}

				this.moveEntity(var35, 0.0D, var37);
				if(var14[0][1] != 0 && MathHelper.floor_double(this.prevPosZ) - var1 == var14[0][0] && MathHelper.floor_double(this.posY) - var3 == var14[0][2]) {
					this.setPosition(this.prevPosZ, this.posX + (double)var14[0][1], this.posY);
				} else if(var14[1][1] != 0 && MathHelper.floor_double(this.prevPosZ) - var1 == var14[1][0] && MathHelper.floor_double(this.posY) - var3 == var14[1][2]) {
					this.setPosition(this.prevPosZ, this.posX + (double)var14[1][1], this.posY);
				}

				if(this.aF != null) {
					this.posZ *= (double)0.997F;
					this.motionX *= 0.0D;
					this.motionY *= (double)0.997F;
				} else {
					if(this.minecartType == 2) {
						var39 = (double)MathHelper.sqrt_double(this.pushX * this.pushX + this.pushZ * this.pushZ);
						if(var39 > 0.01D) {
							var6 = true;
							this.pushX /= var39;
							this.pushZ /= var39;
							double var41 = 0.04D;
							this.posZ *= (double)0.8F;
							this.motionX *= 0.0D;
							this.motionY *= (double)0.8F;
							this.posZ += this.pushX * var41;
							this.motionY += this.pushZ * var41;
						} else {
							this.posZ *= (double)0.9F;
							this.motionX *= 0.0D;
							this.motionY *= (double)0.9F;
						}
					}

					this.posZ *= (double)0.96F;
					this.motionX *= 0.0D;
					this.motionY *= (double)0.96F;
				}

				Vec3D var52 = this.func_514_g(this.prevPosZ, this.posX, this.posY);
				if(var52 != null && var10 != null) {
					double var40 = (var10.yCoord - var52.yCoord) * 0.05D;
					var23 = Math.sqrt(this.posZ * this.posZ + this.motionY * this.motionY);
					if(var23 > 0.0D) {
						this.posZ = this.posZ / var23 * (var23 + var40);
						this.motionY = this.motionY / var23 * (var23 + var40);
					}

					this.setPosition(this.prevPosZ, var52.yCoord, this.posY);
				}

				int var53 = MathHelper.floor_double(this.prevPosZ);
				int var54 = MathHelper.floor_double(this.posY);
				if(var53 != var1 || var54 != var3) {
					var23 = Math.sqrt(this.posZ * this.posZ + this.motionY * this.motionY);
					this.posZ = var23 * (double)(var53 - var1);
					this.motionY = var23 * (double)(var54 - var3);
				}

				double var42;
				if(this.minecartType == 2) {
					var42 = (double)MathHelper.sqrt_double(this.pushX * this.pushX + this.pushZ * this.pushZ);
					if(var42 > 0.01D && this.posZ * this.posZ + this.motionY * this.motionY > 0.001D) {
						this.pushX /= var42;
						this.pushZ /= var42;
						if(this.pushX * this.posZ + this.pushZ * this.motionY < 0.0D) {
							this.pushX = 0.0D;
							this.pushZ = 0.0D;
						} else {
							this.pushX = this.posZ;
							this.pushZ = this.motionY;
						}
					}
				}

				if(var12) {
					var42 = Math.sqrt(this.posZ * this.posZ + this.motionY * this.motionY);
					if(var42 > 0.01D) {
						double var44 = 0.06D;
						this.posZ += this.posZ / var42 * var44;
						this.motionY += this.motionY / var42 * var44;
					} else if(var11 == 1) {
						if(this.aH.func_28100_h(var1 - 1, var2, var3)) {
							this.posZ = 0.02D;
						} else if(this.aH.func_28100_h(var1 + 1, var2, var3)) {
							this.posZ = -0.02D;
						}
					} else if(var11 == 0) {
						if(this.aH.func_28100_h(var1, var2, var3 - 1)) {
							this.motionY = 0.02D;
						} else if(this.aH.func_28100_h(var1, var2, var3 + 1)) {
							this.motionY = -0.02D;
						}
					}
				}
			} else {
				if(this.posZ < -var4) {
					this.posZ = -var4;
				}

				if(this.posZ > var4) {
					this.posZ = var4;
				}

				if(this.motionY < -var4) {
					this.motionY = -var4;
				}

				if(this.motionY > var4) {
					this.motionY = var4;
				}

				if(this.aW) {
					this.posZ *= 0.5D;
					this.motionX *= 0.5D;
					this.motionY *= 0.5D;
				}

				this.moveEntity(this.posZ, this.motionX, this.motionY);
				if(!this.aW) {
					this.posZ *= (double)0.95F;
					this.motionX *= (double)0.95F;
					this.motionY *= (double)0.95F;
				}
			}

			this.rotationYaw = 0.0F;
			double var48 = this.aI - this.prevPosZ;
			double var49 = this.prevPosY - this.posY;
			if(var48 * var48 + var49 * var49 > 0.001D) {
				this.aR = (float)(Math.atan2(var49, var48) * 180.0D / Math.PI);
				if(this.field_856_i) {
					this.aR += 180.0F;
				}
			}

			double var50;
			for(var50 = (double)(this.aR - this.rotationPitch); var50 >= 180.0D; var50 -= 360.0D) {
			}

			while(var50 < -180.0D) {
				var50 += 360.0D;
			}

			if(var50 < -170.0D || var50 >= 170.0D) {
				this.aR += 180.0F;
				this.field_856_i = !this.field_856_i;
			}

			this.setRotation(this.aR, this.rotationYaw);
			List var16 = this.aH.getEntitiesWithinAABBExcludingEntity(this, this.aV.expand((double)0.2F, 0.0D, (double)0.2F));
			if(var16 != null && var16.size() > 0) {
				for(int var51 = 0; var51 < var16.size(); ++var51) {
					Entity var18 = (Entity)var16.get(var51);
					if(var18 != this.aF && var18.canBePushed() && var18 instanceof EntityMinecart) {
						var18.applyEntityCollision(this);
					}
				}
			}

			if(this.aF != null && this.aF.field_9293_aM) {
				this.aF = null;
			}

			if(var6 && this.br.nextInt(4) == 0) {
				--this.fuel;
				if(this.fuel < 0) {
					this.pushX = this.pushZ = 0.0D;
				}

				this.aH.spawnParticle("largesmoke", this.prevPosZ, this.posX + 0.8D, this.posY, 0.0D, 0.0D, 0.0D);
			}

		}
	}

	public Vec3D func_515_a(double var1, double var3, double var5, double var7) {
		int var9 = MathHelper.floor_double(var1);
		int var10 = MathHelper.floor_double(var3);
		int var11 = MathHelper.floor_double(var5);
		if(BlockRail.isRailBlockAt(this.aH, var9, var10 - 1, var11)) {
			--var10;
		}

		int var12 = this.aH.getBlockId(var9, var10, var11);
		if(!BlockRail.isRailBlock(var12)) {
			return null;
		} else {
			int var13 = this.aH.getBlockMetadata(var9, var10, var11);
			if(((BlockRail)Block.blocksList[var12]).getIsPowered()) {
				var13 &= 7;
			}

			var3 = (double)var10;
			if(var13 >= 2 && var13 <= 5) {
				var3 = (double)(var10 + 1);
			}

			int[][] var14 = field_855_j[var13];
			double var15 = (double)(var14[1][0] - var14[0][0]);
			double var17 = (double)(var14[1][2] - var14[0][2]);
			double var19 = Math.sqrt(var15 * var15 + var17 * var17);
			var15 /= var19;
			var17 /= var19;
			var1 += var15 * var7;
			var5 += var17 * var7;
			if(var14[0][1] != 0 && MathHelper.floor_double(var1) - var9 == var14[0][0] && MathHelper.floor_double(var5) - var11 == var14[0][2]) {
				var3 += (double)var14[0][1];
			} else if(var14[1][1] != 0 && MathHelper.floor_double(var1) - var9 == var14[1][0] && MathHelper.floor_double(var5) - var11 == var14[1][2]) {
				var3 += (double)var14[1][1];
			}

			return this.func_514_g(var1, var3, var5);
		}
	}

	public Vec3D func_514_g(double var1, double var3, double var5) {
		int var7 = MathHelper.floor_double(var1);
		int var8 = MathHelper.floor_double(var3);
		int var9 = MathHelper.floor_double(var5);
		if(BlockRail.isRailBlockAt(this.aH, var7, var8 - 1, var9)) {
			--var8;
		}

		int var10 = this.aH.getBlockId(var7, var8, var9);
		if(BlockRail.isRailBlock(var10)) {
			int var11 = this.aH.getBlockMetadata(var7, var8, var9);
			var3 = (double)var8;
			if(((BlockRail)Block.blocksList[var10]).getIsPowered()) {
				var11 &= 7;
			}

			if(var11 >= 2 && var11 <= 5) {
				var3 = (double)(var8 + 1);
			}

			int[][] var12 = field_855_j[var11];
			double var13 = 0.0D;
			double var15 = (double)var7 + 0.5D + (double)var12[0][0] * 0.5D;
			double var17 = (double)var8 + 0.5D + (double)var12[0][1] * 0.5D;
			double var19 = (double)var9 + 0.5D + (double)var12[0][2] * 0.5D;
			double var21 = (double)var7 + 0.5D + (double)var12[1][0] * 0.5D;
			double var23 = (double)var8 + 0.5D + (double)var12[1][1] * 0.5D;
			double var25 = (double)var9 + 0.5D + (double)var12[1][2] * 0.5D;
			double var27 = var21 - var15;
			double var29 = (var23 - var17) * 2.0D;
			double var31 = var25 - var19;
			if(var27 == 0.0D) {
				var1 = (double)var7 + 0.5D;
				var13 = var5 - (double)var9;
			} else if(var31 == 0.0D) {
				var5 = (double)var9 + 0.5D;
				var13 = var1 - (double)var7;
			} else {
				double var33 = var1 - var15;
				double var35 = var5 - var19;
				double var37 = (var33 * var27 + var35 * var31) * 2.0D;
				var13 = var37;
			}

			var1 = var15 + var27 * var13;
			var3 = var17 + var29 * var13;
			var5 = var19 + var31 * var13;
			if(var29 < 0.0D) {
				++var3;
			}

			if(var29 > 0.0D) {
				var3 += 0.5D;
			}

			return Vec3D.createVector(var1, var3, var5);
		} else {
			return null;
		}
	}

	protected void writeEntityToNBT(NBTTagCompound var1) {
		var1.setInteger("Type", this.minecartType);
		if(this.minecartType == 2) {
			var1.setDouble("PushX", this.pushX);
			var1.setDouble("PushZ", this.pushZ);
			var1.setShort("Fuel", (short)this.fuel);
		} else if(this.minecartType == 1) {
			NBTTagList var2 = new NBTTagList();

			for(int var3 = 0; var3 < this.cargoItems.length; ++var3) {
				if(this.cargoItems[var3] != null) {
					NBTTagCompound var4 = new NBTTagCompound();
					var4.setByte("Slot", (byte)var3);
					this.cargoItems[var3].writeToNBT(var4);
					var2.setTag(var4);
				}
			}

			var1.setTag("Items", var2);
		}

	}

	protected void readEntityFromNBT(NBTTagCompound var1) {
		this.minecartType = var1.getInteger("Type");
		if(this.minecartType == 2) {
			this.pushX = var1.getDouble("PushX");
			this.pushZ = var1.getDouble("PushZ");
			this.fuel = var1.getShort("Fuel");
		} else if(this.minecartType == 1) {
			NBTTagList var2 = var1.getTagList("Items");
			this.cargoItems = new ItemStack[this.getSizeInventory()];

			for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
				NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
				int var5 = var4.getByte("Slot") & 255;
				if(var5 >= 0 && var5 < this.cargoItems.length) {
					this.cargoItems[var5] = new ItemStack(var4);
				}
			}
		}

	}

	public float getShadowSize() {
		return 0.0F;
	}

	public void applyEntityCollision(Entity var1) {
		if(!this.aH.multiplayerWorld) {
			if(var1 != this.aF) {
				if(var1 instanceof EntityLiving && !(var1 instanceof EntityPlayer) && this.minecartType == 0 && this.posZ * this.posZ + this.motionY * this.motionY > 0.01D && this.aF == null && var1.riddenByEntity == null) {
					var1.mountEntity(this);
				}

				double var2 = var1.prevPosZ - this.prevPosZ;
				double var4 = var1.posY - this.posY;
				double var6 = var2 * var2 + var4 * var4;
				if(var6 >= (double)1.0E-4F) {
					var6 = (double)MathHelper.sqrt_double(var6);
					var2 /= var6;
					var4 /= var6;
					double var8 = 1.0D / var6;
					if(var8 > 1.0D) {
						var8 = 1.0D;
					}

					var2 *= var8;
					var4 *= var8;
					var2 *= (double)0.1F;
					var4 *= (double)0.1F;
					var2 *= (double)(1.0F - this.bq);
					var4 *= (double)(1.0F - this.bq);
					var2 *= 0.5D;
					var4 *= 0.5D;
					if(var1 instanceof EntityMinecart) {
						double var10 = var1.prevPosZ - this.prevPosZ;
						double var12 = var1.posY - this.posY;
						double var14 = var10 * var1.motionY + var12 * var1.aI;
						var14 *= var14;
						if(var14 > 5.0D) {
							return;
						}

						double var16 = var1.posZ + this.posZ;
						double var18 = var1.motionY + this.motionY;
						if(((EntityMinecart)var1).minecartType == 2 && this.minecartType != 2) {
							this.posZ *= (double)0.2F;
							this.motionY *= (double)0.2F;
							this.addVelocity(var1.posZ - var2, 0.0D, var1.motionY - var4);
							var1.posZ *= (double)0.7F;
							var1.motionY *= (double)0.7F;
						} else if(((EntityMinecart)var1).minecartType != 2 && this.minecartType == 2) {
							var1.posZ *= (double)0.2F;
							var1.motionY *= (double)0.2F;
							var1.addVelocity(this.posZ + var2, 0.0D, this.motionY + var4);
							this.posZ *= (double)0.7F;
							this.motionY *= (double)0.7F;
						} else {
							var16 /= 2.0D;
							var18 /= 2.0D;
							this.posZ *= (double)0.2F;
							this.motionY *= (double)0.2F;
							this.addVelocity(var16 - var2, 0.0D, var18 - var4);
							var1.posZ *= (double)0.2F;
							var1.motionY *= (double)0.2F;
							var1.addVelocity(var16 + var2, 0.0D, var18 + var4);
						}
					} else {
						this.addVelocity(-var2, 0.0D, -var4);
						var1.addVelocity(var2 / 4.0D, 0.0D, var4 / 4.0D);
					}
				}

			}
		}
	}

	public int getSizeInventory() {
		return 27;
	}

	public ItemStack getStackInSlot(int var1) {
		return this.cargoItems[var1];
	}

	public ItemStack decrStackSize(int var1, int var2) {
		if(this.cargoItems[var1] != null) {
			ItemStack var3;
			if(this.cargoItems[var1].stackSize <= var2) {
				var3 = this.cargoItems[var1];
				this.cargoItems[var1] = null;
				return var3;
			} else {
				var3 = this.cargoItems[var1].splitStack(var2);
				if(this.cargoItems[var1].stackSize == 0) {
					this.cargoItems[var1] = null;
				}

				return var3;
			}
		} else {
			return null;
		}
	}

	public void setInventorySlotContents(int var1, ItemStack var2) {
		this.cargoItems[var1] = var2;
		if(var2 != null && var2.stackSize > this.getInventoryStackLimit()) {
			var2.stackSize = this.getInventoryStackLimit();
		}

	}

	public String getInvName() {
		return "Minecart";
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public void onInventoryChanged() {
	}

	public boolean interact(EntityPlayer var1) {
		if(this.minecartType == 0) {
			if(this.aF != null && this.aF instanceof EntityPlayer && this.aF != var1) {
				return true;
			}

			if(!this.aH.multiplayerWorld) {
				var1.mountEntity(this);
			}
		} else if(this.minecartType == 1) {
			if(!this.aH.multiplayerWorld) {
				var1.displayGUIChest(this);
			}
		} else if(this.minecartType == 2) {
			ItemStack var2 = var1.inventory.getCurrentItem();
			if(var2 != null && var2.itemID == Item.coal.shiftedIndex) {
				if(--var2.stackSize == 0) {
					var1.inventory.setInventorySlotContents(var1.inventory.currentItem, (ItemStack)null);
				}

				this.fuel += 1200;
			}

			this.pushX = this.prevPosZ - var1.prevPosZ;
			this.pushZ = this.posY - var1.posY;
		}

		return true;
	}

	public void setPositionAndRotation2(double var1, double var3, double var5, float var7, float var8, int var9) {
		this.field_9414_l = var1;
		this.field_9413_m = var3;
		this.field_9412_n = var5;
		this.field_9411_o = (double)var7;
		this.field_9410_p = (double)var8;
		this.field_9415_k = var9 + 2;
		this.posZ = this.field_9409_q;
		this.motionX = this.field_9408_r;
		this.motionY = this.field_9407_s;
	}

	public void setVelocity(double var1, double var3, double var5) {
		this.field_9409_q = this.posZ = var1;
		this.field_9408_r = this.motionX = var3;
		this.field_9407_s = this.motionY = var5;
	}

	public boolean canInteractWith(EntityPlayer var1) {
		return this.field_9293_aM ? false : var1.getDistanceSqToEntity(this) <= 64.0D;
	}
}
