package XPath

/**
  * Created by Jing Ao on 2016/2/15.
  */
class Step extends XPath {
  var axis: Int = 0
  var test: String = null
  var preds: Pred = null

  def this(axis: Int, test: String, preds: Pred){
    this()
    this.axis = axis
    this.test = test
    this.preds = preds
  }

  override def hasq1 = if(preds != null) true else false
  override def isPC = if (axis == 0) true else false
  override def getTest = test
}
