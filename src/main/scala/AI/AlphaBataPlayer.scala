package AI
import Central.Board

class AlphaBataPlayer extends {
  val pre_search_depth = 3
  val normal_depth = 5
  val wld_depth = 15
  val perfect_depth = 13
} with ComputerPlayer {
  def move(board: Board): (Int, Int) = {
    val movables = board.movable_pos

    // パス判定：Masterクラスで実装済み

    if(movables.size == 1){
      return movables.head
    }

    var limit: Int = 0

    if(board.MAX_TURNS - board.turn_count <= wld_depth){
      limit = Int.MaxValue
    }else{
      limit = normal_depth
    }

    var eval, eval_max = Int.MinValue

    for(move <- movables){
      board.putPiece(move._1, move._2)
    }
    val dummy_x, dummy_y = 0 // 後で消す
    (dummy_x, dummy_y)
  }
}
