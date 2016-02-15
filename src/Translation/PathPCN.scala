package Translation

/**
  * Created by Jing Ao on 2016/2/15.
  */
import XPath._
import StackNode.QListNode
import scala.collection.mutable.ListBuffer

class PathPCN(id: Int, label: String) extends TTNode(id, label) {
  override def translate(path: Path): Int = {
    println(this.id + ": " +  this.label + " ")
    val cid: Int = id
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
  override def doMatch(test: String, qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]) = {
    Map
    //    qforx1.append(q2)
    //    redList.append(this)
    //    qforx2.append(this)
    qforx1 += new QListNode(q2, null)
    redList += new QListNode(this, null)
    qforx2 += new QListNode(this, null)
  }
  override def doNotMatch(test: String, qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]) = {
    //    qforx2.append(this)
    qforx2 += new QListNode(this, null)
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

