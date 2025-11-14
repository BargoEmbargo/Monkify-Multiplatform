package cz.uhk.monkify.database.remote

import cz.uhk.monkify.database.model.DailyTask
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlin.text.get

class FirestoreTaskRepository(firestore: FirebaseFirestore) {
    private val tasksCollection = firestore.collection("tasks")

    suspend fun uploadTask(userId: String, task: DailyTask) {
        tasksCollection.document(userId).collection("userTasks").document(task.id.toString()).set(task)
    }

    suspend fun deleteAllTasks(userId: String) {
        val userTasks = tasksCollection.document(userId).collection("userTasks").get().documents
        userTasks.forEach { it.reference.delete() }
    }

    suspend fun deleteTask(userId: String, taskId: Int) {
        tasksCollection.document(userId).collection("userTasks").document(taskId.toString()).delete()
    }

    suspend fun getTasks(userId: String): List<DailyTask> = tasksCollection.document(userId).collection("userTasks").get().documents.map {
        it.data<DailyTask>()
    }
}
