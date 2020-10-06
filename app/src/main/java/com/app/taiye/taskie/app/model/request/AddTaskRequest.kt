
package com.app.taiye.taskie.app.model.request

import com.squareup.moshi.Json

/**
 * Represents the Add task/note API call JSON body.
 */
class AddTaskRequest(@field:Json(name = "title") val title: String,
                     @field:Json(name = "content") val content: String,
                     @field:Json(name = "taskPriority") val taskPriority: Int)
