package Run

import Translation._
import XPath._
import org.xml.sax.helpers.XMLReaderFactory
import StackNode.{QList, QListNode, StackNode}
import Translation.TTNode
import akka.actor.{ActorRef, Props, ActorSystem}
import scala.collection.mutable
import scala.collection.mutable.{ListBuffer, Stack, Map}
/**
  * Created by Jing Ao on 2016/2/15.
  */
object main {
//  def translate (path: Path, ttNodeIndex: mutable.Map[Int, TTNode]): TTNode = {
//    if (path.hasq1 && path.isPC) {
//      val root = new PathPCY(1, path.getTest)
//      root.translate(path, ttNodeIndex)
//      root
//    } else if (!path.hasq1 && path.isPC) {
//      val root = new PathPCN(1, path.getTest)
//      root.translate(path, ttNodeIndex)
//      root
//    } else if (path.hasq1 && !path.isPC) {
//      val root = new PathADY(1, path.getTest)
//      root.translate(path, ttNodeIndex)
//      root
//    } else {
//      val root = new PathADN(1, path.getTest)
//      root.translate(path, ttNodeIndex)
//      root
//    }
//  }
//
//  def translate (step: Step, ttNodeIndex: mutable.Map[Int, TTNode]): TTNode = {
//    if (step.hasq1 && step.isPC) {
//      val root = new StepPCY(1, step.getTest)
//      root.translate(step, ttNodeIndex)
//      root
//    } else if (step.hasq1 && step.isPC) {
//      val root = new StepPCN(1, step.getTest)
//      root.translate(step, ttNodeIndex)
//      root
//    } else if (step.hasq1 && step.isPC) {
//      val root = new StepADY(1, step.getTest)
//      root.translate(step, ttNodeIndex)
//      root
//    } else {
//      val root = new StepADN(1, step.getTest)
//      root.translate(step, ttNodeIndex)
//      root
//    }
//  }

  def main(args:Array[String]): Unit = {
    //val xmlURI = "D:/Data/dblp/dblp.xml"
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
//    val ttNodeIndex = mutable.Map[Int, TTNode]()

//    val step1 = new Step(1, "TITLE", null)             //  //author
//    val step2 = new Step(1, "AUTHOR", null)              //  /title
//    //val step5 = new Step(0, "ISBN", null)             //  /author
//    //val pred2 = new Pred(step5, null)                   //  [/author]
//
//    val pred1 = new Pred(step2, null)                  //  [/title][/author]
//    val step3 = new Step(1, "ITEM", pred1)           //  //article[/title][/author]
//    val path1 = new Path(step3, new Path(step1, null))  //  //article[/title][/author]//author
//    val step4 = new Step(1, "dblp", null)               //  //dblp
//    val path = new Path(step4, path1)                   //  //dblp//article[/title][/author]//author
//                                                        //  /BOOKS/ITEM[AUTHOR]/TITLE
//                                                        //  若根节点一直未匹配过，则根节点不会做Reduce操作，所以不会有输出（因为输出操作在Reduce中）
    val step1 = new Step(1, "AUTHOR", null)               //  //author
    val step5 = new Step(1, "ISBN", null)                 //  //ISBN
    val pred2 = new Pred(step5, null)                     //  [//ISBN]
    val step2 = new Step(1, "TITLE", null)                //  //TITLE
    val pred1 = new Pred(step2, pred2)                     //  [//TITLE][//ISBN]
    val step3 = new Step(1, "ITEM", pred1)                //  //inproceedings[/title]
    val path1 = new Path(step3, new Path(step1, null))    //  //inproceedings[/title]//author
    val step4 = new Step(1, "BOOKS", null)                //  //dblp
    val query = new Path(step4, path1)                     //  //dblp//inproceedings[/title]//author
                                                          //  //BOOKS//ITEM[//TITLE][//ISBN]//AUTHOR

//    val root = translate(path, ttNodeIndex)
//    root.rStack.push(false)
//    root.setOutput(true)

//    val stack = new Stack[StackNode]                                //  在main中执行parse
//    val originqList = new ListBuffer[QListNode]
//    originqList += new QListNode(root, null)
//    val originStackNode = new QList(stack, 1, originqList)
//    stack.push(originStackNode)
//
//    val parser = XMLReaderFactory.createXMLReader()
//    val saxhandler = new SAXHandler(0, stack)
//
//    System.setProperty("entityExpansionLimit", "3200000")
//    parser.setContentHandler(saxhandler)
//
//    println("Start Timing!")
//    val startTime = System.currentTimeMillis()
//    parser.parse(xmlURI)
//    println("End Timing!")
//    println("WorkTime: " + (System.currentTimeMillis() - startTime))

    //val system = ActorSystem("mySystem")                           //  在MainActor中执行parse
    //val mainActor = system.actorOf(Props[MainActor](new MainActor(stack)), "mainActor")


    val system = ActorSystem("mySystem")                            //   标签作为消息传递给Actor而不是在Actor中解析
    val mainActor = system.actorOf(Props[MainActor](new MainActor(query)), "mainActor")
    val saxActor = system.actorOf(Props[SAXActor](new SAXActor(mainActor)), "SAXActor")

//    val parser = XMLReaderFactory.createXMLReader()
//    val saxHandler = new SAXHandler(0, mainActor)
//    System.setProperty("entityExpansionLimit", "3200000")
//    parser.setContentHandler(saxHandler)
//
//    println("Start Timing!")
//    val startTime = System.currentTimeMillis()
//    parser.parse(xmlURI)
//    println("End Timing!")
//    println("WorkTime: " + (System.currentTimeMillis() - startTime))
  }
}
