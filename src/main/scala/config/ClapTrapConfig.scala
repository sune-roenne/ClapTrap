package config

import com.typesafe.config.{Config, ConfigFactory, ConfigParseOptions, ConfigResolveOptions}

import java.io.File

object ClapTrapConfig :
  private val localConfResolution = ConfigResolveOptions.defaults()
    .setAllowUnresolved(true)
  private val fallbackConfig = ConfigFactory.load("pekko.conf")
    .withFallback(ConfigFactory.load())
  private val localConfigFile = new File("./application.local.conf")

  val config : Config =
    (if localConfigFile.exists()
    then ConfigFactory.parseFile(localConfigFile).resolve()
    else ConfigFactory.load())
      .withFallback(fallbackConfig)

  object Server :
    private val serverConf = config.getConfig("server")
    val Port: Int = serverConf.getInt("port")
    object Https :
      private val httpsConf = serverConf.getConfig("https")
      object Certificate :
        private val certificateConf = httpsConf.getConfig("certificate")
        lazy val Path: String = certificateConf.getString("path")
        lazy val Password: String = certificateConf.getString("password")







