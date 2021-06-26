package Central

import scala.util.control.Breaks._

class Master(board: Board, board_size: Int) {
  private var count_black = 0
  private var count_white = 0

  def game(): Unit = {
    breakable{
      while(true) {
        println("-" * 50)
        countPieces()
        board.checkField(board.pieces, board.current_turn, board.oppositePiece())
        board.printBoard()
        if (!board.existLegalMove) {
          if(board.current_turn == board.black){
            println("BLACK's turn is skipped because there is no place to put a piece.")
          }else{
            println("WHITE's turn is skipped because there is no place to put a piece.")
          }
          board.changeTurn()
          board.checkField(board.pieces, board.current_turn, board.oppositePiece())
          board.printBoard()
          if (!board.existLegalMove) {
            if(board.current_turn == board.black){
              println("BLACK's turn is skipped because there is no place to put a piece.")
            }else{
              println("WHITE's turn is skipped because there is no place to put a piece.")
            }
            println("No one can put a piece.Game is Over")
            break
          }
        }

        println("turn: " + board.printTurn)
        println("You can put the piece where marked 1.")
        val (selected_x, selected_y) = board.selectPutPlace()
        board.putPiece(selected_x, selected_y)
        board.changeTurn()

      }
    }
    countPieces()
    judgeWinner()
  }

  private[Central] def countPieces(): Unit = {
    count_black = 0
    count_white = 0
    for(x <- 1 to board_size){
      for(y <- 1 to board_size){

        if(board.pieces(x)(y) == board.black){
          count_black += 1
        }else if(board.pieces(x)(y) == board.white){
          count_white += 1
        }
      }
    }
    println("BLACK: " + count_black + " WHITE: " + count_white)
  }

  private def judgeWinner(): Unit = {
    if(count_black > count_white){
      println("Winner is BLACK.")
    }else if(count_black < count_white){
      println("Winner is WHITE.")
    }else{
      println("This game is draw.")
    }
  }
}