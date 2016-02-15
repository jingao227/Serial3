package Run

import StackNode._
import Translation.TTNode
import org.xml.sax.InputSource
import scala.collection.mutable._
import akka.actor.{ReceiveTimeout, Actor, Props}
import scala.concurrent.duration._
import org.xml.sax.helpers.XMLReaderFactory
/**
  * Created by Jing Ao on 2016/2/15.
  */
class MainActor(xmlURI: String, root: TTNode) extends Actor{
  val stack = new Stack[StackNode]
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


  //context.setReceiveTimeout(100 millisecond)
  def receive = {
    case _ =>
    /*
    case (_type: Int, test: String, testRank: Int) => {
      //implicit val timeout = Timeout()
      //println("MainActor receive " + _type + test + " of " + testRank)
      stack.top match {
        case qs: QListNode => {
          if (_type == 0 && testRank == qs.getRank) {
            val qforx1 = new ListBuffer[TTNode]
            val qforx2 = new ListBuffer[TTNode]
            val redList = new ListBuffer[TTNode]
            stack.pop().doEachWork(test, qforx1, qforx2, redList)
          }
          else if (_type == 1 && testRank < stack.top.getRank) stack.pop()
          stack.top match {
            case _: ReduceNode => stack.pop().doEachReduce
            case _ =>
          }
        }
      }
    }
    case ReceiveTimeout => {
      println("End Timing!")
      println("WorkTime: " + (System.currentTimeMillis() - startTime))
      //context stop self
    }
    */
  }
}

object MainActor {
  def props(xmlURI: String, root: TTNode) = Props(new MainActor(xmlURI, root))
}

