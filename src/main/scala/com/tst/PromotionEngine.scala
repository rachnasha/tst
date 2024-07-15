package com.tst

// Most Strings here should be nonEmptyString
case class Promotion(code: String, notCombinableWith: Seq[String]) {

  def canCombine(otherPromotion: Promotion): Boolean =
    (otherPromotion.code != code) &&
      !(notCombinableWith.toSet.contains(otherPromotion.code))
}

object PromotionEngine:

  private def findCombinables(allPromotions: Seq[Promotion]): Map[String, Seq[PromotionCombo]] =
    val uncombinablesMap = allPromotions.map(p => (p.code, p.notCombinableWith.toSet)).toMap
    allPromotions.map {
      p =>
        val promosToCombine = allPromotions.filter(p.canCombine)
        val promoCombosForP = promosToCombine.foldLeft(Seq.empty[PromotionCombo]) {
          case (result, promoToCombine) =>
            PromotionCombo.addOrCreateNew(result, p, promoToCombine, uncombinablesMap)
        }
        (p.code, promoCombosForP)
    }.toMap

  def allCombinablePromotions(allPromotions: Seq[Promotion]): Seq[PromotionCombo] =
    val combosByPromotionCode = findCombinables(allPromotions)
    val allCombos             = combosByPromotionCode.values.flatten.toSeq
    PromotionCombo.distinct(allCombos)

  def combinablePromotions(promotionCode: String, allPromotions: Seq[Promotion]): Seq[PromotionCombo] =
    findCombinables(allPromotions).getOrElse(promotionCode, Seq.empty[PromotionCombo])
