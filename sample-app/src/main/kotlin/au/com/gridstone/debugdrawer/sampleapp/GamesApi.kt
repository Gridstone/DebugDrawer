package au.com.gridstone.debugdrawer.sampleapp

import kotlinx.coroutines.Deferred
import retrofit2.http.GET

// TODO: Migrate to suspend function once this issue is resolved
//  https://github.com/square/retrofit/issues/3148
interface GamesApi {
  @GET("games?format=json&filter=name:zelda&field_list=id,name,image,aliases,deck,original_release_date,platforms")
  fun getGames(): Deferred<GamesResponse>
}
