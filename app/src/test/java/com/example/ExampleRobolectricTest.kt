package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.MovieEntity
import com.example.data.local.PlaylistEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Vibe", appName)
  }

  @Test
  fun `create playlist and movie entity defaults`() {
    val playlist = PlaylistEntity(
      title = "Midnight Neon Drive",
      description = "Synthwave soundtrack vibes",
      vibeTag = "Midnight Drive",
      emoji = "🌃"
    )
    assertEquals("Midnight Neon Drive", playlist.title)
    assertEquals("Midnight Drive", playlist.vibeTag)

    val movie = MovieEntity(
      title = "Blade Runner 2049",
      director = "Denis Villeneuve",
      vibeTag = "Mind Bending",
      watchStatus = "WATCHED",
      userRating = 5.0f
    )
    assertEquals("Blade Runner 2049", movie.title)
    assertEquals(5.0f, movie.userRating, 0.01f)
    assertTrue(movie.watchStatus == "WATCHED")
  }

  @Test
  fun `launch MainActivity test`() {
    val controller = org.robolectric.Robolectric.buildActivity(MainActivity::class.java).setup()
    org.junit.Assert.assertNotNull(controller.get())
  }
}
