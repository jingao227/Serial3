package Translation

import StackNode.QListNode
import XPath.Pred

import scala.collection.mutable.ListBuffer
/**
  * Created by Jing Ao on 2016/2/15.
  */
class PredPCYN(id: Int, label: String, father: TTNode) extends TTNode(id, label) {
  override def translate(preds: Pred): Int = {
    println(this.id + ": " +  this.label + " ")
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
    q1.translate(preds.step.preds)
  }
  override def doMatch(test: String, qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]) = {
    if ((father == null && !rStack.top) || (father != null && !rStack.top && !father.rStack.top)) {
      Map
      //      qforx1.append(q1)
      //      redList.append(this)
      //      qforx2.append(this)
      qforx1 += new QListNode(q1, null)
      redList += new QListNode(this, null)
      qforx2 += new QListNode(this, null)
    }
  }
  override def doNotMatch(test: String, qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]) = {
    if ((father == null && !rStack.top) || (father != null && !rStack.top && !father.rStack.top)) {
      //      qforx2.append(this)
      qforx2 += new QListNode(this, null)
    }
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
