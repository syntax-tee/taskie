package com.raywenderlich.android.taskie.ui.notes

import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.taiye.taskie.R
import com.app.taiye.taskie.app.App
import com.app.taiye.taskie.app.networking.NetworkStatusChecker
import com.app.taiye.taskie.app.ui.notes.TaskAdapter
import com.app.taiye.taskie.app.utils.gone
import com.app.taiye.taskie.app.utils.toast
import com.app.taiye.taskie.app.utils.visible
import com.app.taiye.taskie.app.model.Task
import com.raywenderlich.android.taskie.ui.notes.dialog.AddTaskDialogFragment
import com.app.taiye.taskie.app.ui.notes.dialog.TaskOptionsDialogFragment
import kotlinx.android.synthetic.main.fragment_notes.*

/**
 * Fetches and displays notes from the API.
 */
@RequiresApi(Build.VERSION_CODES.M)
class NotesFragment : Fragment(), AddTaskDialogFragment.TaskAddedListener,
    TaskOptionsDialogFragment.TaskOptionSelectedListener {

    private val adapter by lazy { TaskAdapter(::onItemSelected) }
    private val remoteApi = App.remoteApi

    private val networkStatusChecker by lazy {
        NetworkStatusChecker(activity?.getSystemService(ConnectivityManager::class.java))
    }

    override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initListeners()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initUi() {
        progress.visible()
        noData.visible()
        tasksRecyclerView.layoutManager = LinearLayoutManager(context)
        tasksRecyclerView.adapter = adapter
        getAllTasks()
    }

    private fun initListeners() {
        addTask.setOnClickListener { addTask() }
    }

    private fun onItemSelected(taskId: String) {
        val dialog = TaskOptionsDialogFragment.newInstance(taskId)
        dialog.setTaskOptionSelectedListener(this)
        dialog.show(childFragmentManager, dialog.tag)
    }

    override fun onTaskAdded(task: Task) {
        adapter.addData(task)
    }

    private fun addTask() {
        val dialog = AddTaskDialogFragment()
        dialog.setTaskAddedListener(this)
        dialog.show(childFragmentManager, dialog.tag)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getAllTasks() {
        progress.visible()

        networkStatusChecker.performIfConnectedToInternet {
            remoteApi.getTasks { tasks, error ->
                if (tasks.isNotEmpty()) {
                    onTaskListReceived(tasks)
                } else if (error != null || tasks.isEmpty()) {
                    onGetTasksFailed()
                }
            }
        }
    }

    private fun checkList(notes: List<Task>) {
        if (notes.isEmpty()) noData.visible() else noData.gone()
    }

    private fun onTasksReceived(tasks: List<Task>) = adapter.setData(tasks)

    private fun onTaskListReceived(tasks: List<Task>) {
        progress.gone()
        checkList(tasks)
        onTasksReceived(tasks)
    }

    private fun onGetTasksFailed() {
        progress.gone()
        activity?.toast("Failed to fetch tasks!")
    }

    override fun onTaskDeleted(taskId: String) {
        adapter.removeTask(taskId)
        activity?.toast("Task deleted!")
    }

    override fun onTaskCompleted(taskId: String) {
        adapter.removeTask(taskId)
        activity?.toast("Task completed!")
    }
}