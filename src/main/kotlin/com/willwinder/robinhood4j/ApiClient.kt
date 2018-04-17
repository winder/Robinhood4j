package com.willwinder.robinhood4j

import com.google.common.base.Stopwatch
import com.willwinder.robinhood4j.annotations.Authorization
import com.willwinder.robinhood4j.annotations.AuthorizationType
import com.willwinder.robinhood4j.annotations.BaseUrl
import com.willwinder.robinhood4j.retrofit.EnumRetrofitConverterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Multiple retrofit objects are used to organize code, and allow APIs with multiple base-urls. This
 * class wraps those objects and facilitates their initialization and configuration.
 *
 * Pattern from: https://gist.github.com/imminent/3a0855030aa93b355317
 */
class ApiClient(
    username: String,
    password: String,
    token: String? = null
  ) {
  val auth: RobinhoodAuthApi
  val cryptoQuotes: RobinhoodCryptoQuotesApi
  val crypto: RobinhoodCryptoApi

  /**
   * 1. Create http client map (requires authorization API).
   * 2. Create remaining APIs by inspecting annotations.
   */
  init {
    /**
     * Build the retrofit API configured with BaseUrl/Authorization annotations.
     */
    fun <T> buildApi(clazz: Class<T>, clients: Map<AuthorizationType, OkHttpClient>): T {
      // Inspect annotations
      val type = clazz.getDeclaredAnnotation(Authorization::class.java).auth
      val url = clazz.getDeclaredAnnotation(BaseUrl::class.java).url

      // Build api
      return Retrofit.Builder()
          .addConverterFactory(GsonConverterFactory.create())
          .addConverterFactory(EnumRetrofitConverterFactory())
          //.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
          .baseUrl(url)
          .client(clients[type])
          .build()
          .create(clazz)
    }

    // Create clients (auth must be built early to get a token)
    val noAuthClient = OkHttpClient.Builder().build()

    auth = buildApi(RobinhoodAuthApi::class.java, mapOf(AuthorizationType.NO_AUTH to noAuthClient))

    val newToken = getToken(token, username, password, auth)
    val tokenClient = OkHttpClient.Builder()
        .addInterceptor(TokenInterceptor(newToken))
        .build()

    val oAuthClient = OkHttpClient.Builder()
        .addInterceptor(OAuth2Interceptor(newToken, auth))
        .build()

    val clients:Map<AuthorizationType,OkHttpClient> = mapOf(
        AuthorizationType.NO_AUTH to noAuthClient,
        AuthorizationType.TOKEN to tokenClient,
        AuthorizationType.OAUTH2 to oAuthClient
    )

    // Build retrofit objects
    cryptoQuotes = buildApi(RobinhoodCryptoQuotesApi::class.java, clients)
    crypto = buildApi(RobinhoodCryptoApi::class.java, clients)
  }

  /**
   * Use the auth api to grab a valid token.
   */
  private fun getToken(
      optToken: String?,
      username: String,
      password: String,
      auth: RobinhoodAuthApi
  ) : String {
    return if (optToken != null) {
      optToken
    } else {
      val tokens = auth.apiTokenAuth(username, password).execute()
      val response = tokens.body()
          ?: throw RuntimeException("No respone from authentication")

      if (response.mfa_required != null)
        throw RuntimeException("MFA Apps Must Provide Token")

      if (response.token == null)
        throw RuntimeException("No token returned")

      response.token
    }
  }

  /**
   * Inject static authorization token into the request.
   */
  private class TokenInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
      val newRequest = chain.request().newBuilder()
          .addHeader("Authorization", "Token $token")
          .build()
      return chain.proceed(newRequest)
    }
  }

  /**
   * Inject oauth authorization token into request, refreshing as necessary.
   * TODO: Use the refresh token
   */
  private class OAuth2Interceptor(
      private val token: String,
      private val auth: RobinhoodAuthApi
  ) : Interceptor {
    private var bearer = OAuth2Token("", "", 0, "", "")
    private var updated = Stopwatch.createStarted()

    init {
      updateBearerToken(token, true)
    }

    /**
     * Helper to update the bearer token if it has expired.
     * // TODO: is expires_in seconds or minutes?
     */
    private fun updateBearerToken(authToken:String, force: Boolean = false) {
      // Update
      if (force || updated.elapsed(TimeUnit.SECONDS) > bearer.expires_in) {
        val bearerTokenResponse = auth.toOAuth2("Token $authToken").execute()
        val bearerToken = bearerTokenResponse.body()

        if (!bearerTokenResponse.isSuccessful || bearerToken == null)
          throw RuntimeException("Unable to get OAuth2 token.")

        bearer = bearerToken
        updated = Stopwatch.createStarted()
      }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
      updateBearerToken(token, true)
      val newRequest = chain.request().newBuilder()
          .addHeader("Authorization", "Bearer ${bearer.access_token}")
          .build()
      return chain.proceed(newRequest)
    }
  }
}
