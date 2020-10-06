
package com.app.taiye.taskie.app.model.response

import com.app.taiye.taskie.app.model.Task
import com.squareup.moshi.Json

data class GetTasksResponse(@field:Json(name = "notes")val notes: List<Task> = mutableListOf())