package Translation

import StackNode.QListNode
import XPath.Pred

import scala.collection.mutable.ListBuffer
/**
  * Created by Jing Ao on 2016/2/15.
  */
class PredPCNN(id: Int, label: String, father: TTNode) extends TTNode(id, label) {
  override def translate(preds: Pred): Int = {
    println(this.id + ": " +  this.label + " ")
    id
  }
  override def doMatch(test: String, qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]) = {
    if ((father == null && !rStack.top) || (father != null && !rStack.top && !father.rStack.top)) {
      Map
    }
  }
  override def doNotMatch(test: String, qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]) = {
    if ((father == null && !rStack.top) || (father != null && !rStack.top && !father.rStack.top)) {
      //      qforx2.append(this)
      qforx2 += new QListNode(this, null)
    }
  }
  override def Map = {
    rStack.pop()
    rStack.push(true)
  }
  override def Reduce = {}    //  由于没有q1，所以只要match test就意味着已经得到true，不会Map，因此也就不需要Reduce
}
