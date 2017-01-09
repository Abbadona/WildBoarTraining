package com.example.karol.wildboartraining;
import com.example.karol.wildboartraining.ConnectionPackage.ClientConnection;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import static java.security.AccessController.getContext;


public class CreatePlanActivity extends AppCompatActivity implements View.OnClickListener {

    ClientSSLConnection client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("TAG", "tworzysz Plan!!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);
        final Intent intent = new Intent(this,MenuActivity.class);
        if (!ClientConnection.isLogged()){
            AlertDialog alertDialog = new AlertDialog.Builder(CreatePlanActivity.this).create();
            alertDialog.setTitle("Połączenie");
            alertDialog.setMessage("Nie jesteś zalogowany z serwerem!");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Połącz",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which){
                    startActivity(intent);//Usunąć !
                    ClientConnection client = new ClientConnection();
                    boolean isConnected = client.runConnection();
                    if (isConnected){
                        Toast.makeText(CreatePlanActivity.this, "Połączono", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(CreatePlanActivity.this, "Brak połączenia",Toast.LENGTH_LONG).show();
                    }
                }
            });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Anuluj",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which){
                    startActivity(intent);
                }
            });
            alertDialog.show();
        }
    }

    @Override
    public void onClick(View v) {
        }
    }

