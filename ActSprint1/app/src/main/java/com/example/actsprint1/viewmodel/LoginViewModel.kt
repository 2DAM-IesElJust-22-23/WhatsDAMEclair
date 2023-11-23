package com.example.actsprint1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.actsprint1.repository.MessageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import androidx.lifecycle.viewModelScope

class LoginViewModel : ViewModel() {
    private val _loginStatus = MutableLiveData<JSONObject>()
    val loginStatus: MutableLiveData<JSONObject> = _loginStatus

    public final suspend fun login(username: String, server: String){
        viewModelScope.launch(Dispatchers.Main) {
            // Tasques que s'executen en el fil principal

            // Canviem de Dispatcher pe fer operacions
            // d'entrada i eixida

            val resultat = withContext(Dispatchers.IO) {
                // Invoquem al mètode de Login del
                // repositori, proporcionant-li
                // les credencials de l'usuari i
                // esperant la resposta.
                MessageRepository.getInstance().login(username, server)
                // Quan rebem la resposta, actualitzem
                // l'estat de la connexió.
                // Com que estem en una corrutina,
                // el valor d'aquest, que és un LiveData
                // no es podrà modificar directament,
                // sinò que haurem d'utilitzar postValue.

            }
            loginStatus.postValue(resultat)
        }
    }
}
