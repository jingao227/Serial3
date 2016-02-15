package Translation

import scala.collection.mutable.ListBuffer

/**
  * Created by Jing Ao on 2016/2/15.
  */
class WaitList(idforWaitList: Int, originNode: WaitListNode) {
  val waitList = new ListBuffer[WaitListNode]   // 初始化
  var listSize = 1
  waitList += originNode

  def getSzie = listSize
  def addWaitList(newNode: WaitListNode) = waitList += newNode
}
