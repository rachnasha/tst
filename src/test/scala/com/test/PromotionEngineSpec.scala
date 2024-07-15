package com.test

import com.tst.Promotion
import com.tst.PromotionCombo
import com.tst.PromotionEngine
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class PromotionEngineSpec extends AnyFlatSpec:

  "Promotion Engine" should "find combinable promotions for simple input" in {
    val promoCode1 = "P1"
    val promoCode2 = "P2"
    val promoCode3 = "P3"
    val promoCode4 = "P4"

    val promotion1 = Promotion(promoCode1, Seq(promoCode3))
    val promotion2 = Promotion(promoCode2, Seq(promoCode4))
    val promotion3 = Promotion(promoCode3, Seq(promoCode1, promoCode4))
    val promotion4 = Promotion(promoCode4, Seq(promoCode2, promoCode3))

    val allCombos = PromotionEngine.allCombinablePromotions(
      Seq(promotion1, promotion2, promotion3, promotion4)
    )
    allCombos should have size 3
    allCombos should contain theSameElementsAs (Seq(
      PromotionCombo(Seq(promoCode1, promoCode2)),
      PromotionCombo(Seq(promoCode1, promoCode4)),
      PromotionCombo(Seq(promoCode2, promoCode3))
    ))
  }

  it should "return empty when promotions cannot be combined" in {
    val promoCode1 = "P1"
    val promoCode2 = "P2"
    val promoCode3 = "P3"
    val promoCode4 = "P4"

    val promotion1 = Promotion(promoCode1, Seq(promoCode2))
    val promotion2 = Promotion(promoCode2, Seq(promoCode1))

    val allCombos = PromotionEngine.allCombinablePromotions(Seq(promotion1, promotion2))
    allCombos should have size 0
  }

  it should "be able to handle promotions that can be combined with anything - i.e has empty notCombinableWith" in {
    val promoCode1 = "P1"
    val promoCode2 = "P2"
    val promoCode3 = "P3"
    val promoCode4 = "P4"

    val promotion1 = Promotion(promoCode1, Seq(promoCode2))
    val promotion2 = Promotion(promoCode2, Seq(promoCode1))
    val promotion3 = Promotion(promoCode3, Seq.empty)
    val promotion4 = Promotion(promoCode4, Seq.empty)

    val allCombos = PromotionEngine.allCombinablePromotions(Seq(promotion1, promotion2, promotion3, promotion4))
    allCombos should have size 2
  }

  it should "find promotions for individual codes" in {
    val promoCode1 = "P1"
    val promoCode2 = "P2"
    val promoCode3 = "P3"
    val promoCode4 = "P4"

    val promotion1 = Promotion(promoCode1, Seq(promoCode3))
    val promotion2 = Promotion(promoCode2, Seq(promoCode4))
    val promotion3 = Promotion(promoCode3, Seq(promoCode1, promoCode4))
    val promotion4 = Promotion(promoCode4, Seq(promoCode2, promoCode3))
    val promotions = Seq(promotion1, promotion2, promotion3, promotion4)

    val combosForCode1 = PromotionEngine.combinablePromotions(promoCode1, promotions)
    val combosForCode2 = PromotionEngine.combinablePromotions(promoCode2, promotions)
    val combosForCode3 = PromotionEngine.combinablePromotions(promoCode3, promotions)
    val combosForCode4 = PromotionEngine.combinablePromotions(promoCode4, promotions)

    combosForCode1 should have size 2
    combosForCode1 should contain theSameElementsAs (Seq(
      PromotionCombo(Seq(promoCode1, promoCode2)),
      PromotionCombo(Seq(promoCode1, promoCode4))
    ))

    combosForCode2 should have size 2
    combosForCode2 should contain theSameElementsAs (Seq(
      PromotionCombo(Seq(promoCode2, promoCode1)),
      PromotionCombo(Seq(promoCode2, promoCode3))
    ))

    combosForCode3 should have size 1
    combosForCode3 should contain theSameElementsAs (Seq(
      PromotionCombo(Seq(promoCode3, promoCode2))
    ))

    combosForCode4 should have size 1
    combosForCode4 should contain theSameElementsAs (Seq(
      PromotionCombo(Seq(promoCode4, promoCode1))
    ))
  }
