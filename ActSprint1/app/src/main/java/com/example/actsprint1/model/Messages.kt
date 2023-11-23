package com.example.actsprint1.model

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject


object MessageManager {
    private val _messageList = MutableLiveData<ArrayList<Message>>().apply{
        value= ArrayList<Message>()
    }
    val messageList: MutableLiveData<ArrayList<Message>> = _messageList

    fun getMessages(): MutableLiveData<ArrayList<Message>>{
        return messageList
    }

    // Esta funcion es como size()
    fun getItemCount(): Int = messageList.value?.size ?: 0

    // Esta funcion es como getMessageAt(position:Int): Message:
    fun getMessage(pos: Int): Message? = messageList.value?.getOrNull(pos)

    fun getLastNum(): Int {
        val listSize = messageList.value?.size ?: 0
        return if (listSize > 0) listSize - 1 else -1
    }

    fun remove(msg: Message): Boolean {
        val currentList = messageList.value
        currentList?.remove(msg)
        _messageList.value = currentList
        return true
    }

    fun add(msg: Message): Boolean {
        val currentList = messageList.value
        currentList?.add(msg)
        _messageList.postValue(currentList)
        return true
    }

    suspend fun sendMessage(message: Message,server:String) {
        val jsonObject = JSONObject()
        jsonObject.put("command", "newMessage");
        jsonObject.put("user", message.username)
        jsonObject.put("content", message.text)

        val communicationManager = CommunicationManager()
        withContext(Dispatchers.IO) {
            communicationManager.sendServer(jsonObject.toString(),server)
        }
    }

}
