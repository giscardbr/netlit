package netlit.payments.infra.utils

import java.util.*
import java.util.regex.Pattern

class UUIDString(private val uuidString: String) {

    val uuid: UUID? get() = fromString(this.uuidString)

    private fun fromString(id: String?): UUID? {
        return try {
            id?.takeIf { isValid(it) }?.let { UUID.fromString(it) }
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    private fun isValid(vararg ids: String) = sequenceOf(*ids)
            .map { UUID_PATTERN.matcher(it) }
            .all { it.matches() }

    companion object {
        private val UUID_PATTERN = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
    }
}
