package com.example.arraycrud_firebase.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.arraycrud_firebase.R
import com.example.arraycrud_firebase.User
import com.example.arraycrud_firebase.`interface`.Clickinterface

class UserAdapter(private var userList: ArrayList<User>, val Clickinterface: Clickinterface) :
    RecyclerView.Adapter<UserAdapter.viewHolder>() {

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvFirstname: TextView
        var tvLastname: TextView
        var btnDelete: Button
        var btnUpdate: Button

        init {
            tvFirstname = itemView.findViewById<TextView>(R.id.tvFirstName)
            tvLastname = itemView.findViewById<TextView>(R.id.tvLastName)
            btnDelete = itemView.findViewById(R.id.btnDelete)
            btnUpdate = itemView.findViewById(R.id.btnUpdate)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.user_list, parent, false)
        return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val newList = userList[position]

        holder.tvFirstname.text = newList.firstname
        holder.tvLastname.text = newList.lastname
        holder.btnUpdate.setOnClickListener {
            Clickinterface.updateClicked(position)
        }
        holder.btnDelete.setOnClickListener {
            Clickinterface.deleteClicked(position)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}


