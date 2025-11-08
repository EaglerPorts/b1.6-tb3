package net.minecraft.src;

public class EntitySnowShovelFX extends EntityFX {
	float field_27017_a;

	public EntitySnowShovelFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
		this(var1, var2, var4, var6, var8, var10, var12, 1.0F);
	}

	public EntitySnowShovelFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12, float var14) {
		super(var1, var2, var4, var6, var8, var10, var12);
		this.posZ *= (double)0.1F;
		this.motionX *= (double)0.1F;
		this.motionY *= (double)0.1F;
		this.posZ += var8;
		this.motionX += var10;
		this.motionY += var12;
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F - (float)(Math.random() * (double)0.3F);
		this.particleScale *= 12.0F / 16.0F;
		this.particleScale *= var14;
		this.field_27017_a = this.particleScale;
		this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
		this.particleMaxAge = (int)((float)this.particleMaxAge * var14);
		this.bp = false;
	}

	public void renderParticle(Tessellator var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		float var8 = ((float)this.particleAge + var2) / (float)this.particleMaxAge * 32.0F;
		if(var8 < 0.0F) {
			var8 = 0.0F;
		}

		if(var8 > 1.0F) {
			var8 = 1.0F;
		}

		this.particleScale = this.field_27017_a * var8;
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
		this.motionX -= 0.03D;
		this.moveEntity(this.posZ, this.motionX, this.motionY);
		this.posZ *= (double)0.99F;
		this.motionX *= (double)0.99F;
		this.motionY *= (double)0.99F;
		if(this.aW) {
			this.posZ *= (double)0.7F;
			this.motionY *= (double)0.7F;
		}

	}
}
