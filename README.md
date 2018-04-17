Robinhood API Client Library.

This library leverages Retrofit2, Gson and Kotlin to wrap the Unofficial Robinhood REST API.

Initial implementation for the Cryptocurrency endpoints is available. If there is interest the remaining endpoints could also be supported.

# Usage

Artifacts are published on maven:
```xml
    <dependency>
      <groupId>com.willwinder.robinhood4j</groupId>
      <artifactId>robinhood4j</artifactId>
      <version>0.1</version>
    </dependency>
```

API access is done through through the `ApiClient` object:
```java
  ApiClient client = new ApiClient(<login>, <password>, <cached login-token or null>);
  Call<Results<CryptoQuote>> call = client.getCryptoQuotes().getQuoteSymbols("ETHUSD");
  Response<Results<CryptoQuote>> response = call.execute();
  Results<CryptoQuote> result = response.body();
```

Example Kotlin code to make a limit purchase of 0.25 etherium at trading value of $600/ETH:
```kotlin
  val quote = apiClient.cryptoQuotes.getQuote("ETHUSD").execute().body()!!
  val pair = apiClient.crypto.getCurrencyPair(quote.id).execute().body()!!

  apiClient.crypto.makeOrder(RobinhoodCryptoApi.OrderParameters(
      OrderSide.SELL,
      pair.id,
      600.0,
      0.25,
      UUID.randomUUID().toString(),
      TimeInForce.GTC,
      OrderType.LIMIT
  ))
 ```

# Unit Tests
Due to the nature of this API unit tests cannot be run without configuring your
access credentials. Credentials must be stored as encrypted Base64 encoded
environment variables, all of the code which accesses these variables can be
seen in TestParent.kt, you are encouraged to thoroughly inspect this code.

Encrypted strings can be generated and setup as follows:

1. Navigate to ./src/test/kotlin/com/willwinder/robinhood4j/utils/TestParent.kt
2. Comment out the `init` section to prevent initializing a useless ApiClient.
3. Replace `LOGIN_HERE` and `PASSW_HERE` with your credentials.
4. Run the `generateEncryption` test.
5. Set the resulting 24 character strings as `robinhood_login` and `robinhood_password` environment variables.
6. Un-comment the `init` section. You should now be able to run tests.

