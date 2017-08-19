package com.mukilan.streamer.makFlows

import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler}
import akka.stream.{Attributes, Inlet, SinkShape}

/**
  * Created by mukilanashok on 8/10/17.
  */
class PrintSink extends GraphStage[SinkShape[String]]{

  val in: Inlet[String] = Inlet(getClass.getSimpleName)
  override def shape: SinkShape[String] = SinkShape(in)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) {

      override def preStart(): Unit = pull(in)

      setHandler(in, new InHandler {
        override def onPush(): Unit = {
          println(grab(in))
          pull(in)
        }
      })
    }
}
