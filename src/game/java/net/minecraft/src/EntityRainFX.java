package net.minecraft.src;

public class EntityRainFX extends EntityFX {
	public EntityRainFX(World var1, double var2, double var4, double var6) {
		super(var1, var2, var4, var6, 0.0D, 0.0D, 0.0D);
		this.posZ *= (double)0.3F;
		this.motionX = (double)((float)Math.random() * 0.2F + 0.1F);
		this.motionY *= (double)0.3F;
		this.particleRed = 1.0F;
		this.particleGreen = 1.0F;
		this.particleBlue = 1.0F;
		this.particleTextureIndex = 19 + this.br.nextInt(4);
		this.setSize(0.01F, 0.01F);
		this.particleGravity = 0.06F;
		this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
	}

	public void renderParticle(Tessellator var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		super.renderParticle(var1, var2, var3, var4, var5, var6, var7);
	}

	public void onUpdate() {
		this.aI = this.prevPosZ;
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.motionX -= (double)this.particleGravity;
		this.moveEntity(this.posZ, this.motionX, this.motionY);
		this.posZ *= (double)0.98F;
		this.motionX *= (double)0.98F;
		this.motionY *= (double)0.98F;
		if(this.particleMaxAge-- <= 0) {
			this.setEntityDead();
		}

		if(this.aW) {
			if(Math.random() < 0.5D) {
				this.setEntityDead();
			}

			this.posZ *= (double)0.7F;
			this.motionY *= (double)0.7F;
		}

		Material var1 = this.aH.getBlockMaterial(MathHelper.floor_double(this.prevPosZ), MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY));
		if(var1.getIsLiquid() || var1.isSolid()) {
			double var2 = (double)((float)(MathHelper.floor_double(this.posX) + 1) - BlockFluid.getPercentAir(this.aH.getBlockMetadata(MathHelper.floor_double(this.prevPosZ), MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY))));
			if(this.posX < var2) {
				this.setEntityDead();
			}
		}

	}
}
