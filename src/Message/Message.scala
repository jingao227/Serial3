package Message

import StackNode.{QListNode, QList}
import Translation.TTNode

import scala.collection.mutable

/**
  * Created by aojing on 2016/2/16.
  */
class Message(ttNodeID: Int, waitListID: Int, waitListNodeID: Int, qa: Int, qb: Int, qc: Int) {
  def send(dest: String) = {}
  def addQList(ttNodeIndex: mutable.Map[Int, TTNode], qList: QList) = {
    if (qa > 0) {
      val query1 = ttNodeIndex(qa)
      // TODO:push false然后还要记录sender，sender和false打包成栈结点？
      query1.rStack.push(false)
      qList.getqlist += new QListNode(query1, null)
    }
    if (qb > 0) {
      val query2 = ttNodeIndex(qb)
      // TODO:push false然后还要记录sender，sender和false打包成栈结点？
      query2.rStack.push(false)
      qList.getqlist += new QListNode(query2, null)
    }
    if (qc > 0) {
      val query3 = ttNodeIndex(qc)
      // TODO:push false然后还要记录sender，sender和false打包成栈结点？
      query3.rStack.push(false)
      qList.getqlist += new QListNode(query3, null)
    }
  }
}
