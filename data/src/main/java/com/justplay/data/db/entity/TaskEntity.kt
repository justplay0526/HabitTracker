package com.justplay.data.db.entity

import androidx.annotation.ColorInt
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.justplay.data.TASK_TABLE
import com.justplay.data.db.classPkg.PeriodOption
import com.justplay.data.db.classPkg.RepeatOption
import com.justplay.data.db.classPkg.TaskType
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = TASK_TABLE)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val type: TaskType,

    val name: String,

    @param:ColorInt val colorInt: Int,
    val emoji: String,

    /**
     * periodOption: morning / afternoon / evening
     */
    val periodOption: PeriodOption?,

    /**
     * 是否啟用提醒
     */
    val reminderEnabled: Boolean,

    /**
     * 提醒時間
     * - reminderEnabled = true → 必填
     * - reminderEnabled = false → 必須為 null
     */
    val time: LocalTime? = null,

    // ====== 出現日期（擇一） ======
    /**
     * REGULAR: 起始日
     * ONE_TIME: 必須為 null
     */
    val startDate: LocalDate? = null,

    /**
     * ONE_TIME: 出現日期
     * REGULAR: 必須為 null
     */
    val oneTimeDate: LocalDate? = null,

    // ====== Repeat（REGULAR 才用） ======
    /**
     * repeatOption: daily / weekly / monthly
     */
    val repeatOption: RepeatOption? = null,

    /**
     * weekly: 0..6 (0=Sun..6=Sat)
     */
    val selectedDaySet: Set<Int> = emptySet(),

    /**
     * monthly: 1..31
     */
    val selectedDaysOfMonth: Set<Int> = emptySet(),

    /**
     * 你原 RegularTaskEntity 有的欄位
     */
    val freq: Int? = null,
    val endHabitOn: Boolean = false,
    val endHabitDate: LocalDate? = null,

    val sortOrder: Long = 0L,

    /**
     * 軟刪除, = true 時不顯示出來
     *
     * 避免歷史紀錄變成無頭騎士
     */
    val isArchived: Boolean = false
)