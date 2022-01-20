public enum Direction {
    TOP,
    BOTTOM,
    RIGHT,
    LEFT,
    TOP_RIGHT,
    TOP_LEFT,
    BOTTOM_RIGHT,
    BOTTOM_LEFT,
    NONE;

    public Direction getOpposite() {
        switch (this) {
            case TOP -> {
                return Direction.BOTTOM;
            }
            case TOP_LEFT -> {
                return Direction.BOTTOM_RIGHT;
            }
            case TOP_RIGHT -> {
                return Direction.BOTTOM_LEFT;
            }
            case BOTTOM -> {
                return Direction.TOP;
            }
            case BOTTOM_LEFT -> {
                return Direction.TOP_RIGHT;
            }
            case BOTTOM_RIGHT -> {
                return Direction.TOP_LEFT;
            }
            case LEFT -> {
                return Direction.RIGHT;
            }
            case RIGHT -> {
                return Direction.LEFT;
            }
            default -> {
                return Direction.NONE;
            }
        }
    }
}
