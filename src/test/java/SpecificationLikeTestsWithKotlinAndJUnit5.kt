import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SimpleTests {
    @Nested
    @DisplayName("계산기")
    inner class Calculator {
        var calculator = SampleCalculator()

        @Test fun `2 수의 합을 반환해야 한다`() {
            val sum = calculator.sum(i = 2, i1 = 4)
            assertEquals(6, sum)
        }
    }
}

class SampleCalculator {
    fun sum(i: Int, i1: Int): Int {
        return i + i1
    }
}