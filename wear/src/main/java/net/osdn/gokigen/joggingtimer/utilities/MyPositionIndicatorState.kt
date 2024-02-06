package net.osdn.gokigen.joggingtimer.utilities

import androidx.compose.foundation.ScrollState
import androidx.wear.compose.material.PositionIndicatorState
import androidx.wear.compose.material.PositionIndicatorVisibility


class MyPositionIndicatorState() : PositionIndicatorState
{
    var scrollState = ScrollState(0)

    override val positionFraction: Float
        get() {
            return if (scrollState.maxValue == 0) {
                0f
            } else {
                scrollState.value.toFloat() / scrollState.maxValue
            }
        }

    override fun sizeFraction(scrollableContainerSizePx: Float) =
        if (scrollableContainerSizePx + scrollState.maxValue == 0.0f)
        {
            1.0f
        }
        else
        {
            scrollableContainerSizePx / (scrollableContainerSizePx + scrollState.maxValue)
        }

    override fun visibility(scrollableContainerSizePx: Float): PositionIndicatorVisibility
    {
        if (scrollState.maxValue == 0)
        {
            return (PositionIndicatorVisibility.Hide)
        }
        return (PositionIndicatorVisibility.Show)
    }

    override fun equals(other: Any?): Boolean {
        return (other as? MyPositionIndicatorState)?.scrollState == scrollState
    }
    override fun hashCode(): Int {
        return scrollState.hashCode()
    }

}
