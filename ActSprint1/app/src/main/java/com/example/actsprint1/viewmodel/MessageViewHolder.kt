package com.example.actsprint1.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.actsprint1.R
import com.example.actsprint1.model.Message
import com.example.actsprint1.model.MessageManager

class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val messageText = itemView.findViewById(R.id.msg_text) as TextView
    private val time =itemView.findViewById(R.id.msg_me_timestamp) as TextView

    fun bind (msg: Message){
        messageText.text = msg.text
        time.text = msg.time
    }
    class MessageAdapter(private val Messages: MessageManager, function: (Message, View) -> Unit) : RecyclerView.Adapter<MessageViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemView = inflater.inflate(R.layout.my_msg_viewholder,parent,false)
            return MessageViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
            val message = Messages.getMessage(position)
            holder.bind(message)
        }

        override fun getItemCount(): Int {
            return MessageManager.getItemCount()
        }

        fun notifyItemRemoved(index: Boolean) {

        }
    }

}