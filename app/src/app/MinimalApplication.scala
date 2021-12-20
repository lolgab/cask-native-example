package app

object MinimalApplication extends cask.MainRoutes {
  @cask.get("/")
  def hello() = {
    "Hello World!"
  }

  @cask.post("/do-thing")
  def doThing(request: cask.Request) = {
    request.text().reverse
  }

  initialize()

  // This is needed because of a bug in Scala Native
  override def main(args: Array[String]): Unit = {
    val server = io.undertow.Undertow.builder
      .addHttpListener(port, host)
      .setHandler(defaultHandler)
      .build
    server.start()
  }
}
