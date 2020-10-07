package com.app.taiye.taskie.app.model.response

import com.app.taiye.taskie.app.model.Task
import kotlinx.serialization.Serializable

@Serializable
data class GetTasksResponse(val notes: List<Task> = mutableListOf())