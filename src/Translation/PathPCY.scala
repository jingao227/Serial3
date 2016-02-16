package Translation

/**
  * Created by Jing Ao on 2016/2/15.
  */
import XPath._
import StackNode.QListNode
import scala.collection.mutable.ListBuffer

class PathPCY(id: Int, label: String) extends TTNode(id, label) {
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
  override def doMatch(toSend: scala.Boolean, qlistNode: QListNode, sendList: ListBuffer[WaitListNode], test: String,
                       qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]): (Int, Int, Int) = {
    Map
    qforx1 += new QListNode(q1, null)
    qforx1 += new QListNode(q2, null)
    //redList += new QListNode(this, null)
    //qforx2 += new QListNode(this, waitList)   //  如果先向远端发送并加入了waitList，后来x2中未匹配标签或者不需发送，如果waitList处用null的话，之前加入waitList的标签就会被丢掉了
    redList += qlistNode
    qforx2 += qlistNode
    if (!toSend) (0, 0, 0) else (q1.getID, q2.getID, 0)
  }
  override def doNotMatch(toSend: scala.Boolean, qlistNode: QListNode, sendList: ListBuffer[WaitListNode], test: String,
                          qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]): (Int, Int, Int) = {
    qforx2 += qlistNode
//    if (!toSend) qforx2 += new QListNode(this, null)
//    else qforx2 += new QListNode(this, waitList)
    (0, 0, 0)   //  需要远端缓存如何表示？
  }
  override def Map = {MapAllChild(q1) ; q2.rStack.push(false)}
  override def Reduce = {
    val r = ReduceAllChild(q1) & q2.getResult
    if (!rStack.top) {
      rStack.pop()
      rStack.push(r)
    }
    if (output) println("Current result of root: " + this + " is " + rStack.top)
  }
}
