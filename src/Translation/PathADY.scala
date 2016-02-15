package Translation

/**
  * Created by Jing Ao on 2016/2/15.
  */
import XPath._
import StackNode.QListNode
import scala.collection.mutable.ListBuffer

class PathADY(id: Int, label: String) extends TTNode(id, label) {
  override def translate(path: Path): Int = {
    println(this.id + ": " +  this.label + " ")
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
    cid = q1.translate(path.step.preds)

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
    q2.translate(path.path)
  }
  override def getResult = {
    val r = rStack.pop()
    if (r && rStack.nonEmpty) {
      rStack.pop()
      rStack.push(true)
    }
    r
  }
  override def doMatch(toSend: scala.Boolean, waitList: WaitList, sendList: ListBuffer[WaitListNode], test: String,
                       qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]): (Int, Int, Int) = {
    Map
    //    qforx1.append(q1)
    //    qforx1.append(q2)
    //    qforx1.append(this)
    //    redList.append(this)
    //    qforx2.append(this)
    qforx1 += new QListNode(q1, null)
    qforx1 += new QListNode(q2, null)
    qforx1 += new QListNode(this, null)
    redList += new QListNode(this, null)
    if (!toSend) {
      qforx2 += new QListNode(this, null)
      (0, 0, 0)
    } else {
      qforx2 += new QListNode(this, waitList)
      (q1.getID, q2.getID, this.id)
    }
  }
  override def doNotMatch(toSend: scala.Boolean, waitList: WaitList, sendList: ListBuffer[WaitListNode], test: String,
                          qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]): (Int, Int, Int) = {
    //    qforx1.append(this)
    //    qforx2.append(this)
    qforx1 += new QListNode(this, null)
    if (!toSend) {
      qforx2 += new QListNode(this, null)
      (0, 0, 0)
    } else {
      qforx2 += new QListNode(this, waitList)
      (this.id, 0, 0)
    }
  }
  override def Map = {MapAllChild(q1) ; q2.rStack.push(false)}
  override def Reduce = {
    val r = ReduceAllChild(q1) & q2.getResult
    if (!rStack.top) {
      rStack.pop()
      rStack.push(r)
    }
    if (output) println("Current result of " + this + " is " + rStack.top)
  }
}
