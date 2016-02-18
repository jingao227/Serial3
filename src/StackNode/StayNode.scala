package StackNode

import Message.Message

import scala.collection.mutable.{Stack, ListBuffer}


/**
  * Created by aojing on 2016/2/18.
  */
class StayNode(stack: Stack[StackNode], stayList: ListBuffer[QListNode]) extends StackNode(stack) {
  override def getRank = -2
  override def doStayWork(toStayList: ListBuffer[QListNode]): Unit = {
    //foreach，如果!qlistNode.waitList.isEmpty,向toStayList里加入
    for (element <- stayList) {
      element.doStayWork(toStayList)
    }
    if (toStayList.nonEmpty) stack.push(new StayNode(stack, toStayList))
  }
}
