package Translation

/**
  * Created by Jing Ao on 2016/2/15.
  */

import Message.Message
import StackNode.QListNode
import XPath._
import akka.actor.ActorRef
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class PredADNY(id: Int, label: String) extends TTNode(id, label) {
  //override val nodeType = 14
  override def translate(preds: Pred, ttNodeIndex: mutable.Map[Int, TTNode]): Int = {
    println(this.id + ": " +  this.label + " ")
    ttNodeIndex += (this.id ->this)
    var cid: Int = id
    if (preds.preds.hasq1) {
      if (preds.preds.hasq2) {
        if (preds.preds.isPC) q2 = new PredPCYY(cid + 1, preds.preds.getTest)
        else q2 = new PredADYY(cid + 1, preds.preds.getTest)
      } else {
        if (preds.preds.isPC) q2 = new PredPCYN(cid + 1, preds.preds.getTest, null)
        else q2 = new PredADYN(cid + 1, preds.preds.getTest, null)
      }
    } else {
      if (preds.preds.hasq2) {
        if (preds.preds.isPC) q2 = new PredPCNY(cid + 1, preds.preds.getTest)
        else q2 = new PredADNY(cid + 1, preds.preds.getTest)
      } else {
        if (preds.preds.isPC) q2 = new PredPCNN(cid + 1, preds.preds.getTest, null)
        else q2 = new PredADNN(cid + 1, preds.preds.getTest, null)
      }
    }
    cid = q2.translate(preds.preds, ttNodeIndex)
    q3 = new PredADNN(cid + 1, this.label, this)
    q3.translate(new Pred(preds.step, null), ttNodeIndex)
  }
  override def getResult = {
    val r = rStack.pop().getValue
    if (r && rStack.nonEmpty) {
//      rStack.pop()
//      rStack.push(true)
      rStack.top.setValue(true)
      sendOrElse()
    }
    r
  }
  override def receiveQuery(sender: ActorRef, ttNodeID: Int, waitListID: Int, waitListNodeID: Int): Unit =
    receiveQueryToAllChild(this, sender, ttNodeID, waitListID, waitListNodeID)

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
    //if (!rStack.top) {
    Map()
    //}
    val newQLNode = new QListNode(q2, null)
    newQLNode.doWork(toSend, sendList, test, qforx1, qforx2, redList)
    //(0, 0, 0)
  }
  override def doNotMatch(toSend: scala.Boolean, qlistNode: QListNode, sendList: ListBuffer[Message], test: String,
                          qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]): Unit = {
    //if (!rStack.top) {
    qforx1 += new QListNode(q3, null)
//    if (toSend) qlistNode.packMessage(q3.getID, 0, 0, sendList)
//    qforx2 += new QListNode(q3, qlistNode.getwaitList)
    val newforq3 = new QListNode(q3, null)
    if (toSend) newforq3.packMessage(q3.getID, 0, 0, sendList)
    qforx2 += newforq3
    val newQLNode = new QListNode(q2, null)
    newQLNode.doWork(toSend, sendList, test, qforx1, qforx2, redList)
    //if (!toSend) (0, 0, 0) else (q3.getID, 0, 0)
//    if (!toSend) {
//      qforx2 += new QListNode(q3, null)
//      val newQLNode = new QListNode(q2, null)
//      newQLNode.doWork(toSend, sendList, test, qforx1, qforx2, redList)
//      (0, 0, 0)
//    } else {
//      qforx2 += new QListNode(q3, waitList)
//      val newQLNode = new QListNode(q2, null)
//      newQLNode.doWork(toSend, sendList, test, qforx1, qforx2, redList)
//      (q3.getID, 0, 0)
//    }
    //}
    //q2.doWork(test, qforx1, qforx2, redList)
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
  /* TODO:PredYY中的第一个Y结果为true时，还不能返回true，因为第二个Y及后续的所有Y（或q'''）的结果还不知。PredYY的结果应该是所有谓词的合并结果。
  def tryReduceAllChild(p: TTNode): scala.Boolean = {
    var r = p.rStack.top.getValue
    if (p.hasq2) {
      r = r | p.q3.rStack.top.getValue
      r = r & tryReduceAllChild(p.q2)
    }
    r
  }
  */
  override def Map() = {
//    rStack.pop()
//    rStack.push(true)
    rStack.top.setValue(true)
    sendOrElse()
  }
  override def Reduce() = {}    //  由于没有q1，所以只要match test就意味着已经得到true，不会Map，因此也就不需要Reduce
}
