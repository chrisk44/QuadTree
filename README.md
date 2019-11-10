# QuadTree
This is an implementation of the multidimensional data structure **QuadTree**.
It is a generalization of the structure so the dimensionality is a parameter.
Note that since it is not a 2D structure, the name *QuadTree* may be inaccurate, because the space is not necessarily divided into 4 sub-spaces,
but into 2<sup>D</sup> sub-spaces.

Note that increasing the dimensionality too much, *there may not be any benefit of using a complex structure.*

## Interface
The interface of the structure is in the file QuadTree.scala with the functions:

* `insert(point: Point)`  Inserts a given point in the structure.
* `delete(point: Point)`  Deletes a given point from the structure.
* `query(range: Range)`  Returns a ListSet[Point] of points included in a given range.

## Project structure
* `QuadTree.scala` Provides an interface for the structure.
* `Node.scala`  Represents a node in the tree. Implements all the funcionality of the structure.
* `Point.scala`  Represents a D-dimensional point stored in the tree.
* `Range.scala`  Represents a D-dimensional range to query the tree.
* `Main.scala`  Provides a basic CLI to test the structure.

## Credits
This project was written with:
* [pfloros20](https://github.com/pfloros20)
* [PanosXatz](https://github.com/PanosXatz)
