package com.willwinder.robinhood4j

import com.willwinder.robinhood4j.model.OrderSide
import com.willwinder.robinhood4j.model.OrderType
import com.willwinder.robinhood4j.model.TimeInForce
import com.willwinder.robinhood4j.utils.RobinhoodTestParent
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import retrofit2.Call
import java.util.*

class CryptoApiTest : RobinhoodTestParent() {
  private fun <T> call(call: Call<T>) {
    val response = call.execute()
    Assertions.assertTrue(response.isSuccessful)
    val data = response.body()
    println(data)
  }

  @Test
  fun currencyPairs() {
    call(apiClient.crypto.getCurrencyPairs())
    //call(apiClient.crypto.getQuote("3d961844-d360-45fc-989b-f6fca761d511"))
    //call(apiClient.crypto.getQuoteIds("76637d50-c702-4ed1-bcb5-5b0732a81f48"))
    //call(apiClient.crypto.getQuoteSymbols("ETHUSD,BTCUSD"))
  }

  @Test
  fun currencyPair() {
    call(apiClient.crypto.getCurrencyPair("3d961844-d360-45fc-989b-f6fca761d511"))
  }

  @Test
  fun halts() {
    call(apiClient.crypto.getHalts())
  }

  @Test
  fun activations() {
    call(apiClient.crypto.getActivations())
  }

  @Test
  fun watchlist() {
    call(apiClient.crypto.getWatchlist())
  }

  @Test
  fun holdings() {
    call(apiClient.crypto.getHoldings())
  }

  @Test
  fun portfolios() {
    call(apiClient.crypto.getPortfolios())
  }

  @Test
  fun portfolio() {
    val portfolio = apiClient.crypto.getPortfolios().execute().body()!!.results[0]
    call(apiClient.crypto.getPortfolio(portfolio.id))
  }

  @Test
  fun orders() {
    call(apiClient.crypto.getOrders())
  }

  @Test
  fun order() {
    val order = apiClient.crypto.getOrders().execute().body()!!.results[0]
    call(apiClient.crypto.getOrder(order.id))
  }

  @Test
  fun cancelOrder() {
    val order = apiClient.crypto.getOrders().execute().body()!!.results[0]
    call(apiClient.crypto.cancelOrder(order.id))
  }

  @Test
  fun makeOrder() {
    val quote = apiClient.cryptoQuotes.getQuote("ETHUSD").execute().body()!!
    val pair = apiClient.crypto.getCurrencyPair(quote.id).execute().body()!!

    call(apiClient.crypto.makeOrder(RobinhoodCryptoApi.OrderParameters(
        OrderSide.SELL,
        pair.id,
        600.0,
        0.123109,
        UUID.randomUUID().toString(),
        TimeInForce.GTC,
        OrderType.LIMIT
    )))
  }
}
