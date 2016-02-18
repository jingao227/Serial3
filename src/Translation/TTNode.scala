package Translation

/**
  * Created by Jing Ao on 2016/2/15.
  */
import Message.Message
import XPath._
import StackNode.QListNode
import scala.collection.mutable._

class TTNode(id: Int, label: String) {
  val rStack = new Stack[scala.Boolean]
  val waitLists = new ListBuffer[WaitList]
  var listSize: Int = 0
//  val nodeType: Int = ???

  var q1: TTNode= null
  var q2: TTNode = null
  var q3: TTNode = null

  var output = false

  def translate(path: Path): Int = ???
  def translate(preds: Pred): Int = ???
  def translate(step: Step): Int = ???

  def getSize = listSize
  def getID = id
//  def getNodeType = nodeType
  def setOutput(o: scala.Boolean) = output = o

  def addWaitLists(): WaitList = {
    val newlist = new WaitList(listSize + 1)
    waitLists += newlist
    newlist
  }
  def removeWaitLists(waitList: WaitList) = {
    for (element <- waitLists) waitLists.remove(waitLists.indexOf(waitList))
  }

  def receiveTrueforx1 = if (!rStack.top) { rStack.pop() ; rStack.push(true) }
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
              qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]): (Int, Int, Int) = ???
  def doNotMatch(toSend: scala.Boolean, qlistNode: QListNode, sendList: ListBuffer[Message], test: String,
                 qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]): (Int, Int, Int) = ???
  def doWork(toSend: scala.Boolean, qlistNode: QListNode, sendList: ListBuffer[Message], test: String,
             qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]): (Int, Int, Int) = {
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
  def getResult = rStack.pop()
  def alreadyTrue = rStack.top

  def Map(): Unit = ???
  def Reduce(): Unit = ???
  def MapAllChild(p: TTNode): Unit = {
    //var p = c
    p.rStack.push(false)
    if (p.hasq2) {
      p.q3.rStack.push(false)
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
