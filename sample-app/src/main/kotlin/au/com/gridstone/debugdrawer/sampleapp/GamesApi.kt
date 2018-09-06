package au.com.gridstone.debugdrawer.sampleapp

import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET

interface GamesApi {
  @GET("games?format=json&filter=name:zelda&field_list=id,name,image,aliases,deck,original_release_date,platforms")
  fun getGames(): Deferred<GamesResponse>
}
