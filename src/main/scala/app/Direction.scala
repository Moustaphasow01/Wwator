package app

enum Direction(val dx: Int, val dy: Int) {
  case NORTH       extends Direction(0, -1)
  case NORTH_EAST  extends Direction(1, -1)
  case EAST        extends Direction(1, 0)
  case SOUTH_EAST  extends Direction(1, 1)
  case SOUTH       extends Direction(0, 1)
  case SOUTH_WEST  extends Direction(-1, 1)
  case WEST        extends Direction(-1, 0)
  case NORTH_WEST  extends Direction(-1, -1)
}
