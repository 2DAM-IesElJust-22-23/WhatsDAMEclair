package com.example.actsprint1.model

object MessageManager {
    private val messageList = mutableListOf<Message>()

    fun getMessageList(): List<Message> = messageList
    fun getItemCount(): Int = messageList.size
    fun getMessage(pos: Int): Message = messageList[pos]
    fun getLastNum(): Int = messageList.size -1
    fun remove(msg:Message) = messageList.remove(msg)
    fun add(msg: Message) = messageList.add(msg)
}
