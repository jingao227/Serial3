package Run

import Translation._
import XPath._
import org.xml.sax.helpers.XMLReaderFactory
import StackNode.{QListNode, StackNode}
import Translation.TTNode
import akka.actor.{ActorRef, Props, ActorSystem}
import scala.collection.mutable.{ListBuffer, Stack}
/**
  * Created by Jing Ao on 2016/2/15.
  */
object main {
  def translate (path: Path): TTNode = {
    if (path.hasq1 && path.isPC) {
      val root = new PathPCY(path.getTest)
      root.translate(path)
      root
    } else if (!path.hasq1 && path.isPC) {
      val root = new PathPCN(path.getTest)
      root.translate(path)
      root
    } else if (path.hasq1 && !path.isPC) {
      val root = new PathADY(path.getTest)
      root.translate(path)
      root
    } else {
      val root = new PathADN(path.getTest)
      root.translate(path)
      root
    }
  }

  def translate (step: Step): TTNode = {
    if (step.hasq1 && step.isPC) {
      val root = new StepPCY(step.getTest)
      root.translate(step)
      root
    } else if (step.hasq1 && step.isPC) {
      val root = new StepPCN(step.getTest)
      root.translate(step)
      root
    } else if (step.hasq1 && step.isPC) {
      val root = new StepADY(step.getTest)
      root.translate(step)
      root
    } else {
      val root = new StepADN(step.getTest)
      root.translate(step)
      root
    }
  }

  def main(args:Array[String]): Unit = {
    val xmlURI = "E:/Data/dblp/dblp.xml"
    //val xmlURI = "D:/Data/selfmade/books4.xml"
    //val xmlURI = "E:/Data/selfmade/books2.xml"
    /*  == Scala ==
    val source = new InputSource(xmlURI)
    System.setProperty("entityExpansionLimit", "3200000")
    val factory = SAXParserFactory.newInstance()
    val parser = factory.newSAXParser
    val handler1 = new SAXHandler
    //mainActor = system.actorOf(Props[MainActor](new MainActor(stack)), "mainActor")

    handler1.loadXML(source, parser)
    parser.parse(source, handler1)
    */
    //val parser = XMLReaderFactory.createXMLReader()

    val step1 = new Step(0, "author", null)             //  //author
    val step2 = new Step(0, "title", null)              //  /title
    val pred1 = new Pred(step2, null)                   //  [/title]
    val step3 = new Step(0, "article", pred1)     //  //inproceedings[/title]
    val path1 = new Path(step3, new Path(step1, null))  //  //inproceedings[/title]//author
    val step4 = new Step(0, "dblp", null)               //  //dblp
    val path = new Path(step4, path1)                   //  //dblp//inproceedings[/title]//author

    val root = translate(path)
    root.rStack.push(false)
    root.setOutput(true)

    val stack = new Stack[StackNode]                                //  在main中执行parse
    val originqList = new ListBuffer[TTNode]
    originqList += root
    val originStackNode = new QListNode(stack, 1, originqList)
    stack.push(originStackNode)

    val parser = XMLReaderFactory.createXMLReader()
    val saxhandler = new SAXHandler(0, stack)

    System.setProperty("entityExpansionLimit", "3200000")
    parser.setContentHandler(saxhandler)

    println("Start Timing!")
    val startTime = System.currentTimeMillis()
    parser.parse(xmlURI)
    println("End Timing!")
    println("WorkTime: " + (System.currentTimeMillis() - startTime))

    //val system = ActorSystem("mySystem")                           //  在MainActor中执行parse
    //val mainActor = system.actorOf(Props[MainActor](new MainActor(xmlURI, root)), "mainActor")
  }
}
