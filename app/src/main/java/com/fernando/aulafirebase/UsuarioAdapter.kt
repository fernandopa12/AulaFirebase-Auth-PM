package com.fernando.aulafirebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fernando.aulafirebase.model.Usuario

class UsuarioAdapter(private val usuarios:List<Usuario>):
    RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>(){

        class UsuarioViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
            val nomeCompleto:TextView = itemView.findViewById(R.id.nome_completo)
            val telefone:TextView = itemView.findViewById(R.id.telefone)
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.usuario_lista,parent,false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioAdapter.UsuarioViewHolder, position: Int) {
        val usuario = usuarios[position]
        holder.nomeCompleto.text = usuario.nomeCompleto
        holder.telefone.text = usuario.telefone
    }

    override fun getItemCount(): Int {
        return usuarios.size
    }

}
