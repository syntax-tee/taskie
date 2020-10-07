
package com.app.taiye.taskie.app.model.response

import kotlinx.serialization.Serializable


/**
 * Holds the user data, to display on the profile screen.
 */

@Serializable
class UserProfileResponse(val id:String, val email: String?,
                          val name: String?)