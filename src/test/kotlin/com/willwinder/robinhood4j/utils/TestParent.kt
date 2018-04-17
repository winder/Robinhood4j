package com.willwinder.robinhood4j.utils

import com.willwinder.robinhood4j.ApiClient
import org.apache.commons.codec.binary.Base64
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


@Disabled
open class RobinhoodTestParent {
  val apiClient: ApiClient

  init {
    apiClient = ApiClient(
        decrypt(System.getenv("robinhood_login")),
        decrypt(System.getenv("robinhood_password"))
    )
  }

  @Test
  fun validateCredentials() {
    Assertions.assertNotNull(System.getenv(env_login))
    Assertions.assertNotNull(System.getenv(env_pass))

    val login = decrypt(System.getenv(env_login))
    val passw = decrypt(System.getenv(env_pass))

    println("Login: $login")
    println("Passw: $passw")
  }

  @Test
  fun generateEncryption() {
    // 0. This should print "TestTheThing"
    println(""""${decrypt(encrypt("TestTheThing"))}"""")

    // 1. Put your login and password in here.
    println(encrypt("LOGIN_HERE"))
    println(encrypt("PASSW_HERE"))

    // 2. Take the result and store them in 'robinhood_login' and 'robinhood_password' ENV variables
    //    Do not quote the value
  }

  companion object {
    val env_login = "robinhood_login"
    val env_pass = "robinhood_password"
    val aesKey = SecretKeySpec("YourFavoritePass".toByteArray(), "AES")

    fun encrypt(valueToEnc: String): String {
      val c = Cipher.getInstance("AES")
      c.init(Cipher.ENCRYPT_MODE, aesKey)
      val encValue = c.doFinal(valueToEnc.toByteArray())
      //println("Encrypted value: ${String(encValue)}")
      return Base64.encodeBase64String(encValue)
    }

    fun decrypt(encryptedValue: String): String {
      val c = Cipher.getInstance("AES")
      c.init(Cipher.DECRYPT_MODE, aesKey)
      val encValue = Base64.decodeBase64(encryptedValue)
      //println("Encrypted value: ${String(encValue)}")
      val decryptedVal = c.doFinal(encValue)
      return String(decryptedVal)

    }

  }
}
