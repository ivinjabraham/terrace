import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import com.example.terrace.features.home.screens.StarSizeCategory

data class StarData(
    val position: Offset,
    val size: Dp,
    val rotation: Float,
    val twinkleSpeed: Int,
    val drawableRes: Int,
    val sizeCategory: StarSizeCategory
)
