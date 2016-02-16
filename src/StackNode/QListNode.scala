package StackNode

import Message.Message
import Translation.{WaitList, WaitListNode, TTNode}

import scala.collection.mutable.ListBuffer

/**
  * Created by Jing Ao on 2016/2/15.
  */
class QListNode(ttNode: TTNode, var waitList: WaitList) {
  def getttNode = ttNode
  def getwaitList = waitList

//  def sendMessage = {
//    if (waitList == null) {
//      waitList = ttNode.addWaitLists
//    }
//    else {
//      //ttNode.addWaitLists(new WaitListNode(waitList.getSzie + 1, false))
//    }
//  }
//  def receiveMessage = {}

  def packMessage (qa: Int, qb: Int, qc: Int, sendList: ListBuffer[Message]) = {
    var waitListNodeID: Int = ???
    if (waitList == null) { waitList = ttNode.addWaitLists; waitListNodeID = 1 }
    else waitListNodeID = waitList.addWaitList
    sendList += new Message(ttNode.getID, waitList.getID, waitListNodeID, qa, qb, qc)
  }

  def receiveResult = {
    if (!waitList.isEmpty) {
      if (waitList.hasTrue) {
        ttNode.receiveTrueforx1

      }
    }
  }

  def receiveResultfornil = {
    if (!ttNode.alreadyTrue) {
      //  检查队列是否为空，是否有true。最后把waitList从waitLists中remove。
      //  因为如果rStack栈顶已经是true，waitList中的waitList列表已为空。
    }
  }

  def doWork(toSend: scala.Boolean, sendList: ListBuffer[Message], test: String,
             qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]) = {
    val (qa: Int, qb: Int, qc: Int) = ttNode.doWork(toSend, this, sendList, test, qforx1, qforx2, redList)
    if (qa > 0) packMessage(qa, qb, qc, sendList)
  }
}
