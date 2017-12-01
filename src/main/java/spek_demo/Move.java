package spek_demo;

public enum Move {
    ROCK(0), PAPER(1), SCISSORS(2);

    private int order;

    Move(int order) {
        this.order = order;
    }
}

//enum class Move(val order: Int) {
//        ROCK(0),
//        PAPER(1),
//        SCISSORS(2);
//
//        fun beats(other: Move): Boolean {
//        val numberOfOptions = Move.values().size
//        return (this.order - other.order + numberOfOptions) % numberOfOptions == 1
//        }
//        }