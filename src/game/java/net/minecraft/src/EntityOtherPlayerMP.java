package net.minecraft.src;

public class EntityOtherPlayerMP extends EntityPlayer {
	private int field_785_bg;
	private double bL;
	private double field_784_bh;
	private double field_783_bi;
	private double field_782_bj;
	private double field_780_bk;
	float field_20924_a = 0.0F;

	public EntityOtherPlayerMP(World var1, String var2) {
		super(var1);
		this.username = var2;
		this.be = 0.0F;
		this.ySize = 0.0F;
		this.bp = true;
		this.field_22063_x = 0.25F;
		this.aD = 10.0D;
	}

	protected void resetHeight() {
		this.be = 0.0F;
	}

	public boolean attackEntityFrom(Entity var1, int var2) {
		return true;
	}

	public void setPositionAndRotation2(double var1, double var3, double var5, float var7, float var8, int var9) {
		this.bL = var1;
		this.field_784_bh = var3;
		this.field_783_bi = var5;
		this.field_782_bj = (double)var7;
		this.field_780_bk = (double)var8;
		this.field_785_bg = var9;
	}

	public void onUpdate() {
		super.onUpdate();
		this.field_9325_U = this.field_705_Q;
		double var1 = this.prevPosZ - this.aI;
		double var3 = this.posY - this.prevPosY;
		float var5 = MathHelper.sqrt_double(var1 * var1 + var3 * var3) * 4.0F;
		if(var5 > 1.0F) {
			var5 = 1.0F;
		}

		this.field_705_Q += (var5 - this.field_705_Q) * 0.4F;
		this.field_704_R += this.field_705_Q;
	}

	public float getShadowSize() {
		return 0.0F;
	}

	public void onLivingUpdate() {
		super.updatePlayerActionState();
		if(this.field_785_bg > 0) {
			double var1 = this.prevPosZ + (this.bL - this.prevPosZ) / (double)this.field_785_bg;
			double var3 = this.posX + (this.field_784_bh - this.posX) / (double)this.field_785_bg;
			double var5 = this.posY + (this.field_783_bi - this.posY) / (double)this.field_785_bg;

			double var7;
			for(var7 = this.field_782_bj - (double)this.aR; var7 < -180.0D; var7 += 360.0D) {
			}

			while(var7 >= 180.0D) {
				var7 -= 360.0D;
			}

			this.aR = (float)((double)this.aR + var7 / (double)this.field_785_bg);
			this.rotationYaw = (float)((double)this.rotationYaw + (this.field_780_bk - (double)this.rotationYaw) / (double)this.field_785_bg);
			--this.field_785_bg;
			this.setPosition(var1, var3, var5);
			this.setRotation(this.aR, this.rotationYaw);
		}

		this.field_775_e = this.field_774_f;
		float var9 = MathHelper.sqrt_double(this.posZ * this.posZ + this.motionY * this.motionY);
		float var2 = (float)Math.atan(-this.motionX * (double)0.2F) * 15.0F;
		if(var9 > 0.1F) {
			var9 = 0.1F;
		}

		if(!this.aW || this.X <= 0) {
			var9 = 0.0F;
		}

		if(this.aW || this.X <= 0) {
			var2 = 0.0F;
		}

		this.field_774_f += (var9 - this.field_774_f) * 0.4F;
		this.cameraPitch += (var2 - this.cameraPitch) * 0.8F;
	}

	public void outfitWithItem(int var1, int var2, int var3) {
		ItemStack var4 = null;
		if(var2 >= 0) {
			var4 = new ItemStack(var2, 1, var3);
		}

		if(var1 == 0) {
			this.inventory.mainInventory[this.inventory.currentItem] = var4;
		} else {
			this.inventory.armorInventory[var1 - 1] = var4;
		}

	}

	public void func_6420_o() {
	}
}
