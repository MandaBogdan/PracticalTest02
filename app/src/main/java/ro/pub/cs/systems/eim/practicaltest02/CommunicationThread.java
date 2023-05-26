package ro.pub.cs.systems.eim.practicaltest02;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Time;

public class CommunicationThread extends Thread{
    ServerThread serverThread;
    Socket socket;

    BufferedReader bufferedReader;
    PrintWriter printWriter;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            bufferedReader = Utilities.getReader(socket);
            printWriter = Utilities.getWriter(socket);

            // read input
            String line = bufferedReader.readLine();

            String operation = line.split(",")[0];
            String key = line.split(",")[1];
            String value = line.split(",")[2];


            if (operation.equals("add")) {
                int number1 = Integer.parseInt(key);
                int number2 = Integer.parseInt(value);
                long sum = number1 + number2;

                if (sum > Integer.MAX_VALUE) {
                    printWriter.println("overflow");
                } else {
                    String res = Long.toString(sum);
                    printWriter.println(res);
                }
                return;
            }

            if (operation.equals("mul")) {
                int number1 = Integer.parseInt(key);
                int number2 = Integer.parseInt(value);
                long mul = number1 * number2;
                Thread.sleep(6000);

                if (mul > Integer.MAX_VALUE) {
                    printWriter.println("overflow");
                } else {
                    String res = Long.toString(mul);
                    printWriter.println(res);
                }
                return;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
