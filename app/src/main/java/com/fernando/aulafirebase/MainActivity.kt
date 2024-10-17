package com.fernando.aulafirebase

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fernando.aulafirebase.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val autenticacao by lazy{
        FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        verificarUsuarioLogado()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnCadastrarSe.setOnClickListener{
            startActivity(Intent(this,CadastroActivity::class.java))
        }

        binding.btnLogin.setOnClickListener{
            logarUsario()
        }

    }

    private fun cadastrarUsuario(){

    }

    private fun verificarUsuarioLogado(){
        val usuario = autenticacao.currentUser
        //val id = autenticacao.currentUser?.uid

        if(usuario!=null){
            startActivity(Intent(this,LogadoActivity::class.java))
        }
    }

    private fun logarUsario(){
        val email  = binding.editEmail.text.toString()
        val senha = binding.editSenha.text.toString()

        autenticacao.signInWithEmailAndPassword(email,senha)
            .addOnSuccessListener { authResult->
                startActivity(Intent(this,LogadoActivity::class.java))
            }
            .addOnFailureListener{exception->
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Verificar e-mail e senha digitados..")
                    .setNegativeButton("OK"){dialog,posicao->}
                    .create()
                    .show()
            }
    }
}