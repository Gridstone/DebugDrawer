package au.com.gridstone.debugdrawer.sampleapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import au.com.gridstone.debugdrawer.sampleapp.GamesViewModel.State.Error
import au.com.gridstone.debugdrawer.sampleapp.GamesViewModel.State.Idle
import au.com.gridstone.debugdrawer.sampleapp.GamesViewModel.State.Loading
import au.com.gridstone.debugdrawer.sampleapp.GamesViewModel.State.Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class GamesViewModel : ViewModel(), CoroutineScope {

  private val mutableStates: MutableLiveData<State> = MutableLiveData()
  override val coroutineContext: CoroutineContext = Job() + Dispatchers.Main
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
    mutableStates.value = Loading

    launch {

      Timber.v("Fetching games list...")
      val api: GamesApi = AppConfiguration.api

      try {
        val response: GamesResponse = api.getGames().await()
        mutableStates.postValue(Success(response.results))
        Timber.v("Fetched games list successfully.")
      } catch (e: Exception) {
        Timber.e(e, "Something went wrong when fetching games list.")
        mutableStates.postValue(Error)
      }
    }
  }

  override fun onCleared() {
    coroutineContext.cancel()
  }

  sealed class State {
    object Idle : State()
    object Error : State()
    object Loading : State()
    data class Success(val games: List<Game>) : State()
  }
}
