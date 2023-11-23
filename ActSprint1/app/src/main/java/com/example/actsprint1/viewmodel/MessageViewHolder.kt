package com.example.actsprint1.viewmodel

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.actsprint1.R
import com.example.actsprint1.model.Message

class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val messageText = itemView.findViewById(R.id.msg_text) as TextView
    private val time =itemView.findViewById(R.id.msg_me_timestamp) as TextView

    fun bind (msg: Message){
        messageText.text = msg.text
        time.text = msg.time
    }


}