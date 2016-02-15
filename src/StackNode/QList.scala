package StackNode

import scala.collection.mutable.{ListBuffer, Stack}

/**
  * Created by Jing Ao on 2016/2/15.
  */
class QList(stack: Stack[StackNode], rank: Int, qlist: ListBuffer[QListNode]) extends StackNode(stack) {
  override def getRank = rank
  override def doEachWork(test: String, qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]) = {
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
      element.getttNode.doWork(test, qforx1, qforx2, redList)
    }
    //}
    if (qforx2.nonEmpty) stack.push(new QList(stack, rank, qforx2))
    if (redList.nonEmpty) stack.push(new ReduceNode(stack, redList))
    if (qforx1.nonEmpty) stack.push(new QList(stack, rank + 1, qforx1))
  }
}