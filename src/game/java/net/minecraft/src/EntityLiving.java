package net.minecraft.src;

import java.util.List;

public class EntityLiving extends Entity {
	public int D = 20;
	public float E;
	public float field_9365_p;
	public float field_9363_r = 0.0F;
	public float renderYawOffset = 0.0F;
	protected float prevRenderYawOffset;
	protected float field_9362_u;
	protected float field_9361_v;
	protected float field_9360_w;
	protected boolean M = true;
	protected String N = "/mob/char.png";
	protected boolean O = true;
	protected float P = 0.0F;
	protected String Q = null;
	protected float R = 1.0F;
	protected int S = 0;
	protected float T = 0.0F;
	public boolean U = false;
	public float V;
	public float prevSwingProgress;
	public int X = 10;
	public int health;
	private int livingSoundTime;
	public int prevHealth;
	public int hurtTime;
	public float ab = 0.0F;
	public int ac = 0;
	public int deathTime = 0;
	public float ae;
	public float cameraPitch;
	protected boolean ag = false;
	public int ah = -1;
	public float ai = (float)(Math.random() * (double)0.9F + (double)0.1F);
	public float field_9325_U;
	public float field_705_Q;
	public float field_704_R;
	protected int am;
	protected double an;
	protected double newPosX;
	protected double newPosY;
	protected double newPosZ;
	protected double newRotationYaw;
	float as = 0.0F;
	protected int at = 0;
	protected int field_9346_af = 0;
	protected float av;
	protected float moveStrafing;
	protected float moveForward;
	protected boolean ay = false;
	protected float az = 0.0F;
	protected float defaultPitch = 0.7F;
	private Entity currentTarget;
	protected int aB = 0;

	public EntityLiving(World var1) {
		super(var1);
		this.aE = true;
		this.field_9365_p = (float)(Math.random() + 1.0D) * 0.01F;
		this.setPosition(this.prevPosZ, this.posX, this.posY);
		this.E = (float)Math.random() * 12398.0F;
		this.aR = (float)(Math.random() * (double)((float)Math.PI) * 2.0D);
		this.ySize = 0.5F;
	}

	protected void entityInit() {
	}

	public boolean canEntityBeSeen(Entity var1) {
		return this.aH.rayTraceBlocks(Vec3D.createVector(this.prevPosZ, this.posX + (double)this.getEyeHeight(), this.posY), Vec3D.createVector(var1.prevPosZ, var1.posX + (double)var1.getEyeHeight(), var1.posY)) == null;
	}

	public String getEntityTexture() {
		return this.N;
	}

	public boolean canBeCollidedWith() {
		return !this.field_9293_aM;
	}

	public boolean canBePushed() {
		return !this.field_9293_aM;
	}

	public float getEyeHeight() {
		return this.width * 0.85F;
	}

	public int getTalkInterval() {
		return 80;
	}

	public void playLivingSound() {
		String var1 = this.getLivingSound();
		if(var1 != null) {
			this.aH.playSoundAtEntity(this, var1, this.getSoundVolume(), (this.br.nextFloat() - this.br.nextFloat()) * 0.2F + 1.0F);
		}

	}

	public void onEntityUpdate() {
		this.V = this.prevSwingProgress;
		super.onEntityUpdate();
		if(this.br.nextInt(1000) < this.livingSoundTime++) {
			this.livingSoundTime = -this.getTalkInterval();
			this.playLivingSound();
		}

		if(this.isEntityAlive() && this.isEntityInsideOpaqueBlock()) {
			this.attackEntityFrom((Entity)null, 1);
		}

		if(this.bB || this.aH.multiplayerWorld) {
			this.fireResistance = 0;
		}

		int var1;
		if(this.isEntityAlive() && this.isInsideOfMaterial(Material.water) && !this.canBreatheUnderwater()) {
			--this.field_9306_bj;
			if(this.field_9306_bj == -20) {
				this.field_9306_bj = 0;

				for(var1 = 0; var1 < 8; ++var1) {
					float var2 = this.br.nextFloat() - this.br.nextFloat();
					float var3 = this.br.nextFloat() - this.br.nextFloat();
					float var4 = this.br.nextFloat() - this.br.nextFloat();
					this.aH.spawnParticle("bubble", this.prevPosZ + (double)var2, this.posX + (double)var3, this.posY + (double)var4, this.posZ, this.motionX, this.motionY);
				}

				this.attackEntityFrom((Entity)null, 2);
			}

			this.fireResistance = 0;
		} else {
			this.field_9306_bj = this.fire;
		}

		this.ae = this.cameraPitch;
		if(this.deathTime > 0) {
			--this.deathTime;
		}

		if(this.prevHealth > 0) {
			--this.prevHealth;
		}

		if(this.bx > 0) {
			--this.bx;
		}

		if(this.X <= 0) {
			++this.ac;
			if(this.ac > 20) {
				this.unusedEntityMethod();
				this.setEntityDead();

				for(var1 = 0; var1 < 20; ++var1) {
					double var8 = this.br.nextGaussian() * 0.02D;
					double var9 = this.br.nextGaussian() * 0.02D;
					double var6 = this.br.nextGaussian() * 0.02D;
					this.aH.spawnParticle("explode", this.prevPosZ + (double)(this.br.nextFloat() * this.yOffset * 2.0F) - (double)this.yOffset, this.posX + (double)(this.br.nextFloat() * this.width), this.posY + (double)(this.br.nextFloat() * this.yOffset * 2.0F) - (double)this.yOffset, var8, var9, var6);
				}
			}
		}

		this.field_9360_w = this.field_9361_v;
		this.renderYawOffset = this.field_9363_r;
		this.rotationPitch = this.aR;
		this.prevRotationYaw = this.rotationYaw;
	}

	public void spawnExplosionParticle() {
		for(int var1 = 0; var1 < 20; ++var1) {
			double var2 = this.br.nextGaussian() * 0.02D;
			double var4 = this.br.nextGaussian() * 0.02D;
			double var6 = this.br.nextGaussian() * 0.02D;
			double var8 = 10.0D;
			this.aH.spawnParticle("explode", this.prevPosZ + (double)(this.br.nextFloat() * this.yOffset * 2.0F) - (double)this.yOffset - var2 * var8, this.posX + (double)(this.br.nextFloat() * this.width) - var4 * var8, this.posY + (double)(this.br.nextFloat() * this.yOffset * 2.0F) - (double)this.yOffset - var6 * var8, var2, var4, var6);
		}

	}

	public void updateRidden() {
		super.updateRidden();
		this.prevRenderYawOffset = this.field_9362_u;
		this.field_9362_u = 0.0F;
	}

	public void setPositionAndRotation2(double var1, double var3, double var5, float var7, float var8, int var9) {
		this.be = 0.0F;
		this.an = var1;
		this.newPosX = var3;
		this.newPosY = var5;
		this.newPosZ = (double)var7;
		this.newRotationYaw = (double)var8;
		this.am = var9;
	}

	public void onUpdate() {
		super.onUpdate();
		this.onLivingUpdate();
		double var1 = this.prevPosZ - this.aI;
		double var3 = this.posY - this.prevPosY;
		float var5 = MathHelper.sqrt_double(var1 * var1 + var3 * var3);
		float var6 = this.field_9363_r;
		float var7 = 0.0F;
		this.prevRenderYawOffset = this.field_9362_u;
		float var8 = 0.0F;
		if(var5 > 0.05F) {
			var8 = 1.0F;
			var7 = var5 * 3.0F;
			var6 = (float)Math.atan2(var3, var1) * 180.0F / (float)Math.PI - 90.0F;
		}

		if(this.prevSwingProgress > 0.0F) {
			var6 = this.aR;
		}

		if(!this.aW) {
			var8 = 0.0F;
		}

		this.field_9362_u += (var8 - this.field_9362_u) * 0.3F;

		float var9;
		for(var9 = var6 - this.field_9363_r; var9 < -180.0F; var9 += 360.0F) {
		}

		while(var9 >= 180.0F) {
			var9 -= 360.0F;
		}

		this.field_9363_r += var9 * 0.3F;

		float var10;
		for(var10 = this.aR - this.field_9363_r; var10 < -180.0F; var10 += 360.0F) {
		}

		while(var10 >= 180.0F) {
			var10 -= 360.0F;
		}

		boolean var11 = var10 < -90.0F || var10 >= 90.0F;
		if(var10 < -75.0F) {
			var10 = -75.0F;
		}

		if(var10 >= 75.0F) {
			var10 = 75.0F;
		}

		this.field_9363_r = this.aR - var10;
		if(var10 * var10 > 2500.0F) {
			this.field_9363_r += var10 * 0.2F;
		}

		if(var11) {
			var7 *= -1.0F;
		}

		while(this.aR - this.rotationPitch < -180.0F) {
			this.rotationPitch -= 360.0F;
		}

		while(this.aR - this.rotationPitch >= 180.0F) {
			this.rotationPitch += 360.0F;
		}

		while(this.field_9363_r - this.renderYawOffset < -180.0F) {
			this.renderYawOffset -= 360.0F;
		}

		while(this.field_9363_r - this.renderYawOffset >= 180.0F) {
			this.renderYawOffset += 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		this.field_9361_v += var7;
	}

	protected void setSize(float var1, float var2) {
		super.setSize(var1, var2);
	}

	public void heal(int var1) {
		if(this.X > 0) {
			this.X += var1;
			if(this.X > 20) {
				this.X = 20;
			}

			this.bx = this.D / 2;
		}
	}

	public boolean attackEntityFrom(Entity var1, int var2) {
		if(this.aH.multiplayerWorld) {
			return false;
		} else {
			this.field_9346_af = 0;
			if(this.X <= 0) {
				return false;
			} else {
				this.field_705_Q = 1.5F;
				boolean var3 = true;
				if((float)this.bx > (float)this.D / 2.0F) {
					if(var2 <= this.at) {
						return false;
					}

					this.damageEntity(var2 - this.at);
					this.at = var2;
					var3 = false;
				} else {
					this.at = var2;
					this.health = this.X;
					this.bx = this.D;
					this.damageEntity(var2);
					this.prevHealth = this.hurtTime = 10;
				}

				this.ab = 0.0F;
				if(var3) {
					this.aH.func_9425_a(this, (byte)2);
					this.setBeenAttacked();
					if(var1 != null) {
						double var4 = var1.prevPosZ - this.prevPosZ;

						double var6;
						for(var6 = var1.posY - this.posY; var4 * var4 + var6 * var6 < 1.0E-4D; var6 = (Math.random() - Math.random()) * 0.01D) {
							var4 = (Math.random() - Math.random()) * 0.01D;
						}

						this.ab = (float)(Math.atan2(var6, var4) * 180.0D / (double)((float)Math.PI)) - this.aR;
						this.knockBack(var1, var2, var4, var6);
					} else {
						this.ab = (float)((int)(Math.random() * 2.0D) * 180);
					}
				}

				if(this.X <= 0) {
					if(var3) {
						this.aH.playSoundAtEntity(this, this.getDeathSound(), this.getSoundVolume(), (this.br.nextFloat() - this.br.nextFloat()) * 0.2F + 1.0F);
					}

					this.onDeath(var1);
				} else if(var3) {
					this.aH.playSoundAtEntity(this, this.getHurtSound(), this.getSoundVolume(), (this.br.nextFloat() - this.br.nextFloat()) * 0.2F + 1.0F);
				}

				return true;
			}
		}
	}

	public void performHurtAnimation() {
		this.prevHealth = this.hurtTime = 10;
		this.ab = 0.0F;
	}

	protected void damageEntity(int var1) {
		this.X -= var1;
	}

	protected float getSoundVolume() {
		return 1.0F;
	}

	protected String getLivingSound() {
		return null;
	}

	protected String getHurtSound() {
		return "random.hurt";
	}

	protected String getDeathSound() {
		return "random.hurt";
	}

	public void knockBack(Entity var1, int var2, double var3, double var5) {
		float var7 = MathHelper.sqrt_double(var3 * var3 + var5 * var5);
		float var8 = 0.4F;
		this.posZ /= 2.0D;
		this.motionX /= 2.0D;
		this.motionY /= 2.0D;
		this.posZ -= var3 / (double)var7 * (double)var8;
		this.motionX += (double)0.4F;
		this.motionY -= var5 / (double)var7 * (double)var8;
		if(this.motionX > (double)0.4F) {
			this.motionX = (double)0.4F;
		}

	}

	public void onDeath(Entity var1) {
		if(this.S >= 0 && var1 != null) {
			var1.addToPlayerScore(this, this.S);
		}

		if(var1 != null) {
			var1.onKillEntity(this);
		}

		this.ag = true;
		if(!this.aH.multiplayerWorld) {
			this.dropFewItems();
		}

		this.aH.func_9425_a(this, (byte)3);
	}

	protected void dropFewItems() {
		int var1 = this.getDropItemId();
		if(var1 > 0) {
			int var2 = this.br.nextInt(3);

			for(int var3 = 0; var3 < var2; ++var3) {
				this.dropItem(var1, 1);
			}
		}

	}

	protected int getDropItemId() {
		return 0;
	}

	protected void fall(float var1) {
		super.fall(var1);
		int var2 = (int)Math.ceil((double)(var1 - 3.0F));
		if(var2 > 0) {
			this.attackEntityFrom((Entity)null, var2);
			int var3 = this.aH.getBlockId(MathHelper.floor_double(this.prevPosZ), MathHelper.floor_double(this.posX - (double)0.2F - (double)this.be), MathHelper.floor_double(this.posY));
			if(var3 > 0) {
				StepSound var4 = Block.blocksList[var3].stepSound;
				this.aH.playSoundAtEntity(this, var4.func_1145_d(), var4.getVolume() * 0.5F, var4.getPitch() * (12.0F / 16.0F));
			}
		}

	}

	public void moveEntityWithHeading(float var1, float var2) {
		double var3;
		if(this.isInWater()) {
			var3 = this.posX;
			this.moveFlying(var1, var2, 0.02F);
			this.moveEntity(this.posZ, this.motionX, this.motionY);
			this.posZ *= (double)0.8F;
			this.motionX *= (double)0.8F;
			this.motionY *= (double)0.8F;
			this.motionX -= 0.02D;
			if(this.onGround && this.isOffsetPositionInLiquid(this.posZ, this.motionX + (double)0.6F - this.posX + var3, this.motionY)) {
				this.motionX = (double)0.3F;
			}
		} else if(this.handleLavaMovement()) {
			var3 = this.posX;
			this.moveFlying(var1, var2, 0.02F);
			this.moveEntity(this.posZ, this.motionX, this.motionY);
			this.posZ *= 0.5D;
			this.motionX *= 0.5D;
			this.motionY *= 0.5D;
			this.motionX -= 0.02D;
			if(this.onGround && this.isOffsetPositionInLiquid(this.posZ, this.motionX + (double)0.6F - this.posX + var3, this.motionY)) {
				this.motionX = (double)0.3F;
			}
		} else {
			float var8 = 0.91F;
			if(this.aW) {
				var8 = 546.0F * 0.1F * 0.1F * 0.1F;
				int var4 = this.aH.getBlockId(MathHelper.floor_double(this.prevPosZ), MathHelper.floor_double(this.aV.minY) - 1, MathHelper.floor_double(this.posY));
				if(var4 > 0) {
					var8 = Block.blocksList[var4].slipperiness * 0.91F;
				}
			}

			float var9 = 0.16277136F / (var8 * var8 * var8);
			this.moveFlying(var1, var2, this.aW ? 0.1F * var9 : 0.02F);
			var8 = 0.91F;
			if(this.aW) {
				var8 = 546.0F * 0.1F * 0.1F * 0.1F;
				int var5 = this.aH.getBlockId(MathHelper.floor_double(this.prevPosZ), MathHelper.floor_double(this.aV.minY) - 1, MathHelper.floor_double(this.posY));
				if(var5 > 0) {
					var8 = Block.blocksList[var5].slipperiness * 0.91F;
				}
			}

			if(this.isOnLadder()) {
				float var10 = 0.15F;
				if(this.posZ < (double)(-var10)) {
					this.posZ = (double)(-var10);
				}

				if(this.posZ > (double)var10) {
					this.posZ = (double)var10;
				}

				if(this.motionY < (double)(-var10)) {
					this.motionY = (double)(-var10);
				}

				if(this.motionY > (double)var10) {
					this.motionY = (double)var10;
				}

				this.distanceWalkedModified = 0.0F;
				if(this.motionX < -0.15D) {
					this.motionX = -0.15D;
				}

				if(this.isSneaking() && this.motionX < 0.0D) {
					this.motionX = 0.0D;
				}
			}

			this.moveEntity(this.posZ, this.motionX, this.motionY);
			if(this.onGround && this.isOnLadder()) {
				this.motionX = 0.2D;
			}

			this.motionX -= 0.08D;
			this.motionX *= (double)0.98F;
			this.posZ *= (double)var8;
			this.motionY *= (double)var8;
		}

		this.field_9325_U = this.field_705_Q;
		var3 = this.prevPosZ - this.aI;
		double var11 = this.posY - this.prevPosY;
		float var7 = MathHelper.sqrt_double(var3 * var3 + var11 * var11) * 4.0F;
		if(var7 > 1.0F) {
			var7 = 1.0F;
		}

		this.field_705_Q += (var7 - this.field_705_Q) * 0.4F;
		this.field_704_R += this.field_705_Q;
	}

	public boolean isOnLadder() {
		int var1 = MathHelper.floor_double(this.prevPosZ);
		int var2 = MathHelper.floor_double(this.aV.minY);
		int var3 = MathHelper.floor_double(this.posY);
		return this.aH.getBlockId(var1, var2, var3) == Block.ladder.blockID;
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		var1.setShort("Health", (short)this.X);
		var1.setShort("HurtTime", (short)this.prevHealth);
		var1.setShort("DeathTime", (short)this.ac);
		var1.setShort("AttackTime", (short)this.deathTime);
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		this.X = var1.getShort("Health");
		if(!var1.hasKey("Health")) {
			this.X = 10;
		}

		this.prevHealth = var1.getShort("HurtTime");
		this.ac = var1.getShort("DeathTime");
		this.deathTime = var1.getShort("AttackTime");
	}

	public boolean isEntityAlive() {
		return !this.field_9293_aM && this.X > 0;
	}

	public boolean canBreatheUnderwater() {
		return false;
	}

	public void onLivingUpdate() {
		if(this.am > 0) {
			double var1 = this.prevPosZ + (this.an - this.prevPosZ) / (double)this.am;
			double var3 = this.posX + (this.newPosX - this.posX) / (double)this.am;
			double var5 = this.posY + (this.newPosY - this.posY) / (double)this.am;

			double var7;
			for(var7 = this.newPosZ - (double)this.aR; var7 < -180.0D; var7 += 360.0D) {
			}

			while(var7 >= 180.0D) {
				var7 -= 360.0D;
			}

			this.aR = (float)((double)this.aR + var7 / (double)this.am);
			this.rotationYaw = (float)((double)this.rotationYaw + (this.newRotationYaw - (double)this.rotationYaw) / (double)this.am);
			--this.am;
			this.setPosition(var1, var3, var5);
			this.setRotation(this.aR, this.rotationYaw);
		}

		if(this.isMovementBlocked()) {
			this.ay = false;
			this.av = 0.0F;
			this.moveStrafing = 0.0F;
			this.moveForward = 0.0F;
		} else if(!this.U) {
			this.updatePlayerActionState();
		}

		boolean var9 = this.isInWater();
		boolean var2 = this.handleLavaMovement();
		if(this.ay) {
			if(var9) {
				this.motionX += (double)0.04F;
			} else if(var2) {
				this.motionX += (double)0.04F;
			} else if(this.aW) {
				this.jump();
			}
		}

		this.av *= 0.98F;
		this.moveStrafing *= 0.98F;
		this.moveForward *= 0.9F;
		this.moveEntityWithHeading(this.av, this.moveStrafing);
		List var10 = this.aH.getEntitiesWithinAABBExcludingEntity(this, this.aV.expand((double)0.2F, 0.0D, (double)0.2F));
		if(var10 != null && var10.size() > 0) {
			for(int var4 = 0; var4 < var10.size(); ++var4) {
				Entity var11 = (Entity)var10.get(var4);
				if(var11.canBePushed()) {
					var11.applyEntityCollision(this);
				}
			}
		}

	}

	protected boolean isMovementBlocked() {
		return this.X <= 0;
	}

	protected void jump() {
		this.motionX = (double)0.42F;
	}

	protected boolean canDespawn() {
		return true;
	}

	protected void func_27021_X() {
		EntityPlayer var1 = this.aH.getClosestPlayerToEntity(this, -1.0D);
		if(this.canDespawn() && var1 != null) {
			double var2 = var1.prevPosZ - this.prevPosZ;
			double var4 = var1.posX - this.posX;
			double var6 = var1.posY - this.posY;
			double var8 = var2 * var2 + var4 * var4 + var6 * var6;
			if(var8 > 16384.0D) {
				this.setEntityDead();
			}

			if(this.field_9346_af > 600 && this.br.nextInt(800) == 0) {
				if(var8 < 1024.0D) {
					this.field_9346_af = 0;
				} else {
					this.setEntityDead();
				}
			}
		}

	}

	protected void updatePlayerActionState() {
		++this.field_9346_af;
		EntityPlayer var1 = this.aH.getClosestPlayerToEntity(this, -1.0D);
		this.func_27021_X();
		this.av = 0.0F;
		this.moveStrafing = 0.0F;
		float var2 = 8.0F;
		if(this.br.nextFloat() < 0.02F) {
			var1 = this.aH.getClosestPlayerToEntity(this, (double)var2);
			if(var1 != null) {
				this.currentTarget = var1;
				this.aB = 10 + this.br.nextInt(20);
			} else {
				this.moveForward = (this.br.nextFloat() - 0.5F) * 20.0F;
			}
		}

		if(this.currentTarget != null) {
			this.faceEntity(this.currentTarget, 10.0F, (float)this.func_25026_x());
			if(this.aB-- <= 0 || this.currentTarget.field_9293_aM || this.currentTarget.getDistanceSqToEntity(this) > (double)(var2 * var2)) {
				this.currentTarget = null;
			}
		} else {
			if(this.br.nextFloat() < 0.05F) {
				this.moveForward = (this.br.nextFloat() - 0.5F) * 20.0F;
			}

			this.aR += this.moveForward;
			this.rotationYaw = this.az;
		}

		boolean var3 = this.isInWater();
		boolean var4 = this.handleLavaMovement();
		if(var3 || var4) {
			this.ay = this.br.nextFloat() < 0.8F;
		}

	}

	protected int func_25026_x() {
		return 40;
	}

	public void faceEntity(Entity var1, float var2, float var3) {
		double var4 = var1.prevPosZ - this.prevPosZ;
		double var8 = var1.posY - this.posY;
		double var6;
		if(var1 instanceof EntityLiving) {
			EntityLiving var10 = (EntityLiving)var1;
			var6 = this.posX + (double)this.getEyeHeight() - (var10.posX + (double)var10.getEyeHeight());
		} else {
			var6 = (var1.aV.minY + var1.aV.maxY) / 2.0D - (this.posX + (double)this.getEyeHeight());
		}

		double var14 = (double)MathHelper.sqrt_double(var4 * var4 + var8 * var8);
		float var12 = (float)(Math.atan2(var8, var4) * 180.0D / (double)((float)Math.PI)) - 90.0F;
		float var13 = (float)(-(Math.atan2(var6, var14) * 180.0D / (double)((float)Math.PI)));
		this.rotationYaw = -this.updateRotation(this.rotationYaw, var13, var3);
		this.aR = this.updateRotation(this.aR, var12, var2);
	}

	public boolean hasCurrentTarget() {
		return this.currentTarget != null;
	}

	public Entity getCurrentTarget() {
		return this.currentTarget;
	}

	private float updateRotation(float var1, float var2, float var3) {
		float var4;
		for(var4 = var2 - var1; var4 < -180.0F; var4 += 360.0F) {
		}

		while(var4 >= 180.0F) {
			var4 -= 360.0F;
		}

		if(var4 > var3) {
			var4 = var3;
		}

		if(var4 < -var3) {
			var4 = -var3;
		}

		return var1 + var4;
	}

	public void unusedEntityMethod() {
	}

	public boolean getCanSpawnHere() {
		return this.aH.checkIfAABBIsClear(this.aV) && this.aH.getCollidingBoundingBoxes(this, this.aV).size() == 0 && !this.aH.getIsAnyLiquid(this.aV);
	}

	protected void kill() {
		this.attackEntityFrom((Entity)null, 4);
	}

	public float getSwingProgress(float var1) {
		float var2 = this.prevSwingProgress - this.V;
		if(var2 < 0.0F) {
			++var2;
		}

		return this.V + var2 * var1;
	}

	public Vec3D getPosition(float var1) {
		if(var1 == 1.0F) {
			return Vec3D.createVector(this.prevPosZ, this.posX, this.posY);
		} else {
			double var2 = this.aI + (this.prevPosZ - this.aI) * (double)var1;
			double var4 = this.prevPosX + (this.posX - this.prevPosX) * (double)var1;
			double var6 = this.prevPosY + (this.posY - this.prevPosY) * (double)var1;
			return Vec3D.createVector(var2, var4, var6);
		}
	}

	public Vec3D getLookVec() {
		return this.getLook(1.0F);
	}

	public Vec3D getLook(float var1) {
		float var2;
		float var3;
		float var4;
		float var5;
		if(var1 == 1.0F) {
			var2 = MathHelper.cos(-this.aR * ((float)Math.PI / 180.0F) - (float)Math.PI);
			var3 = MathHelper.sin(-this.aR * ((float)Math.PI / 180.0F) - (float)Math.PI);
			var4 = -MathHelper.cos(-this.rotationYaw * ((float)Math.PI / 180.0F));
			var5 = MathHelper.sin(-this.rotationYaw * ((float)Math.PI / 180.0F));
			return Vec3D.createVector((double)(var3 * var4), (double)var5, (double)(var2 * var4));
		} else {
			var2 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * var1;
			var3 = this.rotationPitch + (this.aR - this.rotationPitch) * var1;
			var4 = MathHelper.cos(-var3 * ((float)Math.PI / 180.0F) - (float)Math.PI);
			var5 = MathHelper.sin(-var3 * ((float)Math.PI / 180.0F) - (float)Math.PI);
			float var6 = -MathHelper.cos(-var2 * ((float)Math.PI / 180.0F));
			float var7 = MathHelper.sin(-var2 * ((float)Math.PI / 180.0F));
			return Vec3D.createVector((double)(var5 * var6), (double)var7, (double)(var4 * var6));
		}
	}

	public MovingObjectPosition rayTrace(double var1, float var3) {
		Vec3D var4 = this.getPosition(var3);
		Vec3D var5 = this.getLook(var3);
		Vec3D var6 = var4.addVector(var5.xCoord * var1, var5.yCoord * var1, var5.zCoord * var1);
		return this.aH.rayTraceBlocks(var4, var6);
	}

	public int getMaxSpawnedInChunk() {
		return 4;
	}

	public ItemStack getHeldItem() {
		return null;
	}

	public void handleHealthUpdate(byte var1) {
		if(var1 == 2) {
			this.field_705_Q = 1.5F;
			this.bx = this.D;
			this.prevHealth = this.hurtTime = 10;
			this.ab = 0.0F;
			this.aH.playSoundAtEntity(this, this.getHurtSound(), this.getSoundVolume(), (this.br.nextFloat() - this.br.nextFloat()) * 0.2F + 1.0F);
			this.attackEntityFrom((Entity)null, 0);
		} else if(var1 == 3) {
			this.aH.playSoundAtEntity(this, this.getDeathSound(), this.getSoundVolume(), (this.br.nextFloat() - this.br.nextFloat()) * 0.2F + 1.0F);
			this.X = 0;
			this.onDeath((Entity)null);
		} else {
			super.handleHealthUpdate(var1);
		}

	}

	public boolean isPlayerSleeping() {
		return false;
	}

	public int getItemIcon(ItemStack var1) {
		return var1.getIconIndex();
	}
}
