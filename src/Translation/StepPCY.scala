package Translation

import XPath.{Path, Step}

import scala.collection.mutable.ListBuffer
/**
  * Created by Jing Ao on 2016/2/15.
  */
class StepPCY(label: String) extends TTNode(label) {
  override def translate(path: Path) = translate(path.step)
  override def translate(step: Step): Unit = {
    println(" " + this.label + " ")
    if (step.preds.hasq1) {
      if (step.preds.hasq2) {
        if (step.preds.isPC) q1 = new PredPCYY(step.preds.getTest)
        else q1 = new PredADYY(step.preds.getTest)
      } else {
        if (step.preds.isPC) q1 = new PredPCYN(step.preds.getTest, null)
        else q1 = new PredADYN(step.preds.getTest, null)
      }
    } else {
      if (step.preds.hasq2) {
        if (step.preds.isPC) q1 = new PredPCNY(step.preds.getTest)
        else q1 = new PredADNY(step.preds.getTest)
      } else {
        if (step.preds.isPC) q1 = new PredPCNN(step.preds.getTest, null)
        else q1 = new PredADNN(step.preds.getTest, null)
      }
    }
    q1.translate(step.preds)
  }
  override def doMatch(test: String, qforx1: ListBuffer[TTNode], qforx2: ListBuffer[TTNode], redList: ListBuffer[TTNode]) = {
    Map
    //    qforx1.append(q1)
    //    redList.append(this)
    //    qforx2.append(this)
    qforx1 += q1
    redList += this
    qforx2 += this
  }
  override def doNotMatch(test: String, qforx1: ListBuffer[TTNode], qforx2: ListBuffer[TTNode], redList: ListBuffer[TTNode]) = {
    //    qforx2.append(this)
    qforx2 += this
  }
  override def Map = MapAllChild(q1)
  override def Reduce = {
    val r = ReduceAllChild(q1)
    if (!rStack.top) {
      rStack.pop()
      rStack.push(r)
    }
    if (output) println("Current result of " + this + " is " + rStack.top)
  }
}
