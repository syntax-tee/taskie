
package com.app.taiye.taskie.app.model.request

import com.squareup.moshi.Json

data class UserDataRequest(@field:Json(name = "email")val email: String,
                           @field:Json(name = "password") val password: String,
                           @field:Json(name = "name") val name: String? = null)