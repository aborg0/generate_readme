package fix

import scalafix.v1._
import scala.meta._
import java.io.File
import metaconfig.Configured
import metaconfig.generic._

class GenerateReadme(config: GenerateReadmeConfig) extends SemanticRule("GenerateReadme") {
  def this() = this(GenerateReadmeConfig.default)

  // TODO read the previously saved graphs in workdir

 override def withConfiguration(config: Configuration): Configured[Rule] =
     config.conf
       .getOrElse("GenerateReadme")(this.config)
       .map { newConfig => new GenerateReadme(newConfig) }
  override def fix(implicit doc: SemanticDocument): Patch = {
    println(doc.input.asInstanceOf[Input.VirtualFile].path)
    config.workDir.toFile().mkdirs()
    config.resultDir.toFile().mkdirs()
    val file = new File(config.workDir.toFile(), doc.input.asInstanceOf[Input.VirtualFile].path.replaceAll("/", "_"))
    println(file.getAbsoluteFile())

    // println("Tree.syntax: " + doc.tree.syntax)
    // println("Tree.structure: " + doc.tree.structure)
    // println("Tree.structureLabeled: " + doc.tree.structureLabeled)
    doc.tree.collect {
      case via @ Term.Name("via")      => println(via.symbol.info)
        // println(via.symbol.info.get.isDef)
        // println(via.symbol.info.get.owner.value)
        println(via.traverse{case t => println(t.parent -> t.parent.get.parent.get.children)})
      case to @ Term.Name("to")      => println(to.symbol.info)
        // println(to.symbol.info.get.isDef)
        // println(to.symbol.info.get.owner.value)
        // println(to.parent.get.parent.get.children)
        println(to.parent.get.children)
        // println(to.traverse{case t => println(t.parent -> t.parent.get.parent.get.children)})
      case arrow @ Term.Name("~>")     =>
        println(arrow.symbol.info)
        println(arrow.traverse{case t => println(t.parent -> t.children)})
      case flow @ Term.Name("Flow")    => println(flow.symbol.info)
      case dummy @ Term.Name("simple") => println(dummy.symbol.info)
      case t: Term.Name if t.symbol.info.exists(_.isVal) =>
        println(t -> t.symbol.info.map(_.signature.getClass()))
      case t: Term.Name if t.symbol.info.exists(_.isDef) => println(t -> t.symbol.info.map(_.signature))
    }
    Patch.empty
  }

}
