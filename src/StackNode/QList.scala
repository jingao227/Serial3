package StackNode

import Message.Message
import Translation.WaitListNode

import scala.collection.mutable.{ListBuffer, Stack}

/**
  * Created by Jing Ao on 2016/2/15.
  */
class QList(stack: Stack[StackNode], rank: Int, qlist: ListBuffer[QListNode]) extends StackNode(stack) {
  def getqlist =qlist
  override def getRank = rank
  override def doEachWork(toSend: scala.Boolean, sendList: ListBuffer[Message], test: String,
                          qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]) = {
    //    var cur = qlist
    //    while (cur != Nil){
    //      cur.head.doWork(test, qforx1, qforx2, redList)
    //      cur = cur.tail
    //    }
    //if (qlist.size != 0) {
    //println("qlist size is " + qlist.size)
    //    qlist.foreach {
    //      case element => element.doWork(test, qforx1, qforx2, redList)
    //    }
    for (element <- qlist) {
      element.doWork(toSend, sendList, test, qforx1, qforx2, redList)
    }
    //}
    if (qforx2.nonEmpty) stack.push(new QList(stack, rank, qforx2))
    if (redList.nonEmpty) stack.push(new ReduceNode(stack, redList))
    if (qforx1.nonEmpty) stack.push(new QList(stack, rank + 1, qforx1))
    //TODO: if sendList.nonEmpty foreach element <- sendList element.send
  }

  override def doStayWork(toStayList: ListBuffer[QListNode]): Unit = {
    for (element <- qlist) {
      element.doStayWork(toStayList)
    }
    if (toStayList.nonEmpty) stack.push(new StayNode(stack, toStayList))
  }
}
