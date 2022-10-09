/*
rule = GenerateReadme
GenerateReadme.workDir = target/generatereadme
 */
package fix

import akka.stream.scaladsl.GraphDSL
import akka.stream.FlowShape
import akka.stream.scaladsl.Flow
import akka.NotUsed

object SimpleGraph {
  val simple: Flow[Int, Int, NotUsed] = Flow.fromGraph(GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._
    val input = b.add(Flow.fromFunction(identity[Int]))
    val output = b.add(Flow.fromFunction((i: Int) => i + 2))
    input ~> output
    FlowShape(input.in, output.out)
  })
}
