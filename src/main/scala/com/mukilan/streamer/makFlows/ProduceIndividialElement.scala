package com.mukilan.streamer.makFlows

import java.io.File

import akka.stream.{Attributes, FlowShape, Inlet, Outlet}
import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler, OutHandler}

import scala.io.Source

/**
  * Created by mukilanashok on 8/19/17.
  */
class ProduceIndividialElement extends GraphStage[FlowShape[List[String],String]]{

  val in: Inlet[List[String]] = Inlet("Inlet")
  val out: Outlet[String] = Outlet("Oulet")
  override def shape: FlowShape[List[String], String] = FlowShape(in,out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) {

      setHandler(in,new InHandler {
        override def onPush(): Unit = {
          val files = grab(in)
          emitMultiple(out,files)
        }
      })

      setHandler(out,new OutHandler {
        override def onPull(): Unit = {
          if(!hasBeenPulled(in)) {
            pull(in)
          }
        }

      })

    }
}

