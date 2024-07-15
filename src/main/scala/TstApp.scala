import com.tst.CabinCode
import com.tst.CabinPrice
import com.tst.PricingEngine
import com.tst.Promotion
import com.tst.PromotionEngine
import com.tst.Rate
import com.tst.RateCode
import com.tst.RateGroup

object TstApp:

  @main def run() =
    initPricing()
    initPromotion()

  private def initPricing(): Unit = {

    val cabinCodeCA = CabinCode("CA")
    val cabinCodeCB = CabinCode("CB")

    val RateCodeM1 = RateCode("M1")
    val RateCodeM2 = RateCode("M2")
    val RateCodeS1 = RateCode("S1")
    val RateCodeS2 = RateCode("S2")

    val RateGroupM = RateGroup("Military")
    val RateGroupS = RateGroup("Senior")

    val RateM1 = Rate(RateCodeM1, RateGroupM)
    val RateM2 = Rate(RateCodeM2, RateGroupM)

    val RateS1 = Rate(RateCodeS1, RateGroupS)
    val RateS2 = Rate(RateCodeS2, RateGroupS)

    val inputRates = Seq(RateM1, RateM2, RateS1, RateS2)

    val price1 = CabinPrice(cabinCodeCA, RateCodeM1, 200.00)
    val price2 = CabinPrice(cabinCodeCA, RateCodeM2, 250.00)
    val price3 = CabinPrice(cabinCodeCA, RateCodeS1, 225.00)
    val price4 = CabinPrice(cabinCodeCA, RateCodeS2, 260.00)

    val price5 = CabinPrice(cabinCodeCB, RateCodeM1, 230.00)
    val price6 = CabinPrice(cabinCodeCB, RateCodeM2, 260.00)
    val price7 = CabinPrice(cabinCodeCB, RateCodeS1, 245.00)
    val price8 = CabinPrice(cabinCodeCB, RateCodeS2, 270.00)

    val inputPrices = Seq(price1, price2, price3, price4, price5, price6, price7, price8)

    val bestPrices = PricingEngine.getBestGroupPrices(inputRates, inputPrices)
    println(s"Best Rates:")
    println(s"${bestPrices.mkString("\n")}")
    println(s"-----------")

  }

  private def initPromotion(): Unit = {
    val p1 = "P1"
    val p2 = "P2"
    val p3 = "P3"
    val p4 = "P4"
    val p5 = "P5"

    val promotion1 = Promotion(p1, Seq(p3))
    val promotion2 = Promotion(p2, Seq(p4, p5))
    val promotion3 = Promotion(p3, Seq(p1))
    val promotion4 = Promotion(p4, Seq(p2))
    val promotion5 = Promotion(p5, Seq(p2))

    val input                = Seq(promotion1, promotion2, promotion3, promotion4, promotion5)
    val allValidCombinations = PromotionEngine.allCombinablePromotions(input)
    println(s"All Combinations:")
    println(s"${allValidCombinations.mkString("\n")}")
    println(s"-----------")

    println(s"P1 Combinations:")
    val allValidCombinationsP1 = PromotionEngine.combinablePromotions(p1, input)
    println(s"${allValidCombinationsP1.mkString("\n")}")
    println(s"-----------")

    println(s"P3 Combinations:")
    val allValidCombinationsP3 = PromotionEngine.combinablePromotions(p3, input)
    println(s"${allValidCombinationsP3.mkString("\n")}")
    println(s"-----------")

    println(s"P5 Combinations:")
    val allValidCombinationsP5 = PromotionEngine.combinablePromotions(p5, input)
    println(s"${allValidCombinationsP5.mkString("\n")}")
    println(s"-----------")

  }
