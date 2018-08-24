package info.blockchain.balance

import org.amshove.kluent.`should equal`
import org.junit.Test

class FiatValueFromMinorTests {

    @Test
    fun `from minor GBP`() {
        FiatValue.fromMinor(
            "GBP",
            123
        ) `should equal` FiatValue("GBP", 1.23.toBigDecimal())
    }

    @Test
    fun `from minor GBP 0 end`() {
        FiatValue.fromMinor(
            "GBP",
            200
        ) `should equal` FiatValue("GBP", 2.0.toBigDecimal().setScale(2))
    }

    @Test
    fun `from minor USD`() {
        FiatValue.fromMinor(
            "USD",
            456
        ) `should equal` FiatValue("USD", 4.56.toBigDecimal())
    }

    @Test
    fun `from minor YEN`() {
        FiatValue.fromMinor(
            "JPY",
            456
        ) `should equal` FiatValue("JPY", 456.toBigDecimal())
    }
}
