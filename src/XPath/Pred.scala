package XPath

/**
  * Created by Jing Ao on 2016/2/15.
  */
class Pred extends XPath {
  var step: Step = null
  var preds: Pred = null

  def this(step: Step, preds: Pred){
    this()
    this.step = step
    this.preds = preds
  }

  override def hasq1 = if (step.preds != null) true else false
  def hasq2 = if (preds != null) true else false
  override def isPC = if (step.axis == 0) true else false
  override def getTest = step.test
}
