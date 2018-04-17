package com.willwinder.robinhood4j

import com.willwinder.robinhood4j.model.Bounds
import com.willwinder.robinhood4j.model.Intervals
import com.willwinder.robinhood4j.model.Spans
import com.willwinder.robinhood4j.utils.RobinhoodTestParent
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import retrofit2.Call

@Disabled
class CryptoQuotesTest : RobinhoodTestParent() {

  private fun <T> call(call: Call<T>) {
    val response = call.execute()
    val data = response.body()
    println(data)
  }

  @Test
  fun historicals() {
    call(apiClient.cryptoQuotes.getHistoricals(
        idPair = "3d961844-d360-45fc-989b-f6fca761d511",
        spans = Spans.DAY,
        bounds = Bounds.TWENTY_FOUR_SEVEN,
        interval = Intervals.DAY
    ))
  }
}
