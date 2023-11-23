package com.example.actsprint1.repository

import com.example.actsprint1.model.CommunicationManager
import com.example.actsprint1.model.Message
import com.example.actsprint1.model.MessageManager
import org.json.JSONObject

class MessageRepository {
    var username:String=""
    var server:String=""

    companion object {
        private var INSTANCE: MessageRepository? = null

        fun getInstance(): MessageRepository {
            if (INSTANCE == null) {
                INSTANCE = MessageRepository()
            }
            return INSTANCE!!
        }
    }

    fun getMessageList() = MessageManager.getMessages()

    fun getLastMessage() = MessageManager.getLastNum()

    // Igual que getNumMessages()
    fun getItemCount() = MessageManager.getItemCount()

    fun add(msg: Message) = MessageManager.add(msg)

    fun remove(msg: Message) = MessageManager.remove(msg)

    fun getMessage(position:Int) = MessageManager.getMessage(position)

    suspend fun sendMessage(msg: Message,server: String) {
        MessageManager.sendMessage(msg,server)
    }

    suspend fun login(username: String, server: String): JSONObject {
        val cm = CommunicationManager()
        this.username = username
        this.server = server
        return cm.login(username, server)
    }
}