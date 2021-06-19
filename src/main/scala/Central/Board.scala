package Central

import io.StdIn.readInt

case class BoardStack(color: String, opponentColor: String, putPos: (Int, Int), flipNum: Int, flippedPos: List[(Int,
Int)])

class Board(boardSize: Int) {

  private val withWallSize = boardSize + 2

  var boardStack: List[BoardStack] = List(BoardStack("Sp", " ", (0, 0), 0, List((0,0))))
  private var flippedList: List[(Int, Int)] = Nil

  val black = "B"
  val white = "W"
  private val wall  = "X"
  private val empty = " "

  var currentTurn: String = black

  var pieces: Array[Array[String]] = Array.ofDim[String](withWallSize, withWallSize)
  var fields: Array[Array[Boolean]] = Array.ofDim[Boolean](withWallSize, withWallSize)

  for(x <- 0 until withWallSize) {
    for (y <- 0 until withWallSize) {
      pieces(x)(y) = empty
      fields(x)(y) = false
    }
  }

  for(i <- 0 until withWallSize){
    pieces(0)(i) = wall
    pieces(withWallSize-1)(i) = wall
    pieces(i)(0) = wall
    pieces(i)(withWallSize-1) = wall
  }

  pieces(withWallSize/2-1)(withWallSize/2-1) = black
  pieces(withWallSize/2-1)(withWallSize/2) = white
  pieces(withWallSize/2)(withWallSize/2-1) = white
  pieces(withWallSize/2)(withWallSize/2) = black

  private def countTurnOver(x: Int, y: Int, dx: Int, dy: Int) = {
    var count = 0
    var nextX = x + dx
    var nextY = y + dy
    while(pieces(nextX)(nextY) == oppositePiece){
      count += 1
      nextX += dx
      nextY += dy
    }
    if(pieces(nextX)(nextY) == currentTurn){
      count
    }else{
      0
    }
  }

  def checkField(): Unit = {
    for(x <- 1 to boardSize){
      for(y <- 1 to boardSize){
        fields(x)(y) = false
        if(pieces(x)(y) == empty){
          for(dx <- -1 to 1){
            for(dy <- -1 to 1){
              if(countTurnOver(x, y, dx, dy) != 0){
                fields(x)(y) = true
              }
            }
          }
        }
      }
    }
  }

  def existLegalMove(): Boolean = {
    var iSExist = false
    for(x <- 1 to boardSize){
      for(y <- 1 to boardSize){
        if(fields(x)(y)){
          iSExist = true
        }
      }
    }
    iSExist
  }

  private[Central] def selectPutPlace(): (Int, Int) = {
    println("Please select tha place where you put new piece.(fields numbers are 1 to " + boardSize + ")")
    print("x(←→): ")
    var x = readInt()
    print("y(↑↓): ")
    var y = readInt()
    while(!fields(x)(y)){
      println("You cannot put a piece in that place. Please select the correct location.")
      print("x(←→): ")
      x = readInt()
      print("y(↑↓): ")
      y = readInt()
    }
    (x, y)
  }

  private[Central] def putPiece(x: Int, y: Int): Unit = {
    flippedList = Nil
    pieces(x)(y) = currentTurn
    var count = 0
    var total = 0
    for(dx <- -1 to 1){
      for(dy <- -1 to 1){
        count = countTurnOver(x, y, dx, dy)
        total += count
        for(i <- 1 to count){
          pieces(x+(i*dx))(y+(i*dy)) = currentTurn
          flippedList = (x+(i*dx), y+(i*dy)) :: flippedList
        }
      }
    }
    boardStack = BoardStack(currentTurn, oppositePiece(), (x, y), total, flippedList) :: boardStack
  }

  private[Central] def changeTurn(): Unit = {
    currentTurn = oppositePiece()
  }

  private[Central] def printTurn() = {
    if(currentTurn == black){
      "BLACK('O')"
    }else{
      "WHITE('X')"
    }
  }

  private[Central] def printBoard(): Unit = {
    print("  ")
    for(x <- 0 until withWallSize){
      print(x + "  ")
    }
    print("\n")
    for(y <- 0 until withWallSize){
      print(y + " ")
      for(x <- 0 until withWallSize){
        if(fields(x)(y)){
          print("1  ")
        }else{
          print(pieces(x)(y) + "  ")
        }
      }
      print("\n")
    }
  }

  private def oppositePiece(): String = {
    if(currentTurn == black){
      white
    }else{
      black
    }
  }
}