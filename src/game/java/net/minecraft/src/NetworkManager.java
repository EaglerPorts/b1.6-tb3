package net.minecraft.src;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NetworkManager {
	public static final Object threadSyncObject = new Object();
	public static int numReadThreads;
	public static int numWriteThreads;
	private Object sendQueueLock = new Object();
	private Socket networkSocket;
	private final SocketAddress remoteSocketAddress;
	private DataInputStream socketInputStream;
	private DataOutputStream socketOutputStream;
	private boolean isRunning = true;
	private List readPackets = Collections.synchronizedList(new ArrayList());
	private List dataPackets = Collections.synchronizedList(new ArrayList());
	private List chunkDataPackets = Collections.synchronizedList(new ArrayList());
	private NetHandler netHandler;
	private boolean isServerTerminating = false;
	private Thread writeThread;
	private Thread readThread;
	private boolean isTerminating = false;
	private String terminationReason = "";
	private Object[] field_20101_t;
	private int timeSinceLastRead = 0;
	private int sendQueueByteLength = 0;
	private transient boolean y = false;
	public static int[] field_28145_d = new int[256];
	public static int[] field_28144_e = new int[256];
	public int chunkDataSendCounter = 0;
	private int z = 50;

	public NetworkManager(Socket var1, String var2, NetHandler var3) throws IOException {
		this.networkSocket = var1;
		this.remoteSocketAddress = var1.getRemoteSocketAddress();
		this.netHandler = var3;

		try {
			var1.setSoTimeout(30000);
			var1.setTrafficClass(24);
		} catch (SocketException var5) {
			System.err.println(var5.getMessage());
		}

		this.socketInputStream = new DataInputStream(new BufferedInputStream(var1.getInputStream()));
		this.socketOutputStream = new DataOutputStream(new BufferedOutputStream(var1.getOutputStream()));
		this.readThread = new NetworkReaderThread(this, var2 + " read thread");
		this.writeThread = new NetworkWriterThread(this, var2 + " write thread");
		this.readThread.start();
		this.writeThread.start();
	}

	public void addToSendQueue(Packet var1) {
		if(!this.isServerTerminating) {
			Object var2 = this.sendQueueLock;
			synchronized(var2) {
				this.sendQueueByteLength += var1.getPacketSize() + 1;
				if(var1.isChunkDataPacket) {
					this.chunkDataPackets.add(var1);
				} else {
					this.dataPackets.add(var1);
				}

			}
		}
	}

	private void c() {
		try {
			int[] var10000;
			Packet var1;
			int var10001;
			Object var2;
			if(!this.dataPackets.isEmpty() && (this.chunkDataSendCounter == 0 || System.currentTimeMillis() - ((Packet)this.dataPackets.get(0)).creationTimeMillis >= (long)this.chunkDataSendCounter)) {
				var2 = this.sendQueueLock;
				synchronized(var2) {
					var1 = (Packet)this.dataPackets.remove(0);
					this.sendQueueByteLength -= var1.getPacketSize() + 1;
				}

				Packet.writePacket(var1, this.socketOutputStream);
				var10000 = field_28144_e;
				var10001 = var1.getPacketId();
				var10000[var10001] += var1.getPacketSize();
			}

			if(this.z-- <= 0 && !this.chunkDataPackets.isEmpty() && (this.chunkDataSendCounter == 0 || System.currentTimeMillis() - ((Packet)this.chunkDataPackets.get(0)).creationTimeMillis >= (long)this.chunkDataSendCounter)) {
				var2 = this.sendQueueLock;
				synchronized(var2) {
					var1 = (Packet)this.chunkDataPackets.remove(0);
					this.sendQueueByteLength -= var1.getPacketSize() + 1;
				}

				Packet.writePacket(var1, this.socketOutputStream);
				var10000 = field_28144_e;
				var10001 = var1.getPacketId();
				var10000[var10001] += var1.getPacketSize();
				this.z = 50;
			}
		} catch (Exception var7) {
			if(!this.isTerminating) {
				this.onNetworkError(var7);
			}
		}

	}

	public void wakeThreads() {
		this.y = true;
	}

	private void d() {
		try {
			Packet var1 = Packet.readPacket(this.socketInputStream, this.netHandler.isServerHandler());
			if(var1 != null) {
				int[] var10000 = field_28145_d;
				int var10001 = var1.getPacketId();
				var10000[var10001] += var1.getPacketSize();
				this.readPackets.add(var1);
			} else {
				this.networkShutdown("disconnect.endOfStream", new Object[0]);
			}
		} catch (Exception var2) {
			if(!this.isTerminating) {
				this.onNetworkError(var2);
			}
		}

	}

	private void onNetworkError(Exception var1) {
		var1.printStackTrace();
		this.networkShutdown("disconnect.genericReason", new Object[]{"Internal exception: " + var1.toString()});
	}

	public void networkShutdown(String var1, Object... var2) {
		if(this.isRunning) {
			this.isTerminating = true;
			this.terminationReason = var1;
			this.field_20101_t = var2;
			(new NetworkMasterThread(this)).start();
			this.isRunning = false;

			try {
				this.socketInputStream.close();
				this.socketInputStream = null;
			} catch (Throwable var6) {
			}

			try {
				this.socketOutputStream.close();
				this.socketOutputStream = null;
			} catch (Throwable var5) {
			}

			try {
				this.networkSocket.close();
				this.networkSocket = null;
			} catch (Throwable var4) {
			}

		}
	}

	public void processReadPackets() {
		if(this.sendQueueByteLength > 1048576) {
			this.networkShutdown("disconnect.overflow", new Object[0]);
		}

		if(this.readPackets.isEmpty()) {
			if(this.timeSinceLastRead++ == 1200) {
				this.networkShutdown("disconnect.timeout", new Object[0]);
			}
		} else {
			this.timeSinceLastRead = 0;
		}

		int var1 = 100;

		while(!this.readPackets.isEmpty() && var1-- >= 0) {
			Packet var2 = (Packet)this.readPackets.remove(0);
			var2.processPacket(this.netHandler);
		}

		if(this.isTerminating && this.readPackets.isEmpty()) {
			this.netHandler.handleErrorMessage(this.terminationReason, this.field_20101_t);
		}

	}

	static boolean isRunning(NetworkManager var0) {
		return var0.isRunning;
	}

	static boolean isServerTerminating(NetworkManager var0) {
		return var0.isServerTerminating;
	}

	static void c(NetworkManager var0) {
		var0.d();
	}

	static void d(NetworkManager var0) {
		var0.c();
	}

	static boolean e(NetworkManager var0) {
		return var0.y;
	}

	static boolean a(NetworkManager var0, boolean var1) {
		return var0.y = var1;
	}

	static DataOutputStream f(NetworkManager var0) {
		return var0.socketOutputStream;
	}

	static Thread getReadThread(NetworkManager var0) {
		return var0.readThread;
	}

	static Thread getWriteThread(NetworkManager var0) {
		return var0.writeThread;
	}
}
