package com.routineflow.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.MutatorMutex
import androidx.compose.runtime.Stable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.sign

/**
 * Native Compose adaptation of ClearPDF's DampedDragAnimation.
 *
 * ClearPDF keeps the tab selection as a fractional value while dragging, then rounds it only
 * when the finger is released. This preserves the reference dock's physical "liquid" travel
 * instead of jumping between discrete tabs.
 */
@Stable
class ClearPdfDockMotion(
    private val scope: CoroutineScope,
    initialIndex: Int,
    private val maxIndex: Int,
    private val reducedMotion: Boolean
) {
    private val mutex = MutatorMutex()
    private val target = Animatable(initialIndex.toFloat())
    private val position = Animatable(initialIndex.toFloat())
    private val pressProgress = Animatable(0f)
    private val scaleX = Animatable(1f)
    private val scaleY = Animatable(1f)
    private var settleJob: Job? = null
    private var dragJob: Job? = null
    private var dragValue = initialIndex.toFloat()

    val value: Float get() = position.value
    val targetValue: Float get() = target.value
    val pressed: Float get() = pressProgress.value
    val stretchX: Float get() = scaleX.value
    val stretchY: Float get() = scaleY.value

    private val positionSpec
        get() = if (reducedMotion) {
            spring<Float>(dampingRatio = 1f, stiffness = Spring.StiffnessMedium)
        } else {
            spring<Float>(dampingRatio = 0.82f, stiffness = 1000f)
        }

    private val pressSpec
        get() = if (reducedMotion) {
            spring<Float>(dampingRatio = 1f, stiffness = Spring.StiffnessMedium)
        } else {
            spring<Float>(dampingRatio = 0.72f, stiffness = 900f)
        }

    private val releaseSpec
        get() = if (reducedMotion) {
            spring<Float>(dampingRatio = 1f, stiffness = Spring.StiffnessMedium)
        } else {
            spring<Float>(dampingRatio = 0.46f, stiffness = 420f)
        }

    fun press() {
        settleJob?.cancel()
        scope.launch {
            launch { pressProgress.animateTo(1f, pressSpec) }
            launch { scaleX.animateTo(if (reducedMotion) 1f else 1.25f, pressSpec) }
            launch { scaleY.animateTo(if (reducedMotion) 1f else 1.25f, pressSpec) }
        }
    }

    fun startAt(index: Float) {
        val safeIndex = index.coerceIn(0f, maxIndex.toFloat())
        dragValue = safeIndex
        dragJob?.cancel()
        scope.launch {
            mutex.mutate {
                target.snapTo(safeIndex)
                position.snapTo(safeIndex)
            }
        }
    }

    fun dragBy(deltaX: Float, tabWidthPx: Float) {
        if (tabWidthPx <= 0f) return
        val next = (dragValue + deltaX / tabWidthPx).coerceIn(0f, maxIndex.toFloat())
        dragValue = next
        dragJob?.cancel()
        dragJob = scope.launch {
            mutex.mutate {
                target.snapTo(next)
                position.snapTo(next)
            }
        }
        if (!reducedMotion) {
            val direction = sign(deltaX)
            scope.launch {
                scaleX.animateTo(1.25f + abs(deltaX / tabWidthPx).coerceAtMost(0.08f), pressSpec)
                scaleY.animateTo(1.25f - direction * 0.015f, pressSpec)
            }
        }
    }

    fun release(): Int {
        val selected = dragValue.roundToInt().coerceIn(0, maxIndex)
        settleJob?.cancel()
        settleJob = scope.launch {
            mutex.mutate {
                launch { target.animateTo(selected.toFloat(), releaseSpec) }
                launch { position.animateTo(selected.toFloat(), releaseSpec) }
                launch { pressProgress.animateTo(0f, releaseSpec) }
                launch { scaleX.animateTo(1f, releaseSpec) }
                launch { scaleY.animateTo(1f, releaseSpec) }
            }
        }
        return selected
    }

    fun cancelPress() {
        settleJob?.cancel()
        scope.launch {
            launch { pressProgress.animateTo(0f, releaseSpec) }
            launch { scaleX.animateTo(1f, releaseSpec) }
            launch { scaleY.animateTo(1f, releaseSpec) }
        }
    }

    fun sync(index: Int) {
        val safeIndex = index.coerceIn(0, maxIndex).toFloat()
        dragValue = safeIndex
        scope.launch {
            mutex.mutate {
                launch { target.animateTo(safeIndex, positionSpec) }
                launch { position.animateTo(safeIndex, positionSpec) }
                launch { pressProgress.animateTo(0f, releaseSpec) }
                launch { scaleX.animateTo(1f, releaseSpec) }
                launch { scaleY.animateTo(1f, releaseSpec) }
            }
        }
    }

    fun select(index: Int) {
        val safeIndex = index.coerceIn(0, maxIndex).toFloat()
        dragValue = safeIndex
        settleJob?.cancel()
        scope.launch {
            mutex.mutate {
                launch { target.animateTo(safeIndex, positionSpec) }
                launch { position.animateTo(safeIndex, positionSpec) }
                launch { pressProgress.animateTo(0f, releaseSpec) }
                launch { scaleX.animateTo(1f, releaseSpec) }
                launch { scaleY.animateTo(1f, releaseSpec) }
            }
        }
    }

    private fun Float.roundToInt(): Int = kotlin.math.round(this).toInt()
}
