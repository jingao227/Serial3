package StackNode

import Translation.TTNode

import scala.collection.mutable._
/**
  * Created by Jing Ao on 2016/2/15.
  */
class ReduceNode(stack: Stack[StackNode], redList: ListBuffer[TTNode]) extends StackNode(stack) {
  override def getRank = -1
  override def doEachReduce = {
    redList.foreach {
      case element => element.Reduce
    }
    //    var cur = redList
    //    while (cur != Nil){
    //      cur.head.Reduce
    //      cur = cur.tail
    //    }
  }
}

