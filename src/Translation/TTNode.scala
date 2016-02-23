package Translation

/**
  * Created by Jing Ao on 2016/2/15.
  */
import Message.Message
import Run.MainActor
import XPath._
import StackNode.QListNode
import akka.actor.ActorRef
import scala.collection.mutable
import scala.collection.mutable.{Stack, Map, ListBuffer}

class TTNode(id: Int, label: String) {
  //val rStack = new mutable.Stack[scala.Boolean]
  val rStack = new mutable.Stack[RStackNode]
  val waitLists = new ListBuffer[WaitList]
  var listSize: Int = 0
//  val nodeType: Int = ???

  var q1: TTNode= null
  var q2: TTNode = null
  var q3: TTNode = null

  var output = false

  def translate(path: Path, ttNodeIndex: mutable.Map[Int, TTNode]): Int = ???
  def translate(preds: Pred, ttNodeIndex: mutable.Map[Int, TTNode]): Int = ???
  def translate(step: Step, ttNodeIndex: mutable.Map[Int, TTNode]): Int = ???

  def getSize = listSize
  def getID = id
//  def getNodeType = nodeType
  def setOutput(o: scala.Boolean) = output = o

//  def packMessage (qa: Int, qb: Int, qc: Int, waitList: WaitList, sendList: ListBuffer[Message]): WaitList = {
//    if (waitList == null) {
//      val newWaitList = addWaitLists()
//      sendList += new Message(this.id, newWaitList.getID, 1, qa, qb, qc)
//      newWaitList
//    } else {
//      val waitListNodeID: Int = waitList.addWaitList()
//      sendList += new Message(this.id, waitList.getID, waitListNodeID, qa, qb, qc)
//      waitList
//    }
//  }
  def addWaitLists(): WaitList = {
    val newlist = new WaitList(listSize + 1)
    waitLists += newlist
    listSize = listSize + 1
    newlist
  }
  def removeWaitLists(waitList: WaitList) = {
    //val index: Int = waitLists.indexOf(waitList)
    //for (element <- waitLists) {
    waitLists.remove(waitLists.indexOf(waitList))
    //}
  }

//  def receiveTrueforx1 = if (!rStack.top) { rStack.pop() ; rStack.push(true) }
  // 所有rStack.top.setValue(true)时如果有sender，向sender发送结果元组（ttNodeID,waitListID,waitListNodeID）
  def sendOrElse() = {
    val topNode: RStackNode = rStack.top
    if (topNode.getSender != null && topNode.getDes != (0, 0, 0)) topNode.getSender ! topNode.getDes
    //  TODO:是否需要pop？
  }
  def receiveQuery(sender: ActorRef, ttNodeID: Int, waitListID: Int, waitListNodeID: Int): Unit = ???
  def receiveQueryToAllChild(p: TTNode, sender: ActorRef, ttNodeID: Int, waitListID: Int, waitListNodeID: Int): Unit = {
    p.rStack.push(new RStackNode(sender, ttNodeID, waitListID, waitListNodeID, false))
    if (p.hasq2) {
      //      p.q3.rStack.push(false)
//      p.q3.rStack.push(new RStackNode(sender, ttNodeID, waitListID, waitListNodeID, false))
//      receiveQueryToAllChild(p.q2, sender, ttNodeID, waitListID, waitListNodeID)
      p.q3.rStack.push(new RStackNode(null, 0, 0, 0, false))
      MapAllChild(p.q2)
    }
  }
  def receiveTrueforx1() = if (!rStack.top.getValue) {
    rStack.top.setValue(true)
    sendOrElse()
  }
  def receiveResult(qListNode: QListNode): scala.Boolean = ???  //  返回值代表waitList是否为Empty，不是null
  def searchTrue(waitList: WaitList): scala.Boolean = ???
  def remainReceiving(waitList: WaitList): scala.Boolean = ???
//  def packMessage (qa: Int, qb: Int, qc: Int, sendList: ListBuffer[Message], qListNode: QListNode) = {
//    //val waitList: WaitList = addWaitLists
//    //waitList = addWaitLists
//    //if (waitList == null) sendList += new Message(this.id, waitList.getID,)
//    qListNode.getwaitList = addWaitLists
//
//  }
  def doMatch(toSend: scala.Boolean, qlistNode: QListNode, sendList: ListBuffer[Message], test: String,
              qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]): Unit = ???
  def doNotMatch(toSend: scala.Boolean, qlistNode: QListNode, sendList: ListBuffer[Message], test: String,
                 qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]): Unit = ???
  def doWork(toSend: scala.Boolean, qlistNode: QListNode, sendList: ListBuffer[Message], test: String,
             qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]): Unit = {
    if (qlistNode.getwaitList != null && !qlistNode.getwaitList.isEmpty) receiveResult(qlistNode)
    if (test == label) doMatch(toSend, qlistNode, sendList, test, qforx1, qforx2, redList)
    else doNotMatch(toSend, qlistNode, sendList, test, qforx1, qforx2, redList)
  }
  def doStayWork(qListNode: QListNode, toStayList: ListBuffer[QListNode]): Unit = {
    if (qListNode.getwaitList != null && !qListNode.getwaitList.isEmpty) {
      // 返回值代表是否还有待接收的结果
      if (!receiveResult(qListNode)) removeWaitLists(qListNode.getwaitList)
      else toStayList += qListNode
    }
  }

  def hasq2 = if (q2 != null) true else false
  def getResult = rStack.pop().getValue
  def alreadyTrue = rStack.top.getValue

  def Map(): Unit = ???
  def Reduce(): Unit = ???
  def MapAllChild(p: TTNode): Unit = {
    //var p = c
//  p.rStack.push(false)
    p.rStack.push(new RStackNode(null, 0, 0, 0, false))
    if (p.hasq2) {
//      p.q3.rStack.push(false)
      p.q3.rStack.push(new RStackNode(null, 0, 0, 0, false))
      MapAllChild(p.q2)
    }
    /*    while (p.hasq2) {
          p.q3.rStack.push(false)
          p.q2.rStack.push(false)
          p = p.q2
        }*/
  }
  def ReduceAllChild(p: TTNode): scala.Boolean = {
    var r = p.getResult
    if (p.hasq2) {
      r = r | p.q3.getResult
      r = r & ReduceAllChild(p.q2)
    }
    r
  }
}
