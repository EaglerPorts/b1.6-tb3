package net.minecraft.src;

public class EntityExplodeFX extends EntityFX {
	public EntityExplodeFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
		super(var1, var2, var4, var6, var8, var10, var12);
		this.posZ = var8 + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.05F);
		this.motionX = var10 + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.05F);
		this.motionY = var12 + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.05F);
		this.particleRed = this.particleGreen = this.particleBlue = this.br.nextFloat() * 0.3F + 0.7F;
		this.particleScale = this.br.nextFloat() * this.br.nextFloat() * 6.0F + 1.0F;
		this.particleMaxAge = (int)(16.0D / ((double)this.br.nextFloat() * 0.8D + 0.2D)) + 2;
	}

	public void renderParticle(Tessellator var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		super.renderParticle(var1, var2, var3, var4, var5, var6, var7);
	}

	public void onUpdate() {
		this.aI = this.prevPosZ;
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		if(this.particleAge++ >= this.particleMaxAge) {
			this.setEntityDead();
		}

		this.particleTextureIndex = 7 - this.particleAge * 8 / this.particleMaxAge;
		this.motionX += 0.004D;
		this.moveEntity(this.posZ, this.motionX, this.motionY);
		this.posZ *= (double)0.9F;
		this.motionX *= (double)0.9F;
		this.motionY *= (double)0.9F;
		if(this.aW) {
			this.posZ *= (double)0.7F;
			this.motionY *= (double)0.7F;
		}

	}
}
