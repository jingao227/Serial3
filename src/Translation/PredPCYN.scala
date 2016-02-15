package Translation

import XPath.Pred

import scala.collection.mutable.ListBuffer
/**
  * Created by Jing Ao on 2016/2/15.
  */
class PredPCYN(label: String, father: TTNode) extends TTNode(label) {
  override def translate(preds: Pred): Unit = {
    println(" " + this.label + " ")
    if (preds.step.preds.hasq1) {
      if (preds.step.preds.hasq2) {
        if (preds.step.preds.isPC) q1 = new PredPCYY(preds.step.preds.getTest)
        else q1 = new PredADYY(preds.step.preds.getTest)
      } else {
        if (preds.step.preds.isPC) q1 = new PredPCYN(preds.step.preds.getTest, null)
        else q1 = new PredADYN(preds.step.preds.getTest, null)
      }
    } else {
      if (preds.step.preds.hasq2) {
        if (preds.step.preds.isPC) q1 = new PredPCNY(preds.step.preds.getTest)
        else q1 = new PredADNY(preds.step.preds.getTest)
      } else {
        if (preds.step.preds.isPC) q1 = new PredPCNN(preds.step.preds.getTest, null)
        else q1 = new PredADNN(preds.step.preds.getTest, null)
      }
    }
    q1.translate(preds.step.preds)
  }
  override def doMatch(test: String, qforx1: ListBuffer[TTNode], qforx2: ListBuffer[TTNode], redList: ListBuffer[TTNode]) = {
    if ((father == null && !rStack.top) || (father != null && !rStack.top && !father.rStack.top)) {
      Map
      //      qforx1.append(q1)
      //      redList.append(this)
      //      qforx2.append(this)
      qforx1 += q1
      redList += this
      qforx2 += this
    }
  }
  override def doNotMatch(test: String, qforx1: ListBuffer[TTNode], qforx2: ListBuffer[TTNode], redList: ListBuffer[TTNode]) = {
    if ((father == null && !rStack.top) || (father != null && !rStack.top && !father.rStack.top)) {
      //      qforx2.append(this)
      qforx2 += this
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
