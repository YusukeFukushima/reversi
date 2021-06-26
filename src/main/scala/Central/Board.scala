package Central

import io.StdIn.readInt


case class BoardStack(color: String, opponent_color: String, put_pos: (Int, Int), flip_num: Int, flipped_pos: List[(Int,
Int)])

class Board(board_size: Int) {

  private val with_wall_size = board_size + 2

  val MAX_TURNS: Int = (math.pow(board_size, 2) - 4).toInt

  var board_stack: List[BoardStack] = List(BoardStack("Sp", " ", (0, 0), 0, List((0,0))))
  private var flipped_list: List[(Int, Int)] = Nil
  var movable_pos: List[(Int, Int)] = Nil

  val black = "B"
  val white = "W"
  private val wall  = "X"
  private val empty = " "

  var current_turn: String = black
  var turn_count = 0

  var pieces: Array[Array[String]] = Array.ofDim[String](with_wall_size, with_wall_size)
  var movable_fields: Array[Array[Boolean]] = Array.ofDim[Boolean](with_wall_size, with_wall_size)

  for(x <- 0 until with_wall_size) {
    for (y <- 0 until with_wall_size) {
      pieces(x)(y) = empty
      movable_fields(x)(y) = false
    }
  }

  for(i <- 0 until with_wall_size){
    pieces(0)(i) = wall
    pieces(with_wall_size-1)(i) = wall
    pieces(i)(0) = wall
    pieces(i)(with_wall_size-1) = wall
  }

  pieces(with_wall_size/2-1)(with_wall_size/2-1) = black
  pieces(with_wall_size/2-1)(with_wall_size/2) = white
  pieces(with_wall_size/2)(with_wall_size/2-1) = white
  pieces(with_wall_size/2)(with_wall_size/2) = black

  private def countTurnOver(x: Int, y: Int, dx: Int, dy: Int, checked_pieces: Array[Array[String]], current_color: String, opponent_color: String) = {
    var count = 0
    var next_x = x + dx
    var next_y = y + dy
    while(checked_pieces(next_x)(next_y) == opponent_color){
      count += 1
      next_x += dx
      next_y += dy
    }
    if(checked_pieces(next_x)(next_y) == current_color){
      count
    }else{
      0
    }
  }

  def checkField(checked_pieces: Array[Array[String]], current_color: String, opponent_color: String): Unit = {
    for(x <- 1 to board_size){
      for(y <- 1 to board_size){
        movable_fields(x)(y) = false
        if(checked_pieces(x)(y) == empty){
          for(dx <- -1 to 1){
            for(dy <- -1 to 1){
              if(countTurnOver(x, y, dx, dy, checked_pieces, current_color, opponent_color) != 0){
                movable_fields(x)(y) = true
                movable_pos = (x, y) :: movable_pos
              }
            }
          }
        }
      }
    }
  }

  def existLegalMove(): Boolean = {
    var iS_exist = false
    for(x <- 1 to board_size){
      for(y <- 1 to board_size){
        if(movable_fields(x)(y)){
          iS_exist = true
        }
      }
    }
    iS_exist
  }

  private[Central] def selectPutPlace(): (Int, Int) = {
    println("Please select tha place where you put new piece.(fields numbers are 1 to " + board_size + ")")
    print("x(←→): ")
    var x = readInt()
    print("y(↑↓): ")
    var y = readInt()
    while(!movable_fields(x)(y)){
      println("You cannot put a piece in that place. Please select the correct location.")
      print("x(←→): ")
      x = readInt()
      print("y(↑↓): ")
      y = readInt()
    }
    (x, y)
  }

  def putPiece(x: Int, y: Int): Unit = {
    flipped_list = Nil
    pieces(x)(y) = current_turn
    var count = 0
    var total = 0
    for(dx <- -1 to 1){
      for(dy <- -1 to 1){
        count = countTurnOver(x, y, dx, dy, pieces, current_turn, oppositePiece())
        total += count
        for(i <- 1 to count){
          pieces(x+(i*dx))(y+(i*dy)) = current_turn
          flipped_list = (x+(i*dx), y+(i*dy)) :: flipped_list
        }
      }
    }
    board_stack = BoardStack(current_turn, oppositePiece(), (x, y), total, flipped_list) :: board_stack
  }

  def isGameOver(): Boolean = {
    // ターン数判定
    if(turn_count == MAX_TURNS) return true
    // 手番プレイヤーの打ち手の存在判定
    if(existLegalMove()) return false

    val copied_pieces = pieces

    // 手番でないプレイヤーの打ち手の存在判定
    checkField(copied_pieces, oppositePiece(), current_turn)
    if(existLegalMove()) return false

    true
  }

  def pass(): Boolean = {
    if(existLegalMove()) return false

    if(isGameOver()) return false

    board_stack = BoardStack(current_turn, oppositePiece(), (0, 0), 0, Nil) :: board_stack
    true
  }

  // undoメソッドの実装

  private[Central] def changeTurn(): Unit = {
    current_turn = oppositePiece()
    turn_count = turn_count + 1
  }

  private[Central] def printTurn() = {
    if(current_turn == black){
      "BLACK('O') turn_count = " + (turn_count+1)
    }else{
      "WHITE('X') turn_count = " + (turn_count+1)
    }
  }

  private[Central] def printBoard(): Unit = {
    print("  ")
    for(x <- 0 until with_wall_size){
      print(x + "  ")
    }
    print("\n")
    for(y <- 0 until with_wall_size){
      print(y + " ")
      for(x <- 0 until with_wall_size){
        if(movable_fields(x)(y)){
          print("1  ")
        }else{
          print(pieces(x)(y) + "  ")
        }
      }
      print("\n")
    }
  }

  def oppositePiece(): String = {
    if(current_turn == black){
      white
    }else{
      black
    }
  }
}