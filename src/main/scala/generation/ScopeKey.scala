package generation

import org.bouncycastle.crypto.generators.*
import org.bouncycastle.crypto.params.{RSAKeyGenerationParameters, RSAKeyParameters}

import java.security.SecureRandom
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

object ScopeKey:
  def generateKeys() : Unit =
    val generator = new RSAKeyPairGenerator()

    val params = new RSAKeyGenerationParameters(
      BigInt.apply(65537).bigInteger, // public exponent
      SecureRandom.getInstance("SHA1PRNG"), // random generator
      2048, // strength
      90 // certainty
    )

    generator.init(params)

    val startTime = LocalDateTime.now()
    var keySize = 0
    for(i <- 0 to 100)
      val keyPair = generator.generateKeyPair()
      val privKey = keyPair.getPrivate.asInstanceOf[RSAKeyParameters]
      val thisKeySize = privKey.getExponent.bitCount()
      if thisKeySize > keySize
        then keySize = thisKeySize
    val endTime = LocalDateTime.now()
    println(s"Total time: ${startTime.until(endTime, ChronoUnit.SECONDS)} seconds. Bit count: ${keySize}")


