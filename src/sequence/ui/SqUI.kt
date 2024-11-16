package sequence.ui

import arc.Core
import arc.Events
import arc.math.Mathf
import arc.scene.ui.Button
import arc.scene.ui.Image
import arc.scene.ui.Tooltip
import arc.scene.ui.layout.Table
import mindustry.Vars
import mindustry.ctype.UnlockableContent
import mindustry.game.EventType.ClientLoadEvent
import mindustry.gen.Icon
import mindustry.gen.Tex
import mindustry.type.ItemStack
import mindustry.type.LiquidStack
import mindustry.ui.ItemDisplay
import mindustry.ui.Styles
import mindustry.ui.dialogs.BaseDialog
import mindustry.ui.fragments.MenuFragment.MenuButton
import mindustry.world.meta.StatValue
import sequence.SeqMod
import sequence.content.SqIcon.mainMenu
import sequence.core.SqBundle
import sequence.ui.research.SqResearchDialog
import sequence.ui.wiki.PcWikiDialog
import sequence.world.meta.Formula
import sequence.world.meta.imagine.ImagineEnergyRecord

object SqUI {
    val research: SqResearchDialog by lazy { SqResearchDialog() }
    val mobileMenu: BaseDialog by lazy { MobileMainmenuDialog() }

    val pcWiki: BaseDialog by lazy { PcWikiDialog() }
    val ponder: BaseDialog by lazy { PonderDialog() }

    fun pad(builder: (Table) -> Unit): StatValue =
        StatValue { table ->
            table.row()
            table.table(Styles.grayPanel) { bt ->
                bt.left().top().defaults().padRight(3f).left()
                builder(bt)
            }.padLeft(0f).padTop(5f).padBottom(5f).growX().margin(10f)
            table.row()
        }

    fun Table.pad(builder: (Table) -> Unit) {
        SqUI.pad(builder).display(this)
    }

    fun formula(form: Formula): Button {
        val button = Button()
        button.style = Styles.underlineb
        button.table { t: Table ->
            t.table { bt: Table ->
                bt.left()
                uiILPFormula(
                    form.inputItem,
                    form.inputLiquid,
                    form.inputPower * form.time,
                    form.inputImagine,
                    bt,
                    false
                )
                bt.left()
                bt.add()
                bt.table { ct: Table -> ct.add(Image(Icon.rightSmall)).grow().fill() }
                    .padLeft(10f).padRight(10f)
                bt.add()
                if (form.liquidSecond) {
                    val liquidStacks = Array(form.outputLiquid.size) {
                        LiquidStack(form.outputLiquid[it].liquid, form.outputLiquid[it].amount * form.time)
                    }
                    uiILPFormula(form.outputItem, liquidStacks, form.outputPower, form.outputImagine, bt, false)
                } else uiILPFormula(
                    form.outputItem,
                    form.outputLiquid,
                    form.outputPower * form.time,
                    form.outputImagine,
                    bt,
                    false
                )
            }.left().top()
        }.padLeft(5f).margin(0f).growX().left().top()
        return button
    }

    fun uiILPFormula(
        items: Array<ItemStack>,
        liquids: Array<LiquidStack>,
        power: Float,
        record: ImagineEnergyRecord,
        bt: Table,
        butt: Boolean
    ) {
        var display: Table
        var button: Button
        for (stack in items) {
            display = ItemDisplay(stack.item, stack.amount, false)
            display.addListener(tooltip(stack.item))
            if (butt) {
                button = Button(Styles.grayPanel)
                button.add(display).size(Vars.iconMed)
                button.clicked { Vars.ui.content.show(stack.item) }
                bt.add(button).size(Vars.iconMed).left().padLeft(2f).padRight(2f)
            } else {
                bt.add(display).size(Vars.iconMed).left().padLeft(2f).padRight(2f)
            }
            bt.add()
        }
        for (stack in liquids) {
            display = SqLiquidDisplay(stack.liquid, stack.amount, false, false)
            display.addListener(tooltip(stack.liquid))
            if (butt) {
                button = Button(Styles.grayPanel)
                button.add(display).size(Vars.iconMed)
                button.clicked { Vars.ui.content.show(stack.liquid) }
                bt.add(button).size(Vars.iconMed).left().padLeft(2f).padRight(2f)
            } else {
                bt.add(display).size(Vars.iconMed).left().padLeft(2f).padRight(2f)
            }
            bt.add()
        }
        if (!Mathf.zero(power)) bt.add(PowerDisplay(power))
        if (!record.zero()) {
            bt.add(ImagineEnergyDisplay(record))
        }
    }

    private fun tooltip(content: UnlockableContent) = Tooltip {
        it.background(Tex.button).add(
            content.localizedName + if (Core.settings.getBool("console")) "\n[gray]${content.name}" else ""
        )
    }

    fun load() {
        Events.on(ClientLoadEvent::class.java) {
            Vars.ui.menufrag.addButton(
                MenuButton(
                    SqBundle.mod("mainmenu.text"),
                    mainMenu, { mobileMenu.show() },
                    MenuButton(
                        SqBundle.mod("mainmenu.wiki.text"),
                        Icon.book
                    ) { pcWiki.show() }
                )
            )
            if (SeqMod.dev)
                Vars.ui.menufrag.addButton(
                    MenuButton(
                        "Test ponder",
                        Icon.admin
                    ) { ponder.show() }
                )
        }
    }
}
