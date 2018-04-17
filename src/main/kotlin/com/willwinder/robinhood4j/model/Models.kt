package com.willwinder.robinhood4j.model

import com.google.gson.annotations.SerializedName

data class Results<out T>(
    val next: String?,
    val previous: String?,
    val results: List<T>
)

enum class OrderSide {
  @SerializedName("buy")
  BUY,

  @SerializedName("sell")
  SELL
}

enum class OrderType {
  @SerializedName("limit")
  LIMIT,

  @SerializedName("market")
  MARKET
}

enum class OrderState {
  @SerializedName("queued")
  QUEUED,

  @SerializedName("unconfirmed")
  UNCONFIRMED,

  @SerializedName("confirmed")
  CONFIRMED,

  @SerializedName("partially_filled")
  PARTIALLY_FILLED,

  @SerializedName("filled")
  FILLED,

  @SerializedName("rejected")
  REJECTED,

  @SerializedName("canceled")
  CANCELED,

  @SerializedName("failed")
  FAILED
}

enum class Bounds {
  @SerializedName("24_7")
  TWENTY_FOUR_SEVEN,

  @SerializedName("regular")
  REGULAR,

  @SerializedName("extended")
  EXTENDED,

  @SerializedName("trading")
  TRADING
}

enum class Spans {
  @SerializedName("day")
  DAY,

  @SerializedName("hour")
  HOUR,

  @SerializedName("week")
  WEEK,

  @SerializedName("year")
  YEAR,

  @SerializedName("5year")
  FIVE_YEAR,

  @SerializedName("all")
  ALL
}

enum class Intervals {
  @SerializedName("15second")
  SECONDS_15,

  @SerializedName("5minute")
  MINUTES_5,

  @SerializedName("10minute")
  MINUTES_10,

  @SerializedName("hour")
  HOUR,

  @SerializedName("day")
  DAY,

  @SerializedName("week")
  WEEK
}

// TODO: What is this???
enum class TimeInForce {
  @SerializedName("gfd")
  GFD,

  @SerializedName("gtc")
  GTC,

  @SerializedName("ioc")
  IOC,

  @SerializedName("opg")
  OPG
}
