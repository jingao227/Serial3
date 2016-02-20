package Translation

import Message.Message
import StackNode.QListNode
import XPath.{Path, Step}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
/**
  * Created by Jing Ao on 2016/2/15.
  */
class StepADY(id: Int, label: String) extends TTNode(id, label) {
  //override val nodeType = 4
  override def translate(path: Path, ttNodeIndex: mutable.Map[Int, TTNode]): Int = translate(path.step, ttNodeIndex)
  override def translate(step: Step, ttNodeIndex: mutable.Map[Int, TTNode]): Int = {
    println(this.id + ": " +  this.label + " ")
    ttNodeIndex += (this.id ->this)
    val cid: Int = id
    if (step.preds.hasq1) {
      if (step.preds.hasq2) {
        if (step.preds.isPC) q1 = new PredPCYY(cid + 1, step.preds.getTest)
        else q1 = new PredADYY(cid + 1, step.preds.getTest)
      } else {
        if (step.preds.isPC) q1 = new PredPCYN(cid + 1, step.preds.getTest, null)
        else q1 = new PredADYN(cid + 1, step.preds.getTest, null)
      }
    } else {
      if (step.preds.hasq2) {
        if (step.preds.isPC) q1 = new PredPCNY(cid + 1, step.preds.getTest)
        else q1 = new PredADNY(cid + 1, step.preds.getTest)
      } else {
        if (step.preds.isPC) q1 = new PredPCNN(cid + 1, step.preds.getTest, null)
        else q1 = new PredADNN(cid + 1, step.preds.getTest, null)
      }
    }
    q1.translate(step.preds, ttNodeIndex)
  }
  override def getResult = {
    val r = rStack.pop()
    if (r && rStack.nonEmpty) {
      rStack.pop()
      rStack.push(true)
    }
    r
  }
  override def receiveResult(qListNode: QListNode): scala.Boolean = {
    remainReceiving(qListNode.getwaitList)  //  结果为false即waitList.isEmpty也不能从waitLists中remove这个waitList，因为还没遇到nil，这个waitList在后面x2的检查中还有可能再加入
  }
  override def remainReceiving(waitList: WaitList): scala.Boolean = {
    if (waitList.hasTrueAndDel) if (!alreadyTrue) receiveTrueforx1
    if (!waitList.isEmpty) waitList.updateList()
    if (!waitList.isEmpty) true else false  //  如果还有等待接收的结果(waitList中还有false)
  }
  override def doMatch(toSend: scala.Boolean, qlistNode: QListNode, sendList: ListBuffer[Message], test: String,
                       qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]): (Int, Int, Int) = {
    Map
    qforx1 += new QListNode(q1, null)
    qforx1 += new QListNode(this, null)
    //redList += new QListNode(this, null)
    redList += qlistNode
    qforx2 += qlistNode
    if (!toSend) (0, 0, 0) else (q1.getID, this.id, 0)
  }
  override def doNotMatch(toSend: scala.Boolean, qlistNode: QListNode, sendList: ListBuffer[Message], test: String,
                          qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]): (Int, Int, Int) = {
    qforx1 += new QListNode(this, null)
    qforx2 += qlistNode
    if (!toSend) (0, 0, 0) else (this.id, 0, 0)
  }
//  override def doStayWork(qListNode: QListNode, toStayList: ListBuffer[QListNode]): Unit = {
//    //  如果waitList已空或者就根本没向远端请求过qforx1的结果，就不会需要留在栈中等结果
//    if (qListNode.getwaitList != null && !qListNode.getwaitList.isEmpty) {
//      //  查找waitList中是否还有需要接收的消息
//      val toReceive: scala.Boolean = remainReceiving(qListNode.getwaitList)
//      //  如果waitList已为空，waitList从waitLists中删除
//      if (!toReceive) removeWaitLists(qListNode.getwaitList)
//      //  如果waitList不为空，继续等待接收消息，即把这个qlistNode再压进栈
//      else toStayList += qListNode
//    }
//  }
  override def Map() = MapAllChild(q1)
  override def Reduce() = {
    val r = ReduceAllChild(q1)
    if (!rStack.top) {
      rStack.pop()
      rStack.push(r)
    }
    if (output) println("Current result of " + this + " is " + rStack.top)
  }
}
