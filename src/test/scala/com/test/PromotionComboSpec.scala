package com.test

import com.tst.Promotion
import com.tst.PromotionCombo
import org.scalatest.Checkpoints.Checkpoint
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class PromotionComboSpec extends AnyFlatSpec {

  "Promotion Combo" should "canAdd works for empty and nonEmpty combos" in {
    val promoCode1 = "P1"
    val promoCode2 = "P2"
    val promoCode3 = "P3"
    val promoCode4 = "P4"

    val promoCombo1      = PromotionCombo(Seq(promoCode1, promoCode4))
    val promoCombo2      = PromotionCombo(Seq(promoCode2))
    val promotion1       = Promotion(promoCode1, Seq(promoCode2))
    val promotion2       = Promotion(promoCode2, Seq(promoCode1, promoCode3))
    val promotion3       = Promotion(promoCode3, Seq(promoCode2))
    val uncombinablesMap = Map(promoCode1 -> Set(promoCode2), promoCode2 -> Set(promoCode1, promoCode3), promoCode3 -> Set(promoCode2))

    val addPromo2ToCombo1     = promoCombo1.isCompatible(promotion2, uncombinablesMap)
    val addPromo3ToCombo1     = promoCombo1.isCompatible(promotion3, uncombinablesMap)
    val addPromo1ToCombo2     = promoCombo2.isCompatible(promotion1, uncombinablesMap)
    val addPromo3ToCombo2     = promoCombo2.isCompatible(promotion3, uncombinablesMap)
    val addPromo3ToEmptyCombo = PromotionCombo.empty.isCompatible(promotion3, uncombinablesMap)

    val cp = new Checkpoint()

    cp { addPromo2ToCombo1 shouldBe false }
    cp {addPromo3ToCombo1 shouldBe true }
    cp {addPromo1ToCombo2 shouldBe false }
    cp {addPromo3ToCombo2 shouldBe false }
    cp {addPromo3ToEmptyCombo shouldBe true }

    cp.reportAll()
  }

  it should "not add promotion to a combo if it already exists" in {
    val promoCode1 = "P1"
    val promoCode2 = "P2"
    val promoCode3 = "P3"

    val promoCombo       = PromotionCombo(Seq(promoCode1, promoCode2))
    val uncombinablesMap = Map(promoCode1 -> Set(promoCode3), promoCode2 -> Set.empty, promoCode3 -> Set(promoCode1))

    val addDuplicatedToCombo = promoCombo.exists(promoCode2)
    addDuplicatedToCombo shouldBe true
  }

  it should "create an additional new promoCombo if adding to existing results in an incompatible combo" in {
    val promoCode1 = "P1"
    val promoCode2 = "P2"
    val promoCode3 = "P3"
    val promoCode4 = "P4"

    val promotion1 = Promotion(promoCode1, Seq(promoCode2))
    val promotion2 = Promotion(promoCode2, Seq(promoCode1, promoCode4))
    val promotion3 = Promotion(promoCode3, Seq(promoCode4))
    val promotion4 = Promotion(promoCode4, Seq(promoCode2, promoCode3))
    val uncombinablesMap = Map(
      promoCode1 -> Set(promoCode2),
      promoCode2 -> Set(promoCode1, promoCode4),
      promoCode3 -> Set(promoCode4),
      promoCode4 -> Set(promoCode2, promoCode3)
    )

    val promoCombo     = PromotionCombo(Seq(promoCode1, promoCode3))
    val toAddCode2     = PromotionCombo(Seq(promoCode2))
    val existingCombos = Seq(promoCombo)
    val returnedCombos = PromotionCombo.addOrCreateNew(existingCombos, promotion1, promotion4, uncombinablesMap)
    assert(existingCombos.size != returnedCombos.size)
    returnedCombos should contain(PromotionCombo(Seq(promoCode1, promoCode4)))
  }

  it should "not addOrCreateNew if promotion already exists" in {
    val promoCode1 = "P1"
    val promoCode2 = "P2"
    val promoCode3 = "P3"

    val promoCombo = PromotionCombo(Seq(promoCode1, promoCode3))

    val promotion1 = Promotion(promoCode1, Seq(promoCode2))
    val promotion2 = Promotion(promoCode2, Seq(promoCode1))
    val promotion3 = Promotion(promoCode3, Seq.empty)

    val uncombinablesMap = Map(
      promoCode1 -> Set(promoCode2),
      promoCode2 -> Set(promoCode1)
    )

    val with2NotAdded = PromotionCombo.addOrCreateNew(Seq(promoCombo), promotion1, promotion3, uncombinablesMap)
    assert(with2NotAdded.size == 1)
  }

  it should "only addTo compatible promoCombos and leave the rest unchanged" in {
    val promoCode1 = "P1"
    val promoCode2 = "P2"
    val promoCode3 = "P3"
    val promoCode4 = "P4"
    val promoCode5 = "P5"

    val promoCombo13 = PromotionCombo(Seq(promoCode1, promoCode3))
    val promoCombo14 = PromotionCombo(Seq(promoCode1, promoCode4))

    val promotion1 = Promotion(promoCode1, Seq(promoCode2))
    val promotion2 = Promotion(promoCode2, Seq(promoCode1))
    val promotion3 = Promotion(promoCode3, Seq(promoCode4))
    val promotion4 = Promotion(promoCode4, Seq(promoCode3, promoCode5))
    val promotion5 = Promotion(promoCode5, Seq(promoCode1, promoCode4))

    val promoCombos = Seq(promoCombo13, promoCombo14)
    val uncombinablesMap = Seq(promotion1, promotion2, promotion3, promotion4, promotion5).map {
      p => (p.code, p.notCombinableWith.toSet)
    }.toMap
    val resultCombos = PromotionCombo.addOrCreateNew(promoCombos, promotion1, promotion5, uncombinablesMap)
    resultCombos should have length (promoCombos.length)
    resultCombos should contain theSameElementsAs (
      Seq(
        PromotionCombo(Seq(promoCode1, promoCode3, promoCode5)),
        PromotionCombo(Seq(promoCode1, promoCode4))
      )
    )
  }

  it should "return true when PromotionCombo is fully present in a Seq of PromotionCombos in any order" in {
    val promoCode1 = "P1"
    val promoCode2 = "P2"
    val promoCode3 = "P3"
    val promoCode4 = "P4"

    val promoCombo1 = PromotionCombo(Seq(promoCode1, promoCode2))
    val promoCombo2 = PromotionCombo(Seq(promoCode2, promoCode1))
    val promoCombo3 = PromotionCombo(Seq(promoCode3))
    val promoCombo4 = PromotionCombo(Seq(promoCode1, promoCode2))

    val combos  = Seq(promoCombo1, promoCombo3)
    val result1 = PromotionCombo.containsFull(combos, promoCombo2)
    val result2 = PromotionCombo.containsFull(combos, promoCombo4)
    val cp = new Checkpoint()
    cp{ result1 should be(true) }
    cp { result2 should be(true) }
    cp.reportAll()
  }

  it should "return true when PromotionCombo is partially present in a Seq of PromotionCombos" in {
    val promoCode1 = "P1"
    val promoCode2 = "P2"
    val promoCode3 = "P3"
    val promoCode4 = "P4"
    val promoCode5 = "P5"

    val promoCombo1 = PromotionCombo(Seq(promoCode1, promoCode2))
    val promoCombo2 = PromotionCombo(Seq(promoCode1, promoCode4, promoCode5))
    val promoCombo3 = PromotionCombo(Seq(promoCode4, promoCode1))
    val promoCombo4 = PromotionCombo(Seq(promoCode4, promoCode3))

    val combos  = Seq(promoCombo1, promoCombo2)
    val result1 = PromotionCombo.containsFull(combos, promoCombo3)
    val result2 = PromotionCombo.containsFull(combos, promoCombo4)
    val cp = new Checkpoint()
    cp {result1 should be(true)}
    cp{ result2 should be(false)}
    cp.reportAll()
  }

  it should "return false when PromotionCombo is not fully or partially present in a Seq of combos" in {
    val promoCode1 = "P1"
    val promoCode2 = "P2"
    val promoCode3 = "P3"
    val promoCode4 = "P4"
    val promoCode5 = "P5"

    val promoCombo1 = PromotionCombo(Seq(promoCode1, promoCode2))
    val promoCombo2 = PromotionCombo(Seq(promoCode1, promoCode4, promoCode5))
    val promoCombo3 = PromotionCombo(Seq(promoCode2, promoCode4)) // 2 present in 1 combo and 4 in another which cannot be mixed

    val combos = Seq(promoCombo1, promoCombo2)
    val result = PromotionCombo.containsFull(combos, promoCombo3)
    result should be(false)
  }
}
