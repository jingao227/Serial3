package Translation

import scala.collection.mutable.ListBuffer

/**
  * Created by Jing Ao on 2016/2/15.
  */
class WaitList(idforWaitList: Int) {
  val waitList = new ListBuffer[WaitListNode]   // 初始化
  var listSize: Int = 1
  waitList += new WaitListNode(1, false, System.currentTimeMillis())

  def getID = idforWaitList
  def getSize = listSize
  //def addWaitList(newNode: WaitListNode) = waitList += newNode ; listSize = listSize + 1
  def isEmpty: scala.Boolean = waitList.isEmpty

  def hasTrue: scala.Boolean = {
    for (element <- waitList) if (element.getResult) true
    false
  }
  def hasTrueAndDel: scala.Boolean = {
    var isTrue: scala.Boolean = false
    for (element <- waitList) {
      if (element.getResult) {
        waitList.remove(waitList.indexOf(element))
        if (!isTrue)  isTrue = true
      }
    }
    isTrue
  }
  def receiveTrueForNode(waitListNodeID: Int) = {
    //var found: scala.Boolean = false
    def findWaitListNode(): Unit = {
      for (element <- waitList) {
        if (element.getID == waitListNodeID) {
          element.receiveTrue()
          //found = true
          return
        }
      }
    }
    findWaitListNode()
  }
  def addWaitList(): Int = {
    waitList += new WaitListNode(listSize + 1, false, System.currentTimeMillis())
    listSize = listSize + 1
    listSize
  }
  def updateList() = {
    //  淘汰过期查询
    val currentTime: Long = System.currentTimeMillis()
    for (element <- waitList) {
      if (element.outOfDate(currentTime)) {
        waitList.remove(waitList.indexOf(element))
      }
    }
  }
  def clearWaitList() = waitList.clear()
}
