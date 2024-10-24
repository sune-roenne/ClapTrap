import generation.ScopeKey

object Launcher
  @main def main() : Unit =
    ClapTrapPekkoServer.startServer()
    //ScopeKey.generateKeys()

