package StackNode

import Translation.{WaitList, WaitListNode, TTNode}

import scala.collection.mutable.ListBuffer

/**
  * Created by Jing Ao on 2016/2/15.
  */
class QListNode(ttNode: TTNode, waitList: WaitList) {
  def getttNode = ttNode
  def getwaitList = waitList

  def sendMessage = {
    if (waitList == null) {
      ttNode.addWaitLists(new WaitListNode(1, false))
    }
    else {
      ttNode.addWaitLists(new WaitListNode(waitList.getSzie + 1, false))
    }
  }
  def receiveMessage = {}

  def doWork(toSend: scala.Boolean, SendList: ListBuffer[WaitListNode], qforx1: ListBuffer[QListNode], redList: ListBuffer[QListNode], qforx2: ListBuffer[QListNode]) = {

  }
}
