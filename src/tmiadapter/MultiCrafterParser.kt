//package tmiadapter
//
//import arc.struct.Seq
//import mindustry.type.Item
//import mindustry.world.Block
//import sequence.world.blocks.production.MultiCrafter
//import tmi.recipe.Recipe
//import tmi.recipe.RecipeParser
//import tmi.recipe.RecipeType
//
//class MultiCrafterParser : RecipeParser<MultiCrafter>() {
//    override fun isTarget(content: Block) = content is MultiCrafter
//
//    override fun parse(content: MultiCrafter): Seq<Recipe> {
//        val recipes: Seq<Recipe> = Seq()
//        for (formula in content.formulas)
//            recipes.add(Recipe(RecipeType.factory, getWrap(content), formula.time).apply {
//                for (item in formula.inputItem) addMaterialInteger(getWrap<Item?>(item.item), item.amount)
//                for (item in formula.inputLiquid) addMaterialFloat(getWrap(item.liquid), item.amount)
//                for (item in formula.outputItem) addProductionInteger(getWrap(item.item), item.amount)
//                for (item in formula.outputLiquid) addProductionFloat(getWrap(item.liquid), item.amount)
//            })
//        return recipes
//    }
//}