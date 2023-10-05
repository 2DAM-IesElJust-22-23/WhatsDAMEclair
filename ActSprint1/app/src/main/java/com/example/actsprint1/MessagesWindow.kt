package com.example.actsprint1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.example.actsprint1.databinding.ActivityMessagesWindowBinding

class MessagesWindow : AppCompatActivity() {

    private lateinit var serverAddress: String
    private lateinit var nickName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages_window)

        // Obtener los valores de serverAddress y nickName del Intent
        serverAddress = intent.getStringExtra("serverAddress") ?: ""
        nickName = intent.getStringExtra("nickName") ?: ""

        // Configurar el texto del TextView reemplazando los marcadores de posición
        val connectionInfoTextView = findViewById<TextView>(R.id.connectionInfoTextView)
        val formattedText = getString(R.string.connection_info_template, serverAddress, nickName)
        connectionInfoTextView.text = formattedText

        // Referencias a elementos de UI
        val messageEditText = findViewById<EditText>(R.id.MessageText)
        val sendButton = findViewById<ImageButton>(R.id.sendMessage)

        // Configurar el clic del botón de enviar (borrar el texto por ahora)
        sendButton.setOnClickListener {
            messageEditText.text.clear()
        }
    }
}
