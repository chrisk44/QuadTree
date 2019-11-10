import scala.collection.immutable.ListSet
import Array._

class Node(var D: Int, var parent: Node, var range: Range ) {
  val MAX_BUCKET_SIZE = 5
  var nodes: Array[Node] = new Array( Math.pow(2, D).toInt )
  var bucket: ListSet[Point] = new ListSet[Point]()

  for(i <- 0 until Math.pow(2, D).toInt){
    nodes(i) = null
  }

  def add(point: Point): Unit = {
    // Check for error
    if(hasChildren){
      throw new Exception("Error: insert() called on node with children")
    }

    // Check if point already exists in bucket
    if (bucket.exists( (p: Point) => p.equals(point) ) ){
      throw new Exception("Point already exists")
    }

    // Add to bucket if possible
    if(bucket.size < MAX_BUCKET_SIZE){
      bucket += point
      return
    }

    // Split the node

    val new_limits = ofDim[Double](Math.pow(2, D).toInt, 2*D)

    for(d <- 0 until D){
      val min = range.limits(d)(0)
      val cntr = center.c(d)
      val max = range.limits(d)(1)

      val parts = Math.pow(2, d+1).toInt
      val part_size = Math.pow(2, D).toInt/parts

      for(p <- 0 until parts){
        if(p % 2 == 0){
          for(i <- p*part_size until (p+1)*part_size ){
            new_limits( i )(2*d) = min
            new_limits( i )(2*d + 1) = cntr
          }
        }else{
          for(i <- p*part_size until (p+1)*part_size ){
            new_limits( i )(2*d) = cntr
            new_limits( i )(2*d + 1) = max
          }
        }
      }
    }


    for(i <- 0 until Math.pow(2, D).toInt){
      val tmp_limits = ofDim[Double](D, 2)

      for(j <- 0 until D){
        tmp_limits(j)(0) = new_limits(i)(2*j)
        tmp_limits(j)(1) = new_limits(i)(2*j + 1)
      }

      nodes(i) = new Node(D, this, new Range(D, tmp_limits))
    }


    // Add new point in bucket to insert it in new nodes
    bucket += point
    for(p <- bucket){
      findNode(p).add(p)
    }

    // Empty the bucket
    bucket = new ListSet[Point]()
  }
  def delete(point: Point): Unit = {
    // Check if point exists
    if (!bucket.exists( (p: Point) => p.equals(point) ) ){
      throw new Exception("Point doesn't exist")
    }

    // Delete the point from bucket
    bucket -= point

    // Check if bucket is empty
    if(bucket.isEmpty){
      // Try to merge parent
      parent.tryMerge()
    }
  }
  def query(qRange: Range): ListSet[Point] = {
    var list: ListSet[Point] = new ListSet[Point]()

    if(qRange.intersects(this.range)) {
      if (hasChildren) {

        for(i <- 0 until Math.pow(2, D).toInt){
          list ++= nodes(i).query(qRange)
        }

      } else {

        for (p <- bucket) {
          if (qRange.contains(p)) list += p
        }

      }
    }

    list
  }

  def findNode(point: Point): Node = {
    if(hasChildren){

      var l: Int = 0
      var r: Int = Math.pow(2, D).toInt-1

      for(i <- 0 until D){
        if(point.c(i)<center.c(i)){
          // Look at left half
          r = (l+r)/2
        }else{
          // Look at right half
          l = (l+r)/2 + 1
        }
      }

      nodes(l).findNode(point)

    }else this
  }

  def tryMerge(): Unit = {
    for(i <- 0 until Math.pow(2, D).toInt){
      // If one node is not empty, abort merging
      if(!nodes(i).isEmpty) return
    }

    // All the child nodes are empty, delete them
    for(i <- 0 until Math.pow(2, D).toInt){
      nodes(i) = null
    }

    // Maybe the parent can now me merged too
    if(parent!=null) parent.tryMerge()
  }
  def center: Point = {
    var c = new Array[Double](D)

    for(i <- 0 until D){
      c(i) = (range.limits(i)(0) + range.limits(i)(1))/2
    }

    new Point(D, c)
  }
  def hasChildren: Boolean = nodes(0)!=null
  def size: Int = bucket.size
  def isEmpty: Boolean = size==0 && !hasChildren

  def print(level: Int): Unit ={
    for (_ <- 1 to level) {
      printf("\t")
    }
    if(parent==null) printf("[Root] ")
    println(this + " " + range)

    if(bucket.nonEmpty){
      for ( p <- bucket ) {
        for (_ <- 1 to level+1) {
          printf("\t")
        }
        println("Point " + p)
      }
    }else{
      if(nodes(0)!=null) for ( n <- nodes ) n.print(level+1)
    }
  }

}
