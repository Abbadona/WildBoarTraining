package com.example.karol.wildboartraining;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.security.cert.Certificate;

import org.apache.http.conn.ssl.SSLSocketFactory;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ClientSSLConnection {

    private EditText mText;
    private Button mSend;
    private TextView mResponse;
    private EditText mIPaddress;
    private EditText mPort;

    // port to use
    private String ip_address;
    private int port;
    private SSLSocket socket = null;
    private BufferedWriter out = null;
    private BufferedReader in = null;
    private final String TAG;
    private char keystorepass[];
    private char keypassword[];

    public ClientSSLConnection() {
        port = 9998;
        TAG = "TAG";
        keystorepass = "key12345".toCharArray();
        keypassword = "12345key".toCharArray();
        ip_address = "89.70.222.60";
    }

    public void runConnection(View v) {
        Log.i(TAG, "makes it to here");
        try {
            KeyStore ks = KeyStore.getInstance("BKS");
            InputStream keyin = v.getResources().openRawResource(R.raw.testkey);
            ks.load(keyin, keystorepass);
            SSLSocketFactory socketFactory = new SSLSocketFactory(ks);
            socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            socket = (SSLSocket) socketFactory.createSocket(new Socket(ip_address, port), ip_address, port, false);
            socket.startHandshake();

            printServerCertificate(socket);
            printSocketInfo(socket);

            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String temp = "absa";
            chat(temp);
        } catch (UnknownHostException e) {
            Toast.makeText(v.getContext(), "Unknown host", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Unknown host");
        } catch (IOException e) {
            Toast.makeText(v.getContext(), "No I/O", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "No I/O");
            e.printStackTrace();
        } catch (KeyStoreException e) {
            Toast.makeText(v.getContext(), "Keystore ks error", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Keystore ks error");
            //System.exit(-1);
        } catch (NoSuchAlgorithmException e) {
            Toast.makeText(v.getContext(), "No such algorithm for ks.load", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "No such algorithm for ks.load");
            e.printStackTrace();
            //System.exit(-1);
        } catch (CertificateException e) {
            Toast.makeText(v.getContext(), "certificate missing", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "certificate missing");
            e.printStackTrace();
            //System.exit(-1);
        } catch (UnrecoverableKeyException e) {
            Toast.makeText(v.getContext(), "UnrecoverableKeyException", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "unrecoverableKeyException");
            e.printStackTrace();
            //System.exit(-1);
        } catch (KeyManagementException e) {
            Toast.makeText(v.getContext(), "KeyManagementException", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "key management exception");
            e.printStackTrace();
        }
    }

    private void printServerCertificate(SSLSocket socket) {
        try {
            Certificate[] serverCerts =
                    socket.getSession().getPeerCertificates();
            for (int i = 0; i < serverCerts.length; i++) {
                Certificate myCert = serverCerts[i];
                Log.i(TAG, "====Certificate:" + (i + 1) + "====");
                Log.i(TAG, "-Public Key-\n" + myCert.getPublicKey());
                Log.i(TAG, "-Certificate Type-\n " + myCert.getType());

                System.out.println();
            }
        } catch (SSLPeerUnverifiedException e) {
            Log.i(TAG, "Could not verify peer");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void printSocketInfo(SSLSocket s) {
        Log.i(TAG, "Socket class: " + s.getClass());
        Log.i(TAG, "   Remote address = "
                + s.getInetAddress().toString());
        Log.i(TAG, "   Remote port = " + s.getPort());
        Log.i(TAG, "   Local socket address = "
                + s.getLocalSocketAddress().toString());
        Log.i(TAG, "   Local address = "
                + s.getLocalAddress().toString());
        Log.i(TAG, "   Local port = " + s.getLocalPort());
        Log.i(TAG, "   Need client authentication = "
                + s.getNeedClientAuth());
        SSLSession ss = s.getSession();
        Log.i(TAG, "   Cipher suite = " + ss.getCipherSuite());
        Log.i(TAG, "   Protocol = " + ss.getProtocol());
    }

    private void chat(String temp) {
        String message = temp;
        String line = "";
        // send id of the device to match with the image
        try {
            out.write(message + "\n");
            out.flush();
        } catch (IOException e2) {
            Log.i(TAG, "Read failed");
            System.exit(1);
        }
        // receive a ready command from the server
        try {
            line = in.readLine();
            mResponse.setText("SERVER SAID: " + line);
            //Log.i(TAG,line);
        } catch (IOException e1) {
            Log.i(TAG, "Read failed");
            System.exit(1);
        }
    }
}