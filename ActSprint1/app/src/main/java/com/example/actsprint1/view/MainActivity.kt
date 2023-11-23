package com.example.actsprint1.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.actsprint1.databinding.ActivityMainBinding
import com.example.actsprint1.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var nickName: String
    private lateinit var serverAddress: String
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = LoginViewModel()

        val buttonConnect = binding.buttonConnect

        buttonConnect.setOnClickListener {
            // Obtain the values of NickName and the server IP address
            nickName = binding.nickNameText.text.toString()
            serverAddress = binding.serverAddressText.text.toString()

            // Validate NickName is not blank and the IP address is valid
            if (isValidNickName(nickName) && isValidIPAddress(serverAddress)) {
                // Show the progress bar and hide the button
                binding.progressBar.visibility = View.VISIBLE
                buttonConnect.visibility = View.GONE

                // Call the Login method of the ViewModel
                lifecycleScope.launch {
                    try {
                        // Call the suspend function within the coroutine
                        viewModel.login(nickName, serverAddress)
                    } catch (e: Exception) {

                    }
                }
            } else {
                // Show an error message if validations fail
                Toast.makeText(this, "Invalid username or IP address", Toast.LENGTH_SHORT).show()
            }
        }

        // Observe the loginStatus LiveData
        viewModel.loginStatus.observe(this) { loginStatus ->
            // Check if the JSON contains the "status" key
            if (loginStatus.has("status")) {
                // Extract the value of the "status" key
                val status = loginStatus.getString("status")

                when (status) {
                    "error" -> {
                        // Handle the error state
                        val errorMessage = "ERROR DESCONOCIDO"
                        if (loginStatus.has("message")){
                            val errorMessage = loginStatus.getString("message")
                        }

                        binding.statusTextView.text = errorMessage
                        binding.statusTextView.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        buttonConnect.visibility = View.VISIBLE // Show the button again
                    }
                    "ok" -> {
                        // Proceed to launch the MessagesWindow activity
                        openMessageWindow()
                    }
                }
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
        Log.d("prueba","antes del intent")
        startActivity(intent)
    }
}