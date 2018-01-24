package com.yikang.heartmark.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

/**
 * ---方法列表与备注---
 *  start()
 *  connect() 
 *  connected() 
 *  stop() 
 *  write(byte[])
 *  connectionFailed() 
 *  connectionLost()
 *  AcceptThread
 *  ConnectThread
 *  ConnectedThread
 */

public class Bluetooth {
	// 创建服务器套接字时的SDP记录名称
	private final String NAME = "BluetoothChat";
	// 唯一的UUID此应用程序
	private final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private BluetoothAdapter mAdapter;
	private Handler mHandler;
	private AcceptThread mAcceptThread;
	private ConnectThread mConnectThread;
	private ConnectedThread mConnectedThread;
	private int mState;
	//用于指示当前连接状态
	public static final int STATE_NONE = 0; // 空闲状态
	public static final int STATE_LISTEN = 1; // 现在侦听传入连接
	public static final int STATE_CONNECTING = 2; // 现在发起一个传出连接
	public static final int STATE_CONNECTED = 3; // 现在连接到远程设备
	
	public static final int connect_fail = 22;
	
	public static final int change = 11;  //
	public static final int anima = 12;  //机内的时候停止界面开始动画，等待结果
	public static final int write = 13; // 将消息传给设备以后
	public static final int rede = 14; //接收到设备的消息后
	private final String TAG = "Bluetooth";
	public String MEASSURE_RESULT = "";

	public Bluetooth(Context context, Handler handler) {
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		mHandler = handler;
	}

	public synchronized int getState() {
		return mState;
	}
	//参数是数据，不是what
	private synchronized void setState(int state) {
		mState = state;
		mHandler.obtainMessage(change, state, -1).sendToTarget();
	}
	
	public synchronized void start() {

		// 取消试图连接设备的线程
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		// 取消正在连接的线程
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// 启动监听BluetoothServerSocket的线程
		if (mAcceptThread == null) {
			mAcceptThread = new AcceptThread();
			mAcceptThread.start();
		}
		setState(STATE_LISTEN);//现在侦听传入连接
	}
//该方法同一时间只能被一个线程访问 防止并发
	public synchronized void connect(BluetoothDevice device) {

		// 取消试图连接设备的线程
		if (mState == STATE_CONNECTING) {
			if (mConnectThread != null) {
				mConnectThread.cancel();
				mConnectThread = null;
			}
		}

		// 取消正在连接的线程
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// 启动监听设备读写的线程
		mConnectThread = new ConnectThread(device);
		mConnectThread.start();
		setState(STATE_CONNECTING);
	}

	public synchronized void connected(BluetoothSocket socket,
			BluetoothDevice device) {

		// 取消试图连接设备的线程
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		// 取消正在连接的线程
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// 取消接受线程（因为我们只想连接到一个）
		if (mAcceptThread != null) {
			mAcceptThread.cancel();
			mAcceptThread = null;
		}

		// 启动线程来管理连接，进行传输
		mConnectedThread = new ConnectedThread(socket);
		mConnectedThread.start();

		// Send the name of the connected device back to the UI
		// Activity连接的设备名发送到界面
		// Message msg =
		// mHandler.obtainMessage(BluetoothChat.MESSAGE_DEVICE_NAME);
		// Bundle bundle = new Bundle();
		// bundle.putString(BluetoothChat.DEVICE_NAME, device.getName());
		// msg.setData(bundle);
		// mHandler.sendMessage(msg);
		//
	    setState(STATE_CONNECTED);
	}

	// 停掉所有的线程
	public synchronized void stop() {
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		if (mAcceptThread != null) {
			mAcceptThread.cancel();
			mAcceptThread = null;
		}
		setState(STATE_NONE);
	}

	// 给连接到的设备传送数据，十六进制
	public void write(byte[] out) {
		ConnectedThread connectedThread;
		// Synchronize a copy of the ConnectedThread
		synchronized (this) {
			if (mState != STATE_CONNECTED)
				return;
			connectedThread = mConnectedThread;
		}
		// 执行写不同步（操作流会有耗时，异步进行）
		connectedThread.write(out);
	}

	// 链接失败方法，通知界面更新ui
	private void connectionFailed() {
		   setState(STATE_LISTEN);

		// Message msg = mHandler.obtainMessage(BluetoothChat.MESSAGE_TOAST);
		// Bundle bundle = new Bundle();
		// bundle.putString(BluetoothChat.TOAST, "无法连接设备");
		// msg.setData(bundle);
		// mHandler.sendMessage(msg);
	}

	// 链接丢失（sokete有可能会因为某种愿意导致链接断开）
	private void connectionLost() {
		   setState(STATE_LISTEN);

		// Message msg = mHandler.obtainMessage(BluetoothChat.MESSAGE_TOAST);
		// Bundle bundle = new Bundle();
		// bundle.putString(BluetoothChat.TOAST, "设备连接丢失");
		// msg.setData(bundle);
		// mHandler.sendMessage(msg);
	}

	// 数据传输线程
	private class AcceptThread extends Thread {
		// 手机跟设备通讯的socket
		private final BluetoothServerSocket mmServerSocket;

		public AcceptThread() {
			BluetoothServerSocket tmp = null;
			try {
				tmp = mAdapter
						.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
			} catch (IOException e) {
				Log.e("", "listen() failed", e);
			}
			mmServerSocket = tmp;
		}

		public void run() {
			setName("AcceptThread");
			BluetoothSocket socket = null;

			// Listen to the server socket if we're not connected
			while (mState != STATE_CONNECTED) {
				try {
					// This is a blocking call and will only return on a
					// successful connection or an exception
					if (mmServerSocket != null)
						socket = mmServerSocket.accept();
				} catch (IOException e) {
					break;
				}

				// 如果连接被接收
				if (socket != null) {
					synchronized (Bluetooth.this) {
						switch (mState) {
						case STATE_LISTEN:
						case STATE_CONNECTING:
							// 正常情况开始监听接收
							connected(socket, socket.getRemoteDevice());
							break;
						case STATE_NONE:
						case STATE_CONNECTED:
							// 没有连接成功或正在连接
							try {
								socket.close();
							} catch (IOException e) {
								Log.e("", "Could not close unwanted socket", e);
							}
							break;
						}
					}
				}
			}
		}

		public void cancel() {
			if (mmServerSocket != null)
				try {
					mmServerSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	// 连接线程
	private class ConnectThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;

		public ConnectThread(BluetoothDevice device) {
			mmDevice = device;
			BluetoothSocket tmp = null;

			// 从设备得到一个连接
			try {
				tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
			} catch (IOException e) {
				Log.e(TAG, "create() failed", e);
			}
			mmSocket = tmp;
		}

		public void run() {
			setName("ConnectThread");

			// 取消链接
			//mAdapter.cancelDiscovery();

			try {
				mmSocket.connect();
			} catch (IOException e) {
				mHandler.sendEmptyMessage(connect_fail);
				connectionFailed();
				try {
					mmSocket.close();
				} catch (IOException e2) {

				}
				Bluetooth.this.start();
				return;
			}

			// 复位
			synchronized (Bluetooth.this) {
				mConnectThread = null;
			}
			connected(mmSocket, mmDevice);
		}

		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 这个线程运行与远程设备的连接,处理所有传出跟传入的数据
	private class ConnectedThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;

		public ConnectedThread(BluetoothSocket socket) {
			Log.d(TAG, "create ConnectedThread");
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
				Log.e(TAG, "temp sockets not created", e);
			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}

		public void run() {
			byte[] buffer = new byte[1024];
			int bytes;
			// 不断接收数据
			while (true) {
				try {
					// Read from the InputStream
					// bytes = mmInStream.read(buffer);
					if ((bytes = mmInStream.read(buffer)) > 0) {
						byte[] buf_data = new byte[bytes];
						for (int i = 0; i < bytes; i++) {
							buf_data[i] = buffer[i];
						}
						String s = new String(buf_data);
						if (s.indexOf("READY") != -1 || s.indexOf("QROK") != -1) {
							mHandler.obtainMessage(rede,
									bytes, -1, s).sendToTarget();
						} else {
							MEASSURE_RESULT = MEASSURE_RESULT
									+ s;
							if (MEASSURE_RESULT.indexOf("#") != -1) {
								mHandler.obtainMessage(
										rede, bytes, -1,
										MEASSURE_RESULT)
										.sendToTarget();
								MEASSURE_RESULT = "";
							} 
//							else if (MEASSURE_RESULT
//									.indexOf("s") != -1) {
//								mHandler.obtainMessage(
//										rede,
//										bytes, -1, s).sendToTarget();
//								MEASSURE_RESULT = "";
//							}
						}
					}
					// Send the obtained bytes to the UI Activity
				} catch (IOException e) {
					connectionLost();
					break;
				}
			}
		}

		// 给设备传数据
		public void write(byte[] buffer) {
			try {
				mmOutStream.write(buffer);
				// Share the sent message back to the UI Activity
				mHandler.obtainMessage(write, -1, -1,buffer).sendToTarget();
			} catch (IOException e) {
				Log.e(TAG, "Exception during write", e);
			}
		}

		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	   // 取消配对
		@SuppressWarnings({ "rawtypes", "unchecked" })
		
		static public boolean cancelBondProcess(Class btClass,BluetoothDevice device)
		    throws Exception
			    {
			        Method removeBondMethod = btClass.getMethod("removeBond");
			        Boolean returnValue = (Boolean) removeBondMethod.invoke(device);
				    return returnValue.booleanValue();
			    }

}
