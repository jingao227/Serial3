package Translation

import StackNode.QListNode
import XPath.{Path, Step}

import scala.collection.mutable.ListBuffer
/**
  * Created by Jing Ao on 2016/2/15.
  */
class StepADN(id: Int, label: String) extends TTNode(id, label) {
  override def translate(path: Path): Int = translate(path.step)
  override def translate(step: Step): Int = {
    println(this.id + ": " +  this.label + " ")
    id
  }
  override def getResult = {
    val r = rStack.pop()
    if (r && rStack.nonEmpty) {
      rStack.pop()
      rStack.push(true)
    }
    r
  }
  override def doMatch(test: String, qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]) = {
    Map
    //    qforx1.append(this)
    //    //redList.append(this)    //  没有的Map，就不用Reduce
    //    qforx2.append(this)
    qforx1 += new QListNode(this, null)
    qforx2 += new QListNode(this, null)
  }
  override def doNotMatch(test: String, qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]) = {
    //    qforx1.append(this)
    //    qforx2.append(this)
    qforx1 += new QListNode(this, null)
    qforx2 += new QListNode(this, null)
  }
  override def Map = {
    rStack.pop()
    rStack.push(true)
  }
  override def Reduce = if (output) println("Current result of " + this + " is " + rStack.top)
}
