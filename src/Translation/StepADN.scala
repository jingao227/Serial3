package Translation

import XPath.{Path, Step}

import scala.collection.mutable.ListBuffer
/**
  * Created by Jing Ao on 2016/2/15.
  */
class StepADN(label: String) extends TTNode(label) {
  override def translate(path: Path) = translate(path.step)
  override def translate(step: Step) = println(" " + this.label + " ")
  override def getResult = {
    val r = rStack.pop()
    if (r && rStack.nonEmpty) {
      rStack.pop()
      rStack.push(true)
    }
    r
  }
  override def doMatch(test: String, qforx1: ListBuffer[TTNode], qforx2: ListBuffer[TTNode], redList: ListBuffer[TTNode]) = {
    Map
    //    qforx1.append(this)
    //    //redList.append(this)    //  没有的Map，就不用Reduce
    //    qforx2.append(this)
    qforx1 += this
    qforx2 += this
  }
  override def doNotMatch(test: String, qforx1: ListBuffer[TTNode], qforx2: ListBuffer[TTNode], redList: ListBuffer[TTNode]) = {
    //    qforx1.append(this)
    //    qforx2.append(this)
    qforx1 += this
    qforx2 += this
  }
  override def Map = {
    rStack.pop()
    rStack.push(true)
  }
  override def Reduce = if (output) println("Current result of " + this + " is " + rStack.top)
}
