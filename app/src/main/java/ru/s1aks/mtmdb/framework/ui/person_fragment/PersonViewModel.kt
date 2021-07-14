package ru.s1aks.mtmdb.framework.ui.person_fragment

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.s1aks.mtmdb.model.PersonState
import ru.s1aks.mtmdb.model.entities.Person
import ru.s1aks.mtmdb.model.repository.*

class PersonViewModel(
    private val repository: RepositoryImpl = RepositoryImpl(RemoteDataSource()),
) : ViewModel(), LifecycleObserver{
    val personLiveData: MutableLiveData<PersonState> = MutableLiveData()

    fun getPerson(id: Int) {
        repository.getPersonFromServer(id, callbackPerson)
    }

    private val callbackPerson = object :
        Callback<Person> {
        override fun onResponse(call: Call<Person>, response: Response<Person>) {
            val serverResponse: Person? = response.body()
            personLiveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
                } else {
                    PersonState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call<Person>, t: Throwable) {
            personLiveData.postValue(PersonState.Error(Throwable(t.message ?: REQUEST_ERROR)))
        }

        private fun checkResponse(serverResponse: Person): PersonState {
            return if (serverResponse.name == null) {
                PersonState.Error(Throwable(CORRUPTED_DATA))
            } else {
                PersonState.Success(serverResponse)
            }
        }
    }
}
