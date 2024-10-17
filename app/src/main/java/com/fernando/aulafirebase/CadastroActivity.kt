package com.fernando.aulafirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class CadastroActivity : AppCompatActivity() {
    private val autenticacao by lazy {
        FirebaseAuth.getInstance()
    }

    private lateinit var btnCadastrar: Button
    private lateinit var editEmail: EditText
    private lateinit var editSenha: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cadastro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnCadastrar = findViewById(R.id.btnCadastrar)
        editEmail = findViewById(R.id.editEmail)
        editSenha = findViewById(R.id.editSenha)

        btnCadastrar.setOnClickListener {
            val email = editEmail.text.toString()
            val senha = editSenha.text.toString()


            //Passar os paramentros para criação do usuario $email e $senha

            autenticacao.createUserWithEmailAndPassword(email, senha)
                .addOnSuccessListener { authResult ->
                    val id = authResult.user?.uid
                    val email = authResult.user?.email

                    AlertDialog.Builder(this)
                        .setTitle("Usuario Criado")
                        .setMessage("Usuario Criado com sucesso")
                        .setPositiveButton("OK"){dialog,posicao->
                            startActivity(Intent(this,MainActivity::class.java))
                        }
                        .create()
                        .show()
                }.addOnFailureListener { exception ->
                    val mensagemErro = exception.message

                    AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Error ao criar usuario $mensagemErro")
                        .setNegativeButton("OK"){dialog,posicao->}
                        .create()
                        .show()

                }
        }
    }
}