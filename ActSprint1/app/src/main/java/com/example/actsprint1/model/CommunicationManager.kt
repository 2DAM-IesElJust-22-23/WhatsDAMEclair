package com.example.actsprint1.model

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket


class CommunicationManager {
    val port: Int = 9999

    var listenPort: Int? = null

    /*suspend fun sendServer(msg: String): (JSONObject) = withContext(Dispatchers.IO) {
        try {
            val socket = Socket(server, port)
            val out = PrintWriter(socket.getOutputStream(), true)
            val input = BufferedReader(InputStreamReader(socket.getInputStream()))

            out.println(msg)
            out.flush()
            val response = input.readLine()

            out.close()
            input.close()
            socket.close()

            return@withContext JSONObject(response)
        } catch (e: ConnectException) {
            Log.e("errorlogin", "Error de conexión: ${e.message}")
            throw e
        } catch (e: Exception) {
            Log.e("errorlogin", "Otro error durante la comunicación con el servidor", e)
            throw e
        }
    }*/
    suspend fun sendServer(msg: String,server: String): (JSONObject) = withContext(Dispatchers.IO) {
        var resposta: JSONObject? = null
        val socket = Socket()
        Log.d("prueba","antes de socketAddr $server")
        val socketAddr: InetSocketAddress = InetSocketAddress(server,port)
        Log.d("prueba","despues de socketAddr")
        try {
            // Establecer la conexión con el servidor
            socket.connect(socketAddr)

            // Obtener flujos de entrada y salida para la comunicación con el servidor
            val `is` = socket.getInputStream()
            val os = socket.getOutputStream()
            val isr = InputStreamReader(`is`)
            val osw = OutputStreamWriter(os)
            val bReader = BufferedReader(isr)
            val pWriter = PrintWriter(osw)

            // Enviar el mensaje al servidor
            println(msg)
            pWriter.println(msg)
            pWriter.flush()

            // Leer la respuesta del servidor
            val linia = bReader.readLine()
            resposta = JSONObject(linia)

            // Cerrar los flujos y el socket
            pWriter.close()
            bReader.close()
            isr.close()
            osw.close()
            `is`.close()
            os.close()
            socket.close()
        } catch (e: java.lang.Exception) {
            // Manejar excepciones imprimiendo un mensaje de error
            println("Error: " + e.message)
            e.printStackTrace()
        }
        return@withContext resposta!!
    }

    suspend fun prepareListener() {
        val serverSocket = ServerSocket(0)
        listenPort = serverSocket.localPort
        Log.d("errorlogin", "LISTENPORT: $listenPort")
        GlobalScope.launch(Dispatchers.IO) {
            while (true) {
                val client = serverSocket.accept()

                GlobalScope.launch(Dispatchers.IO) {
                    val input = BufferedReader(InputStreamReader(client.getInputStream()))
                    val output = PrintWriter(client.getOutputStream(), true)

                    val request = input.readLine()
                    val response = processNotification(request)

                    output.println(response.toString())

                    input.close()
                    output.close()
                    client.close()
                }
            }
        }
    }

    private fun processNotification(s: String) {
        val json = JSONObject(s)
        val type = json.optString("type")
        when (type) {
            "message" -> {
                val message = Message(
                    json.optString("user"),
                    json.optString("content"),
                    json.optString("time")
                )
                MessageManager.add(message)
            }
            // Otros casos según los tipos de notificaciones necesarios
            else -> {
                // Otra lógica de procesamiento según el tipo de notificación
            }
        }
    }

    suspend fun login(username: String, server: String): JSONObject {
        return try {
            prepareListener()
            val msg = JSONObject()
            msg.put("command", "register")
            msg.put("user", username)
            msg.put("listenPort", listenPort)

            sendServer(msg.toString(),server)
        } catch (e: Exception) {
            JSONObject().put("status", "error")
        }
    }
}