package com.ibititec.lffa.admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
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
import com.ibititec.lffa.util.RegistrationIntentService;

import java.io.IOException;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private Button btnCadastro;
    private EditText txtEmail, txtSenha, txtNome, txtConfSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_cadastro_usuario);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            findViewByIdComponente();
            executarAcoes();
            iniciarAppodeal();
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro: onCreate CadastroUsuario: " + ex.getMessage());
        }
    }

    private void iniciarAppodeal() {
        try {
            Appodeal.show(this, Appodeal.BANNER);
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro: iniciarAppodeal: " + ex.getMessage());
        }
    }

//    private void salvarUsuarioServidor(Usuario usuario) {
//
//        try {
//
//            if(retorno == "OK"){
//                //MOSTRAR A MENSAGEM DE CADASTRADO COM SUCESSO
//                String mensagem = "Cadastro realizado.";
//                Snackbar.make(findViewById(R.id.btnCadastrar_cadastro), mensagem, Snackbar.LENGTH_SHORT).show();
//
//                sharedPreferences.edit().putString("usuario.json", jsonUsuario).apply();
//
//            }else{
//                String mensagem = "Cadastro não realizado, tente novamente.";
//                Snackbar.make(findViewById(R.id.btnCadastrar_cadastro), mensagem, Snackbar.LENGTH_SHORT).show();
//
//                Log.i(MainActivity.TAG, "Cadastro não realizado.");
//            }
//        }catch (Exception ex){
//            Log.i(MainActivity.TAG, "Erro ao salvar usuario." + ex.getMessage());
//        }
//    }

    private void executarAcoes() {
        try {
            btnCadastro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(txtEmail.getWindowToken(), 0);
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    Usuario usuario = new Usuario();
                    usuario.setLoginEmail(txtEmail.getText().toString());
                    usuario.setSenha(txtSenha.getText().toString());
                    usuario.setNomeUsuario(txtNome.getText().toString());
                    usuario.setConfirmaSenha(txtConfSenha.getText().toString());
                    usuario.setTipoUsuario("0");
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(CadastroUsuarioActivity.this);
                    String token = sharedPreferences.getString(RegistrationIntentService.GCM_TOKEN, "");
                    usuario.setToken(token);

                    if (!usuario.getSenha().equals(usuario.getConfirmaSenha())) {
                        String mensagem = "Senhas não conferem.";
                        exibirMensagem(mensagem, "Atenção", false);
                    } else if (!usuario.getLoginEmail().contains("@")) {
                        String mensagem = "Digite um e-mail válido.";
                        exibirMensagem(mensagem, "Atenção", false);
                    } else if (usuario.getNomeUsuario().length() <= 4) {
                        String mensagem = "Seu nome deve possuir mais de 4 caracteres.";
                        exibirMensagem(mensagem, "Atenção", false);
                    } else if (usuario.getLoginEmail().length() <= 4) {
                        String mensagem = "Seu e-mail deve possuir mais de 4 caracteres.";
                        exibirMensagem(mensagem, "Atenção", false);

                    } else if (usuario.getSenha().length() <= 4 || usuario.getConfirmaSenha().length() <= 4) {
                        String mensagem = "Sua senha deve possuir mais de 4 caracteres.";
                        exibirMensagem(mensagem, "Atenção", false);
                    } else {
                        if(HttpHelper.existeConexao(CadastroUsuarioActivity.this)) {
                            salvarUsuarioServidor(usuario);
                        }else{
                            String mensagem = "Verifique se sua conexão com a internet está ativa.";
                            exibirMensagem(mensagem, "Atenção", false);
                        }
                    }
                }
            });
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro: executarAcoes CadastroUsuario: " + ex.getMessage());
        }
    }

    private void findViewByIdComponente() {
        try {
            btnCadastro = (Button) findViewById(R.id.btnCadastrar_cadastro);
            txtEmail = (EditText) findViewById(R.id.txtEmailUsuario_cadastro);
            txtSenha = (EditText) findViewById(R.id.txtSenhaUsuario_cadastro);
            txtConfSenha = (EditText) findViewById(R.id.txtConfSenhaUsuario_cadastro);
            txtNome = (EditText) findViewById(R.id.txtNomeUsuario_cadastro);
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro: findViewByIdComponente CadastroUsuario: " + ex.getMessage());
        }
    }

    private void salvarUsuarioServidor(Usuario usuario) {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(CadastroUsuarioActivity.this);
            final String usuarioJson = JsonHelper.objectToJson(usuario);

            (new AsyncTask<String, Void, String>() {
                ProgressDialog progressDialog;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog = ProgressDialog.show(CadastroUsuarioActivity.this, "Aguarde", "Atualizando dados");
                }

                @Override
                protected String doInBackground(String... params) {
                    String json = null;

                    try {
                        String url = params[0];

                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(CadastroUsuarioActivity.this);

                        json = HttpHelper.POSTJson(url, usuarioJson);

                        if (json.equals("OK")) {
                            sharedPreferences.edit().putString("usuario.json", usuarioJson).apply();
                            Log.i(MainActivity.TAG, json);
                        }
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
                    if (json.equals("OK")) {
                        startarActivity();
                    } else {
                        String mensagem = "Cadastro não realizado, tente novamente.";
                        Snackbar.make(findViewById(R.id.btnCadastrar_cadastro), mensagem, Snackbar.LENGTH_SHORT).show();
                    }
                }
            }).execute(getString(R.string.url_cadastrar_usuario));
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
            Log.i(MainActivity.TAG, "Erro: startarActivity CadastroUsuario: " + ex.getMessage());
        }
    }

}
