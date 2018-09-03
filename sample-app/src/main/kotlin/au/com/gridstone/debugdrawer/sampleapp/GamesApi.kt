package au.com.gridstone.debugdrawer.sampleapp

import kotlinx.coroutines.experimental.Deferred
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

object GamesApi {

  private const val API_URL = "https://www.giantbomb.com/api/"
  private const val API_KEY = "8ca8e06cb445b8c055442108161848001a1c66fc"

  private val client = OkHttpClient.Builder()
      .addInterceptor(AuthInterceptor)
      .build()

  private val api: GiantBombApi = Retrofit.Builder()
      .baseUrl(API_URL)
      .client(client)
      .addConverterFactory(MoshiConverterFactory.create())
      .build()
      .create(GiantBombApi::class.java)

  suspend fun getGames(): GamesResult {
    return try {
      GamesResult(true, api.getGames().await().results)
    } catch (e: Exception) {
      GamesResult(false, emptyList())
    }
  }

  private data class GamesResponse(val results: List<Game>)

  private interface GiantBombApi {
    @GET("games?format=json&filter=name:zelda&field_list=id,name,image,aliases,deck,original_release_date,platforms")
    fun getGames(): Deferred<GamesResponse>
  }

  private object AuthInterceptor : Interceptor {

    override fun intercept(chain: Chain): okhttp3.Response {
      val url: HttpUrl = chain.request().url().newBuilder()
          .addQueryParameter("api_key", API_KEY)
          .build()

      val request: Request = chain.request().newBuilder()
          .url(url)
          .build()

      return chain.proceed(request)
    }
  }
}
