package fix

import akka.stream.scaladsl._
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.FlowShape
import akka.NotUsed

object Scalafixmyrules {
  implicit val system: ActorSystem = ActorSystem("dummy")
  implicit val mat: ActorMaterializer = ActorMaterializer()
  // Add code that needs fixing here.
  val src = Source.fromIterator(() => List(2, 41, 42).iterator)
  val flow = Flow.fromFunction((a: Int) => a / 2)
  src.via(flow).to(Sink.seq).run()
  val other: Flow[Int, Int, NotUsed] = Flow.fromFunction(_ / 2)
  val graph: Flow[Int, Int, NotUsed] = Flow.fromGraph(GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._
    val divide = b.add(flow)
    val multiply = b.add(Flow.fromFunction((i: Int) => i * 2))
    divide ~> SimpleGraph.simple ~> multiply
    FlowShape(divide.in, multiply.out)
  })
  src.via(graph).to(Sink.seq).run()
}
