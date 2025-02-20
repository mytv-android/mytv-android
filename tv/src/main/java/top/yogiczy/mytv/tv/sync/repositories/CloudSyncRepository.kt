package top.yogiczy.mytv.tv.sync.repositories

import top.yogiczy.mytv.tv.sync.CloudSyncData

interface CloudSyncRepository {
    suspend fun push(data: CloudSyncData): Boolean
    suspend fun pull(): CloudSyncData
}