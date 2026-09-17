package org.example.project.model

enum class GridSize(val rows: Int, val cols: Int) {
    FOUR_BY_FOUR(4, 4),
    FIVE_BY_FIVE(5, 5),
    SIX_BY_SIX(6, 6);

    val totalCells: Int get() = rows * cols
    val pairsNeeded: Int get() = if (this == FIVE_BY_FIVE) (totalCells - 1) / 2 else totalCells / 2
}
