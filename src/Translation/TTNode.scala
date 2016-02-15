package Translation

/**
  * Created by Jing Ao on 2016/2/15.
  */
import XPath._
import scala.collection.mutable._

class TTNode(label: String) {
  val rStack = new Stack[scala.Boolean]

  var q1: TTNode= null
  var q2: TTNode = null
  var q3: TTNode = null

  var output = false

  def translate(path: Path): Unit = ???
  def translate(preds: Pred): Unit = ???
  def translate(step: Step): Unit = ???

  def setOutput(o: scala.Boolean) = output = o

  def doMatch(test: String, qforx1: ListBuffer[TTNode], qforx2: ListBuffer[TTNode], redList: ListBuffer[TTNode]): Unit = ???
  def doNotMatch(test: String, qforx1: ListBuffer[TTNode], qforx2: ListBuffer[TTNode], redList: ListBuffer[TTNode]): Unit = ???
  def doWork(test: String, qforx1: ListBuffer[TTNode], qforx2: ListBuffer[TTNode], redList: ListBuffer[TTNode]): Unit = {
    if (test == label) doMatch(test, qforx1, qforx2, redList)
    else doNotMatch(test, qforx1, qforx2, redList)
  }

  def hasq2 = if (q2 != null) true else false
  def getResult = rStack.pop()

  def Map: Unit = ???
  def Reduce: Unit = ???
  def MapAllChild(p: TTNode): Unit = {
    //var p = c
    p.rStack.push(false)
    if (p.hasq2) {
      p.q3.rStack.push(false)
      MapAllChild(p.q2)
    }
    /*    while (p.hasq2) {
          p.q3.rStack.push(false)
          p.q2.rStack.push(false)
          p = p.q2
        }*/
  }
  def ReduceAllChild(p: TTNode): scala.Boolean = {
    var r = p.getResult
    if (p.hasq2) {
      r = r | p.q3.getResult
      r = r & ReduceAllChild(p.q2)
    }
    r
  }
}
