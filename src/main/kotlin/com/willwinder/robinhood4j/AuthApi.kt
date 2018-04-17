package com.willwinder.robinhood4j

import com.willwinder.robinhood4j.annotations.Authorization
import com.willwinder.robinhood4j.annotations.AuthorizationType
import com.willwinder.robinhood4j.annotations.BaseUrl
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

data class Token(
    val token: String?,
    val mfa_type: String?,
    val mfa_required: Boolean?
)

data class OAuth2Token(
    val access_token: String,
    val scope: String,
    val expires_in: Int,
    val refresh_token: String,
    val token_type: String
)

@Authorization(AuthorizationType.NO_AUTH)
@BaseUrl("https://api.robinhood.com/")
interface RobinhoodAuthApi {

  /**
   * Creates an authentication token with Multi-Factor Authentication (MFA).
   */
  @FormUrlEncoded
  @POST("api-token-auth/")
  fun apiTokenAuth(
      @Field("username") username: String,
      @Field("password") password: String,
      @Field("mfa_code") mfa_code: String): Call<Token>

  /**
   * Creates an authentication token. If Multi-Factor Authentication (MFA) is enabled [mfa_type]
   * and [mfa_required] will be set, and your MFA tool will be notified.
   */
  @FormUrlEncoded
  @POST("api-token-auth/")
  fun apiTokenAuth(
      @Field("username") username: String,
      @Field("password") password: String): Call<Token>

  /**
   */
  @POST("oauth2/migrate_token/")
  fun toOAuth2(
      @Header("Authorization") authHeader: String
  ): Call<OAuth2Token>
}
