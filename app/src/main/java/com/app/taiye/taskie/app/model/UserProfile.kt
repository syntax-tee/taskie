
package com.app.taiye.taskie.app.model

import kotlinx.serialization.Serializable

/**
 * Full user data.
 */
@Serializable
class UserProfile(val email: String, val name: String, val numberOfNotes: Int)