package com.example.terrace

import android.content.Context
import android.graphics.Canvas // Used to draw graphics on the screen
import android.graphics.drawable.Drawable // Represents an image that can be drawn
import android.util.AttributeSet
import android.view.MotionEvent // Detects user touch gestures
import android.view.ScaleGestureDetector // Detects pinch-to-zoom gestures
import android.view.View // Base UI class for custom views
import androidx.core.content.ContextCompat // Provides compatibility for resource loading

/**
 * A custom view that displays a movable and zoomable background image.
 * Users can drag the background and pinch to zoom in/out.
 */
class BackgroundComponent(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    // Holds the background image
    private var backgroundDrawable: Drawable? = null

    // Position of the background image
    private var posX = 0f
    private var posY = 0f

    // Stores the last touch position to calculate movement distance
    private var lastTouchX = 0f
    private var lastTouchY = 0f

    // Zoom variables
    private var scaleFactor = 1.5f // Initial zoom level
    private val minScale = 1.0f // Minimum zoom allowed
    private val maxScale = 5.0f // Maximum zoom allowed

    // Scale gesture detector for pinch zoom
    private val scaleGestureDetector = ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            backgroundDrawable?.let { drawable ->
                val w = drawable.intrinsicWidth.toFloat()
                val h = drawable.intrinsicHeight.toFloat()

                // Save the previous scale factor and compute the new scale factor.
                val oldScale = scaleFactor
                val newScale = (scaleFactor * detector.scaleFactor).coerceIn(minScale, maxScale)

                // The focal point of the gesture in view coordinates.
                val focusX = detector.focusX
                val focusY = detector.focusY

                // Calculate the old centering offsets.
                val oldOffsetX = (width - w * oldScale) / 2f
                val oldOffsetY = (height - h * oldScale) / 2f

                // Calculate the new centering offsets.
                val newOffsetX = (width - w * newScale) / 2f
                val newOffsetY = (height - h * newScale) / 2f

                // Adjust posX and posY so that the image point under the focus remains constant:
                posX = focusX - newOffsetX - (newScale / oldScale) * (focusX - oldOffsetX - posX)
                posY = focusY - newOffsetY - (newScale / oldScale) * (focusY - oldOffsetY - posY)

                scaleFactor = newScale
                invalidate()
            }
            return true
        }
    })

    init {
        // Load the background image from resources (drawable folder)
        backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.stars_background)
    }

    /**
     * Draws the zoomed and positioned background image.
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        backgroundDrawable?.let { drawable ->
            val intrinsicWidth = drawable.intrinsicWidth
            val intrinsicHeight = drawable.intrinsicHeight

            val scaledWidth = (intrinsicWidth * scaleFactor).toInt()
            val scaledHeight = (intrinsicHeight * scaleFactor).toInt()

            // Calculate centering offsets
            val offsetX = (width - scaledWidth) / 2f
            val offsetY = (height - scaledHeight) / 2f

            // Determine allowed panning range:
            // For X, the left edge (posX + offsetX) must be ≤ 0,
            // and the right edge (posX + offsetX + scaledWidth) must be ≥ width.
            // This clamps posX between offsetX and -offsetX.
            posX = posX.coerceIn(offsetX, -offsetX)
            posY = posY.coerceIn(offsetY, -offsetY)

            // Set the bounds of the drawable using the new clamped position
            drawable.setBounds(
                (posX + offsetX).toInt(),
                (posY + offsetY).toInt(),
                (posX + offsetX + scaledWidth).toInt(),
                (posY + offsetY + scaledHeight).toInt()
            )

            drawable.draw(canvas)
        }
    }

    /**
     * Handles user touch gestures for dragging and pinch zoom.
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event) // Detect pinch zoom

        when (event.action) {
            MotionEvent.ACTION_DOWN -> { // When the user touches the screen
                lastTouchX = event.x
                lastTouchY = event.y
            }

            MotionEvent.ACTION_MOVE -> { // When the user moves their finger (dragging)
                val dx = event.x - lastTouchX // Calculate horizontal movement
                val dy = event.y - lastTouchY // Calculate vertical movement

                posX += dx // Update X position of the background
                posY += dy // Update Y position of the background

                lastTouchX = event.x
                lastTouchY = event.y

                invalidate() // Redraw the view with the new position
            }
        }
        return true // Indicate that the touch event has been handled
    }
}