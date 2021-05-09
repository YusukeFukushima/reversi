package Central

import scala.io.StdIn.readInt

class Board(boardSize: Int) {

  private val withWallSize = boardSize + 2

  val black = "B"
  val white = "W"
  private val wall  = "×"
  private val empty = " "

  var turn = black

  var pieces = Array.ofDim[String](withWallSize, withWallSize)
  var fields = Array.ofDim[Boolean](withWallSize, withWallSize)

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
    if(pieces(nextX)(nextY) == turn){
      count
    }else{
      0
    }
  }

  def checkField() = {
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

  def existLegalMove() = {
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

  private[Central] def putPiece() = {
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
    pieces(x)(y) = turn
    var count = 0
    for(dx <- -1 to 1){
      for(dy <- -1 to 1){
        count = countTurnOver(x, y, dx, dy)
        for(i <- 1 to count){
          pieces(x+(i*dx))(y+(i*dy)) = turn
        }
      }
    }
  }

  private[Central] def changeTurn() = {
    turn = oppositePiece
  }

  private[Central] def printTurn() = {
    if(turn == black){
      "BLACK"
    }else{
      "WHITE"
    }
  }

  private[Central] def printBoard() = {
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

  private def oppositePiece() = {
    if(turn == black){
      white
    }else{
      black
    }
  }
}