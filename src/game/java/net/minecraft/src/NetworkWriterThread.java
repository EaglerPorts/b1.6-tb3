package net.minecraft.src;

import java.io.IOException;

class NetworkWriterThread extends Thread {
	final NetworkManager netManager;

	NetworkWriterThread(NetworkManager var1, String var2) {
		super(var2);
		this.netManager = var1;
	}

	public void run() {
		Object var1 = NetworkManager.threadSyncObject;
		synchronized(var1) {
			++NetworkManager.numWriteThreads;
		}

		while(true) {
			boolean var12 = false;

			try {
				var12 = true;
				if(!NetworkManager.isRunning(this.netManager)) {
					var12 = false;
					break;
				}

				NetworkManager.d(this.netManager);
				if(NetworkManager.e(this.netManager)) {
					NetworkManager.a(this.netManager, false);

					try {
						NetworkManager.f(this.netManager).flush();
					} catch (IOException var15) {
						var15.printStackTrace();
					}
				}
			} finally {
				if(var12) {
					Object var5 = NetworkManager.threadSyncObject;
					synchronized(var5) {
						--NetworkManager.numWriteThreads;
					}
				}
			}
		}

		var1 = NetworkManager.threadSyncObject;
		synchronized(var1) {
			--NetworkManager.numWriteThreads;
		}
	}
}
