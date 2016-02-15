package XPath

/**
  * Created by Jing Ao on 2016/2/15.
  */
class Path extends XPath {
  var step: Step = null
  var path: Path = null

  def this(step: Step, path: Path) {
    this()
    this.step = step
    this.path = path
  }

  override def hasq1 = if (step.preds != null) true else false
  def q2ispath = if (path.path != null) true else false
  override def isPC = if (step.axis == 0) true else false
  override def getTest = step.test
}
