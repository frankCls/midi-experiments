enum class TD17(val midiNote: Int, val drum: TD17Drum) {

    KICK(36, TD17Drum.KICK),
    SNARE_CROSS_STICK(37, TD17Drum.SNARE),
    SNARE_HEAD(38, TD17Drum.SNARE),
    SNARE_RIM(40, TD17Drum.SNARE),
    TOM_1_HEAD(48, TD17Drum.TOM_1),
    TOM_1_RIM(50, TD17Drum.TOM_1),
    TOM_2_HEAD(45, TD17Drum.TOM_2),
    TOM_2_RIM(47, TD17Drum.TOM_2),
    FLOOR_HEAD(43, TD17Drum.FLOOR),
    FLOOR_RIM(58, TD17Drum.FLOOR),
    HI_HAT_OPEN_BOW(46, TD17Drum.HI_HAT),
    HI_HAT_OPEN_EDGE(26, TD17Drum.HI_HAT),
    HI_HAT_CLOSE_BOW(42, TD17Drum.HI_HAT),
    HI_HAT_CLOSE_EDGE(22, TD17Drum.HI_HAT),
    HI_HAT_PEDAL(44, TD17Drum.HI_HAT_PEDAL),
    CYMBAL_1_BOW(57, TD17Drum.CYMBAL_1),
    CYMBAL_1_EDGE(52, TD17Drum.CYMBAL_1),
    CYMBAL_2_BOW(55, TD17Drum.CYMBAL_2),
    CYMBAL_2_EDGE(49, TD17Drum.CYMBAL_2),
    RIDE_BOW(59, TD17Drum.RIDE),
    RIDE_EDGE(51, TD17Drum.RIDE),
    RIDE_BELL(53, TD17Drum.RIDE);

    companion object {
        fun getDrum(midiNote: Int): TD17Drum {
            return TD17.values().find { it.midiNote == midiNote }!!.drum ?: TD17Drum.KICK
        }
    }
}

enum class TD17Drum {
    KICK,
    SNARE,
    TOM_1,
    TOM_2,
    FLOOR,
    HI_HAT,
    HI_HAT_PEDAL,
    CYMBAL_1,
    CYMBAL_2,
    RIDE
}

