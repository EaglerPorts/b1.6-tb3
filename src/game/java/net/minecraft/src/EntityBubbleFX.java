package net.minecraft.src;

public class EntityBubbleFX extends EntityFX {
	public EntityBubbleFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
		super(var1, var2, var4, var6, var8, var10, var12);
		this.particleRed = 1.0F;
		this.particleGreen = 1.0F;
		this.particleBlue = 1.0F;
		this.particleTextureIndex = 32;
		this.setSize(0.02F, 0.02F);
		this.particleScale *= this.br.nextFloat() * 0.6F + 0.2F;
		this.posZ = var8 * (double)0.2F + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.02F);
		this.motionX = var10 * (double)0.2F + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.02F);
		this.motionY = var12 * (double)0.2F + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.02F);
		this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
	}

	public void onUpdate() {
		this.aI = this.prevPosZ;
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.motionX += 0.002D;
		this.moveEntity(this.posZ, this.motionX, this.motionY);
		this.posZ *= (double)0.85F;
		this.motionX *= (double)0.85F;
		this.motionY *= (double)0.85F;
		if(this.aH.getBlockMaterial(MathHelper.floor_double(this.prevPosZ), MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY)) != Material.water) {
			this.setEntityDead();
		}

		if(this.particleMaxAge-- <= 0) {
			this.setEntityDead();
		}

	}
}
