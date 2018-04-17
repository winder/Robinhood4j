Robinhood API Client Library.

This library leverages Retrofit2, Gson and Kotlin to wrap the Unofficial Robinhood REST API.


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

