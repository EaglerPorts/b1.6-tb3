package net.minecraft.src;

import java.util.ArrayList;

import dev.colbster937.eaglercraft.SingleplayerCommands;
import net.minecraft.client.Minecraft;

public class EntityPlayerSP extends EntityPlayer {
	public MovementInput movementInput;
	protected Minecraft mc;
	private MouseFilter bL = new MouseFilter();
	private MouseFilter field_21903_bJ = new MouseFilter();
	private MouseFilter field_21904_bK = new MouseFilter();
	private ArrayList bO = new ArrayList();

	public EntityPlayerSP(Minecraft var1, World var2, Session var3, int var4) {
		super(var2);
		this.mc = var1;
		this.dimension = var4;
		this.username = var3.username;
	}

	public void moveEntity(double var1, double var3, double var5) {
		super.moveEntity(var1, var3, var5);
	}

	public void updatePlayerActionState() {
		super.updatePlayerActionState();
		this.av = this.movementInput.moveStrafe;
		this.moveStrafing = this.movementInput.moveForward;
		this.ay = this.movementInput.jump;
	}

	public void onLivingUpdate() {
		if(!this.mc.statFileWriter.hasAchievementUnlocked(AchievementList.openInventory)) {
			this.mc.guiAchievement.queueAchievementInformation(AchievementList.openInventory);
		}

		this.timeInPortal = this.A;
		if(this.z) {
			if(!this.aH.multiplayerWorld) {
				if(this.riddenByEntity != null) {
					this.mountEntity((Entity)null);
				}

				if(this.mc.currentScreen != null) {
					this.mc.displayGuiScreen((GuiScreen)null);
				}
			}

			if(this.A == 0.0F) {
				this.mc.sndManager.playSoundFX("portal.trigger", 1.0F, this.br.nextFloat() * 0.4F + 0.8F);
			}

			this.A += 0.0125F;
			if(this.A >= 1.0F) {
				this.A = 1.0F;
				if(!this.aH.multiplayerWorld) {
					this.y = 10;
					this.mc.sndManager.playSoundFX("portal.travel", 1.0F, this.br.nextFloat() * 0.4F + 0.8F);
					this.mc.usePortal();
				}
			}

			this.z = false;
		} else {
			if(this.A > 0.0F) {
				this.A -= 0.05F;
			}

			if(this.A < 0.0F) {
				this.A = 0.0F;
			}
		}

		if(this.y > 0) {
			--this.y;
		}

		this.movementInput.updatePlayerMoveState(this);
		if(this.movementInput.sneak && this.bn < 0.2F) {
			this.bn = 0.2F;
		}

		this.func_28014_c(this.prevPosZ - (double)this.yOffset * 0.45D, this.aV.minY, this.posY + (double)this.yOffset * 0.45D);
		this.func_28014_c(this.prevPosZ - (double)this.yOffset * 0.45D, this.aV.minY, this.posY - (double)this.yOffset * 0.45D);
		this.func_28014_c(this.prevPosZ + (double)this.yOffset * 0.45D, this.aV.minY, this.posY - (double)this.yOffset * 0.45D);
		this.func_28014_c(this.prevPosZ + (double)this.yOffset * 0.45D, this.aV.minY, this.posY + (double)this.yOffset * 0.45D);
		super.onLivingUpdate();
	}

	public void resetPlayerKeyState() {
		this.movementInput.resetKeyState();
	}

	public void handleKeyPress(int var1, boolean var2) {
		this.movementInput.checkKeyForMovementInput(var1, var2);
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
		var1.setInteger("Score", this.score);
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
		this.score = var1.getInteger("Score");
	}

	public void func_20059_m() {
		super.func_20059_m();
		this.mc.displayGuiScreen((GuiScreen)null);
	}

	public void displayGUIEditSign(TileEntitySign var1) {
		this.mc.displayGuiScreen(new GuiEditSign(var1));
	}

	public void displayGUIChest(IInventory var1) {
		this.mc.displayGuiScreen(new GuiChest(this.inventory, var1));
	}

	public void displayWorkbenchGUI(int var1, int var2, int var3) {
		this.mc.displayGuiScreen(new GuiCrafting(this.inventory, this.aH, var1, var2, var3));
	}

	public void displayGUIFurnace(TileEntityFurnace var1) {
		this.mc.displayGuiScreen(new GuiFurnace(this.inventory, var1));
	}

	public void displayGUIDispenser(TileEntityDispenser var1) {
		this.mc.displayGuiScreen(new GuiDispenser(this.inventory, var1));
	}

	public void onItemPickup(Entity var1, int var2) {
		this.mc.effectRenderer.addEffect(new EntityPickupFX(this.mc.theWorld, var1, this, -0.5F));
	}

	public int getPlayerArmorValue() {
		return this.inventory.getTotalArmorValue();
	}

	public void sendChatMessage(String var1) {
		SingleplayerCommands.showDummyChat(var1);
	}

	public boolean isSneaking() {
		return this.movementInput.sneak && !this.sleeping;
	}

	public void setHealth(int var1) {
		int var2 = this.X - var1;
		if(var2 <= 0) {
			this.X = var1;
			if(var2 < 0) {
				this.bx = this.D / 2;
			}
		} else {
			this.at = var2;
			this.health = this.X;
			this.bx = this.D;
			this.damageEntity(var2);
			this.prevHealth = this.hurtTime = 10;
		}

	}

	public void respawnPlayer() {
		this.mc.respawn(false, 0);
	}

	public void func_6420_o() {
	}

	public void addChatMessage(String var1) {
		this.mc.ingameGUI.func_22064_c(var1);
	}

	public void addStat(StatBase var1, int var2) {
		if(var1 != null) {
			if(var1.func_25067_a()) {
				Achievement var3 = (Achievement)var1;
				if(var3.parentAchievement == null || this.mc.statFileWriter.hasAchievementUnlocked(var3.parentAchievement)) {
					if(!this.mc.statFileWriter.hasAchievementUnlocked(var3)) {
						this.mc.guiAchievement.queueTakenAchievement(var3);
					}

					this.mc.statFileWriter.func_25100_a(var1, var2);
				}
			} else {
				this.mc.statFileWriter.func_25100_a(var1, var2);
			}

		}
	}

	private boolean func_28027_d(int var1, int var2, int var3) {
		int var4 = this.aH.getBlockId(var1, var2, var3);
		if(var4 == 0) {
			return false;
		} else {
			this.bO.clear();
			Block var5 = Block.blocksList[var4];
			if(!var5.renderAsNormalBlock()) {
				return false;
			} else {
				var5.getCollidingBoundingBoxes(this.aH, var1, var2, var3, this.aV, this.bO);
				return this.bO.size() > 0;
			}
		}
	}

	protected boolean func_28014_c(double var1, double var3, double var5) {
		int var7 = MathHelper.floor_double(var1);
		int var8 = MathHelper.floor_double(var3);
		int var9 = MathHelper.floor_double(var5);
		double var10 = var1 - (double)var7;
		double var12 = var5 - (double)var9;
		if(this.func_28027_d(var7, var8, var9) || this.func_28027_d(var7, var8 + 1, var9)) {
			boolean var14 = !this.func_28027_d(var7 - 1, var8, var9) && !this.func_28027_d(var7 - 1, var8 + 1, var9);
			boolean var15 = !this.func_28027_d(var7 + 1, var8, var9) && !this.func_28027_d(var7 + 1, var8 + 1, var9);
			boolean var16 = !this.func_28027_d(var7, var8, var9 - 1) && !this.func_28027_d(var7, var8 + 1, var9 - 1);
			boolean var17 = !this.func_28027_d(var7, var8, var9 + 1) && !this.func_28027_d(var7, var8 + 1, var9 + 1);
			byte var18 = -1;
			double var19 = 9999.0D;
			if(var14 && var10 < var19) {
				var19 = var10;
				var18 = 0;
			}

			if(var15 && 1.0D - var10 < var19) {
				var19 = 1.0D - var10;
				var18 = 1;
			}

			if(var16 && var12 < var19) {
				var19 = var12;
				var18 = 4;
			}

			if(var17 && 1.0D - var12 < var19) {
				var19 = 1.0D - var12;
				var18 = 5;
			}

			float var21 = 0.1F;
			if(var18 == 0) {
				this.posZ = (double)(-var21);
			}

			if(var18 == 1) {
				this.posZ = (double)var21;
			}

			if(var18 == 4) {
				this.motionY = (double)(-var21);
			}

			if(var18 == 5) {
				this.motionY = (double)var21;
			}
		}

		return false;
	}
}
