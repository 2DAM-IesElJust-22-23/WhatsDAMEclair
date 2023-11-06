package com.example.actsprint1.repository

import com.example.actsprint1.model.Message
import com.example.actsprint1.model.MessageManager

class MessageRepository {
    companion object {
        private var INSTANCE: MessageRepository? = null

        fun getInstance(): MessageRepository {
            if (INSTANCE == null) {
                INSTANCE = MessageRepository()
            }
            return INSTANCE!!
        }
    }

    fun getMessageList() = MessageManager.getMessageList()

    fun getItemCount() = MessageManager.getItemCount()

    fun add(msg: Message) = MessageManager.add(msg)

    fun remove(msg: Message) = MessageManager.remove(msg)

}