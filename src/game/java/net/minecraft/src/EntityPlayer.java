package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

public abstract class EntityPlayer extends EntityLiving {
	public InventoryPlayer inventory = new InventoryPlayer(this);
	public Container inventorySlots;
	public Container craftingInventory;
	public byte field_9371_f = 0;
	public int score = 0;
	public float field_775_e;
	public float field_774_f;
	public boolean isSwinging = false;
	public int swingProgressInt = 0;
	public String username;
	public int dimension;
	public String playerCloakUrl;
	public double field_20066_r;
	public double field_20065_s;
	public double field_20064_t;
	public double field_20063_u;
	public double field_20062_v;
	public double field_20061_w;
	protected boolean sleeping;
	private ChunkCoordinates a;
	private int b;
	public float v;
	public float field_22063_x;
	public float field_22062_y;
	private ChunkCoordinates bL;
	private ChunkCoordinates startMinecartRidingCoordinate;
	public int y = 20;
	protected boolean z = false;
	public float A;
	public float timeInPortal;
	private int damageRemainder = 0;
	public EntityFish C = null;

	public EntityPlayer(World var1) {
		super(var1);
		this.inventorySlots = new ContainerPlayer(this.inventory, !var1.multiplayerWorld);
		this.craftingInventory = this.inventorySlots;
		this.be = 1.62F;
		ChunkCoordinates var2 = var1.getSpawnPoint();
		this.setLocationAndAngles((double)var2.x + 0.5D, (double)(var2.y + 1), (double)var2.z + 0.5D, 0.0F, 0.0F);
		this.X = 20;
		this.Q = "humanoid";
		this.P = 180.0F;
		this.ticksExisted = 20;
		this.N = "/mob/char.png";
	}

	protected void entityInit() {
		super.entityInit();
		this.bC.addObject(16, Byte.valueOf((byte)0));
	}

	public void onUpdate() {
		if(this.isPlayerSleeping()) {
			++this.b;
			if(this.b > 100) {
				this.b = 100;
			}

			if(!this.isInBed()) {
				this.wakeUpPlayer(true, true, false);
			} else if(!this.aH.multiplayerWorld && this.aH.isDaytime()) {
				this.wakeUpPlayer(false, true, true);
			}
		} else if(this.b > 0) {
			++this.b;
			if(this.b >= 110) {
				this.b = 0;
			}
		}

		super.onUpdate();
		if(!this.aH.multiplayerWorld && this.craftingInventory != null && !this.craftingInventory.isUsableByPlayer(this)) {
			this.func_20059_m();
			this.craftingInventory = this.inventorySlots;
		}

		this.field_20066_r = this.field_20063_u;
		this.field_20065_s = this.field_20062_v;
		this.field_20064_t = this.field_20061_w;
		double var1 = this.prevPosZ - this.field_20063_u;
		double var3 = this.posX - this.field_20062_v;
		double var5 = this.posY - this.field_20061_w;
		double var7 = 10.0D;
		if(var1 > var7) {
			this.field_20066_r = this.field_20063_u = this.prevPosZ;
		}

		if(var5 > var7) {
			this.field_20064_t = this.field_20061_w = this.posY;
		}

		if(var3 > var7) {
			this.field_20065_s = this.field_20062_v = this.posX;
		}

		if(var1 < -var7) {
			this.field_20066_r = this.field_20063_u = this.prevPosZ;
		}

		if(var5 < -var7) {
			this.field_20064_t = this.field_20061_w = this.posY;
		}

		if(var3 < -var7) {
			this.field_20065_s = this.field_20062_v = this.posX;
		}

		this.field_20063_u += var1 * 0.25D;
		this.field_20061_w += var5 * 0.25D;
		this.field_20062_v += var3 * 0.25D;
		this.addStat(StatList.minutesPlayedStat, 1);
		if(this.riddenByEntity == null) {
			this.startMinecartRidingCoordinate = null;
		}

	}

	protected boolean isMovementBlocked() {
		return this.X <= 0 || this.isPlayerSleeping();
	}

	protected void func_20059_m() {
		this.craftingInventory = this.inventorySlots;
	}

	public void updateCloak() {
		this.playerCloakUrl = "http://s3.amazonaws.com/MinecraftCloaks/" + this.username + ".png";
		this.skinUrl = this.playerCloakUrl;
	}

	public void updateRidden() {
		double var1 = this.prevPosZ;
		double var3 = this.posX;
		double var5 = this.posY;
		super.updateRidden();
		this.field_775_e = this.field_774_f;
		this.field_774_f = 0.0F;
		this.addMountedMovementStat(this.prevPosZ - var1, this.posX - var3, this.posY - var5);
	}

	public void preparePlayerToSpawn() {
		this.be = 1.62F;
		this.setSize(0.6F, 1.8F);
		super.preparePlayerToSpawn();
		this.X = 20;
		this.ac = 0;
	}

	protected void updatePlayerActionState() {
		if(this.isSwinging) {
			++this.swingProgressInt;
			if(this.swingProgressInt >= 8) {
				this.swingProgressInt = 0;
				this.isSwinging = false;
			}
		} else {
			this.swingProgressInt = 0;
		}

		this.prevSwingProgress = (float)this.swingProgressInt / 8.0F;
	}

	public void onLivingUpdate() {
		if(this.aH.difficultySetting == 0 && this.X < 20 && this.bs % 20 * 12 == 0) {
			this.heal(1);
		}

		this.inventory.decrementAnimations();
		this.field_775_e = this.field_774_f;
		super.onLivingUpdate();
		float var1 = MathHelper.sqrt_double(this.posZ * this.posZ + this.motionY * this.motionY);
		float var2 = (float)Math.atan(-this.motionX * (double)0.2F) * 15.0F;
		if(var1 > 0.1F) {
			var1 = 0.1F;
		}

		if(!this.aW || this.X <= 0) {
			var1 = 0.0F;
		}

		if(this.aW || this.X <= 0) {
			var2 = 0.0F;
		}

		this.field_774_f += (var1 - this.field_774_f) * 0.4F;
		this.cameraPitch += (var2 - this.cameraPitch) * 0.8F;
		if(this.X > 0) {
			List var3 = this.aH.getEntitiesWithinAABBExcludingEntity(this, this.aV.expand(1.0D, 0.0D, 1.0D));
			if(var3 != null) {
				for(int var4 = 0; var4 < var3.size(); ++var4) {
					Entity var5 = (Entity)var3.get(var4);
					if(!var5.field_9293_aM) {
						this.func_451_h(var5);
					}
				}
			}
		}

	}

	private void func_451_h(Entity var1) {
		var1.onCollideWithPlayer(this);
	}

	public int getScore() {
		return this.score;
	}

	public void onDeath(Entity var1) {
		super.onDeath(var1);
		this.setSize(0.2F, 0.2F);
		this.setPosition(this.prevPosZ, this.posX, this.posY);
		this.motionX = (double)0.1F;
		if(this.username.equals("Notch")) {
			this.dropPlayerItemWithRandomChoice(new ItemStack(Item.appleRed, 1), true);
		}

		this.inventory.dropAllItems();
		if(var1 != null) {
			this.posZ = (double)(-MathHelper.cos((this.ab + this.aR) * (float)Math.PI / 180.0F) * 0.1F);
			this.motionY = (double)(-MathHelper.sin((this.ab + this.aR) * (float)Math.PI / 180.0F) * 0.1F);
		} else {
			this.posZ = this.motionY = 0.0D;
		}

		this.be = 0.1F;
		this.addStat(StatList.deathsStat, 1);
	}

	public void addToPlayerScore(Entity var1, int var2) {
		this.score += var2;
		if(var1 instanceof EntityPlayer) {
			this.addStat(StatList.playerKillsStat, 1);
		} else {
			this.addStat(StatList.mobKillsStat, 1);
		}

	}

	public void dropCurrentItem() {
		this.dropPlayerItemWithRandomChoice(this.inventory.decrStackSize(this.inventory.currentItem, 1), false);
	}

	public void dropPlayerItem(ItemStack var1) {
		this.dropPlayerItemWithRandomChoice(var1, false);
	}

	public void dropPlayerItemWithRandomChoice(ItemStack var1, boolean var2) {
		if(var1 != null) {
			EntityItem var3 = new EntityItem(this.aH, this.prevPosZ, this.posX - (double)0.3F + (double)this.getEyeHeight(), this.posY, var1);
			var3.delayBeforeCanPickup = 40;
			float var4 = 0.1F;
			float var5;
			if(var2) {
				var5 = this.br.nextFloat() * 0.5F;
				float var6 = this.br.nextFloat() * (float)Math.PI * 2.0F;
				var3.posZ = (double)(-MathHelper.sin(var6) * var5);
				var3.motionY = (double)(MathHelper.cos(var6) * var5);
				var3.motionX = (double)0.2F;
			} else {
				var4 = 0.3F;
				var3.posZ = (double)(-MathHelper.sin(this.aR / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * var4);
				var3.motionY = (double)(MathHelper.cos(this.aR / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * var4);
				var3.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * var4 + 0.1F);
				var4 = 0.02F;
				var5 = this.br.nextFloat() * (float)Math.PI * 2.0F;
				var4 *= this.br.nextFloat();
				var3.posZ += Math.cos((double)var5) * (double)var4;
				var3.motionX += (double)((this.br.nextFloat() - this.br.nextFloat()) * 0.1F);
				var3.motionY += Math.sin((double)var5) * (double)var4;
			}

			this.joinEntityItemWithWorld(var3);
			this.addStat(StatList.dropStat, 1);
		}
	}

	protected void joinEntityItemWithWorld(EntityItem var1) {
		this.aH.entityJoinedWorld(var1);
	}

	public float getCurrentPlayerStrVsBlock(Block var1) {
		float var2 = this.inventory.getStrVsBlock(var1);
		if(this.isInsideOfMaterial(Material.water)) {
			var2 /= 5.0F;
		}

		if(!this.aW) {
			var2 /= 5.0F;
		}

		return var2;
	}

	public boolean canHarvestBlock(Block var1) {
		return this.inventory.canHarvestBlock(var1);
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
		NBTTagList var2 = var1.getTagList("Inventory");
		this.inventory.readFromNBT(var2);
		this.dimension = var1.getInteger("Dimension");
		this.sleeping = var1.getBoolean("Sleeping");
		this.b = var1.getShort("SleepTimer");
		if(this.sleeping) {
			this.a = new ChunkCoordinates(MathHelper.floor_double(this.prevPosZ), MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY));
			this.wakeUpPlayer(true, true, false);
		}

		if(var1.hasKey("SpawnX") && var1.hasKey("SpawnY") && var1.hasKey("SpawnZ")) {
			this.bL = new ChunkCoordinates(var1.getInteger("SpawnX"), var1.getInteger("SpawnY"), var1.getInteger("SpawnZ"));
		}

	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
		var1.setTag("Inventory", this.inventory.writeToNBT(new NBTTagList()));
		var1.setInteger("Dimension", this.dimension);
		var1.setBoolean("Sleeping", this.sleeping);
		var1.setShort("SleepTimer", (short)this.b);
		if(this.bL != null) {
			var1.setInteger("SpawnX", this.bL.x);
			var1.setInteger("SpawnY", this.bL.y);
			var1.setInteger("SpawnZ", this.bL.z);
		}

	}

	public void displayGUIChest(IInventory var1) {
	}

	public void displayWorkbenchGUI(int var1, int var2, int var3) {
	}

	public void onItemPickup(Entity var1, int var2) {
	}

	public float getEyeHeight() {
		return 0.12F;
	}

	protected void resetHeight() {
		this.be = 1.62F;
	}

	public boolean attackEntityFrom(Entity var1, int var2) {
		this.field_9346_af = 0;
		if(this.X <= 0) {
			return false;
		} else {
			if(this.isPlayerSleeping()) {
				this.wakeUpPlayer(true, true, false);
			}

			if(var1 instanceof EntityMob || var1 instanceof EntityArrow) {
				if(this.aH.difficultySetting == 0) {
					var2 = 0;
				}

				if(this.aH.difficultySetting == 1) {
					var2 = var2 / 3 + 1;
				}

				if(this.aH.difficultySetting == 3) {
					var2 = var2 * 3 / 2;
				}
			}

			if(var2 == 0) {
				return false;
			} else {
				Object var3 = var1;
				if(var1 instanceof EntityArrow && ((EntityArrow)var1).owner != null) {
					var3 = ((EntityArrow)var1).owner;
				}

				if(var3 instanceof EntityLiving) {
					this.alertWolves((EntityLiving)var3, false);
				}

				this.addStat(StatList.damageTakenStat, var2);
				return super.attackEntityFrom(var1, var2);
			}
		}
	}

	protected boolean func_27025_G() {
		return false;
	}

	protected void alertWolves(EntityLiving var1, boolean var2) {
		if(!(var1 instanceof EntityCreeper) && !(var1 instanceof EntityGhast)) {
			if(var1 instanceof EntityWolf) {
				EntityWolf var3 = (EntityWolf)var1;
				if(var3.isWolfTamed() && this.username.equals(var3.getWolfOwner())) {
					return;
				}
			}

			if(!(var1 instanceof EntityPlayer) || this.func_27025_G()) {
				List var7 = this.aH.getEntitiesWithinAABB(EntityWolf.class, AxisAlignedBB.getBoundingBoxFromPool(this.prevPosZ, this.posX, this.posY, this.prevPosZ + 1.0D, this.posX + 1.0D, this.posY + 1.0D).expand(16.0D, 4.0D, 16.0D));
				Iterator var4 = var7.iterator();

				while(true) {
					EntityWolf var6;
					do {
						do {
							do {
								do {
									if(!var4.hasNext()) {
										return;
									}

									Entity var5 = (Entity)var4.next();
									var6 = (EntityWolf)var5;
								} while(!var6.isWolfTamed());
							} while(var6.getTarget() != null);
						} while(!this.username.equals(var6.getWolfOwner()));
					} while(var2 && var6.isWolfSitting());

					var6.setWolfSitting(false);
					var6.setTarget(var1);
				}
			}
		}
	}

	protected void damageEntity(int var1) {
		int var2 = 25 - this.inventory.getTotalArmorValue();
		int var3 = var1 * var2 + this.damageRemainder;
		this.inventory.damageArmor(var1);
		var1 = var3 / 25;
		this.damageRemainder = var3 % 25;
		super.damageEntity(var1);
	}

	public void displayGUIFurnace(TileEntityFurnace var1) {
	}

	public void displayGUIDispenser(TileEntityDispenser var1) {
	}

	public void displayGUIEditSign(TileEntitySign var1) {
	}

	public void useCurrentItemOnEntity(Entity var1) {
		if(!var1.interact(this)) {
			ItemStack var2 = this.getCurrentEquippedItem();
			if(var2 != null && var1 instanceof EntityLiving) {
				var2.useItemOnEntity((EntityLiving)var1);
				if(var2.stackSize <= 0) {
					var2.func_1097_a(this);
					this.destroyCurrentEquippedItem();
				}
			}

		}
	}

	public ItemStack getCurrentEquippedItem() {
		return this.inventory.getCurrentItem();
	}

	public void destroyCurrentEquippedItem() {
		this.inventory.setInventorySlotContents(this.inventory.currentItem, (ItemStack)null);
	}

	public double getYOffset() {
		return (double)(this.be - 0.5F);
	}

	public void swingItem() {
		this.swingProgressInt = -1;
		this.isSwinging = true;
	}

	public void attackTargetEntityWithCurrentItem(Entity var1) {
		int var2 = this.inventory.getDamageVsEntity(var1);
		if(var2 > 0) {
			if(this.motionX < 0.0D) {
				++var2;
			}

			var1.attackEntityFrom(this, var2);
			ItemStack var3 = this.getCurrentEquippedItem();
			if(var3 != null && var1 instanceof EntityLiving) {
				var3.hitEntity((EntityLiving)var1, this);
				if(var3.stackSize <= 0) {
					var3.func_1097_a(this);
					this.destroyCurrentEquippedItem();
				}
			}

			if(var1 instanceof EntityLiving) {
				if(var1.isEntityAlive()) {
					this.alertWolves((EntityLiving)var1, true);
				}

				this.addStat(StatList.damageDealtStat, var2);
			}
		}

	}

	public void respawnPlayer() {
	}

	public abstract void func_6420_o();

	public void onItemStackChanged(ItemStack var1) {
	}

	public void setEntityDead() {
		super.setEntityDead();
		this.inventorySlots.onCraftGuiClosed(this);
		if(this.craftingInventory != null) {
			this.craftingInventory.onCraftGuiClosed(this);
		}

	}

	public boolean isEntityInsideOpaqueBlock() {
		return !this.sleeping && super.isEntityInsideOpaqueBlock();
	}

	public EnumStatus sleepInBedAt(int var1, int var2, int var3) {
		if(!this.isPlayerSleeping() && this.isEntityAlive()) {
			if(this.aH.worldProvider.isNether) {
				return EnumStatus.NOT_POSSIBLE_HERE;
			} else if(this.aH.isDaytime()) {
				return EnumStatus.NOT_POSSIBLE_NOW;
			} else if(Math.abs(this.prevPosZ - (double)var1) <= 3.0D && Math.abs(this.posX - (double)var2) <= 2.0D && Math.abs(this.posY - (double)var3) <= 3.0D) {
				this.setSize(0.2F, 0.2F);
				this.be = 0.2F;
				if(this.aH.blockExists(var1, var2, var3)) {
					int var4 = this.aH.getBlockMetadata(var1, var2, var3);
					int var5 = BlockBed.getDirectionFromMetadata(var4);
					float var6 = 0.5F;
					float var7 = 0.5F;
					switch(var5) {
					case 0:
						var7 = 0.9F;
						break;
					case 1:
						var6 = 0.1F;
						break;
					case 2:
						var7 = 0.1F;
						break;
					case 3:
						var6 = 0.9F;
					}

					this.func_22052_e(var5);
					this.setPosition((double)((float)var1 + var6), (double)((float)var2 + 15.0F / 16.0F), (double)((float)var3 + var7));
				} else {
					this.setPosition((double)((float)var1 + 0.5F), (double)((float)var2 + 15.0F / 16.0F), (double)((float)var3 + 0.5F));
				}

				this.sleeping = true;
				this.b = 0;
				this.a = new ChunkCoordinates(var1, var2, var3);
				this.posZ = this.motionY = this.motionX = 0.0D;
				if(!this.aH.multiplayerWorld) {
					this.aH.updateAllPlayersSleepingFlag();
				}

				return EnumStatus.OK;
			} else {
				return EnumStatus.TOO_FAR_AWAY;
			}
		} else {
			return EnumStatus.OTHER_PROBLEM;
		}
	}

	private void func_22052_e(int var1) {
		this.v = 0.0F;
		this.field_22062_y = 0.0F;
		switch(var1) {
		case 0:
			this.field_22062_y = -1.8F;
			break;
		case 1:
			this.v = 1.8F;
			break;
		case 2:
			this.field_22062_y = 1.8F;
			break;
		case 3:
			this.v = -1.8F;
		}

	}

	public void wakeUpPlayer(boolean var1, boolean var2, boolean var3) {
		this.setSize(0.6F, 1.8F);
		this.resetHeight();
		ChunkCoordinates var4 = this.a;
		ChunkCoordinates var5 = this.a;
		if(var4 != null && this.aH.getBlockId(var4.x, var4.y, var4.z) == Block.blockBed.blockID) {
			BlockBed.setBedOccupied(this.aH, var4.x, var4.y, var4.z, false);
			var5 = BlockBed.getNearestEmptyChunkCoordinates(this.aH, var4.x, var4.y, var4.z, 0);
			if(var5 == null) {
				var5 = new ChunkCoordinates(var4.x, var4.y + 1, var4.z);
			}

			this.setPosition((double)((float)var5.x + 0.5F), (double)((float)var5.y + this.be + 0.1F), (double)((float)var5.z + 0.5F));
		}

		this.sleeping = false;
		if(!this.aH.multiplayerWorld && var2) {
			this.aH.updateAllPlayersSleepingFlag();
		}

		if(var1) {
			this.b = 0;
		} else {
			this.b = 100;
		}

		if(var3) {
			this.setPlayerSpawnCoordinate(this.a);
		}

	}

	private boolean isInBed() {
		return this.aH.getBlockId(this.a.x, this.a.y, this.a.z) == Block.blockBed.blockID;
	}

	public static ChunkCoordinates func_25060_a(World var0, ChunkCoordinates var1) {
		IChunkProvider var2 = var0.getIChunkProvider();
		var2.func_538_d(var1.x - 3 >> 4, var1.z - 3 >> 4);
		var2.func_538_d(var1.x + 3 >> 4, var1.z - 3 >> 4);
		var2.func_538_d(var1.x - 3 >> 4, var1.z + 3 >> 4);
		var2.func_538_d(var1.x + 3 >> 4, var1.z + 3 >> 4);
		if(var0.getBlockId(var1.x, var1.y, var1.z) != Block.blockBed.blockID) {
			return null;
		} else {
			ChunkCoordinates var3 = BlockBed.getNearestEmptyChunkCoordinates(var0, var1.x, var1.y, var1.z, 0);
			return var3;
		}
	}

	public float getBedOrientationInDegrees() {
		if(this.a != null) {
			int var1 = this.aH.getBlockMetadata(this.a.x, this.a.y, this.a.z);
			int var2 = BlockBed.getDirectionFromMetadata(var1);
			switch(var2) {
			case 0:
				return 90.0F;
			case 1:
				return 0.0F;
			case 2:
				return 270.0F;
			case 3:
				return 180.0F;
			}
		}

		return 0.0F;
	}

	public boolean isPlayerSleeping() {
		return this.sleeping;
	}

	public boolean isPlayerFullyAsleep() {
		return this.sleeping && this.b >= 100;
	}

	public int func_22060_M() {
		return this.b;
	}

	public void addChatMessage(String var1) {
	}

	public ChunkCoordinates getPlayerSpawnCoordinate() {
		return this.bL;
	}

	public void setPlayerSpawnCoordinate(ChunkCoordinates var1) {
		if(var1 != null) {
			this.bL = new ChunkCoordinates(var1);
		} else {
			this.bL = null;
		}

	}

	public void triggerAchievement(StatBase var1) {
		this.addStat(var1, 1);
	}

	public void addStat(StatBase var1, int var2) {
	}

	protected void jump() {
		super.jump();
		this.addStat(StatList.jumpStat, 1);
	}

	public void moveEntityWithHeading(float var1, float var2) {
		double var3 = this.prevPosZ;
		double var5 = this.posX;
		double var7 = this.posY;
		super.moveEntityWithHeading(var1, var2);
		this.addMovementStat(this.prevPosZ - var3, this.posX - var5, this.posY - var7);
	}

	private void addMovementStat(double var1, double var3, double var5) {
		if(this.riddenByEntity == null) {
			int var7;
			if(this.isInsideOfMaterial(Material.water)) {
				var7 = Math.round(MathHelper.sqrt_double(var1 * var1 + var3 * var3 + var5 * var5) * 100.0F);
				if(var7 > 0) {
					this.addStat(StatList.distanceDoveStat, var7);
				}
			} else if(this.isInWater()) {
				var7 = Math.round(MathHelper.sqrt_double(var1 * var1 + var5 * var5) * 100.0F);
				if(var7 > 0) {
					this.addStat(StatList.distanceSwumStat, var7);
				}
			} else if(this.isOnLadder()) {
				if(var3 > 0.0D) {
					this.addStat(StatList.distanceClimbedStat, (int)Math.round(var3 * 100.0D));
				}
			} else if(this.aW) {
				var7 = Math.round(MathHelper.sqrt_double(var1 * var1 + var5 * var5) * 100.0F);
				if(var7 > 0) {
					this.addStat(StatList.distanceWalkedStat, var7);
				}
			} else {
				var7 = Math.round(MathHelper.sqrt_double(var1 * var1 + var5 * var5) * 100.0F);
				if(var7 > 25) {
					this.addStat(StatList.distanceFlownStat, var7);
				}
			}

		}
	}

	private void addMountedMovementStat(double var1, double var3, double var5) {
		if(this.riddenByEntity != null) {
			int var7 = Math.round(MathHelper.sqrt_double(var1 * var1 + var3 * var3 + var5 * var5) * 100.0F);
			if(var7 > 0) {
				if(this.riddenByEntity instanceof EntityMinecart) {
					this.addStat(StatList.distanceByMinecartStat, var7);
					if(this.startMinecartRidingCoordinate == null) {
						this.startMinecartRidingCoordinate = new ChunkCoordinates(MathHelper.floor_double(this.prevPosZ), MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY));
					} else if(this.startMinecartRidingCoordinate.getSqDistanceTo(MathHelper.floor_double(this.prevPosZ), MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY)) >= 1000.0D) {
						this.addStat(AchievementList.onARail, 1);
					}
				} else if(this.riddenByEntity instanceof EntityBoat) {
					this.addStat(StatList.distanceByBoatStat, var7);
				} else if(this.riddenByEntity instanceof EntityPig) {
					this.addStat(StatList.distanceByPigStat, var7);
				}
			}
		}

	}

	protected void fall(float var1) {
		if(var1 >= 2.0F) {
			this.addStat(StatList.distanceFallenStat, (int)Math.round((double)var1 * 100.0D));
		}

		super.fall(var1);
	}

	public void onKillEntity(EntityLiving var1) {
		if(var1 instanceof EntityMob) {
			this.triggerAchievement(AchievementList.killEnemy);
		}

	}

	public int getItemIcon(ItemStack var1) {
		int var2 = super.getItemIcon(var1);
		if(var1.itemID == Item.fishingRod.shiftedIndex && this.C != null) {
			var2 = var1.getIconIndex() + 16;
		}

		return var2;
	}

	public void setInPortal() {
		if(this.y > 0) {
			this.y = 10;
		} else {
			this.z = true;
		}
	}
}
