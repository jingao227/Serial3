package Translation

import akka.actor.ActorRef

/**
  * Created by Jing Ao on 2016/2/21.
  */
class RStackNode(sender: ActorRef, ttNodeID: Int, waitListID: Int, waitListNodeID: Int, var value: scala.Boolean) {
  def getSender: ActorRef = sender
  def getDes: (Int, Int, Int) = (ttNodeID, waitListID, waitListNodeID)
  def getValue = value
  def setValue(r: scala.Boolean) = value = r
  def replyToSender() = sender ! (ttNodeID, waitListID, waitListNodeID)
}
