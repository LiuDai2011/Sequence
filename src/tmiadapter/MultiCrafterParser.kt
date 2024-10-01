package tmiadapter

import arc.struct.Seq
import mindustry.world.Block
import sequence.world.blocks.production.MultiCrafter
import tmi.recipe.Recipe
import tmi.recipe.RecipeParser
import tmi.recipe.RecipeType

class MultiCrafterParser : RecipeParser<MultiCrafter>() {
    override fun isTarget(content: Block) = content is MultiCrafter

    override fun parse(content: MultiCrafter): Seq<Recipe> {
        val recipes: Seq<Recipe> = Seq()
        for (formula in content.formulas)
            recipes.add(Recipe(RecipeType.factory).apply {
                setTime(formula.time)
                setBlock(getWrap(content))
                for (item in formula.inputItem) addMaterial(getWrap(item.item), item.amount)
                for (item in formula.inputLiquid) addMaterial(getWrap(item.liquid), item.amount)
                for (item in formula.outputItem) addProduction(getWrap(item.item), item.amount)
                for (item in formula.outputLiquid) addProduction(getWrap(item.liquid), item.amount)
            })
        return recipes
    }
}