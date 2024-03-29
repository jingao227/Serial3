package Translation

import Message.Message
import StackNode.QListNode
import XPath.Pred
import akka.actor.ActorRef

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
/**
  * Created by Jing Ao on 2016/2/15.
  */
class PredPCYN(id: Int, label: String, father: TTNode) extends TTNode(id, label) {
  //override val nodeType = 11
  override def translate(preds: Pred, ttNodeIndex: mutable.Map[Int, TTNode]): Int = {
    println(this.id + ": " +  this.label + " ")
    ttNodeIndex += (this.id ->this)
    val cid: Int = id
    if (preds.step.preds.hasq1) {
      if (preds.step.preds.hasq2) {
        if (preds.step.preds.isPC) q1 = new PredPCYY(cid + 1, preds.step.preds.getTest)
        else q1 = new PredADYY(cid + 1, preds.step.preds.getTest)
      } else {
        if (preds.step.preds.isPC) q1 = new PredPCYN(cid + 1, preds.step.preds.getTest, null)
        else q1 = new PredADYN(cid + 1, preds.step.preds.getTest, null)
      }
    } else {
      if (preds.step.preds.hasq2) {
        if (preds.step.preds.isPC) q1 = new PredPCNY(cid + 1, preds.step.preds.getTest)
        else q1 = new PredADNY(cid + 1, preds.step.preds.getTest)
      } else {
        if (preds.step.preds.isPC) q1 = new PredPCNN(cid + 1, preds.step.preds.getTest, null)
        else q1 = new PredADNN(cid + 1, preds.step.preds.getTest, null)
      }
    }
    q1.translate(preds.step.preds, ttNodeIndex)
  }
  override def receiveQuery(sender: ActorRef, ttNodeID: Int, waitListID: Int, waitListNodeID: Int): Unit =
    rStack.push(new RStackNode(sender, ttNodeID, waitListID, waitListNodeID, false))

  override def receiveResult(qListNode: QListNode): scala.Boolean = {
    searchTrue(qListNode.getwaitList)
  }
  override def searchTrue(waitList: WaitList): scala.Boolean = {
    if (waitList.hasTrue) {
      receiveTrueforx1()
      waitList.clearWaitList()   // 谓词获得了一个true就不用再等别的true
      false                      // waitList已为空，没有要等待接收的结果
    } else {
      waitList.updateList()      // 淘汰过期查询
      if (!waitList.isEmpty) true else false  //  如果还有等待接收的结果(waitList中还有false)
    }
  }
  override def doMatch(toSend: scala.Boolean, qlistNode: QListNode, sendList: ListBuffer[Message], test: String,
                       qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]): Unit = {
    if (((father == null || father.rStack.isEmpty) && !rStack.top.getValue) || (father != null && father.rStack.nonEmpty && !rStack.top.getValue && !father.rStack.top.getValue)) {
      Map()
      qforx1 += new QListNode(q1, null)
      //redList += new QListNode(this, null)
      if (toSend) qlistNode.packMessage(q1.getID, 0, 0, sendList)
      redList += qlistNode
      qforx2 += qlistNode
      //if (!toSend) (0, 0, 0) else (q1.getID, 0, 0)
    } //else (0, 0, 0)
  }
  override def doNotMatch(toSend: scala.Boolean, qlistNode: QListNode, sendList: ListBuffer[Message], test: String,
                          qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]): Unit = {
    if (((father == null || father.rStack.isEmpty) && !rStack.top.getValue) || (father != null && father.rStack.nonEmpty && !rStack.top.getValue && !father.rStack.top.getValue)) {
      qforx2 += qlistNode
//      if (!toSend) qforx2 += new QListNode(this, null)
//      else qforx2 += new QListNode(this, waitList)
    }
    //(0, 0, 0)
  }
//  override def doStayWork(qListNode: QListNode, toStayList: ListBuffer[QListNode]): Unit = {
//    //  如果已输出true或者就根本没向远端请求过qforx1的结果，就不会需要留在栈中等结果
//    if (!alreadyTrue && qListNode.getwaitList != null) {
//      //  查找waitList中是否已接收到true消息
//      val searchResult: scala.Boolean = searchTrue(qListNode.getwaitList)
//      //  如果有true消息，即waitList已空，从waitLists中把waitList删除
//      if (searchResult) removeWaitLists(qListNode.getwaitList) // waitList从waitLists中删除
//      //  如果没有true消息，继续等待接收消息，即把这个qlistNode再压进栈
//      else toStayList += qListNode
//    }
//  }
  override def Map() = MapAllChild(q1)
  override def Reduce() = {
    val r = ReduceAllChild(q1)
    if (!rStack.top.getValue) {
//      rStack.pop()
//      rStack.push(r)
      rStack.top.setValue(r)
      sendOrElse()
    }
  }
}
