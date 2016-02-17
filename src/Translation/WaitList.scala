package Translation

import scala.collection.mutable.ListBuffer

/**
  * Created by Jing Ao on 2016/2/15.
  */
class WaitList(idforWaitList: Int) {
  val waitList = new ListBuffer[WaitListNode]   // 初始化
  var listSize: Int = 1
  waitList += new WaitListNode(1, false)

  def getID = idforWaitList
  def getSize = listSize
  //def addWaitList(newNode: WaitListNode) = waitList += newNode ; listSize = listSize + 1
  def isEmpty: scala.Boolean = !(waitList.nonEmpty)

  def hasTrue: scala.Boolean = {
    for (element <- waitList) if (element.getResult) true
    false
  }
  def addWaitList(): Int = {
    waitList += new WaitListNode(listSize + 1, false)
    listSize = listSize + 1
    listSize
  }
  def updateList() = {
    //  淘汰过期查询
  }
  def removeWaitList() = {}
  def clearWaitList() = waitList.clear()
}
