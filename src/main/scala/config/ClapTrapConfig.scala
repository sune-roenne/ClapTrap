package config

import com.typesafe.config.{ConfigFactory, ConfigParseOptions, ConfigResolveOptions}

object ClapTrapConfig :
  private val localConfResolution = ConfigResolveOptions.defaults()
    .setAllowUnresolved(true)
  private val config = ConfigFactory
    .load("conf/application.local.conf", ConfigParseOptions.defaults().setAllowMissing(true), localConfResolution)
    .withFallback(
       ConfigFactory.load("conf/application.conf")
    ).withFallback(ConfigFactory.load())

  object Server :
    private val serverConf = config.getConfig("server")
    object Https :
      private val httpsConf = serverConf.getConfig("https")
      object Certificate :
        private val certificateConf = httpsConf.getConfig("certificate")
        lazy val Path: String = certificateConf.getString("path")
        lazy val Password: String = certificateConf.getString("password")







