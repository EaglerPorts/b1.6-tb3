package net.minecraft.src;

import java.util.List;

public class EntityPigZombie extends EntityZombie {
	private int angerLevel = 0;
	private int randomSoundDelay = 0;
	private static final ItemStack defaultHeldItem = new ItemStack(Item.swordGold, 1);

	public EntityPigZombie(World var1) {
		super(var1);
		this.N = "/mob/pigzombie.png";
		this.defaultPitch = 0.5F;
		this.attackStrength = 5;
		this.bB = true;
	}

	public void onUpdate() {
		this.defaultPitch = this.playerToAttack != null ? 0.95F : 0.5F;
		if(this.randomSoundDelay > 0 && --this.randomSoundDelay == 0) {
			this.aH.playSoundAtEntity(this, "mob.zombiepig.zpigangry", this.getSoundVolume() * 2.0F, ((this.br.nextFloat() - this.br.nextFloat()) * 0.2F + 1.0F) * 1.8F);
		}

		super.onUpdate();
	}

	public boolean getCanSpawnHere() {
		return this.aH.difficultySetting > 0 && this.aH.checkIfAABBIsClear(this.aV) && this.aH.getCollidingBoundingBoxes(this, this.aV).size() == 0 && !this.aH.getIsAnyLiquid(this.aV);
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
		var1.setShort("Anger", (short)this.angerLevel);
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
		this.angerLevel = var1.getShort("Anger");
	}

	protected Entity findPlayerToAttack() {
		return this.angerLevel == 0 ? null : super.findPlayerToAttack();
	}

	public void onLivingUpdate() {
		super.onLivingUpdate();
	}

	public boolean attackEntityFrom(Entity var1, int var2) {
		if(var1 instanceof EntityPlayer) {
			List var3 = this.aH.getEntitiesWithinAABBExcludingEntity(this, this.aV.expand(32.0D, 32.0D, 32.0D));

			for(int var4 = 0; var4 < var3.size(); ++var4) {
				Entity var5 = (Entity)var3.get(var4);
				if(var5 instanceof EntityPigZombie) {
					EntityPigZombie var6 = (EntityPigZombie)var5;
					var6.becomeAngryAt(var1);
				}
			}

			this.becomeAngryAt(var1);
		}

		return super.attackEntityFrom(var1, var2);
	}

	private void becomeAngryAt(Entity var1) {
		this.playerToAttack = var1;
		this.angerLevel = 400 + this.br.nextInt(400);
		this.randomSoundDelay = this.br.nextInt(40);
	}

	protected String getLivingSound() {
		return "mob.zombiepig.zpig";
	}

	protected String getHurtSound() {
		return "mob.zombiepig.zpighurt";
	}

	protected String getDeathSound() {
		return "mob.zombiepig.zpigdeath";
	}

	protected int getDropItemId() {
		return Item.porkCooked.shiftedIndex;
	}

	public ItemStack getHeldItem() {
		return defaultHeldItem;
	}
}
