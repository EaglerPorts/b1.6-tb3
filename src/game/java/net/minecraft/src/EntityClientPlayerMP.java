package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class EntityClientPlayerMP extends EntityPlayerSP {
	public NetClientHandler bL;
	private int bM = 0;
	private boolean bN = false;
	private double bO;
	private double oldPosX;
	private double field_9378_bz;
	private double oldPosY;
	private float bS;
	private float oldRotationYaw;
	private boolean bU = false;
	private boolean field_9382_bF = false;
	private int bW = 0;

	public EntityClientPlayerMP(Minecraft var1, World var2, Session var3, NetClientHandler var4) {
		super(var1, var2, var3, 0);
		this.bL = var4;
	}

	public boolean attackEntityFrom(Entity var1, int var2) {
		return false;
	}

	public void heal(int var1) {
	}

	public void onUpdate() {
		if(this.aH.blockExists(MathHelper.floor_double(this.prevPosZ), 64, MathHelper.floor_double(this.posY))) {
			super.onUpdate();
			this.func_4056_N();
		}
	}

	public void func_4056_N() {
		if(this.bM++ == 20) {
			this.sendInventoryChanged();
			this.bM = 0;
		}

		boolean var1 = this.isSneaking();
		if(var1 != this.field_9382_bF) {
			if(var1) {
				this.bL.addToSendQueue(new Packet19EntityAction(this, 1));
			} else {
				this.bL.addToSendQueue(new Packet19EntityAction(this, 2));
			}

			this.field_9382_bF = var1;
		}

		double var2 = this.prevPosZ - this.bO;
		double var4 = this.aV.minY - this.oldPosX;
		double var6 = this.posX - this.field_9378_bz;
		double var8 = this.posY - this.oldPosY;
		double var10 = (double)(this.aR - this.bS);
		double var12 = (double)(this.rotationYaw - this.oldRotationYaw);
		boolean var14 = var4 != 0.0D || var6 != 0.0D || var2 != 0.0D || var8 != 0.0D;
		boolean var15 = var10 != 0.0D || var12 != 0.0D;
		if(this.riddenByEntity != null) {
			if(var15) {
				this.bL.addToSendQueue(new Packet11PlayerPosition(this.posZ, -999.0D, -999.0D, this.motionY, this.aW));
			} else {
				this.bL.addToSendQueue(new Packet13PlayerLookMove(this.posZ, -999.0D, -999.0D, this.motionY, this.aR, this.rotationYaw, this.aW));
			}

			var14 = false;
		} else if(var14 && var15) {
			this.bL.addToSendQueue(new Packet13PlayerLookMove(this.prevPosZ, this.aV.minY, this.posX, this.posY, this.aR, this.rotationYaw, this.aW));
			this.bW = 0;
		} else if(var14) {
			this.bL.addToSendQueue(new Packet11PlayerPosition(this.prevPosZ, this.aV.minY, this.posX, this.posY, this.aW));
			this.bW = 0;
		} else if(var15) {
			this.bL.addToSendQueue(new Packet12PlayerLook(this.aR, this.rotationYaw, this.aW));
			this.bW = 0;
		} else {
			this.bL.addToSendQueue(new Packet10Flying(this.aW));
			if(this.bU == this.aW && this.bW <= 200) {
				++this.bW;
			} else {
				this.bW = 0;
			}
		}

		this.bU = this.aW;
		if(var14) {
			this.bO = this.prevPosZ;
			this.oldPosX = this.aV.minY;
			this.field_9378_bz = this.posX;
			this.oldPosY = this.posY;
		}

		if(var15) {
			this.bS = this.aR;
			this.oldRotationYaw = this.rotationYaw;
		}

	}

	public void dropCurrentItem() {
		this.bL.addToSendQueue(new Packet14BlockDig(4, 0, 0, 0, 0));
	}

	private void sendInventoryChanged() {
	}

	protected void joinEntityItemWithWorld(EntityItem var1) {
	}

	public void sendChatMessage(String var1) {
		this.bL.addToSendQueue(new Packet3Chat(var1));
	}

	public void swingItem() {
		super.swingItem();
		this.bL.addToSendQueue(new Packet18Animation(this, 1));
	}

	public void respawnPlayer() {
		this.sendInventoryChanged();
		this.bL.addToSendQueue(new Packet9Respawn((byte)this.dimension));
	}

	protected void damageEntity(int var1) {
		this.X -= var1;
	}

	public void func_20059_m() {
		this.bL.addToSendQueue(new Packet101CloseWindow(this.craftingInventory.windowId));
		this.inventory.setItemStack((ItemStack)null);
		super.func_20059_m();
	}

	public void setHealth(int var1) {
		if(this.bN) {
			super.setHealth(var1);
		} else {
			this.X = var1;
			this.bN = true;
		}

	}

	public void addStat(StatBase var1, int var2) {
		if(var1 != null) {
			if(var1.field_27088_g) {
				super.addStat(var1, var2);
			}

		}
	}

	public void func_27027_b(StatBase var1, int var2) {
		if(var1 != null) {
			if(!var1.field_27088_g) {
				super.addStat(var1, var2);
			}

		}
	}
}
