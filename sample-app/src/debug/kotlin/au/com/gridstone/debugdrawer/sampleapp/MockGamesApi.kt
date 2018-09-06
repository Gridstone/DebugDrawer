package au.com.gridstone.debugdrawer.sampleapp

import kotlinx.coroutines.experimental.Deferred
import retrofit2.mock.BehaviorDelegate
import retrofit2.mock.MockRetrofit

class MockGamesApi(mockRetrofit: MockRetrofit) : GamesApi {
  private val delegate: BehaviorDelegate<GamesApi> =
      mockRetrofit.create(GamesApi::class.java)

  override fun getGames(): Deferred<GamesResponse> {
    val response = GamesResponse(mockGames)
    return delegate.returningResponse(response).getGames()
  }

  private val mockGames = listOf(
      Game(27668,
           "The Witness",
           null,
           "An exploration-focused puzzle-adventure game led by the creator of the 2008 indie game Braid. While exploring a quiet but colorful island, players must solve a series of maze-like puzzles on numerous electronic puzzle consoles.",
           Image("https://www.giantbomb.com/api/image/screen_medium/2994285-box_tw.png",
                 "https://www.giantbomb.com/api/image/scale_large/2994285-box_tw.png"),
           "2016-01-26 00:00:00",
           listOf(Platform("MAC"), Platform("PC"), Platform("Xbox One"), Platform("PlayStation 4"))
      ),
      Game(31772,
           "Journey",
           null,
           "Journey is thatgamecompany's third release for Sony. Roam the lands discovering the history of an ancient civilization on a trek toward a distant mountain. Go at it alone or explore with strangers online.",
           Image(
               "https://www.giantbomb.com/api/image/scale_small/2388618-2172218_journey_detailcropped.png",
               "https://www.giantbomb.com/api/image/scale_large/2388618-2172218_journey_detailcropped.png"),
           "2012-03-13 00:00:00",
           listOf(Platform("PlayStation 3"), Platform("PlayStation 4"))
      ),
      Game(41484,
           "The Witcher 3: Wild Hunt",
           "The Witcher III: Wild Hunt",
           "CD Projekt RED's third Witcher combines the series' non-linear storytelling with a sprawling open world that concludes the saga of Geralt of Rivia.",
           Image(
               "https://www.giantbomb.com/api/image/scale_small/2945734-the%20witcher%203%20-%20wild%20hunt.jpg",
               "https://www.giantbomb.com/api/image/scale_large/2945734-the%20witcher%203%20-%20wild%20hunt.jpg"),
           "2015-05-19 00:00:00",
           listOf(Platform("PC"), Platform("Xbox One"), Platform("PlayStation 4"))
      ),
      Game(13594,
           "The Legend of Zelda: Majora's Mask",
           "LoZ: MM\\r\\nZelda no Densetsu: Majora no Kamen\\r\\nゼルダの伝説 ムジュラの仮面\\r\\nZelda Gaiden\\r\\nThe Legend of Zelda: Majora's Mask 3D",
           "The follow-up to Ocarina of Time sees the series stalwart Link embark on a journey to save the land of Termina from being crushed by the moon in three days. To defeat the Skull Kid, Link has to live the same three days over and over again.",
           Image("https://www.giantbomb.com/api/image/scale_small/2204861-box_tlozmm.png",
                 "https://www.giantbomb.com/api/image/scale_large/2204861-box_tlozmm.png"),
           "2000-04-27 00:00:00",
           listOf(Platform("Nintendo 64"), Platform("Nintendo 3DS"))
      ),
      Game(20716,
           "Braid",
           null,
           "Manipulate time to complete puzzles in this 2D platform game made by indie developer Jonathan Blow.",
           Image("https://www.giantbomb.com/api/image/scale_small/1331143-braid_box.png",
                 "https://www.giantbomb.com/api/image/scale_large/1331143-braid_box.png"),
           "2008-08-06 00:00:00",
           listOf(Platform("PC"), Platform("MAC"), Platform("Xbox 360"), Platform("PlayStation 3"))
      )
  )
}
