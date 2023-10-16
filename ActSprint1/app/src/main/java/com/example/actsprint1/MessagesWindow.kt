package com.example.actsprint1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.actsprint1.databinding.ActivityMessagesWindowBinding
import java.text.SimpleDateFormat
import java.util.Date

data class Message(val username: String, val text: String)

object MessageManager {
    private val messageList = mutableListOf<Message>()

    fun addMessage(username: String, text: String) {
        val message = Message(username, text)
        messageList.add(message)
    }

    fun getMessageList(): List<Message> {
        return messageList
    }
}

class MessageAdapter(private val messageList: List<Message>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    var horaFormatada: String = ""

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextView: TextView = itemView.findViewById(R.id.msg_text)
        val timestampTextView: TextView = itemView.findViewById(R.id.msg_me_timestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.my_msg_viewholder, parent, false)
        return MessageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messageList[position]
        holder.messageTextView.text = message.text
        holder.timestampTextView.text = horaFormatada
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
}

class MessagesWindow : AppCompatActivity() {
    private lateinit var serverAddress: String
    private lateinit var nickName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages_window)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val messageList = MessageManager.getMessageList()

        val adapter = MessageAdapter(messageList)
        recyclerView.adapter = adapter

        serverAddress = intent.getStringExtra("serverAddress") ?: ""
        nickName = intent.getStringExtra("nickName") ?: ""

        val connectionInfoTextView = findViewById<TextView>(R.id.connectionInfoTextView)
        val formattedText = getString(R.string.connection_info_template, serverAddress, nickName)
        connectionInfoTextView.text = formattedText

        val messageEditText = findViewById<EditText>(R.id.MessageText)
        val sendButton = findViewById<ImageButton>(R.id.sendMessage)

        sendButton.setOnClickListener {
            val messageText = messageEditText.text.toString()
            val dateFormat = SimpleDateFormat("HH:mm")
            val horaActual = Date()
            adapter.horaFormatada = dateFormat.format(horaActual)
            if (messageText.isNotEmpty()) {
                MessageManager.addMessage(nickName, messageText)
                messageEditText.text.clear()
                adapter.notifyDataSetChanged()

                // Desplazarse al último mensaje
                val lastIndex = messageList.size - 1
                recyclerView.smoothScrollToPosition(lastIndex)
            }
        }
    }
}
