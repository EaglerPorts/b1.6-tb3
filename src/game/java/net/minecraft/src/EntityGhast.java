package net.minecraft.src;

public class EntityGhast extends EntityFlying implements IMob {
	public int courseChangeCooldown = 0;
	public double waypointX;
	public double waypointY;
	public double waypointZ;
	private Entity targetedEntity = null;
	private int aggroCooldown = 0;
	public int prevAttackCounter = 0;
	public int attackCounter = 0;

	public EntityGhast(World var1) {
		super(var1);
		this.N = "/mob/ghast.png";
		this.setSize(4.0F, 4.0F);
		this.bB = true;
	}

	protected void entityInit() {
		super.entityInit();
		this.bC.addObject(16, Byte.valueOf((byte)0));
	}

	public void onUpdate() {
		super.onUpdate();
		byte var1 = this.bC.getWatchableObjectByte(16);
		this.N = var1 == 1 ? "/mob/ghast_fire.png" : "/mob/ghast.png";
	}

	protected void updatePlayerActionState() {
		if(!this.aH.multiplayerWorld && this.aH.difficultySetting == 0) {
			this.setEntityDead();
		}

		this.func_27021_X();
		this.prevAttackCounter = this.attackCounter;
		double var1 = this.waypointX - this.prevPosZ;
		double var3 = this.waypointY - this.posX;
		double var5 = this.waypointZ - this.posY;
		double var7 = (double)MathHelper.sqrt_double(var1 * var1 + var3 * var3 + var5 * var5);
		if(var7 < 1.0D || var7 > 60.0D) {
			this.waypointX = this.prevPosZ + (double)((this.br.nextFloat() * 2.0F - 1.0F) * 16.0F);
			this.waypointY = this.posX + (double)((this.br.nextFloat() * 2.0F - 1.0F) * 16.0F);
			this.waypointZ = this.posY + (double)((this.br.nextFloat() * 2.0F - 1.0F) * 16.0F);
		}

		if(this.courseChangeCooldown-- <= 0) {
			this.courseChangeCooldown += this.br.nextInt(5) + 2;
			if(this.isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, var7)) {
				this.posZ += var1 / var7 * 0.1D;
				this.motionX += var3 / var7 * 0.1D;
				this.motionY += var5 / var7 * 0.1D;
			} else {
				this.waypointX = this.prevPosZ;
				this.waypointY = this.posX;
				this.waypointZ = this.posY;
			}
		}

		if(this.targetedEntity != null && this.targetedEntity.field_9293_aM) {
			this.targetedEntity = null;
		}

		if(this.targetedEntity == null || this.aggroCooldown-- <= 0) {
			this.targetedEntity = this.aH.getClosestPlayerToEntity(this, 100.0D);
			if(this.targetedEntity != null) {
				this.aggroCooldown = 20;
			}
		}

		double var9 = 64.0D;
		if(this.targetedEntity != null && this.targetedEntity.getDistanceSqToEntity(this) < var9 * var9) {
			double var11 = this.targetedEntity.prevPosZ - this.prevPosZ;
			double var13 = this.targetedEntity.aV.minY + (double)(this.targetedEntity.width / 2.0F) - (this.posX + (double)(this.width / 2.0F));
			double var15 = this.targetedEntity.posY - this.posY;
			this.field_9363_r = this.aR = -((float)Math.atan2(var11, var15)) * 180.0F / (float)Math.PI;
			if(this.canEntityBeSeen(this.targetedEntity)) {
				if(this.attackCounter == 10) {
					this.aH.playSoundAtEntity(this, "mob.ghast.charge", this.getSoundVolume(), (this.br.nextFloat() - this.br.nextFloat()) * 0.2F + 1.0F);
				}

				++this.attackCounter;
				if(this.attackCounter == 20) {
					this.aH.playSoundAtEntity(this, "mob.ghast.fireball", this.getSoundVolume(), (this.br.nextFloat() - this.br.nextFloat()) * 0.2F + 1.0F);
					EntityFireball var17 = new EntityFireball(this.aH, this, var11, var13, var15);
					double var18 = 4.0D;
					Vec3D var20 = this.getLook(1.0F);
					var17.prevPosZ = this.prevPosZ + var20.xCoord * var18;
					var17.posX = this.posX + (double)(this.width / 2.0F) + 0.5D;
					var17.posY = this.posY + var20.zCoord * var18;
					this.aH.entityJoinedWorld(var17);
					this.attackCounter = -40;
				}
			} else if(this.attackCounter > 0) {
				--this.attackCounter;
			}
		} else {
			this.field_9363_r = this.aR = -((float)Math.atan2(this.posZ, this.motionY)) * 180.0F / (float)Math.PI;
			if(this.attackCounter > 0) {
				--this.attackCounter;
			}
		}

		if(!this.aH.multiplayerWorld) {
			byte var21 = this.bC.getWatchableObjectByte(16);
			byte var12 = (byte)(this.attackCounter > 10 ? 1 : 0);
			if(var21 != var12) {
				this.bC.updateObject(16, Byte.valueOf(var12));
			}
		}

	}

	private boolean isCourseTraversable(double var1, double var3, double var5, double var7) {
		double var9 = (this.waypointX - this.prevPosZ) / var7;
		double var11 = (this.waypointY - this.posX) / var7;
		double var13 = (this.waypointZ - this.posY) / var7;
		AxisAlignedBB var15 = this.aV.copy();

		for(int var16 = 1; (double)var16 < var7; ++var16) {
			var15.offset(var9, var11, var13);
			if(this.aH.getCollidingBoundingBoxes(this, var15).size() > 0) {
				return false;
			}
		}

		return true;
	}

	protected String getLivingSound() {
		return "mob.ghast.moan";
	}

	protected String getHurtSound() {
		return "mob.ghast.scream";
	}

	protected String getDeathSound() {
		return "mob.ghast.death";
	}

	protected int getDropItemId() {
		return Item.gunpowder.shiftedIndex;
	}

	protected float getSoundVolume() {
		return 10.0F;
	}

	public boolean getCanSpawnHere() {
		return this.br.nextInt(20) == 0 && super.getCanSpawnHere() && this.aH.difficultySetting > 0;
	}

	public int getMaxSpawnedInChunk() {
		return 1;
	}
}
