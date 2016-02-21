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
    var waitListNodeID: Int = 0
    if (waitList == null) { waitList = ttNode.addWaitLists(); waitListNodeID = 1 }
    else waitListNodeID = waitList.addWaitList()
    sendList += new Message(ttNode.getID, waitList.getID, waitListNodeID, qa, qb, qc)
  }

//  // 应该为每个规则写一个searchTrue
//  def searchTrue(): scala.Boolean = {
//    if (waitList.hasTrue) {
//      ttNode.receiveTrueforx1
//      waitList.clearWaitList()
//      true
//    } else { removeOldQuery(); false }
//  }
  //  waitList为null，或者waitList中的waitList为空，不做任何操作。
  //  因为前者表示该qlistNode没有向远端发过qforx1，所以不需receive
  //  后者表示该qlistNode向远端发过的qforx1中已经有为true的返回值了，ttNode.rStack的栈顶已经被置true，所以也不需要再recevie
//  def receiveResult() = if (waitList != null && !waitList.isEmpty) searchTrue()

  //  检查ttNode.rStack的栈顶是否已置true
  //  若是，说明已有qforx1返回true且waitList中的waitList必然已为空，所以不用再receive，从ttNode.waitLists中去掉该waitList
  //  若否，说明还没有qforx1返回true，所以要检查队列是否为空，是否有true。(一直循环就无法得到新消息，怎么办？)
  //  最后把waitList从waitLists中remove。
//  def receiveResultfornil() = {
//    var searchResult: scala.Boolean = false
//    if (!ttNode.alreadyTrue) while(!searchResult) searchResult = searchTrue()
//    // ttNode把waitList从waitLists中删除
//  }

  // 淘汰过期查询，需要调用ttNode.updateList
//  def removeOldQuery() = waitList.updateList()

  def doWork(toSend: scala.Boolean, sendList: ListBuffer[Message], test: String,
             qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]) = {
    //val (qa: Int, qb: Int, qc: Int) = ttNode.doWork(toSend, this, sendList, test, qforx1, qforx2, redList)
    //if (qa > 0) packMessage(qa, qb, qc, sendList)
    ttNode.doWork(toSend, this, sendList, test, qforx1, qforx2, redList)
  }

  def doStayWork(toStayList: ListBuffer[QListNode]) = {
//    //  如果已输出true或者就根本没向远端请求过qforx1的结果，就不会需要留在栈中等结果
//    val nodeType = ttNode.getNodeType
//    if (!ttNode.alreadyTrue && waitList != null) {
//      //  查找waitList中是否已接收到true消息
//      val searchResult: scala.Boolean = searchTrue()
//      //  如果有true消息，waitList已空，从ttNode.waitLists中把waitList删除
//      if (searchResult) ttNode.removeWaitLists(waitList) // ttNode把waitList从waitLists中删除
//      //  如果没有true消息，继续等待接收消息，即把这个qlistNode再压进栈
//      else toStayList += this
//    }
    ttNode.doStayWork(this, toStayList)
  }
}
