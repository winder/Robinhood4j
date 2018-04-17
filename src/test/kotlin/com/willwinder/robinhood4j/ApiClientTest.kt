package com.willwinder.robinhood4j

import com.willwinder.robinhood4j.utils.RobinhoodTestParent
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@Disabled
class ApiClientTest : RobinhoodTestParent() {
  @Test
  fun test() {
    val call = apiClient.cryptoQuotes.getQuote("ETHUSD")
    //val call = apiClient.crypto.getQuote("3d961844-d360-45fc-989b-f6fca761d511")
    //val call = apiClient.crypto.getQuoteIds("76637d50-c702-4ed1-bcb5-5b0732a81f48")
    //val call = apiClient.crypto.getQuoteSymbols("ETHUSD,BTCUSD")
    val response = call.execute()
    val data = response.body()
    println(data)
  }
}
