
package com.app.taiye.taskie.app.model.response

import com.raywenderlich.android.taskie.model.Task

data class GetTasksResponse(val notes: List<Task> = mutableListOf())