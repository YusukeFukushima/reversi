package AI

import Central.Board

trait ComputerPlayer {

  val pre_search_depth: Int // alpha-bata法, NegaScout法において、事前に手を調べて探索順序を決めるための先読み手数
  val normal_depth: Int    // 序盤・中盤の探索における先読み手数
  val wld_depth: Int       // 終盤において、必勝読みを始める残り手数(wld = win, lose, draw)
  val perfect_depth: Int   // 終盤において完全よみを始める残り手数

}
