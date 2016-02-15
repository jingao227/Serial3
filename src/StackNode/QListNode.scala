package StackNode

import Translation.{WaitList, WaitListNode, TTNode}

import scala.collection.mutable.ListBuffer

/**
  * Created by Jing Ao on 2016/2/15.
  */
class QListNode(ttNode: TTNode, var waitList: WaitList) {
  def getttNode = ttNode
  def getwaitList = waitList

  def sendMessage = {
    if (waitList == null) {
      waitList = ttNode.addWaitLists(new WaitListNode(1, false))
    }
    else {
      //ttNode.addWaitLists(new WaitListNode(waitList.getSzie + 1, false))
    }
  }
  def receiveMessage = {}

  def doWork(toSend: scala.Boolean, sendList: ListBuffer[WaitListNode], test: String,
             qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]) = {
    //(val qa: Int, val qb: Int) =
    ttNode.doWork(toSend, waitList, sendList, test, qforx1, qforx2, redList)
  }
}
