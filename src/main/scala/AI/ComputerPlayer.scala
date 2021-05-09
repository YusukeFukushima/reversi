package AI

import Central.Board

trait ComputerPlayer {

  val preSearchDepth: Int
  val normalDepth: Int
  val wldDepth: Int
  val perfectDepth: Int

  def move(board: Board): Unit
}
