package net.minecraft.src;

public class EntityMob extends EntityCreature implements IMob {
	protected int attackStrength = 2;

	public EntityMob(World var1) {
		super(var1);
		this.X = 20;
	}

	public void onLivingUpdate() {
		float var1 = this.getEntityBrightness(1.0F);
		if(var1 > 0.5F) {
			this.field_9346_af += 2;
		}

		super.onLivingUpdate();
	}

	public void onUpdate() {
		super.onUpdate();
		if(!this.aH.multiplayerWorld && this.aH.difficultySetting == 0) {
			this.setEntityDead();
		}

	}

	protected Entity findPlayerToAttack() {
		EntityPlayer var1 = this.aH.getClosestPlayerToEntity(this, 16.0D);
		return var1 != null && this.canEntityBeSeen(var1) ? var1 : null;
	}

	public boolean attackEntityFrom(Entity var1, int var2) {
		if(super.attackEntityFrom(var1, var2)) {
			if(this.aF != var1 && this.riddenByEntity != var1) {
				if(var1 != this) {
					this.playerToAttack = var1;
				}

				return true;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	protected void attackEntity(Entity var1, float var2) {
		if(this.deathTime <= 0 && var2 < 2.0F && var1.aV.maxY > this.aV.minY && var1.aV.minY < this.aV.maxY) {
			this.deathTime = 20;
			var1.attackEntityFrom(this, this.attackStrength);
		}

	}

	protected float getBlockPathWeight(int var1, int var2, int var3) {
		return 0.5F - this.aH.getLightBrightness(var1, var2, var3);
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
	}

	public boolean getCanSpawnHere() {
		int var1 = MathHelper.floor_double(this.prevPosZ);
		int var2 = MathHelper.floor_double(this.aV.minY);
		int var3 = MathHelper.floor_double(this.posY);
		if(this.aH.getSavedLightValue(EnumSkyBlock.Sky, var1, var2, var3) > this.br.nextInt(32)) {
			return false;
		} else {
			int var4 = this.aH.getBlockLightValue(var1, var2, var3);
			if(this.aH.func_27160_B()) {
				int var5 = this.aH.skylightSubtracted;
				this.aH.skylightSubtracted = 10;
				var4 = this.aH.getBlockLightValue(var1, var2, var3);
				this.aH.skylightSubtracted = var5;
			}

			return var4 <= this.br.nextInt(8) && super.getCanSpawnHere();
		}
	}
}
