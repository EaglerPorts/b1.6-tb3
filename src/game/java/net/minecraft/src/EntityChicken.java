package net.minecraft.src;

public class EntityChicken extends EntityAnimal {
	public boolean field_753_a = false;
	public float field_752_b = 0.0F;
	public float destPos = 0.0F;
	public float field_757_d;
	public float field_756_e;
	public float field_755_h = 1.0F;
	public int timeUntilNextEgg;

	public EntityChicken(World var1) {
		super(var1);
		this.N = "/mob/chicken.png";
		this.setSize(0.3F, 0.4F);
		this.X = 4;
		this.timeUntilNextEgg = this.br.nextInt(6000) + 6000;
	}

	public void onLivingUpdate() {
		super.onLivingUpdate();
		this.field_756_e = this.field_752_b;
		this.field_757_d = this.destPos;
		this.destPos = (float)((double)this.destPos + (double)(this.aW ? -1 : 4) * 0.3D);
		if(this.destPos < 0.0F) {
			this.destPos = 0.0F;
		}

		if(this.destPos > 1.0F) {
			this.destPos = 1.0F;
		}

		if(!this.aW && this.field_755_h < 1.0F) {
			this.field_755_h = 1.0F;
		}

		this.field_755_h = (float)((double)this.field_755_h * 0.9D);
		if(!this.aW && this.motionX < 0.0D) {
			this.motionX *= 0.6D;
		}

		this.field_752_b += this.field_755_h * 2.0F;
		if(!this.aH.multiplayerWorld && --this.timeUntilNextEgg <= 0) {
			this.aH.playSoundAtEntity(this, "mob.chickenplop", 1.0F, (this.br.nextFloat() - this.br.nextFloat()) * 0.2F + 1.0F);
			this.dropItem(Item.egg.shiftedIndex, 1);
			this.timeUntilNextEgg = this.br.nextInt(6000) + 6000;
		}

	}

	protected void fall(float var1) {
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
	}

	protected String getLivingSound() {
		return "mob.chicken";
	}

	protected String getHurtSound() {
		return "mob.chickenhurt";
	}

	protected String getDeathSound() {
		return "mob.chickenhurt";
	}

	protected int getDropItemId() {
		return Item.feather.shiftedIndex;
	}
}
