package com.ibititec.lffa.admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.appodeal.ads.Appodeal;
import com.ibititec.lffa.MainActivity;
import com.ibititec.lffa.R;
import com.ibititec.lffa.helpers.HttpHelper;
import com.ibititec.lffa.helpers.JsonHelper;
import com.ibititec.lffa.modelo.Usuario;

import java.io.IOException;

public class LoginUsuarioActivity extends AppCompatActivity {

    private Button btnLogin, btnSemCadastroLogin, btnCadastrarUsuario;
    private EditText txtEmail, txtSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_login_usuario);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            findViewByIdComponente();
            executarAcoes();
            validarUsuarioCadastrado();
            iniciarAppodeal();
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro: onCreate LoginUsuario: " + ex.getMessage());
        }
    }

    private void iniciarAppodeal() {
        try {
            Appodeal.show(this, Appodeal.BANNER);
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro: iniciarAppodeal: " + ex.getMessage());
        }
    }

    private void validarUsuarioCadastrado() {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginUsuarioActivity.this);
            final String usuarioJson = sharedPreferences.getString("usuario.json", "");

            Usuario usuarioLocal = (Usuario) JsonHelper.getObject(usuarioJson, Usuario.class);

            if (usuarioLocal != null) {
                startarActivity();
            }
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro: validarUsuarioCadastrado LoginUsuario: " + ex.getMessage());
        }
    }


    private void executarAcoes() {
        try {
            btnCadastrarUsuario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityCadastro();
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(txtEmail.getWindowToken(), 0);
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                }
            });

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(txtEmail.getWindowToken(), 0);
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    Usuario usuario = new Usuario();
                    usuario.setLoginEmail(txtEmail.getText().toString());
                    usuario.setSenha(txtSenha.getText().toString());

                    fazerLogin(usuario);
                }
            });

            btnSemCadastroLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startarActivity();
                }
            });
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro: executarAcoes LoginUsuario: " + ex.getMessage());
        }
    }

    private void startActivityCadastro() {
        try {
            Intent intent = new Intent(this, CadastroUsuarioActivity.class);
            startActivity(intent);
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro: startarActivityCadastro LoginUsuario: " + ex.getMessage());
        }
    }

    private void findViewByIdComponente() {
        try {
            btnLogin = (Button) findViewById(R.id.btnLogin_login);
            btnCadastrarUsuario = (Button) findViewById(R.id.btnCadastrar_login);
            btnSemCadastroLogin = (Button) findViewById(R.id.btnSemCadastrro_login);

            txtEmail = (EditText) findViewById(R.id.txtEmailUsuario_login);
            txtSenha = (EditText) findViewById(R.id.txtSenhaUsuario_login);
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro: findViewByIdComponente LoginUsuario: " + ex.getMessage());
        }
    }

    private void fazerLogin(Usuario usuario) {

        try {
            final Usuario usuarioLocal = usuario;
            if (HttpHelper.existeConexao(this)) {
                (new AsyncTask<String, Void, String>() {
                    ProgressDialog progressDialog;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog = ProgressDialog.show(LoginUsuarioActivity.this, "Aguarde", "Atualizando dados");
                    }

                    @Override
                    protected String doInBackground(String... params) {
                        String json = null;

                        try {
                            String url = params[0];
                            if(HttpHelper.existeConexao(LoginUsuarioActivity.this)) {
                                json = HttpHelper.POSTJson(url, JsonHelper.objectToJson(usuarioLocal));
                            }else{
                                json = "";
                            }

                            Log.i(MainActivity.TAG, "Json retornado ao fazer o login: " + json);
                            if (json != null) {
                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginUsuarioActivity.this);
                                sharedPreferences.edit().putString(MainActivity.USUARIO + ".json", json).apply();
                            }

                            Log.i(MainActivity.TAG, json);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e(MainActivity.TAG, String.format(getString(R.string.msg_erro_json), e.getMessage()));
                        }
                        return json;
                    }

                    @Override
                    protected void onPostExecute(String json) {
                        super.onPostExecute(json);

                        progressDialog.dismiss();
                        if (json != null && !json.equals("Erro") && !json.equals("")) {
                            startarActivity();
                        } else {
                            String mensagem = "Usuário ou senha incorretos.";
                            exibirMensagem(mensagem, "Atenção", false);
                        }

                    }
                }).execute(getString(R.string.url_login_usuario));
            } else {

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginUsuarioActivity.this);
                final String usuarioJson = JsonHelper.objectToJson(usuario);
                Log.i(MainActivity.TAG, "Json lido do banco local ao fazer o login: " + usuarioJson);
                usuario = (Usuario) JsonHelper.getObject(sharedPreferences.getString(MainActivity.USUARIO + ".json", ""), Usuario.class);

                if (usuarioLocal != null && usuario != null && usuario.getSenha() == usuarioLocal.getSenha() && usuario.getLoginEmail() == usuarioLocal.getLoginEmail()) {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                } else {
                    String mensagem = "Usuário ou senha incorretos.";
                    exibirMensagem(mensagem, "Atenção", false);
                }
            }
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro ao salvar usuario." + ex.getMessage());
        }
    }

    private void exibirMensagem(String mensagem, String titulo, final boolean returnPrincipal) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //define o titulo
        builder.setTitle(titulo);
        //define a mensagem
        builder.setMessage(mensagem);
        //define um botão como positivo
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                if (returnPrincipal) {
                    onBackPressed();
                } else {
                    return;
                }
                // Toast.makeText(MainActivity.this, "positivo=" + arg1, Toast.LENGTH_SHORT).show();
            }
        });
        //cria o AlertDialog
        AlertDialog alerta = builder.create();
        //Exibe
        alerta.show();
    }


    private void startarActivity() {
        try {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro: startarActivity LoginUsuario: " + ex.getMessage());
        }
    }
}
