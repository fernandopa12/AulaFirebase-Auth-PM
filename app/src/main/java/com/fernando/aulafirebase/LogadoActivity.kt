package com.fernando.aulafirebase

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fernando.aulafirebase.databinding.ActivityLogadoBinding
import com.fernando.aulafirebase.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LogadoActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityLogadoBinding.inflate(layoutInflater)
    }

    private val autenticacao by lazy{
        FirebaseAuth.getInstance()
    }

    private val bancoDados by lazy{
        FirebaseFirestore.getInstance()
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
        binding.btnDeslogar.setOnClickListener{
            autenticacao.signOut()
            startActivity(Intent(this,MainActivity::class.java))
        }

        binding.btnSalvar.setOnClickListener{
            salvarUsuario()
        }
        binding.btnAtualizar.setOnClickListener{
            atualizarUsuario()
        }
        binding.btnRemover.setOnClickListener{
            removerUsuario()
        }

    }

    private fun salvarUsuario(){
        val dados = mapOf(
            "nomeCompleto" to binding.editNomeCompleto.text.toString(),
            "telefone" to binding.editTelefone.text.toString()
        )

        val idUsuarioAtual = autenticacao.currentUser?.uid

        if(idUsuarioAtual!=null){
            val referenciaColecao = bancoDados.collection("usuarios")

            //Adicionando novo usuário..
            referenciaColecao.document(idUsuarioAtual).set(dados)
                .addOnSuccessListener {
                    AlertDialog.Builder(this)
                        .setTitle("Sucesso")
                        .setMessage("Cadastro de realizado com sucesso..")
                        .setNegativeButton("OK"){dialog,posicao->}
                        .create()
                        .show()
                }
                .addOnFailureListener{
                    AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Dados não salvo")
                        .setNegativeButton("OK"){dialog,posicao->}
                        .create()
                        .show()
                }
        }
    }
    private fun atualizarUsuario() {
        val dados = mapOf(
            "telefone" to binding.editTelefone.text.toString()
        )

        val idUsuarioAtual = autenticacao.currentUser?.uid

        if (idUsuarioAtual != null) {
            val referenciaUsuario = bancoDados.collection("usuarios")
                .document(idUsuarioAtual)

            //Atualizando usuário..
                referenciaUsuario.update("telefone",binding.editTelefone.text.toString())
                .addOnSuccessListener {
                    AlertDialog.Builder(this)
                        .setTitle("Sucesso ao atualizar")
                        .setMessage("Registro atualizado com sucesso..")
                        .setNegativeButton("OK") { dialog, posicao -> }
                        .create()
                        .show()
                }
                .addOnFailureListener {
                    AlertDialog.Builder(this)
                        .setTitle("Error ao atualizar")
                        .setMessage("Registro não atualizado")
                        .setNegativeButton("OK") { dialog, posicao -> }
                        .create()
                        .show()
                }
        }
    }
    private fun removerUsuario(){
        val idUsuarioLogado = autenticacao.currentUser?.uid
        if(idUsuarioLogado!=null){
            bancoDados.collection("usuarios")
                .document(idUsuarioLogado)
                .delete()
                .addOnSuccessListener {
                    AlertDialog.Builder(this)
                        .setTitle("SUCESSO AO REMOVER")
                        .setMessage("Registros deletados com sucesso")
                        .setNegativeButton("OK") { dialog, posicao -> }
                        .create()
                        .show()
                }
                .addOnFailureListener{
                    AlertDialog.Builder(this)
                        .setTitle("ERROR AO REMOVER")
                        .setMessage("Não foi possível remover dados do usuário logado")
                        .setNegativeButton("OK") { dialog, posicao -> }
                        .create()
                        .show()
                }
        }
    }
}