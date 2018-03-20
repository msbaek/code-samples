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
            val sum = calculator.sum(2, 4)
            assertEquals(6, sum)
        }
    }

    @Test
    fun `2수의 차를 반환해야 한다`() {
        val c = SampleCalculator()
        val res = c.sub(6, 2)
        assertEquals(4, res)
    }
}
