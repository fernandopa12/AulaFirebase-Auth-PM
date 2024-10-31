package com.fernando.aulafirebase

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fernando.aulafirebase.databinding.ActivityUploadImagemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.UUID

class UploadImagemActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityUploadImagemBinding.inflate(layoutInflater)
    }
    private val autenticacao by lazy{
        FirebaseAuth.getInstance()
    }

    private val armazenamento by lazy {
        FirebaseStorage.getInstance()
    }
    private var uriImagemSelecionada:Uri? = null

    private val abrirGaleria = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ){uri->
        if(uri!=null){
            binding.imageViewImgCarregada.setImageURI(uri)
            uriImagemSelecionada = uri
        }
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

        binding.imageButtonGaleria.setOnClickListener{
            abrirGaleria.launch("image/*")
        }
        binding.btnUpload.setOnClickListener{
            uploadGaleria()
        }
        binding.btnRecuperarImagem.setOnClickListener{
            recuperarImagemFirebase()
        }
    }

    private fun recuperarImagemFirebase() {
        val idUsuarioLogado = autenticacao.currentUser?.uid
        if(idUsuarioLogado!=null){
            armazenamento
                .getReference("fotos")
                .child(idUsuarioLogado)
                .child("foto.jpg")
                .downloadUrl
                .addOnSuccessListener { uri->
                   Picasso.get()
                       .load(uri)
                       .into(binding.imageViewGetImagem)
                }

        }
    }

    private fun uploadGaleria() {
        val idUsuarioLogado = autenticacao.currentUser?.uid
        val nomeImagem = UUID.randomUUID().toString()
        if(uriImagemSelecionada!=null && idUsuarioLogado!=null){
            armazenamento
                .getReference("fotos")
                .child(idUsuarioLogado)
                .child("foto.jpg")
                .putFile(uriImagemSelecionada!!)
                .addOnSuccessListener {
                    AlertDialog.Builder(this)
                        .setTitle("Sucesso")
                        .setMessage("Imagem Enviado com sucesso")
                        .setNegativeButton("OK"){dialog,posicao->}
                        .create()
                        .show()
                }
                .addOnFailureListener{exepction->
                    AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Imagem não enviada")
                        .setNegativeButton("OK"){dialog,posicao->}
                        .create()
                        .show()
                }
        }
    }
}