package Central

import scala.util.control.Breaks._

class Master(board: Board, boardSize: Int) {
  private var countBlack = 0
  private var countWhite = 0

  def game(): Unit = {
    breakable{
      while(true) {
        println("-" * 50)
        countPieces()
        board.checkField()
        board.printBoard()
        if (!board.existLegalMove) {
          if(board.currentTurn == board.black){
            println("BLACK's turn is skipped because there is no place to put a piece.")
          }else{
            println("WHITE's turn is skipped because there is no place to put a piece.")
          }
          board.changeTurn()
          board.checkField()
          board.printBoard()
          if (!board.existLegalMove) {
            if(board.currentTurn == board.black){
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
    countBlack = 0
    countWhite = 0
    for(x <- 1 to boardSize){
      for(y <- 1 to boardSize){

        if(board.pieces(x)(y) == board.black){
          countBlack += 1
        }else if(board.pieces(x)(y) == board.white){
          countWhite += 1
        }
      }
    }
    println("BLACK: " + countBlack + " WHITE: " + countWhite)
  }

  private def judgeWinner(): Unit = {
    if(countBlack > countWhite){
      println("Winner is BLACK.")
    }else if(countBlack < countWhite){
      println("Winner is WHITE.")
    }else{
      println("This game is draw.")
    }
  }
}