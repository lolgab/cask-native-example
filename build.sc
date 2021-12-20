import mill._
import mill.scalalib._
import mill.scalanativelib._

trait Common extends ScalaModule {
  def scalaVersion = "2.13.7"
  def millSourcePath = super.millSourcePath / os.up
}

object app extends Module {
  object jvm extends Common {
    def ivyDeps = Agg(
      ivy"com.lihaoyi::cask:0.8.0"
    )
  }
  object native extends Common with ScalaNativeModule {
    val port = 8081

    def scalaNativeVersion = "0.4.2"

    def ivyDeps = Agg(
      ivy"com.github.lolgab::snunit-cask::0.0.12"
    )

    def deployApp() = {
      def doCurl(json: ujson.Value) = {
        os.proc(
          "curl",
          "-X",
          "PUT",
          "--data-binary",
          json.toString,
          "--unix-socket",
          "/usr/local/var/run/unit/control.sock",
          "http://localhost/config"
        ).call()
      }
      T.command {
        val binary = nativeLink()
        def unitConfig(numProcesses: Int) = {
          ujson.Obj(
            "applications" -> ujson.Obj(
              "app" -> ujson.Obj(
                "type" -> "external",
                "executable" -> binary.toString,
                "processes" -> numProcesses
              )
            ),
            "listeners" -> ujson.Obj(
              s"*:$port" -> ujson.Obj(
                "pass" -> s"applications/app"
              )
            )
          )
        }
        doCurl(unitConfig(numProcesses = 0))
        doCurl(unitConfig(numProcesses = 1))
      }
    }
  }
}
