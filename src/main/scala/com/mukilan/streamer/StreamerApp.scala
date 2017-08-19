package com.mukilan.streamer

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import com.mukilan.streamer.makFlows.{PrintSink, ProduceIndividialElement, StreamFile}

import scala.concurrent.duration._
/**
  * Created by mukilanashok on 8/10/17.
  */
object StreamerApp {

  implicit val system = ActorSystem("Streaming-Application")
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  def main(args: Array[String]): Unit = {
    throttle(List("build.sbt","test.txt"))
  }

  def throttle(files:List[String]): Unit = {
    val source = Source.tick(0 second ,5 seconds,files)
    val produceFiles = Flow.fromGraph(new ProduceIndividialElement)
    val streamFile = Flow.fromGraph(new StreamFile(10))
    val sink = Sink.fromGraph(new PrintSink)
    source via  produceFiles via streamFile runWith   sink
  }

}
