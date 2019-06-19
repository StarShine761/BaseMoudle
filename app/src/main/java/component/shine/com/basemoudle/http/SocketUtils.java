package component.shine.com.basemoudle.http;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import component.shine.com.basemoudle.utils.DataUtils;
import component.shine.com.basemoudle.utils.LogUtils;
import component.shine.com.basemoudle.utils.ThreadPoolManager;

import static java.lang.Thread.sleep;

/**
 * Created by cc
 * On 2019/6/13.
 * Socket工具类 server端
 */
public class SocketUtils {
    private static final String TAG = "SocketUtils";
    private static final int PORT = 1883;//监听的端口号
    private static volatile ConcurrentHashMap<String, Socket> map = new ConcurrentHashMap<>();//线程安全
    private String mapKey = "test";


    private volatile static SocketUtils singleton;

    private SocketUtils() {
    }

    public static SocketUtils init() {
        if (singleton == null) {
            synchronized (SocketUtils.class) {
                if (singleton == null) {
                    singleton = new SocketUtils();
                }
            }
        }
        return singleton;
    }


    public void iniSocket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ServerSocket serverSocket = null;
                try {
                    serverSocket = new ServerSocket(PORT);
                    while (true) {
                        Socket client = serverSocket.accept();
                        map.put(mapKey, client);
                        LogUtils.d(TAG, "socket init success ");

                        //一个客户端连接就开户两个线程处理读写
                        new Thread(new ReadHandlerThread(client)).start();
                        new Thread(new WriteHandlerThread(client)).start();
                    }
                } catch (Exception e) {
                    LogUtils.d(TAG, "socket init false  " + e.getMessage());

                    e.printStackTrace();
                } finally {
                    try {
                        if (serverSocket != null) {
                            serverSocket.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

    }

    /*
     *处理读操作的线程
     */
    class ReadHandlerThread implements Runnable {
        private Socket client;

        public ReadHandlerThread(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            DataInputStream dis = null;
            try {
                while (true) {
                    dis = new DataInputStream(client.getInputStream());
                    byte[] readData = new byte[50];
                    int size = dis.read(readData);
                    if (size > 0) {
                        String readString = DataUtils.bytesToHexString(readData);
                        LogUtils.d(TAG, "socket recive msg ===" + readString);


                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (dis != null) {
                        dis.close();
                    }
                    if (client != null) {
                        client = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
     * 处理写操作的线程
     */
    class WriteHandlerThread implements Runnable {

        private Socket client;

        public WriteHandlerThread(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            DataOutputStream dos = null;
            BufferedReader br = null;
            try {
                while (true) {
                    dos = new DataOutputStream(client.getOutputStream());
                    byte[] midbytes3 = DataUtils.hexStringToBytes("test");
                    //向客户端回复信息
                    dos.write(midbytes3);


                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (dos != null) {
                        dos.close();
                    }
                    if (br != null) {
                        br.close();
                    }
                    if (client != null) {
                        client = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public void sendMsg(String msg) {
        LogUtils.d(TAG, "socket send msg ===" + msg);

        ThreadPoolManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                Socket test = map.get(mapKey);
                if (test == null) {
                    LogUtils.d(TAG, "socket is null");
                    return;
                }
                DataOutputStream dos = null;
                try {

                    //向客户端回复信息
                    dos = new DataOutputStream(test.getOutputStream());
                    byte[] midbytes = DataUtils.hexStringToBytes(msg);
                    dos.write(midbytes);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


}
