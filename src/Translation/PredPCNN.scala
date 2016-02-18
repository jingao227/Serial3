package Translation

import Message.Message
import StackNode.QListNode
import XPath.Pred

import scala.collection.mutable.ListBuffer
/**
  * Created by Jing Ao on 2016/2/15.
  */
class PredPCNN(id: Int, label: String, father: TTNode) extends TTNode(id, label) {
  //override val nodeType = 9
  override def translate(preds: Pred): Int = {
    println(this.id + ": " +  this.label + " ")
    id
  }
  override def receiveResult(qListNode: QListNode): scala.Boolean = {
    searchTrue(qListNode.getwaitList)
  }
  override def searchTrue(waitList: WaitList): scala.Boolean = {
    if (waitList.hasTrue) {
      receiveTrueforx1
      waitList.clearWaitList()   // 谓词获得了一个true就不用再等别的true
      false                      // waitList已为空，没有要等待接收的结果
    } else {
      waitList.updateList()      // 淘汰过期查询
      if (!waitList.isEmpty) true else false  //  如果还有等待接收的结果(waitList中还有false)
    }
  }
  override def doMatch(toSend: scala.Boolean, qlistNode: QListNode, sendList: ListBuffer[Message], test: String,
                       qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]): (Int, Int, Int) = {
    if ((father == null && !rStack.top) || (father != null && !rStack.top && !father.rStack.top)) {
      Map
    }
    (0, 0, 0)
  }
  override def doNotMatch(toSend: scala.Boolean, qlistNode: QListNode, sendList: ListBuffer[Message], test: String,
                          qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]): (Int, Int, Int) = {
    if ((father == null && !rStack.top) || (father != null && !rStack.top && !father.rStack.top)) {
      qforx2 += qlistNode
//      if (!toSend) qforx2 += new QListNode(this, null)
//      else qforx2 += new QListNode(this, waitList)
    }
    (0, 0, 0)
  }
//  override def doStayWork(qListNode: QListNode, toStayList: ListBuffer[QListNode]): Unit = {
//    //  如果已输出true或者就根本没向远端请求过qforx1的结果，就不会需要留在栈中等结果
//    if (!alreadyTrue && qListNode.getwaitList != null) {
//      //  查找waitList中是否已接收到true消息
//      val searchResult: scala.Boolean = searchTrue(qListNode.getwaitList)
//      //  如果有true消息，即waitList已空，从waitLists中把waitList删除
//      if (searchResult) removeWaitLists(qListNode.getwaitList) // waitList从waitLists中删除
//      //  如果没有true消息，继续等待接收消息，即把这个qlistNode再压进栈
//      else toStayList += qListNode
//    }
//  }
  override def Map() = {
    rStack.pop()
    rStack.push(true)
  }
  override def Reduce() = {}    //  由于没有q1，所以只要match test就意味着已经得到true，不会Map，因此也就不需要Reduce
}
