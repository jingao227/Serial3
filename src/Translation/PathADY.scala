package Translation

/**
  * Created by Jing Ao on 2016/2/15.
  */

import Message.Message
import XPath._
import StackNode.QListNode
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class PathADY(id: Int, label: String) extends TTNode(id, label) {
  //override val nodeType = 8
  override def translate(path: Path, ttNodeIndex: mutable.Map[Int, TTNode]): Int = {
    println(this.id + ": " +  this.label + " ")
    ttNodeIndex += (this.id ->this)
    var cid: Int = id
    if(path.step.preds.hasq1) {
      if(path.step.preds.hasq2) {
        if(path.step.preds.isPC) q1 = new PredPCYY(cid + 1, path.step.preds.getTest)
        else q1 = new PredADYY(cid + 1, path.step.preds.getTest)
      } else {
        if (path.step.preds.isPC) q1 = new PredPCYN(cid + 1, path.step.preds.getTest, null)
        else q1 = new PredADYN(cid + 1, path.step.preds.getTest, null)
      }
    } else {
      if(path.step.preds.hasq2) {
        if(path.step.preds.isPC) q1 = new PredPCNY(cid + 1, path.step.preds.getTest)
        else q1 = new PredADNY(cid + 1, path.step.preds.getTest)
      } else {
        if (path.step.preds.isPC) q1 = new PredPCNN(cid + 1, path.step.preds.getTest, null)
        else q1 = new PredADNN(cid + 1, path.step.preds.getTest, null)
      }
    }
    cid = q1.translate(path.step.preds, ttNodeIndex)

    if(path.path.hasq1) {
      if(path.path.isPC) {
        if(path.q2ispath) q2 = new PathPCY(cid + 1, path.path.getTest)
        else q2 = new StepPCY(cid + 1, path.path.getTest)
      } else {
        if(path.q2ispath) q2 = new PathADY(cid + 1, path.path.getTest)
        else q2 = new StepADY(cid + 1, path.path.getTest)
      }
    } else {
      if(path.path.isPC) {
        if(path.q2ispath) q2 = new PathPCN(cid + 1, path.path.getTest)
        else q2 = new StepPCN(cid + 1, path.path.getTest)
      } else {
        if(path.q2ispath) q2 = new PathADN(cid + 1, path.path.getTest)
        else q2 = new StepADN(cid + 1, path.path.getTest)
      }
    }
    q2.translate(path.path, ttNodeIndex)
  }
  override def getResult = {
    val r = rStack.pop().getValue
    if (r && rStack.nonEmpty) {
//      rStack.pop()
//      rStack.push(true)
      rStack.top.setValue(true)
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
                       qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]): Unit = {
    Map()
    qforx1 += new QListNode(q1, null)
    qforx1 += new QListNode(q2, null)
    qforx1 += new QListNode(this, null)
    //redList += new QListNode(this, null)
    if (toSend) qlistNode.packMessage(q1.getID, q2.getID, this.id, sendList)
    redList += qlistNode
    qforx2 += qlistNode
    //if (!toSend) (0, 0, 0) else (q1.getID, q2.getID, this.id)
  }
  override def doNotMatch(toSend: scala.Boolean, qlistNode: QListNode, sendList: ListBuffer[Message], test: String,
                          qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]): Unit = {
    qforx1 += new QListNode(this, null)
    if (toSend) qlistNode.packMessage(this.id, 0, 0, sendList)
    qforx2 += qlistNode
    //if (!toSend) (0, 0, 0) else (this.id, 0, 0)
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
  override def Map() = {MapAllChild(q1) ; q2.rStack.push(new RStackNode(null, 0, 0, 0, false))}
  override def Reduce() = {
    val r = ReduceAllChild(q1) & q2.getResult
    if (!rStack.top.getValue) {
//      rStack.pop()
//      rStack.push(r)
      rStack.top.setValue(r)
    }
    if (output) println("Current result of " + this + " is " + rStack.top.getValue)
  }
}
