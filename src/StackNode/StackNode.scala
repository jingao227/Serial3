package StackNode

import Message.Message
import Translation.{WaitListNode, TTNode}

import scala.collection.mutable._
/**
  * Created by Jing Ao on 2016/2/15.
  */
class StackNode(stack: Stack[StackNode]) {
  def getRank: Int = ???
  def doEachWork(toSend: scala.Boolean, sendList: ListBuffer[Message], test: String, qforx1: ListBuffer[QListNode], qforx2: ListBuffer[QListNode], redList: ListBuffer[QListNode]): Unit = ???
  def doEachReduce(): Unit = ???
  def doStayWork(toStayList: ListBuffer[QListNode]): Unit = ???
}
