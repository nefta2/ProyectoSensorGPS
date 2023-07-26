package com.example.gps_final;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Enter extends AppCompatActivity {

    Button bttn_enviar;
    EditText name_user;
    boolean isFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_name);

        name_user = findViewById(R.id.name_usr);
        bttn_enviar = findViewById(R.id.bttn_enviar);

        // Desabilitamos el botón
        bttn_enviar.setEnabled(false);

        name_user.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // Cuando se presiona el botón elimina el texto en él
                if (isFirstTime && hasFocus) {
                    name_user.setText("");
                    isFirstTime = false;
                }
            }
        });

        name_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Se habilita el botón si se introduce un nombre.
                bttn_enviar.setEnabled(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        bttn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = name_user.getText().toString().trim();

                // Se inicia la siguiente actividad solo si se ha introducido un nombre,
                if (!userName.isEmpty()) {
                    Intent intent = new Intent(Enter.this, Result.class);
                    intent.putExtra("user_name", userName);
                    startActivity(intent);
                }
            }
        });
    }
}
