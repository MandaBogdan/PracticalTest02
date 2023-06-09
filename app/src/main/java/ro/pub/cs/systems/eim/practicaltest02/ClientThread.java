package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread{

    String serverAddress;
    int serverPort;
    Socket socket;
    String operation;
    String op1;
    String op2;
    TextView resultTextView;

    public ClientThread(String serverAddress, int serverPort, String operation, String op1, String op2, TextView resultTextView) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.operation = operation;
        this.op1 = op1;
        this.op2 = op2;
        this.resultTextView = resultTextView;
    }


    @Override
    public void run() {
        try {
            // tries to establish a socket connection to the server
            socket = new Socket(serverAddress, serverPort);

            // gets the reader and writer for the socket
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            printWriter.println(operation + "," + op1 + "," + op2);

            String response;

            // print output
            while ((response = bufferedReader.readLine()) != null) {
                final String finalizedResponse = response;

                System.out.println(response);

                resultTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        resultTextView.setText(finalizedResponse);
                    }
                });
            }
        } // if an exception occurs, it is logged
        catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());

        } finally {
            if (socket != null) {
                try {
                    // closes the socket regardless of errors or not
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                }
            }
        }
    }

}
