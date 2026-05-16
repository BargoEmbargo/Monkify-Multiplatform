package cz.uhk.monkify.database.remote

import cz.uhk.monkify.database.model.DailyTask
import cz.uhk.monkify.database.model.UserStats
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlin.text.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FirestoreTaskRepository(firestore: FirebaseFirestore) {
    private val tasksCollection = firestore.collection("tasks")
    private val statsCollection = firestore.collection("userStats")

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

    suspend fun uploadStats(userId: String, stats: UserStats) {
        statsCollection.document(userId).set(stats)
    }

    suspend fun getStats(userId: String): UserStats? {
        val doc = statsCollection.document(userId).get()
        return if (doc.exists) doc.data<UserStats>() else null
    }

    fun observeTasks(userId: String): Flow<List<DailyTask>> =
        tasksCollection.document(userId).collection("userTasks").snapshots.map { snapshot ->
            snapshot.documents.map { it.data<DailyTask>() }
        }

    fun observeStats(userId: String): Flow<UserStats?> =
        statsCollection.document(userId).snapshots.map { snapshot ->
            if (snapshot.exists) snapshot.data<UserStats>() else null
        }
}
