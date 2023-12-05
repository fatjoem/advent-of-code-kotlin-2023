
val digitsMap = mapOf(
  Pair("zero", "0"),
  Pair("one", "1"),
  Pair("two", "2"),
  Pair("three", "3"),
  Pair("four", "4"),
  Pair("five", "5"),
  Pair("six", "6"),
  Pair("seven", "7"),
  Pair("eight", "8"),
  Pair("nine", "9"),
)

val digits = digitsMap.keys + digitsMap.values


fun main() {
  fun part1(input: List<String>) = input.fold(0) { acc, line ->
    acc + 10 * line.first { it.isDigit() }.digitToInt() + line.last { it.isDigit() }.digitToInt()
  }

  fun part2(input: List<String>) = input.fold(0) { acc, line ->
    val firstDigit = line.findAnyOf(digits)!!.second.let { digitsMap[it] ?: it }.toInt()
    val lastDigit = line.findLastAnyOf(digits)!!.second.let { digitsMap[it] ?: it }.toInt()
    acc + 10 * firstDigit + lastDigit
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day01_test")
  check(part2(testInput) == 281)

  val input = readInput("Day01")
  part1(input).println()
  part2(input).println()
}
