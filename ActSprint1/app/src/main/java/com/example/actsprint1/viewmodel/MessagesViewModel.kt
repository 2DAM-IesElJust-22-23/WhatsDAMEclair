package com.example.actsprint1.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.actsprint1.model.Message
import com.example.actsprint1.model.MessageManager
import com.example.actsprint1.repository.MessageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MessagesViewModel(application: Application) : AndroidViewModel(application) {
    private val _adaptador = MutableLiveData<MessageAdapter>().apply {
        value = MessageAdapter(
            MessageManager
        ) { m: Message, v: View -> MessageLongClickedManager(m, v) }
    }
    val adaptador: MutableLiveData<MessageAdapter> =_adaptador

    public fun add(msg: Message,server:String) {
        viewModelScope.launch {
            // Lanzamiento en el hilo principal
            val result = withContext(Dispatchers.IO) {
                // Cambiamos al hilo de E/S para la operación de escritura
                repository.sendMessage(msg,server)
            }

            if (result != null) {
                adaptador.value?.notifyItemInserted(repository.getItemCount() - 1)
            }
        }
    }
    public fun MessageLongClickedManager(msg: Message, v:View):Boolean{
        val index = MessageRepository.getInstance().remove(msg)

        adaptador.value?.notifyItemRemoved(index)
        return true
    }
    fun getUserName(): String {
        return repository.username
    }
    fun getServer(): String{
        return repository.server
    }

    val repository = MessageRepository.getInstance()
    val llistaMissatges: LiveData<ArrayList<Message>> by lazy{
        repository.getMessageList()
    }
}