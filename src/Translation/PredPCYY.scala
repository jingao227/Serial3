package Translation

import XPath.Pred

import scala.collection.mutable.ListBuffer
/**
  * Created by Jing Ao on 2016/2/15.
  */
class PredPCYY(label: String) extends TTNode(label) {
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
    q3 = new PredPCYN(this.label, this)
    q3.translate(new Pred(preds.step, null))
  }
  override def doMatch(test: String, qforx1: ListBuffer[TTNode], qforx2: ListBuffer[TTNode], redList: ListBuffer[TTNode]) = {
    //if (!rStack.top) {
    Map
    //      qforx1.append(q1)
    //      redList.append(this)    //  只在match且压入q'''(x2)时做Reduce
    //      qforx2.append(q3)
    qforx1 += q1
    redList += this
    qforx2 += q3
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
  override def Map = MapAllChild(q1)
  override def Reduce = {
    val r = ReduceAllChild(q1)
    if (!rStack.top) {
      rStack.pop()
      rStack.push(r)
    }
  }
}
