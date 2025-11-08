package net.minecraft.src;

public class EntitySplashFX extends EntityRainFX {
	public EntitySplashFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
		super(var1, var2, var4, var6);
		this.particleGravity = 0.04F;
		++this.particleTextureIndex;
		if(var10 == 0.0D && (var8 != 0.0D || var12 != 0.0D)) {
			this.posZ = var8;
			this.motionX = var10 + 0.1D;
			this.motionY = var12;
		}

	}
}
