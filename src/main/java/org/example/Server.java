package org.example;

import lombok.extern.log4j.Log4j2;
import org.example.Utils.DataBaseManager;
import org.example.Utils.ServerHelper;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.time.LocalDate;

@Log4j2
public class Server implements Runnable {
    public static LocalDate dateCollection;
    private static int port = 50000;
    private DatagramChannel datagramChannel;
    private SocketAddress socketAddress;
    private DataBaseManager dataBaseManager;

    public Server(DataBaseManager dataBaseManager) {
        this.dataBaseManager = dataBaseManager;
    }

    public static void main(String[] args) {
        System.out.println("Сервер был запущен.");

        try {
            dateCollection = LocalDate.now();
            Server server = new Server(new DataBaseManager());
            new Thread(server).start();

        } catch (IllegalStateException e) {
            System.exit(-1);
        }
    }

    //В методе receive осуществялется вся обработка входящих команд
    private void receive() throws IOException, TransformerException, ParserConfigurationException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(65536);
        byteBuffer.clear();
        socketAddress = datagramChannel.receive(byteBuffer);
        byteBuffer.flip();
        DatagramChannel d = datagramChannel;

        if (socketAddress != null && !new String(byteBuffer.array()).trim().isEmpty()) {
            ServerHelper serverHelper = new ServerHelper(d, socketAddress, dataBaseManager, byteBuffer);
            serverHelper.run();
        }
    }

    @Override
    public void run() {
        try {
            socketAddress = new InetSocketAddress(port);
            datagramChannel = DatagramChannel.open();
            datagramChannel.bind(new InetSocketAddress(port));
            datagramChannel.configureBlocking(false);
            System.out.println("Порт Сервера " + port);
            while (true) {
                try {
                    receive();
                } catch (TransformerException | ParserConfigurationException e) {
                    e.printStackTrace();
                }
            }
        } catch (SocketException e) {
            port++;
            run();
        } catch (ClosedChannelException e) {
            System.err.println("Что не так с каналом");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

