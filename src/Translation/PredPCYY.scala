package Translation

import StackNode.QListNode
import XPath.Pred

import scala.collection.mutable.ListBuffer
/**
  * Created by Jing Ao on 2016/2/15.
  */
class PredPCYY(id: Int, label: String) extends TTNode(id, label) {
  override def translate(preds: Pred): Int = {
    println(this.id + ": " +  this.label + " ")
    var cid: Int = id
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
    cid = q1.translate(preds.step.preds)

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
    cid = q2.translate(preds.preds)
    q3 = new PredPCYN(cid + 1, this.label, this)
    q3.translate(new Pred(preds.step, null))
  }
  override def doMatch(toSend: scala.Boolean, qlistNode: QListNode, sendList: ListBuffer[WaitListNode], test: String,
                       qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]): (Int, Int, Int) = {
    //if (!rStack.top) {
    Map
    qforx1 += new QListNode(q1, null)
    //redList += new QListNode(this, null)    //  只在match且压入q'''(x2)时做Reduce
    redList += qlistNode
    qforx2 += new QListNode(q3, qlistNode.getwaitList)
    val newQLNode = new QListNode(q2, null)
    newQLNode.doWork(toSend, sendList, test, qforx1, qforx2, redList)
    if (!toSend) (0, 0, 0) else (q1.getID, 0, 0)
//    if (!toSend) {
//      qforx2 += new QListNode(q3, null)
//      val newQLNode = new QListNode(q2, null)
//      newQLNode.doWork(toSend, sendList, test, qforx1, qforx2, redList)
//      (0, 0, 0)
//    } else {
//      qforx2 += new QListNode(q3, waitList)
//      val newQLNode = new QListNode(q2, null)
//      newQLNode.doWork(toSend, sendList, test, qforx1, qforx2, redList)
//      (q1.getID, 0, 0)
//    }
    //}
    //q2.doWork(test, qforx1, qforx2, redList)
  }
  override def doNotMatch(toSend: scala.Boolean, qlistNode: QListNode, sendList: ListBuffer[WaitListNode], test: String,
                          qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]): (Int, Int, Int) = {
    //if (!rStack.top) {
//    if (!toSend) qforx2 += new QListNode(q3, null)
//    else qforx2 += new QListNode(q3, waitList)
    qforx2 += new QListNode(q3, qlistNode.getwaitList)
    val newQLNode = new QListNode(q2, null)
    newQLNode.doWork(toSend, sendList, test, qforx1, qforx2, redList)
    (0, 0, 0)
    //}
    //q2.doWork(test, qforx1, qforx2, redList)
  }
  override def Map = MapAllChild(q1)
  override def Reduce = {
    val r = ReduceAllChild(q1)
    if (!rStack.top) {
      rStack.pop()
      rStack.push(r)
    }
  }
}
