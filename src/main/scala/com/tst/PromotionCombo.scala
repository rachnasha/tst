package com.tst

case class PromotionCombo(promotionCodes: Seq[String]) {

  def add(promotion: Promotion): PromotionCombo =
    PromotionCombo(promotionCodes.:+(promotion.code))

  def exists(promotionCode: String): Boolean =
    promotionCodes.exists(_ == promotionCode)

  def isCompatible(promotion: Promotion, uncombinablesMap: Map[String, Set[String]]): Boolean = {
    !promotionCodes.exists(p => uncombinablesMap.get(p).exists { s => s.contains(promotion.code) })
  }
}

object PromotionCombo:
  val empty: PromotionCombo = PromotionCombo(Seq.empty[String])

  def fromPromotions(promotions: Seq[Promotion]): PromotionCombo =
    PromotionCombo(promotions.map(_.code))

  def createNew(existingCombos: Seq[PromotionCombo], addToPromotion: Promotion, promotionToAdd: Promotion): Boolean =
    !existingCombos.exists(_.exists(promotionToAdd.code)) && addToPromotion.canCombine(promotionToAdd)

  def addOrCreateNew(
      existingCombos: Seq[PromotionCombo],
      addToPromotion: Promotion,
      promotionToAdd: Promotion, // if these are diff types its safer
      uncombinablesMap: Map[String, Set[String]]
  ): Seq[PromotionCombo] = {
    val (combosToAddTo, otherCombos) = existingCombos.partition { pc =>
      !pc.exists(promotionToAdd.code) && pc.isCompatible(promotionToAdd, uncombinablesMap)
    }
    if (combosToAddTo.nonEmpty) {
      otherCombos ++ combosToAddTo.map(pc => pc.add(promotionToAdd))
    } else if (createNew(existingCombos, addToPromotion, promotionToAdd)) {
      existingCombos.+:(PromotionCombo.fromPromotions(Seq(addToPromotion, promotionToAdd)))
    } else {
      existingCombos
    }
  }

  def containsFull(combos: Seq[PromotionCombo], combo: PromotionCombo): Boolean =
    combo.promotionCodes.nonEmpty &&
    combos.exists(aCombo => combo.promotionCodes.forall(aCombo.promotionCodes.contains))

  def distinct(allCombos: Seq[PromotionCombo]): Seq[PromotionCombo] =
    if (allCombos.isEmpty) allCombos
    else {
      // perhaps we should have sortedPromotionCodes in PromotionCombo - This will keep the output deterministic.
      val sortedByLengthDesc = allCombos.sortBy(_.promotionCodes.length * -1)
      sortedByLengthDesc.tail.foldLeft(sortedByLengthDesc.headOption.toSeq) {
        case (result, aCombos) =>
          if (PromotionCombo.containsFull(result, aCombos)) result
          else result.:+(aCombos)
      }
    }
