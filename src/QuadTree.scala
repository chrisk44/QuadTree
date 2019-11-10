import scala.collection.immutable.ListSet
import Array._

class QuadTree (var D: Int, lower_bound: Double = -10000.0, upper_bound: Double = 10000.0) {
  val limits = ofDim[Double](D, 2)
  for(i <- 0 until D){
    limits(i)(0) = lower_bound
    limits(i)(1) = upper_bound
  }

  var root: Node = new Node(D, null, new Range(D, limits))

  def insert(point: Point): Unit = {
    if(point.D!=this.D) throw new IllegalArgumentException("Can't insert a " + point.D + "-dimensional object in a " + D + "-dimensional tree")
    if(!root.range.contains(point)){
      throw new IllegalArgumentException("Point out of bounds")
    }
    root.findNode(point).add(point)
  }
  def delete(point: Point): Unit = {
    if(point.D!=this.D) throw new IllegalArgumentException("Can't delete a " + point.D + "-dimensional object from a " + D + "-dimensional tree")
    root.findNode(point).delete(point)
  }
  def query(range: Range): ListSet[Point] ={
    if(range.D!=this.D) throw new IllegalArgumentException("Can't query a " + range.D + "-dimensional range in a " + D + "-dimensional tree")
    root.query(range)
  }

  def print(): Unit = {
    root.print(0)
  }
}
