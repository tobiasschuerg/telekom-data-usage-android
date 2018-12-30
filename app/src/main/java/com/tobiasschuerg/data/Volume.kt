package com.tobiasschuerg.data

data class Volume(val bytes: Long) {

    operator fun plus(other: Volume): Volume {
        return Volume(bytes + other.bytes)
    }

    operator fun minus(other: Volume): Volume {
        return Volume(bytes - other.bytes)
    }

    override fun toString(): String {
        return if (bytes > 1024) {
            val kb = bytes / 1024.0
            if (kb > 1024) {
                val mb = kb / 1024.0
                if (mb > 1024) {
                    val gb = mb / 1024.0
                    "%.2f".format(gb) + " GB"
                } else {
                    "%.2f".format(mb) + " MB"
                }
            } else {
                "%.2f".format(kb) + " KB"
            }
        } else {
            "%.2f".format(bytes) + " B"
        }
    }
}