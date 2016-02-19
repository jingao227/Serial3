package Run

import Translation.TTNode
import akka.actor.{ActorRef, Actor, Props}
import org.xml.sax.helpers.XMLReaderFactory

/**
  * Created by aojing on 2016/2/17.
  */
class SAXActor(mainActor: ActorRef) extends Actor {
  //val xmlURI = "E:/Data/dblp/dblp.xml"
  val xmlURI = "E:/Data/selfmade/books2.xml"
  val parser = XMLReaderFactory.createXMLReader()
  val saxHandler = new SAXHandler(0, mainActor)
  System.setProperty("entityExpansionLimit", "3200000")
  parser.setContentHandler(saxHandler)

  println("Start Timing!")
  val startTime = System.currentTimeMillis()
  parser.parse(xmlURI)
  println("End Timing!")
  println("WorkTime: " + (System.currentTimeMillis() - startTime))

  def receive = {
    case _ =>
  }
}

object SAXActor {
  def props(mainActor: ActorRef) = Props(new SAXActor(mainActor))
}