package fix

import java.nio.file.Path

final case class GenerateReadmeConfig(
    workDir: Path = Path.of("target/generatereadme_"),
    resultDir: Path = Path.of("target/readme")
)

object GenerateReadmeConfig {
  val default = GenerateReadmeConfig()
  implicit val surface =
    metaconfig.generic.deriveSurface[GenerateReadmeConfig]
  implicit val decoder =
    metaconfig.generic.deriveDecoder(default)
}
