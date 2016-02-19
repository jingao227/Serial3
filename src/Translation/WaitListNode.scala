package Translation

/**
  * Created by Jing Ao on 2016/2/15.
  */
class WaitListNode(idforx1: Int, var result: scala.Boolean, addTime: Long) {
  def getResult: scala.Boolean = result
  def outOfDate(currentTime: Long): scala.Boolean = if (currentTime - addTime > 10) true else false
}
