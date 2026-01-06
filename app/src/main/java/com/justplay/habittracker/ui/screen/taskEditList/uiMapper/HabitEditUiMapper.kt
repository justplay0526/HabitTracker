package com.justplay.habittracker.ui.screen.taskEditList.uiMapper

import com.justplay.data.db.entity.TaskEntity
import com.justplay.habittracker.data.HabitEditUi

fun TaskEntity.toHabitEditUi(): HabitEditUi = HabitEditUi(
    id = id,
    color = colorInt,
    title = name,
    icon = iconRes
)