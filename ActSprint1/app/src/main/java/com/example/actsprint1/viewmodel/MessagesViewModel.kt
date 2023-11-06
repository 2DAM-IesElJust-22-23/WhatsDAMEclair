package com.example.actsprint1.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.actsprint1.model.Message
import com.example.actsprint1.repository.MessageRepository
import com.example.actsprint1.model.MessageManager

class MessagesViewModel(application: Application) : AndroidViewModel(application) {
    private val _adaptador = MutableLiveData<MessageViewHolder.MessageAdapter>().apply {
        value = MessageViewHolder.MessageAdapter(
            MessageManager
        ) { m: Message, v: View -> MessageLongClickedManager(m, v) }
    }
    val adaptador: MutableLiveData<MessageViewHolder.MessageAdapter> =_adaptador

    public fun add(msg:Message){
        if(MessageRepository.getInstance().add(msg)){
            adaptador.value?.notifyItemInserted(MessageRepository.getInstance().getItemCount()-1)
        }
    }
    public fun MessageLongClickedManager(msg: Message, v:View):Boolean{
        val index = MessageRepository.getInstance().remove(msg)

        adaptador.value?.notifyItemRemoved(index)
        return true
    }
}