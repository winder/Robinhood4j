package com.willwinder.robinhood4j

import com.google.gson.annotations.SerializedName
import com.willwinder.robinhood4j.annotations.Authorization
import com.willwinder.robinhood4j.annotations.AuthorizationType
import com.willwinder.robinhood4j.annotations.BaseUrl
import com.willwinder.robinhood4j.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.*

@Authorization(AuthorizationType.OAUTH2)
@BaseUrl("https://nummus.robinhood.com/")
interface RobinhoodCryptoApi {

  data class CurrencyPair(
      val asset_currency: Currency,
      val display_only: Boolean,
      val id: String,
      val max_order_size: Double,
      val min_order_size: Double,
      val min_order_price_increment: Double,
      val min_order_quantity_increment: Double,
      val name: String,
      val quote_currency: Currency,
      val symbol: String,
      val tradability: Tradeable
  ) {
    enum class Tradeable(val tradable: Boolean) {
      @SerializedName("tradable")
      TRADEABLE(true),

      @SerializedName("untradable")
      UNTRADEABLE(false),
    }

    data class Currency(
        val code: String,
        val id: String,
        val increment: String,
        val name: String,
        val type: String
    )
  }

  /**
   * List the different crypto currency pairs. These IDs are necessary
   */
  @GET("currency_pairs/")
  fun getCurrencyPairs(): Call<Results<CurrencyPair>>

  @GET("currency_pairs/{pair-id}/")
  fun getCurrencyPair(
      @Path("pair-id") currencyPair: String
  ): Call<CurrencyPair>

  /**
   * Endpoint service notices.
   */
  @GET("halts/")
  fun getHalts(): Call<Results<Any>>

  /**
   * Data about crypto activation.
   */
  @GET("activations/")
  fun getActivations(): Call<Results<Any>>

  @GET("watchlists/")
  fun getWatchlist(): Call<Results<Any>>

  @GET("holdings/")
  fun getHoldings(): Call<Results<Any>>

  ////////////////
  // Portfolios //
  ////////////////

  data class Portfolio(
      val id: String,
      val account_id: String,
      val equity: Double,
      val extended_hours_equity: Double,
      val market_value: Double,
      val extended_hours_market_value: Double,
      val previous_close: Double,
      val updated_at: Date
  )
  @GET("portfolios/")
  fun getPortfolios(): Call<Results<Portfolio>>

  @GET("portfolios/{id}/")
  fun getPortfolio(
      @Path("id") portfolioId: String
  ): Call<Portfolio>

  ////////////
  // Orders //
  ////////////

  data class Order(
      val account_id: String,
      val cancel_url: String,
      val created_at: Date,
      val cumulative_quantity: Double,
      val currency_pair_id: String,
      val executions: List<Execution>,
      val id: String,
      val last_transaction_at: Date,
      val price: Double,
      val quantity: Double,
      val ref_id: String,
      val side: OrderSide,
      val state: OrderState,
      val time_in_force: String,
      val type: OrderType,
      val updated_at: Date
  ) {
    data class Execution(
        val effective_price: String,
        val id: String,
        val quantity: Double,
        val timestamp: Date
    )
  }

  @GET("orders/")
  fun getOrders(): Call<Results<Order>>

  @GET("orders/{order-id}")
  fun getOrder(
      @Path("order-id") orderId: String
  ): Call<Order>

  @POST("orders/{order-id}/cancel/")
  fun cancelOrder(
      @Path("order-id") orderId: String
  ): Call<Any>

  data class OrderParameters(
      val side: OrderSide,
      val currency_pair_id: String,
      val price: Double,
      val quantity: Double,
      val ref_id: String,
      val time_in_force: TimeInForce,
      val type: OrderType
  )

  data class OrderResponse(
      val account_id: String,
      val cancel_url: String,
      val created_at: Date,
      val cumulative_quantity: Double,
      val currency_pair_id: String,
      val executions: List<Any>,
      val id: String,
      val last_transaction_at: String?,
      val price: Double,
      val quantity: Double,
      val ref_id: String,
      val side: OrderSide,
      val state: OrderState,
      val time_in_force: TimeInForce,
      val type: OrderType,
      val updated_at: Date
  )

  @POST("orders/")
  fun makeOrder(
      @Body parameters: OrderParameters
  ): Call<OrderResponse>
}
