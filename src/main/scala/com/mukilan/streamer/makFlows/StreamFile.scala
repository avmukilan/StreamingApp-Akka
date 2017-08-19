package com.mukilan.streamer.makFlows

import java.io.File

import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler, OutHandler}
import akka.stream.{Attributes, FlowShape, Inlet, Outlet}
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.write

import scala.io.Source

/**
  * Created by mukilanashok on 8/10/17.
  */
class StreamFile(batchSize:Integer) extends GraphStage[FlowShape[String,String]]{

  case class Data(filName: String,lines:List[String])
  implicit val formats = DefaultFormats
  val in: Inlet[String] = Inlet("Inlet")
  val out: Outlet[String] = Outlet("Oulet")
  override def shape: FlowShape[String, String] = FlowShape(in,out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) {

      setHandler(in,new InHandler {
        override def onPush(): Unit = {
          val file = new File(grab(in))
          Source.fromFile(file).getLines().sliding(batchSize,batchSize).map(lines =>
            emit(out,write(Data(file.getAbsolutePath,lines.toList))))
          emit(out, file.getAbsoluteFile + ",END")
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
