package Message

/**
  * Created by aojing on 2016/2/18.
  */
class Tag (_type: Int, test: String, testRank: Int, send: scala.Boolean) {
  def getType = _type
  def getTest = test
  def getTestRank = testRank
  def getSend = send
}
