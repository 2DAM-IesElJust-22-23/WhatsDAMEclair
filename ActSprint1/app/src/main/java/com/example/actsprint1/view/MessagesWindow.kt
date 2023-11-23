package com.example.actsprint1.view

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.actsprint1.databinding.ActivityMessagesWindowBinding
import com.example.actsprint1.model.Message
import com.example.actsprint1.model.MessageManager
import com.example.actsprint1.viewmodel.MessageAdapter
import com.example.actsprint1.viewmodel.MessagesViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class MessagesWindow : AppCompatActivity() {

    // Propiedad para acceder a las vistas del diseño XML mediante ViewBinding
    private lateinit var binding: ActivityMessagesWindowBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa la vista utilizando el enlace generado por ViewBinding.
        binding = ActivityMessagesWindowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtiene referencias a las vistas de la interfaz de usuario
        val messageText = binding.MessageText
        val textView = binding.connectionInfoTextView
        val sendMessage = binding.sendMessage

        // Configurar el RecyclerView
        val recyclerView = binding.MessagesRecyclerView

        // Asociar el LayoutManager al RecyclerView
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        // Crear e inicializar tu adaptador (MyAdapter) y asignarlo al RecyclerView

        val adapter = MessageAdapter(MessageManager) { m: Message, v: View ->
            val missatgesViewModel = MessagesViewModel(this.application) // Crea una instancia de MissatgesViewModel
            missatgesViewModel.MessageLongClickedManager(m, v) // Llama a la función desde la instancia
        }
        recyclerView.adapter = adapter

        // Indicamos que el tamaño sea fijo
        recyclerView.setHasFixedSize(true)

        // Creamos una instancia de adaptador
        val missatgesViewModel = MessagesViewModel(this.application)

        recyclerView.adapter = MessageAdapter(MessageManager) { m: Message, v: View ->
            missatgesViewModel.MessageLongClickedManager(m, v)
        }

        missatgesViewModel.llistaMissatges.observe(this) { messages ->
            if (messages.isNotEmpty()) {
                missatgesViewModel.adaptador.value?.notifyItemInserted(messages.size - 1)
            }
        }

        // Obtiene los valores de "NICKNAME_KEY" e "IPSERVER" del Intent
        val serverAddress = missatgesViewModel.getServer()
        val nickName = missatgesViewModel.getUserName()

        // Actualiza el texto de la información de conexión
        textView.text = "Conectat a $serverAddress com a $nickName"

        // Configura el clic del botón "Send Message"
        // Limpia el texto del campo de mensaje
        sendMessage.setOnClickListener {
            val horaActual = LocalTime.now()
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            val hora = horaActual.format(formatter)

            // Crear un nuevo objeto de Mensaje y agregarlo a la lista mensajesEnviados.
            missatgesViewModel.add(Message(nickName,messageText.text.toString(),hora),serverAddress)
            //MessageManager.add(Message(nickName,messageText.text.toString(),hora))

            // Notificar al adaptador de MensajesRecyclerView que se ha insertado un nuevo elemento en la lista.
            binding.MessagesRecyclerView.adapter?.notifyItemInserted(missatgesViewModel.repository.getLastMessage())


            // Desplazar la vista del RecyclerView hacia la última posición de la lista (el mensaje recién agregado).
            recyclerView.scrollToPosition(missatgesViewModel.repository.getLastMessage())

            messageText.text.clear()
        }

    }
}