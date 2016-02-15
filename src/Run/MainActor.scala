package Run

import StackNode._
import Translation.{WaitListNode, TTNode}
import org.xml.sax.InputSource
import scala.collection.mutable._
import akka.actor.{ReceiveTimeout, Actor, Props}
import scala.concurrent.duration._
import org.xml.sax.helpers.XMLReaderFactory
/**
  * Created by Jing Ao on 2016/2/15.
  */
class MainActor(root: TTNode) extends Actor{
  val stack = new Stack[StackNode]
  val originqList = new ListBuffer[QListNode]
  originqList += new QListNode(root, null)
  val originStackNode = new QList(stack, 1, originqList)
  stack.push(originStackNode)

  /*
  val parser = XMLReaderFactory.createXMLReader()
  val saxhandler = new SAXHandler(0, stack)

  System.setProperty("entityExpansionLimit", "3200000")
  parser.setContentHandler(saxhandler)

  println("Start Timing!")
  val startTime = System.currentTimeMillis()
  parser.parse(xmlURI)
  println("End Timing!")
  println("WorkTime: " + (System.currentTimeMillis() - startTime))
  */

  //context.setReceiveTimeout(100 millisecond)
  def receive = {
    case (_type: Int, test: String, testRank: Int) if _type == 0 => {
      if (testRank == stack.top.getRank) {
        val qforx1 = new ListBuffer[QListNode]
        val qforx2 = new ListBuffer[QListNode]
        val redList = new ListBuffer[QListNode]
        val sendList = new ListBuffer[WaitListNode]
        stack.pop().doEachWork(false, sendList, test, qforx1, qforx2, redList)
      }
    }
    case (_type: Int, test: String, testRank: Int) if _type == 1 => {
      if (testRank < stack.top.getRank) {
        stack.pop()
        while (stack.top.getRank == -1) {
          stack.pop().doEachReduce
        }
      }
    }
    //case _ =>
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
  def props(xmlURI: String, root: TTNode) = Props(new MainActor(root))
}

