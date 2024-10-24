package config

import com.typesafe.config.{Config, ConfigFactory, ConfigParseOptions, ConfigResolveOptions}

import java.io.File

object ClapTrapConfig :
  private val localConfResolution = ConfigResolveOptions.defaults()
    .setAllowUnresolved(true)
  private val localConfigFile = new File("./application.local.conf")
  val config : Config =
    (if localConfigFile.exists()
    then ConfigFactory.parseFile(localConfigFile)
          .withFallback(ConfigFactory.load())
    else ConfigFactory.load())
      .withFallback(ConfigFactory.load("pekko.conf"))
  object Server :
    private val serverConf = config.getConfig("server")
    object Https :
      private val httpsConf = serverConf.getConfig("https")
      object Certificate :
        private val certificateConf = httpsConf.getConfig("certificate")
        lazy val Path: String = certificateConf.getString("path")
        lazy val Password: String = certificateConf.getString("password")







