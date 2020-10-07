
package com.app.taiye.taskie.app.model.request

import kotlinx.serialization.Serializable

@Serializable
data class UserDataRequest(val email: String,
                           val password: String,
                           val name: String? = null)