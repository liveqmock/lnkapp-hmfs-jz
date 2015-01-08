package org.fbi.hmfsjz.gateway.client;


import org.fbi.hmfsjz.gateway.domain.base.Tia;
import org.fbi.hmfsjz.gateway.domain.base.Toa;
import org.fbi.hmfsjz.helper.ProjectConfigManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * ͬ��Socket�ͻ���
 */
public class SyncSocketClient {
    private static final String SERVER_IP = ProjectConfigManager.getInstance().getProperty("server.ip");
    private static final int SERVER_PORT = Integer.parseInt(ProjectConfigManager.getInstance().getProperty("server.port"));
    private static final String SERVER_TIMEOUT = ProjectConfigManager.getInstance().getProperty("server.timeout");

    // �Զ��� SocketClient
    public Toa onRequest(Tia tia) throws IOException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        Socket socket = new Socket(SERVER_IP, SERVER_PORT);
        socket.setSoTimeout(Integer.parseInt(SERVER_TIMEOUT));
        OutputStream os = socket.getOutputStream();
        byte[] reqBytes = CustomCodeHandler.encode(tia);
        os.write(reqBytes);
        os.flush();
        InputStream is = socket.getInputStream();
        byte[] lengthBytes = new byte[8];
        is.read(lengthBytes);
        int toReadlength = Integer.parseInt(new String(lengthBytes).trim()) - 8;
        byte[] dataBytes = new byte[toReadlength];
     /*   byte[] bytes = new byte[64];
        int index = 0;
        int curlen = 0;
        while ((curlen = is.read(bytes)) == 64) {
            System.arraycopy(bytes, 0, dataBytes, index, bytes.length);
            index += bytes.length;
        }
        if (curlen > 0) {
            System.arraycopy(bytes, 0, dataBytes, index, curlen);
        }*/
        try {
            Thread.currentThread().sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int available = 0;
        int readIndex = 0;

        while (readIndex < toReadlength) {
            int toRead = 0;
            available = is.available();
            if (toReadlength - readIndex >= available) {
                toRead = available;
            } else {
                toRead = toReadlength - readIndex;
            }
            byte[] buf = new byte[toRead];
            is.read(buf);
            System.arraycopy(buf, 0, dataBytes, readIndex, buf.length);
            readIndex += toRead;
        }
        return CustomCodeHandler.decode(dataBytes);
    }

}
