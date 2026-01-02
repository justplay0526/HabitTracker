package com.justplay.habittracker.ui.screen.task.valid

import com.justplay.data.db.classPkg.PeriodOption
import com.justplay.data.db.classPkg.TaskType
import com.justplay.data.db.entity.TaskEntity

/**
 * 確保 TaskEntity 資料一致
 * 建議在 Repository upsert 前呼叫
 */
fun TaskEntity.validate(): TaskEntity {

    require(name.isNotBlank()) { "name must not be blank" }
    require(periodOption != null) { "periodOption must not be null" }
    require(periodOption != PeriodOption.ALL) { "periodOption must not be ALL" }

    // reminder 與 time 的關係
    if (reminderEnabled) {
        require(time != null) { "reminderEnabled=true requires time != null" }
    } else {
        require(time == null) { "reminderEnabled=false requires time == null" }
    }

    return when (type) {
        TaskType.ONE_TIME -> {
            require(oneTimeDate != null) { "ONE_TIME must have oneTimeDate" }
            require(startDate == null) { "ONE_TIME must not have startDate" }
            require(repeatOption == null) { "ONE_TIME must not have repeatOption" }
            this
        }

        TaskType.REGULAR -> {
            require(startDate != null) { "REGULAR must have startDate" }
            require(oneTimeDate == null) { "REGULAR must not have oneTimeDate" }
            require(repeatOption != null) { "REGULAR must have repeatOption" }
            this
        }
    }
}