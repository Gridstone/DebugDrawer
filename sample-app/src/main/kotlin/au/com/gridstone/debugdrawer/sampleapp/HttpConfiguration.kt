package au.com.gridstone.debugdrawer.sampleapp

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

object HttpConfiguration {

  private const val API_KEY = "8ca8e06cb445b8c055442108161848001a1c66fc"
  const val API_URL = "https://www.giantbomb.com/api/"

  val client: OkHttpClient = OkHttpClient.Builder()
      .addInterceptor(AuthInterceptor)
      .build()

  private object AuthInterceptor : Interceptor {

    override fun intercept(chain: Chain): Response {
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
