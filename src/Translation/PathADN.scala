package Translation

/**
  * Created by Jing Ao on 2016/2/15.
  */
import XPath._

import scala.collection.mutable.ListBuffer

class PathADN(label: String) extends TTNode(label) {
  override def translate(path: Path): Unit = {
    println(" " + this.label + " ")
    if(path.path.hasq1) {
      if(path.path.isPC) {
        if(path.q2ispath) q2 = new PathPCY(path.path.getTest)
        else q2 = new StepPCY(path.path.getTest)
      } else {
        if(path.q2ispath) q2 = new PathADY(path.path.getTest)
        else q2 = new StepADY(path.path.getTest)
      }
    } else {
      if(path.path.isPC) {
        if(path.q2ispath) q2 = new PathPCN(path.path.getTest)
        else q2 = new StepPCN(path.path.getTest)
      } else {
        if(path.q2ispath) q2 = new PathADN(path.path.getTest)
        else q2 = new StepADN(path.path.getTest)
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
  override def doMatch(test: String, qforx1: ListBuffer[TTNode], qforx2: ListBuffer[TTNode], redList: ListBuffer[TTNode]) = {
    Map
    //    qforx1.append(q2)
    //    qforx1.append(this)
    //    redList.append(this)
    //    qforx2.append(this)
    qforx1 += q2
    qforx1 += this
    redList += this
    qforx2 += this
  }
  override def doNotMatch(test: String, qforx1: ListBuffer[TTNode], qforx2: ListBuffer[TTNode], redList: ListBuffer[TTNode]) = {
    //    qforx1.append(this)
    //    qforx2.append(this)
    qforx1 += this
    qforx2 += this
  }
  override def Map = {
    //rStack.pop()
    //rStack.push(true)
    q2.rStack.push(false)
  }
  override def Reduce = {
    val r = q2.getResult
    if (!rStack.top){
      rStack.pop()
      rStack.push(r)
    }
    if (output) println("Current result of " + this + " is " + rStack.top)
  }
}
