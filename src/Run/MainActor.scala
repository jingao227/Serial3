package Run

import Message.{Tag, Message}
import StackNode._
import Translation._
import XPath.{XPath, Path, Pred, Step}
import org.xml.sax.InputSource
import scala.collection.mutable
import scala.collection.mutable._
import akka.actor.{ActorRef, ReceiveTimeout, Actor, Props}
import scala.concurrent.duration._
import org.xml.sax.helpers.XMLReaderFactory
/**
  * Created by Jing Ao on 2016/2/15.
  */
class MainActor(query: Path) extends Actor{
  val ttNodeIndex = mutable.Map[Int, TTNode]()

  val root = translate(query, ttNodeIndex)
//  root.rStack.push(false)
  root.setOutput(true)

  val stack = new mutable.Stack[StackNode]
//  val originqList = new ListBuffer[QListNode]
//  originqList += new QListNode(root, null)
//  val originStackNode = new QList(stack, 1, originqList)
//  stack.push(originStackNode)

  val tagCache = new mutable.Queue[Tag]

  val testQueryList = List(new Message(0, 0, 0, 1, 0, 0))
  self ! testQueryList
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

  def translate (path: Path, ttNodeIndex: mutable.Map[Int, TTNode]): TTNode = {
    if (path.path == null) translate(path.step, ttNodeIndex) else {
      if (path.hasq1 && path.isPC) {
        val root = new PathPCY(1, path.getTest)
        root.translate(path, ttNodeIndex)
        root
      } else if (!path.hasq1 && path.isPC) {
        val root = new PathPCN(1, path.getTest)
        root.translate(path, ttNodeIndex)
        root
      } else if (path.hasq1 && !path.isPC) {
        val root = new PathADY(1, path.getTest)
        root.translate(path, ttNodeIndex)
        root
      } else {
        val root = new PathADN(1, path.getTest)
        root.translate(path, ttNodeIndex)
        root
      }
    }
  }

  def translate (step: Step, ttNodeIndex: mutable.Map[Int, TTNode]): TTNode = {
    if (step.hasq1 && step.isPC) {
      val root = new StepPCY(1, step.getTest)
      root.translate(step, ttNodeIndex)
      root
    } else if (step.hasq1 && step.isPC) {
      val root = new StepPCN(1, step.getTest)
      root.translate(step, ttNodeIndex)
      root
    } else if (step.hasq1 && step.isPC) {
      val root = new StepADY(1, step.getTest)
      root.translate(step, ttNodeIndex)
      root
    } else {
      val root = new StepADN(1, step.getTest)
      root.translate(step, ttNodeIndex)
      root
    }
  }

  def doQListWork (_type: Int, test: String, testRank: Int, send: scala.Boolean) = {
    println(_type + test + testRank)
    if (_type == 0) {
      if (testRank == stack.top.getRank) {
        val qforx1 = new ListBuffer[QListNode]
        val qforx2 = new ListBuffer[QListNode]
        val redList = new ListBuffer[QListNode]
        val sendList = new ListBuffer[Message]
        stack.pop().doEachWork(send, sendList, test, qforx1, qforx2, redList)
        if (send && sendList.nonEmpty) sendQuery(sendList.toList)
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

  def doTagWork (_type: Int, test: String, testRank: Int, send: scala.Boolean) = {
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
      tagCache += new Tag(_type, test, testRank, send)
      stack.pop.doStayWork(new ListBuffer[QListNode])
      while (stack.top.getRank == -1) {
        stack.pop().doEachReduce()
      }
      while (stack.top.getRank != -2 && tagCache.nonEmpty) {
        val tag: Tag = tagCache.dequeue()
        println("Dequeue:")
        doQListWork(tag.getType, tag.getTest, tag.getTestRank, tag.getSend)
      }
    } else doQListWork(_type, test, testRank, send)
  }

  def doResultWork(ttNodeID: Int, waitListID: Int, waitListNodeID: Int) = {
    val waitLists = ttNodeIndex(ttNodeID).waitLists
    //var found: scala.Boolean = false
    def findWaitList(): Unit = {
      for (element <- waitLists) {
        if (element.getID == waitListID) {
          element.receiveTrueForNode(waitListNodeID)
          //found = true
          return
        }
      }
    }
    findWaitList()
  }

  def doWorkIfNotEnd(_type: Int, test: String, testRank: Int, send: scala.Boolean) = {
    if (_type == 2) {
      while (stack.top.getRank == -2) stack.pop()   //  因为用SAX模拟时，已经到文档结束了就肯定不能再接到结果消息，而stay就是为了后面接到结果消息，所以压入stay就没有意义了，直接弹出
      while (stack.top.getRank == -1) stack.pop().doEachReduce()
      if (stack.top.getRank != -2) {
        println("Came to document end.")
        var tag: Tag = null
        while (tagCache.nonEmpty) {
          tag = tagCache.dequeue()
          println("Dequeue:")
          doTagWork(tag.getType, tag.getTest, tag.getTestRank, tag.getSend)
          while (stack.top.getRank == -2) stack.pop()
          while (stack.top.getRank == -1) stack.pop().doEachReduce()
        }
      }
    } else doTagWork(_type, test, testRank, send)
  }

  def startQuery(queryList: List[Message], sender: ActorRef) = {
    val qList = new QList(stack, 1, new ListBuffer[QListNode])
    for (element <- queryList) element.unpackMessage(ttNodeIndex, qList, sender)
    stack.push(qList)
  }

  def sendQuery(queryList: List[Message]) = {
    // TODO:获得对方Actor引用，! queryList
  }

  def receive = {
    case (_type: Int, test: String, testRank: Int, send: scala.Boolean) => {
      //doTagWork(_type, test, testRank)
      if (stack.nonEmpty) {
        doWorkIfNotEnd(_type, test, testRank, send)
      }
    }
    case (queryList: List[Message]) => {if (stack.isEmpty) startQuery(queryList, sender)}
    case (ttNodeID: Int, waitListID: Int, waitListNodeID: Int) => doResultWork(ttNodeID, waitListID, waitListNodeID)
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
  def props(query: Path) = Props(new MainActor(query))
}

