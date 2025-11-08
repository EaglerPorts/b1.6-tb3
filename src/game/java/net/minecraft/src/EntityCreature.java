package net.minecraft.src;

public class EntityCreature extends EntityLiving {
	private PathEntity pathToEntity;
	protected Entity playerToAttack;
	protected boolean hasAttacked = false;

	public EntityCreature(World var1) {
		super(var1);
	}

	protected boolean isMovementCeased() {
		return false;
	}

	protected void updatePlayerActionState() {
		this.hasAttacked = this.isMovementCeased();
		float var1 = 16.0F;
		if(this.playerToAttack == null) {
			this.playerToAttack = this.findPlayerToAttack();
			if(this.playerToAttack != null) {
				this.pathToEntity = this.aH.getPathToEntity(this, this.playerToAttack, var1);
			}
		} else if(!this.playerToAttack.isEntityAlive()) {
			this.playerToAttack = null;
		} else {
			float var2 = this.playerToAttack.getDistanceToEntity(this);
			if(this.canEntityBeSeen(this.playerToAttack)) {
				this.attackEntity(this.playerToAttack, var2);
			} else {
				this.attackBlockedEntity(this.playerToAttack, var2);
			}
		}

		if(this.hasAttacked || this.playerToAttack == null || this.pathToEntity != null && this.br.nextInt(20) != 0) {
			if(!this.hasAttacked && (this.pathToEntity == null && this.br.nextInt(80) == 0 || this.br.nextInt(80) == 0)) {
				boolean var21 = false;
				int var3 = -1;
				int var4 = -1;
				int var5 = -1;
				float var6 = -99999.0F;

				for(int var7 = 0; var7 < 10; ++var7) {
					int var8 = MathHelper.floor_double(this.prevPosZ + (double)this.br.nextInt(13) - 6.0D);
					int var9 = MathHelper.floor_double(this.posX + (double)this.br.nextInt(7) - 3.0D);
					int var10 = MathHelper.floor_double(this.posY + (double)this.br.nextInt(13) - 6.0D);
					float var11 = this.getBlockPathWeight(var8, var9, var10);
					if(var11 > var6) {
						var6 = var11;
						var3 = var8;
						var4 = var9;
						var5 = var10;
						var21 = true;
					}
				}

				if(var21) {
					this.pathToEntity = this.aH.getEntityPathToXYZ(this, var3, var4, var5, 10.0F);
				}
			}
		} else {
			this.pathToEntity = this.aH.getPathToEntity(this, this.playerToAttack, var1);
		}

		int var22 = MathHelper.floor_double(this.aV.minY + 0.5D);
		boolean var23 = this.isInWater();
		boolean var24 = this.handleLavaMovement();
		this.rotationYaw = 0.0F;
		if(this.pathToEntity != null && this.br.nextInt(100) != 0) {
			Vec3D var25 = this.pathToEntity.getPosition(this);
			double var26 = (double)(this.yOffset * 2.0F);

			while(var25 != null && var25.squareDistanceTo(this.prevPosZ, var25.yCoord, this.posY) < var26 * var26) {
				this.pathToEntity.incrementPathIndex();
				if(this.pathToEntity.isFinished()) {
					var25 = null;
					this.pathToEntity = null;
				} else {
					var25 = this.pathToEntity.getPosition(this);
				}
			}

			this.ay = false;
			if(var25 != null) {
				double var27 = var25.xCoord - this.prevPosZ;
				double var28 = var25.zCoord - this.posY;
				double var12 = var25.yCoord - (double)var22;
				float var14 = (float)(Math.atan2(var28, var27) * 180.0D / (double)((float)Math.PI)) - 90.0F;
				float var15 = var14 - this.aR;

				for(this.moveStrafing = this.defaultPitch; var15 < -180.0F; var15 += 360.0F) {
				}

				while(var15 >= 180.0F) {
					var15 -= 360.0F;
				}

				if(var15 > 30.0F) {
					var15 = 30.0F;
				}

				if(var15 < -30.0F) {
					var15 = -30.0F;
				}

				this.aR += var15;
				if(this.hasAttacked && this.playerToAttack != null) {
					double var16 = this.playerToAttack.prevPosZ - this.prevPosZ;
					double var18 = this.playerToAttack.posY - this.posY;
					float var20 = this.aR;
					this.aR = (float)(Math.atan2(var18, var16) * 180.0D / (double)((float)Math.PI)) - 90.0F;
					var15 = (var20 - this.aR + 90.0F) * (float)Math.PI / 180.0F;
					this.av = -MathHelper.sin(var15) * this.moveStrafing * 1.0F;
					this.moveStrafing = MathHelper.cos(var15) * this.moveStrafing * 1.0F;
				}

				if(var12 > 0.0D) {
					this.ay = true;
				}
			}

			if(this.playerToAttack != null) {
				this.faceEntity(this.playerToAttack, 30.0F, 30.0F);
			}

			if(this.onGround && !this.hasPath()) {
				this.ay = true;
			}

			if(this.br.nextFloat() < 0.8F && (var23 || var24)) {
				this.ay = true;
			}

		} else {
			super.updatePlayerActionState();
			this.pathToEntity = null;
		}
	}

	protected void attackEntity(Entity var1, float var2) {
	}

	protected void attackBlockedEntity(Entity var1, float var2) {
	}

	protected float getBlockPathWeight(int var1, int var2, int var3) {
		return 0.0F;
	}

	protected Entity findPlayerToAttack() {
		return null;
	}

	public boolean getCanSpawnHere() {
		int var1 = MathHelper.floor_double(this.prevPosZ);
		int var2 = MathHelper.floor_double(this.aV.minY);
		int var3 = MathHelper.floor_double(this.posY);
		return super.getCanSpawnHere() && this.getBlockPathWeight(var1, var2, var3) >= 0.0F;
	}

	public boolean hasPath() {
		return this.pathToEntity != null;
	}

	public void setPathToEntity(PathEntity var1) {
		this.pathToEntity = var1;
	}

	public Entity getTarget() {
		return this.playerToAttack;
	}

	public void setTarget(Entity var1) {
		this.playerToAttack = var1;
	}
}
