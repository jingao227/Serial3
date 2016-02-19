package Run

import Message.Message
import Translation.{WaitListNode, TTNode}
import org.xml.sax.Attributes
import akka.actor.{ActorRef}
import scala.collection.mutable._
import StackNode._
/**
  * Created by Jing Ao on 2016/2/15.
  */
class SAXHandler(var rank: Int, mainActor: ActorRef) extends scala.xml.parsing.FactoryAdapter { // stack: Stack[StackNode], mainActor: ActorRef
  //var mainActor: ActorRef = null
  /*
  def startParse(root: TTNode) = {
    /*
    val stack = new Stack[StackNode]
    val originqList = new ListBuffer[TTNode]
    originqList += root
    val originStackNode = new QListNode(stack, 1, originqList)
    stack.push(originStackNode)

    val system = ActorSystem("mySystem")
    */
    val source = new org.xml.sax.InputSource(xmlURI)

    System.setProperty("entityExpansionLimit", "3200000")
    val factory = SAXParserFactory.newInstance();
    val parser = factory.newSAXParser();

    mainActor = system.actorOf(Props[MainActor](new MainActor(stack)), "mainActor")

    this.loadXML(source, parser)
    parser.parse(source, this)
  }
  */
  override def startDocument() = {}
  override def startElement(uri: String, _localName: String, qname: String, attributes: Attributes): Unit = {
    rank = rank + 1
    //println("<" + qname + "> of " + rank)
    mainActor ! (0, qname, rank)

//    if (rank == stack.top.getRank) {
//      val qforx1 = new ListBuffer[QListNode]
//      val qforx2 = new ListBuffer[QListNode]
//      val redList = new ListBuffer[QListNode]
//      val sendList = new ListBuffer[Message]
//      stack.pop().doEachWork(false, sendList, qname, qforx1, qforx2, redList)
//    }

  }

  override def endElement(uri: String, _localName: String, qname: String): Unit = {
    //println("</" + qname + "> of " + rank)
    mainActor ! (1, qname, rank)

//    if (rank < stack.top.getRank) {
//      stack.pop()
//      //      stack.top match {
//      //        case _: ReduceNode => stack.pop().doEachReduce
//      //        case _ =>
//      //      }
//      while (stack.top.getRank == -1) {
//        stack.pop().doEachReduce
//      }
//    }

    rank = rank - 1
  }

  override def endDocument() = mainActor ! (2, "EndDocument", 0)
  def createNode(pre: String,elemName: String,attribs: scala.xml.MetaData,scope: scala.xml.NamespaceBinding,chIter: List[scala.xml.Node]): scala.xml.Node = ???
  def createProcInstr(target: String,data: String): Seq[scala.xml.ProcInstr] = ???
  def createText(text: String): scala.xml.Text = ???
  def nodeContainsText(localName: String): Boolean = ???
}
