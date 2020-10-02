
package com.app.taiye.taskie.app.model.request

/**
 * Represents the Add task/note API call JSON body.
 */
class AddTaskRequest(val title: String, val content: String, val taskPriority: Int)