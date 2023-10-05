package com.example.actsprint1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.actsprint1.databinding.ActivityMainBinding
import java.net.Inet4Address

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var nickName: String
    private lateinit var serverAddress: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val buttonConnect = binding.buttonConnect

        buttonConnect.setOnClickListener {
            // Obtener el valor del NickName y la dirección IP del servidor
            nickName = binding.nickNameText.text.toString()
            serverAddress = binding.serverAddressText.text.toString()

            // Validar el NickName no esté vacío y la dirección IP sea válida
            if (isValidNickName(nickName) && isValidIPAddress(serverAddress)) {
                // Abrir la ventana de mensajes (puedes agregar tu lógica aquí)
                openMessageWindow()
            } else {
                // Mostrar un mensaje de error si las validaciones fallan
                Toast.makeText(this, "Nombre de usuario o dirección IP no válidos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidNickName(nickName: String): Boolean {
        return nickName.isNotBlank()
    }

    private fun isValidIPAddress(ipAddress: String): Boolean {
        // Primero, verifica el formato utilizando expresiones regulares
        val ipAddressRegex = """^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$""".toRegex()
        if (!ipAddress.matches(ipAddressRegex)) {
            return false // El formato es incorrecto
        }

        val parts = ipAddress.split(".")

        // Deben haber exactamente 4 partes separadas por puntos
        if (parts.size != 4) {
            return false
        }

        for (part in parts) {
            try {
                val number = part.toInt()

                // Cada parte debe ser un número entre 0 y 255
                if (number < 0 || number > 255) {
                    return false
                }
            } catch (e: NumberFormatException) {
                // Si una parte no es un número válido, la dirección IP es inválida
                return false
            }
        }

        // Si se superan todas las verificaciones, la dirección IP es válida
        return true
    }

    private fun openMessageWindow() {
        // Aquí puedes abrir la ventana de mensajes
        val intent = Intent(this, MessagesWindow::class.java)
        intent.putExtra("serverAddress", serverAddress)
        intent.putExtra("nickName", nickName)
        startActivity(intent)
    }
}