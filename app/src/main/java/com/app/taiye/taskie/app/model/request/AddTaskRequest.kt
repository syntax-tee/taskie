
package com.app.taiye.taskie.app.model.request

import kotlinx.serialization.Serializable

/**
 * Represents the Add task/note API call JSON body.
 */
@Serializable
class AddTaskRequest(val title: String,
                     val content: String,
                     val taskPriority: Int)
