package au.com.gridstone.debugdrawer.sampleapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import au.com.gridstone.debugdrawer.sampleapp.GamesViewModel.State.Error
import au.com.gridstone.debugdrawer.sampleapp.GamesViewModel.State.Idle
import au.com.gridstone.debugdrawer.sampleapp.GamesViewModel.State.Loading
import au.com.gridstone.debugdrawer.sampleapp.GamesViewModel.State.Success
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch

class GamesViewModel : ViewModel() {

  private val getGamesJob: Job? = null
  private val mutableStates: MutableLiveData<State> = MutableLiveData()

  val states: LiveData<State> = mutableStates

  init {
    mutableStates.value = Idle
  }

  fun refreshIfNecessary() {
    if (mutableStates.value is Idle || mutableStates.value is Error) {
      refresh()
    }
  }

  fun refresh() {
    getGamesJob?.cancel()
    mutableStates.value = Loading

    launch {

      val api: GamesApi = AppConfiguration.api

      try {
        val response: GamesResponse = api.getGames().await()
        mutableStates.postValue(Success(response.results))
      } catch (_: Exception) {
        mutableStates.postValue(Error)
      }
    }
  }

  override fun onCleared() {
    getGamesJob?.cancel()
  }

  sealed class State {
    object Idle : State()
    object Error : State()
    object Loading : State()
    data class Success(val games: List<Game>) : State()
  }
}
