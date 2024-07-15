package com.test

import com.tst.*
import org.scalatest.Checkpoints.Checkpoint
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class PricingEngineSpec extends AnyFlatSpec:

  "Pricing Engine" should "find best prices for a given input" in {

    val cabinCode1 = CabinCode("C1")

    val RateCode1 = RateCode("R1")
    val RateCode2 = RateCode("R2")
    val RateCode3 = RateCode("R3")
    val RateCode4 = RateCode("R4")

    val RateGroupK = RateGroup("Kids")
    val RateGroupS = RateGroup("Students")

    val RatesKids1 = Rate(RateCode1, RateGroupK)
    val RatesKids2 = Rate(RateCode2, RateGroupK)

    val RatesStudent1 = Rate(RateCode3, RateGroupS)
    val RatesStudent2 = Rate(RateCode4, RateGroupS)

    val priceKids1 = CabinPrice(cabinCode1, RateCode1, 100)
    val priceKids2 = CabinPrice(cabinCode1, RateCode2, 50)

    val priceStudent1 = CabinPrice(cabinCode1, RateCode3, 150)
    val priceStudent2 = CabinPrice(cabinCode1, RateCode4, 750)

    val inputRates  = Seq(RatesKids1, RatesKids2, RatesStudent1, RatesStudent2)
    val inputPrices = Seq(priceKids1, priceKids2, priceStudent1, priceStudent2)

    val resultBestPrices = PricingEngine.getBestGroupPrices(inputRates, inputPrices)

    val resultKidsPrice    = resultBestPrices.filter(_.rateGroup == RateGroupK).map(_.price)
    val resultStudentPrice = resultBestPrices.filter(_.rateGroup == RateGroupS).map(_.price)

    resultBestPrices should have length 2
    val cp = new Checkpoint()
    cp { resultKidsPrice should equal(Seq(50)) }
    cp { resultStudentPrice should equal(Seq(150)) }

    cp.reportAll()
  }
