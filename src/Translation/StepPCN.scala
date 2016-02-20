package Translation

import Message.Message
import StackNode.QListNode
import XPath.{Path, Step}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
/**
  * Created by Jing Ao on 2016/2/15.
  */
class StepPCN(id: Int, label: String) extends TTNode(id, label) {
  //override val nodeType = 1
  override def translate(path: Path, ttNodeIndex: mutable.Map[Int, TTNode]): Int = translate(path.step, ttNodeIndex)
  override def translate(step: Step, ttNodeIndex: mutable.Map[Int, TTNode]): Int = {
    println(this.id + ": " +  this.label + " ")
    ttNodeIndex += (this.id ->this)
    id
  }
  override def receiveResult(qListNode: QListNode): scala.Boolean = {
    remainReceiving(qListNode.getwaitList)  //  结果为false即waitList.isEmpty也不能从waitLists中remove这个waitList，因为还没遇到nil，这个waitList在后面x2的检查中还有可能再加入
  }
  override def remainReceiving(waitList: WaitList): scala.Boolean = {
    if (waitList.hasTrueAndDel) if (!alreadyTrue) receiveTrueforx1
    if (!waitList.isEmpty) waitList.updateList()
    if (!waitList.isEmpty) true else false  //  如果还有等待接收的结果(waitList中还有false)
  }
  override def doMatch(toSend: scala.Boolean, qlistNode: QListNode, sendList: ListBuffer[Message], test: String,
                       qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]): (Int, Int, Int) = {
    Map //  没有的Map，就不用Reduce
    qforx2 += qlistNode
//    if (!toSend) qforx2 += new QListNode(this, null)
//    else qforx2 += new QListNode(this, waitList)
    (0, 0, 0)   //  如何通知远端需要缓存？
  }
  override def doNotMatch(toSend: scala.Boolean, qlistNode: QListNode, sendList: ListBuffer[Message], test: String,
                          qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]): (Int, Int, Int) = {
    qforx2 += qlistNode
//    if (!toSend) qforx2 += new QListNode(this, null)
//    else qforx2 += new QListNode(this, waitList)
    (0, 0, 0)   //  如何通知远端需要缓存？
  }
//  override def doStayWork(qListNode: QListNode, toStayList: ListBuffer[QListNode]): Unit = {
//    //  如果waitList已空或者就根本没向远端请求过qforx1的结果，就不会需要留在栈中等结果
//    if (qListNode.getwaitList != null && !qListNode.getwaitList.isEmpty) {
//      //  查找waitList中是否还有需要接收的消息
//      val toReceive: scala.Boolean = remainReceiving(qListNode.getwaitList)
//      //  如果waitList已为空，waitList从waitLists中删除
//      if (!toReceive) removeWaitLists(qListNode.getwaitList)
//      //  如果waitList不为空，继续等待接收消息，即把这个qlistNode再压进栈
//      else toStayList += qListNode
//    }
//  }
  override def Map() = {
    rStack.pop()
    rStack.push(true)
  }
  override def Reduce() = if (output) println("Current result of " + this + " is " + rStack.top)
}
