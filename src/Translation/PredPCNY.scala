package Translation

import XPath.Pred

import scala.collection.mutable.ListBuffer
/**
  * Created by Jing Ao on 2016/2/15.
  */
class PredPCNY(label: String) extends TTNode(label) {
  override def translate(preds: Pred): Unit = {
    println(" " + this.label + " ")
    if (preds.preds.hasq1) {
      if (preds.preds.hasq2) {
        if (preds.preds.isPC) q2 = new PredPCYY(preds.preds.getTest)
        else q2 = new PredADYY(preds.preds.getTest)
      } else {
        if (preds.preds.isPC) q2 = new PredPCYN(preds.preds.getTest, null)
        else q2 = new PredADYN(preds.preds.getTest, null)
      }
    } else {
      if (preds.preds.hasq2) {
        if (preds.preds.isPC) q2 = new PredPCNY(preds.preds.getTest)
        else q2 = new PredADNY(preds.preds.getTest)
      } else {
        if (preds.preds.isPC) q2 = new PredPCNN(preds.preds.getTest, null)
        else q2 = new PredADNN(preds.preds.getTest, null)
      }
    }
    q2.translate(preds.preds)
    q3 = new PredPCNN(this.label, this)
    q3.translate(new Pred(preds.step, null))
  }
  override def doMatch(test: String, qforx1: ListBuffer[TTNode], qforx2: ListBuffer[TTNode], redList: ListBuffer[TTNode]) = {
    //if (!rStack.top) {
    Map
    //}
    q2.doWork(test, qforx1, qforx2, redList)
  }
  override def doNotMatch(test: String, qforx1: ListBuffer[TTNode], qforx2: ListBuffer[TTNode], redList: ListBuffer[TTNode]) = {
    //if (!rStack.top) {
    //      qforx2.append(q3)
    qforx2 += q3
    //}
    q2.doWork(test, qforx1, qforx2, redList)
  }
  override def Map = {
    rStack.pop()
    rStack.push(true)
  }
  override def Reduce = {}    //  由于没有q1，所以只要match test就意味着已经得到true，不会Map，因此也就不需要Reduce
}
