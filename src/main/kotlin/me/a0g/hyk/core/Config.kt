package me.a0g.hyk.core

import gg.essential.api.utils.GuiUtil
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.*
import me.a0g.hyk.core.components.PositionDouble
import me.a0g.hyk.gui.Gui
import java.awt.Color
import java.io.File

class Config(file: File) : Vigilant(file, "Hyk", sortingBehavior = ConfigSorting) {

    //Position
    private var sprintStandard = PositionDouble(5.0, 25.0, 1.0)
    @Property(type = PropertyType.TEXT,name = "sprint",category = "Position",hidden = true)
    var sprintPos: String = sprintStandard.toString()

    private var timeStandard = PositionDouble(5.0, 5.0, 1.0)
    @Property(type = PropertyType.TEXT,name = "time",category = "Position",hidden = true)
    var timePos: String = timeStandard.toString()

    private var fpsStandard = PositionDouble(35.0, 5.0, 1.0)
    @Property(type = PropertyType.TEXT,name = "fps",category = "Position",hidden = true)
    var fpsPos: String = fpsStandard.toString()

    private var cpsStandard = PositionDouble(85.0, 5.0, 1.0)
    @Property(type = PropertyType.TEXT,name = "cps",category = "Position",hidden = true)
    var cpsPos: String = cpsStandard.toString()

    private var armorStandard = PositionDouble(5.0, 100.0, 1.0)
    @Property(type = PropertyType.TEXT,name = "armor",category = "Position",hidden = true)
    var armorPos: String = armorStandard.toString()

    private var fryctStandart = PositionDouble(500.0, 5.0, 1.0)
    @Property(type = PropertyType.TEXT,name = "fryct",category = "Position",hidden = true)
    var fryctPos: String = fryctStandart.toString()

    private var visitorsOffersStandard = PositionDouble(5.0, 80.0, 1.0)
    @Property(type = PropertyType.TEXT,name = "visitors",category = "Position",hidden = true)
    var visitorsOffersPos: String = visitorsOffersStandard.toString()

    fun setStandard(){
        sprintPos = sprintStandard.toString()
        timePos = timeStandard.toString()
        fpsPos = fpsStandard.toString()
        cpsPos = cpsStandard.toString()
        armorPos = armorStandard.toString()
        fryctPos = fryctStandart.toString()
    }

    //Client
    @Property(
        type = PropertyType.SWITCH, name = "chatcopy", subcategory = "General", category = "Client",
        description = "Allows you to copy chat line with ALT + Right mouse click ( copy With Formatting when used with left shift)"
    )
    var chatCopy = true

    @Property(
        type = PropertyType.SWITCH, name = "Path", subcategory = "General", category = "Client",
        description = "Allows you to add waypoint paths. /hyk path - for help"
    )
    var path = false

    //General
    @Property(
        type = PropertyType.BUTTON, name = "Gui Edit", category = "General",
        description = "Edit mod gui."
    )
    fun editGui() {
        GuiUtil.open(Gui())
    }

    @Property(
        type = PropertyType.SWITCH, name = "AutoUpdate", category = "General",
        description = "Auto update the mod when new version released."
    )
    var autoUpdate = true

    @Property(
        type = PropertyType.SWITCH, name = "NameChange", category = "General", subcategory = "Name",
        description = "Toggle custom name."
    )
    val nameChanger = false

    @Property(
        type = PropertyType.TEXT, name = "CustomName", subcategory = "Name", category = "General",
        description = "Name to display as your custom name"
    )
    val customName = "&6Hyk"

    @Property(
        type = PropertyType.SWITCH, name = "RPC", category = "General", subcategory = "Discord",
        description = "Discord RPC."
    )
    val rpc = false


    @Property(
        type = PropertyType.TEXT, name = "Api key", category = "Hypixel", subcategory = "z", protectedText = true,
        description = "Hypixel ApiKey for stats."
    )
    var apiKey = ""


    @Property(
        type = PropertyType.SWITCH, name = "AutoSprint", category = "General",
        description = "Toggle AutoSprint."
    )
    val autoSprintEnabled = true

    @Property(
        type = PropertyType.TEXT, name = "AutoSprint Text", category = "Render", subcategory = "sprint",
        description = "AutoSprint text.", placeholder = "Sprint"
    )
    var sprintText = "Sprint"

    @Property(
        type = PropertyType.COLOR, name = "AutoSprint Color", category = "Render", subcategory = "sprint",
        description = "AutoSprint display color."
    )
    var sprintColor: Color = Color.WHITE

    @Property(
        type = PropertyType.SWITCH, name = "Time", category = "Render", subcategory = "time",
        description = "Time display."
    )
    val timeDisplay = false

    @Property(
        type = PropertyType.COLOR, name = "Time Color", category = "Render", subcategory = "time",
        description = "Time display color."
    )
    val timeDisplayColor: Color = Color.WHITE

    @Property(
        type = PropertyType.SWITCH, name = "ArmorHud", category = "Render", subcategory = "armor",
        description = "Armor display."
    )
    val armorHud = false


    @Property(
        type = PropertyType.SWITCH, name = "FPS", category = "Render", subcategory = "fps",
        description = "FPS display."
    )
    val fpsDisplay = false

    @Property(
        type = PropertyType.COLOR, name = "FPS Color", category = "Render", subcategory = "fps",
        description = "FPS display color."
    )
    val fpsDisplayColor: Color = Color.WHITE


    @Property(
        type = PropertyType.SWITCH, name = "CPS", category = "Render", subcategory = "cps",
        description = "CPS display."
    )
    val cpsDisplay = false

    @Property(
        type = PropertyType.COLOR, name = "CPS Color", category = "Render", subcategory = "cps",
        description = "CPS display color."
    )
    val cpsDisplayColor: Color = Color.WHITE

    @Property(
        type = PropertyType.SWITCH, name = "Fryct", category = "Render", subcategory = "fryct",
        description = "Timer until fryct come back."
    )
    val fryctDisplay = false

    @Property(
        type = PropertyType.TEXT, name = "Fryct Text", category = "Render", subcategory = "fryct",
        description = "Fryct timer text."
    )
    var fryctText = "fryct comeback in"




    //SKyBlock

    //Skyblock
    @Property(
        type = PropertyType.SWITCH, name = "Price", category = "SkyBlock", subcategory = "Price",
        description = "Display price per each on bin"
    )
    val sbPriceEach = false


    @Property(
        type = PropertyType.SWITCH, name = "Skiblock", category = "SkyBlock", subcategory = "Extra",
        description = "In 2023 april admins made scoreboard title to 'SKIBLOCK' breaking all the mods. So if it will happen in the future again there is this setting."
    )
    var noAprilFool = false

    @Property(
        type = PropertyType.SWITCH, name = "GuessTheBuild", category = "Hypixel", subcategory = "BuildBattle",
        description = "Shows possible Guess the build words /hyk bbh."
    )
    val bbh = true

    @Property(
        type = PropertyType.SWITCH, name = "GuessTheBuildSend", category = "Hypixel", subcategory = "BuildBattle",
        description = "Automatically send the word when there is only 1 possible."
    )
    val bbhAutoSend = true

    @Property(
        type = PropertyType.SWITCH, name = "Autotip", category = "Hypixel",
        description = "Automatically tips all player every 5 minutes."
    )
    val autoTip = false

    @Property(
        type = PropertyType.SWITCH, name = "Packets", category = "Client", subcategory = "Extra",
        description = "Log all received packets."
    )
    val receivePacketLog = false

    @Property(
        type = PropertyType.SWITCH, name = "Packets", category = "Client", subcategory = "Extra",
        description = "Log all send packets."
    )
    val sendPacketLog = false

    @Property(
        type = PropertyType.SWITCH, name = "HitFix", category = "Client", subcategory = "Best",
        description = "Toggle HitFix(just google HitFix)."
    )
    val hitFix = true

    fun init() {
        initialize()

        setCategoryDescription("General", "All general features")
        setSubcategoryDescription("General", "Name", "Allows you to rename yourself. Colors support &[0-9a-f]{name}")

        setCategoryDescription("Hypixel", "Hypixel related features")
        setCategoryDescription("SkyBlock", "Features used in SkyBlock")
        setSubcategoryDescription("SkyBlock", "Garden", "Garden stuff, there is also farming gui /hyk farm")


        setCategoryDescription("Render", "Components that renders in the gui")
    }

    private object ConfigSorting : SortingBehavior() {
        override fun getCategoryComparator(): Comparator<in Category> = Comparator { o1, o2 ->
            if (o1.name == "General") return@Comparator -1
            if (o2.name == "General") return@Comparator 1
            else compareValuesBy(o1, o2) {
                it.name
            }
        }

        override fun getSubcategoryComparator(): Comparator<in Map.Entry<String, List<PropertyData>>> = Comparator { o1, o2 ->
            if (o1.key == "Garden") return@Comparator -1
            if (o2.key == "Garden") return@Comparator 1

            if (o1.key == "Extra") return@Comparator 1
            if (o2.key == "Extra") return@Comparator -1

            else compareValuesBy(o1, o2) {
                it.key
            }
        }
    }
}