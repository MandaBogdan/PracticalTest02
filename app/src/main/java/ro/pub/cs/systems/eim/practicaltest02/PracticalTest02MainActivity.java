package ro.pub.cs.systems.eim.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02MainActivity extends AppCompatActivity {

    EditText serverPortEditText = null;
    Button serverConnectButton = null;


    EditText clientAddressEditText = null;
    EditText clientPortEditText = null;
    Button clientSendRequestButton = null;

    ServerThread serverThread = null;

    TextView resultTextView = null;

    EditText op1EditText = null;
    EditText op2EditText = null;

    Spinner operationSpinner = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        serverPortEditText = (EditText) findViewById(R.id.server_port_edit_text);
        serverConnectButton = (Button) findViewById(R.id.server_connect_button);

        clientAddressEditText = (EditText) findViewById(R.id.client_address_edit_text);
        clientPortEditText = (EditText) findViewById(R.id.client_port_edit_text);

        clientSendRequestButton = (Button) findViewById(R.id.client_send_request_button);

        resultTextView = findViewById(R.id.result_text_view);

        op1EditText = findViewById(R.id.op1_edit_text);
        op2EditText = findViewById(R.id.op2_edit_text);
        operationSpinner = findViewById(R.id.operation);

        serverConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serverPort = serverPortEditText.getText().toString();
                if (serverPort.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();

                    return;
                }

                serverThread = new ServerThread(Integer.parseInt(serverPort));
                if (serverThread.serverSocket == null) {
                    Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Could not create server thread!", Toast.LENGTH_SHORT).show();

                    return;
                }

                serverThread.start();
            }
        });

        clientSendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serverAddress = clientAddressEditText.getText().toString();
                String serverPort = clientPortEditText.getText().toString();

                String operation = operationSpinner.getSelectedItem().toString();
                String op1 = op1EditText.getText().toString();
                String op2 = op2EditText.getText().toString();

                if (op1.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] OP1 should be filled!", Toast.LENGTH_SHORT).show();

                    return;
                }

                if (serverAddress.isEmpty() || serverPort.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();

                    return;
                }

                if (serverThread == null || !serverThread.isAlive()) {
                    Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();

                    return;
                }

                // get input data

                ClientThread clientThread = new ClientThread(
                        serverAddress,
                        Integer.parseInt(serverPort),
                        operation,
                        op1,
                        op2,
                        resultTextView);

                clientThread.start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (serverThread != null) {
            serverThread.stopThread();
        }
    }
}