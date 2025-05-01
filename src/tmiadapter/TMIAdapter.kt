//package tmiadapter
//
//import sequence.core.SqLog
//import tmi.RecipeEntry
//import tmi.TooManyItems
//
//class TMIAdapter : RecipeEntry {
//    override fun afterInit() {
//        SqLog.info("TMI Adapter: After Init")
//
//    }
//
//    override fun init() {
//        SqLog.info("TMI Adapter: Init...")
//        TooManyItems.recipesManager.registerParser(MultiCrafterParser())
//    }
//}
