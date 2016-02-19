package Run

import Message.{Tag, Message}
import StackNode._
import Translation.{WaitListNode, TTNode}
import org.xml.sax.InputSource
import scala.collection.mutable
import scala.collection.mutable._
import akka.actor.{ReceiveTimeout, Actor, Props}
import scala.concurrent.duration._
import org.xml.sax.helpers.XMLReaderFactory
/**
  * Created by Jing Ao on 2016/2/15.
  */
class MainActor(root: TTNode) extends Actor{
  val stack = new mutable.Stack[StackNode]
  val originqList = new ListBuffer[QListNode]
  originqList += new QListNode(root, null)
  val originStackNode = new QList(stack, 1, originqList)
  stack.push(originStackNode)

  val tagCache = new mutable.Queue[Tag]
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
  def doQListWork (_type: Int, test: String, testRank: Int) = {
    println(_type + test + testRank)
    if (_type == 0) {
      if (testRank == stack.top.getRank) {
        val qforx1 = new ListBuffer[QListNode]
        val qforx2 = new ListBuffer[QListNode]
        val redList = new ListBuffer[QListNode]
        val sendList = new ListBuffer[Message]
        stack.pop().doEachWork(toSend = true, sendList, test, qforx1, qforx2, redList)
      }
    } else {
      if (testRank < stack.top.getRank) {
        stack.pop().doStayWork(new ListBuffer[QListNode])
        while (stack.top.getRank == -1) {
          stack.pop().doEachReduce()
        }
      }
    }
  }

  def doTagWork (_type: Int, test: String, testRank: Int) = {
//    if (stack.top.getRank == -2) {
//      tagCache += new Tag(_type, test, testRank)
//      stack.pop.doStayWork(new ListBuffer[QListNode])
//      while (stack.top.getRank == -1) {
//        stack.pop().doEachReduce()
//      }
//    } else {
//      while (stack.top.getRank != -2 && tagCache.nonEmpty) {
//        // 从tagCache取出一个标签给doQListWork
//        val tag: Tag = tagCache.dequeue()
//        doQListWork(tag.getType, tag.getTest, tag.getTestRank)
//      }
//      //if (stack.top.getRank == -2 && tagCache.nonEmpty) {} // (_type: Int, test: String, testRank: Int)加入队尾
//      //if (stack.top.getRank == -2 && tagCache.isEmpty) {} // (_type: Int, test: String, testRank: Int)加入队尾
//      if (stack.top.getRank == -2) tagCache += new Tag(_type, test, testRank) // (_type: Int, test: String, testRank: Int)加入队尾
//      else if (tagCache.isEmpty) doQListWork(_type, test, testRank)
//      while (stack.top.getRank == -1) {
//        stack.pop().doEachReduce()
//      }
//    }
    if (stack.top.getRank == -2) {
      tagCache += new Tag(_type, test, testRank)
      stack.pop.doStayWork(new ListBuffer[QListNode])
      while (stack.top.getRank == -1) {
        stack.pop().doEachReduce()
      }
      while (stack.top.getRank != -2 && tagCache.nonEmpty) {
        val tag: Tag = tagCache.dequeue()
        doQListWork(tag.getType, tag.getTest, tag.getTestRank)
      }
    } else doQListWork(_type, test, testRank)
  }

  def doResultWork() = {}

  def doWorkIfNotEnd(_type: Int, test: String, testRank: Int) = {
    if (_type == 2) {
      while (stack.top.getRank == -2) stack.pop()   //  因为用SAX模拟时，已经到文档结束了就肯定不能再接到结果消息，而stay就是为了后面接到结果消息，所以压入stay就没有意义了，直接弹出
      while (stack.top.getRank == -1) stack.pop().doEachReduce()
      if (stack.top.getRank != -2) {
        println("Came to document end.")
        var tag: Tag = null
        while (tagCache.nonEmpty) {
          tag = tagCache.dequeue()
          doTagWork(tag.getType, tag.getTest, tag.getTestRank)
          while (stack.top.getRank == -2) stack.pop()
          while (stack.top.getRank == -1) stack.pop().doEachReduce()
        }
      }
    } else doTagWork(_type, test, testRank)
  }

  def receive = {
    case (_type: Int, test: String, testRank: Int) => {
      //doTagWork(_type, test, testRank)
      doWorkIfNotEnd(_type, test, testRank)
    }
    /*
                                                      {
      if (_type == 0) {
        if (testRank == stack.top.getRank) {
          val qforx1 = new ListBuffer[QListNode]
          val qforx2 = new ListBuffer[QListNode]
          val redList = new ListBuffer[QListNode]
          val sendList = new ListBuffer[Message]
          stack.pop().doEachWork(false, sendList, test, qforx1, qforx2, redList)
        }
      } else {
        if (testRank < stack.top.getRank) {
          stack.pop()
          while (stack.top.getRank == -1) {
            stack.pop().doEachReduce
          }
        }
      }
    }
    */
    /*
    case (_type: Int, test: String, testRank: Int) if _type == 1 => {
      if (testRank < stack.top.getRank) {
        stack.pop()
        while (stack.top.getRank == -1) {
          stack.pop().doEachReduce
        }
      }
    }
    */
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
  def props(root: TTNode) = Props(new MainActor(root))
}

