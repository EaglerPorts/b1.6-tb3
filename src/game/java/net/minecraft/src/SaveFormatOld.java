package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.internal.vfs2.VFile2;

public class SaveFormatOld implements ISaveFormat {
	protected final VFile2 field_22180_a;

	public SaveFormatOld(VFile2 var1) {
		this.field_22180_a = var1;
	}

	public String func_22178_a() {
		return "Old Format";
	}

	public List func_22176_b() {
		ArrayList var1 = new ArrayList();

		for(int var2 = 0; var2 < 5; ++var2) {
			String var3 = "World" + (var2 + 1);
			WorldInfo var4 = this.func_22173_b(var3);
			if(var4 != null) {
				var1.add(new SaveFormatComparator(var3, "", var4.getLastTimePlayed(), var4.getSizeOnDisk(), false));
			}
		}

		return var1;
	}

	public void flushCache() {
	}

	public WorldInfo func_22173_b(String var1) {
		VFile2 var2 = new VFile2(this.field_22180_a, var1);
		VFile2 var3 = new VFile2(var2, "level.dat");
		if(!var3.exists()) {
			return null;
		} else {
			NBTTagCompound var4;
			NBTTagCompound var5;
			if(var3.exists()) {
				try {
					var4 = CompressedStreamTools.func_1138_a(var3.getInputStream());
					var5 = var4.getCompoundTag("Data");
					return new WorldInfo(var5);
				} catch (Exception var7) {
					var7.printStackTrace();
				}
			}

			var3 = new VFile2(var2, "level.dat_old");
			if(var3.exists()) {
				try {
					var4 = CompressedStreamTools.func_1138_a(var3.getInputStream());
					var5 = var4.getCompoundTag("Data");
					return new WorldInfo(var5);
				} catch (Exception var6) {
					var6.printStackTrace();
				}
			}

			return null;
		}
	}

	public void func_22170_a(String var1, String var2) {
		VFile2 var3 = new VFile2(this.field_22180_a, var1, "level.dat");
		if(var3.exists()) {
			try {
				NBTTagCompound var5 = CompressedStreamTools.func_1138_a(var3.getInputStream());
				NBTTagCompound var6 = var5.getCompoundTag("Data");
				var6.setString("LevelName", var2);
				CompressedStreamTools.writeGzippedCompoundToOutputStream(var5, var3.getOutputStream());
			} catch (Exception var7) {
				var7.printStackTrace();
			}

		}
	}

	public void func_22172_c(String var1) {
		VFile2 var2 = new VFile2(this.field_22180_a, var1);
		VFile2 var3 = new VFile2(var2, "level.dat");
		if(var3.exists()) {
			func_22179_a(var2.listFiles());
			var2.delete();
		}
	}

	protected static void func_22179_a(VFile2[] var0) {
		for(int var1 = 0; var1 < var0.length; ++var1) {
			var0[var1].delete();
		}

	}

	public ISaveHandler getSaveLoader(String var1, boolean var2) {
		return new SaveHandler(this.field_22180_a, var1, var2);
	}

	public boolean isOldMapFormat(String var1) {
		return false;
	}

	public boolean convertMapFormat(String var1, IProgressUpdate var2) {
		return false;
	}
}
