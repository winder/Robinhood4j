package com.willwinder.robinhood4j

import com.willwinder.robinhood4j.annotations.Authorization
import com.willwinder.robinhood4j.annotations.AuthorizationType
import com.willwinder.robinhood4j.annotations.BaseUrl
import com.willwinder.robinhood4j.model.Bounds
import com.willwinder.robinhood4j.model.Intervals
import com.willwinder.robinhood4j.model.Results
import com.willwinder.robinhood4j.model.Spans
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*


data class CryptoQuote (
    val ask_price: Double,
    val bid_price: Double,
    val mark_price: Double,
    val high_price: Double,
    val low_price: Double,
    val open_price: Double,
    val symbol: String,
    val id: String,
    val volume: Double
)

@Authorization(AuthorizationType.OAUTH2)
@BaseUrl("https://api.robinhood.com/")
interface RobinhoodCryptoQuotesApi {
  /**
   * @param ids comma separated list of ids
   */
  @GET("marketdata/forex/quotes/")
  fun getQuoteIds(
      @Query("ids") ids: String
  ): Call<Results<CryptoQuote>>

  /**
   * @param symbols comma separated list of symbols
   */
  @GET("marketdata/forex/quotes/")
  fun getQuoteSymbols(
      @Query("symbols") symbols: String
  ): Call<Results<CryptoQuote>>

  /**
   * @param symbol id or symbol for a pairing.
   */
  @GET("marketdata/forex/quotes/{symbol}/")
  fun getQuote(
      @Path("symbol") symbol: String
  ): Call<CryptoQuote>

  data class Historical (
      val data_points: List<DataPoint>,
      val bounds: Bounds,
      val interval: Intervals,
      val span: Spans,
      val symbol: String,
      val id: String,
      val open_price: Double?,
      val open_time: Date?,
      val previous_close_price: Double?,
      val previous_close_time: Date?
  ) {
    data class DataPoint(
        val begins_at: Date,
        val open_price: Double,
        val close_price: Double,
        val high_price: Double,
        val low_price: Double,
        val volume: Double,
        val session: String,
        val interpolated: Boolean
    )
  }
  @GET("marketdata/forex/historicals/{id-pair}/")
  fun getHistoricals(
      @Path("id-pair") idPair: String,
      @Query("bounds") bounds: Bounds,
      @Query("spans") spans: Spans,
      @Query("interval") interval: Intervals
  ): Call<Historical>
}
