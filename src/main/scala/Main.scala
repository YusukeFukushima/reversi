import scala.io.StdIn.readInt
import Central.{Board, Master}

object Main extends App{

  println("Please input the size of board.(only even number)")
  val boardSize = readInt

  if(boardSize%2 != 0){
    throw new RuntimeException("Board size must be even.")
  }else if(boardSize < 4){
    throw new RuntimeException("Board size is too small.")
  }

  val board = new Board(boardSize)
  val master = new Master(board, boardSize)

  master.game
}
