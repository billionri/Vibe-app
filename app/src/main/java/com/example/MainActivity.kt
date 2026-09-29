package com.example

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.VibeAppViewModel
import com.example.ui.VibeTab
import com.example.ui.components.CouponsModal
import com.example.ui.components.GPayCheckoutModal
import com.example.ui.components.NowPlayingBar
import com.example.ui.components.ShareTrackModal
import com.example.ui.components.SpeakerOfferModal
import com.example.ui.components.TshirtOfferModal
import com.example.ui.components.VibeHeader
import com.example.ui.components.VibeTabBar
import com.example.ui.tabs.KaraokeTab
import com.example.ui.tabs.MerchTab
import com.example.ui.tabs.MoviesTab
import com.example.ui.tabs.MusicTab
import com.example.ui.tabs.ReelsTab
import com.example.ui.tabs.StudyTab
import com.example.ui.tabs.TheatreTab
import com.example.ui.tabs.VinylTab
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.VibeAccent
import com.example.ui.theme.VibeBg
import com.example.ui.theme.VibeBorder
import com.example.ui.theme.VibeCard
import com.example.ui.theme.VibeGold
import com.example.ui.theme.VibeMuted
import com.example.ui.theme.VibeSurface
import com.example.ui.theme.VibeText

class MainActivity : ComponentActivity() {

    private val viewModel: VibeAppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val context = LocalContext.current

                val activeTab by viewModel.activeTab.collectAsStateWithLifecycle()
                val isTopAdVisible by viewModel.isTopAdVisible.collectAsStateWithLifecycle()

                // Modals
                val showSpeakerOffer by viewModel.showSpeakerOffer.collectAsStateWithLifecycle()
                val showTshirtOffer by viewModel.showTshirtOffer.collectAsStateWithLifecycle()
                val checkoutProduct by viewModel.checkoutProduct.collectAsStateWithLifecycle()
                val showTasteCard by viewModel.showTasteCard.collectAsStateWithLifecycle()
                val showCouponsModal by viewModel.showCouponsModal.collectAsStateWithLifecycle()
                val shareTrack by viewModel.shareTrack.collectAsStateWithLifecycle()
                val toastMessage by viewModel.showToastMessage.collectAsStateWithLifecycle()

                // Player state
                val currentTrack by viewModel.currentTrack.collectAsStateWithLifecycle()
                val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
                val isShuffle by viewModel.isShuffle.collectAsStateWithLifecycle()
                val isRepeat by viewModel.isRepeat.collectAsStateWithLifecycle()
                val isRadio by viewModel.isRadio.collectAsStateWithLifecycle()

                // Toast Handler
                LaunchedEffect(toastMessage) {
                    toastMessage?.let {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        viewModel.clearToast()
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.safeDrawing,
                    containerColor = VibeBg,
                    bottomBar = {
                        NowPlayingBar(
                            currentTrack = currentTrack,
                            isPlaying = isPlaying,
                            isShuffle = isShuffle,
                            isRepeat = isRepeat,
                            isRadio = isRadio,
                            onTogglePlay = { viewModel.togglePlay() },
                            onNext = { viewModel.playNext() },
                            onPrev = { viewModel.playPrev() },
                            onToggleShuffle = { viewModel.toggleShuffle() },
                            onToggleRepeat = { viewModel.toggleRepeat() },
                            onToggleRadio = { viewModel.toggleRadio() },
                            modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                        )
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(VibeBg)
                    ) {
                        // Vibe Header with Logo, Pro Badge & Top Google AdSense Banner
                        VibeHeader(
                            isTopAdVisible = isTopAdVisible,
                            onCloseTopAd = { viewModel.closeTopAd() },
                            onOpenSpeakerOffer = { viewModel.openSpeakerOffer() }
                        )

                        // 8-Tab Navigation Bar matching vibe-playlist.html
                        VibeTabBar(
                            activeTab = activeTab,
                            onTabSelected = { viewModel.switchTab(it) }
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Tab Content Body
                        Box(modifier = Modifier.weight(1f)) {
                            when (activeTab) {
                                VibeTab.MUSIC -> MusicTab(viewModel = viewModel)
                                VibeTab.MOVIES -> MoviesTab(viewModel = viewModel)
                                VibeTab.KARAOKE -> KaraokeTab(viewModel = viewModel)
                                VibeTab.THEATRE -> TheatreTab(viewModel = viewModel)
                                VibeTab.REELS -> ReelsTab(viewModel = viewModel)
                                VibeTab.STUDY -> StudyTab(viewModel = viewModel)
                                VibeTab.STORE -> MerchTab(viewModel = viewModel)
                                VibeTab.SHOP -> VinylTab(viewModel = viewModel)
                            }
                        }
                    }
                }

                // --- MODALS & AD DIALOGS MATCHING THE SITE ---
                if (showSpeakerOffer) {
                    SpeakerOfferModal(
                        onDismiss = { viewModel.closeSpeakerOffer() },
                        onOrderGPay = {
                            viewModel.merchProducts.value.firstOrNull { it.id == "sp1" }?.let {
                                viewModel.openCheckout(it)
                            }
                        }
                    )
                }

                if (showTshirtOffer) {
                    TshirtOfferModal(
                        onDismiss = { viewModel.closeTshirtOffer() },
                        onOrderGPay = {
                            viewModel.merchProducts.value.firstOrNull { it.id == "sp2" }?.let {
                                viewModel.openCheckout(it)
                            }
                        }
                    )
                }

                checkoutProduct?.let { prod ->
                    GPayCheckoutModal(
                        product = prod,
                        onDismiss = { viewModel.closeCheckout() },
                        onConfirm = { viewModel.completeCheckout(it) }
                    )
                }

                if (showCouponsModal) {
                    CouponsModal(onDismiss = { viewModel.closeCouponsModal() })
                }

                shareTrack?.let { track ->
                    ShareTrackModal(track = track, onDismiss = { viewModel.closeShareTrack() })
                }

                if (showTasteCard) {
                    TasteCardDialog(
                        onDismiss = { viewModel.closeTasteCard() }
                    )
                }
            }
        }
    }
}

@Composable
fun TasteCardDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val tasteSummary = "🎬 MY VIBE CINEMA TASTE CARD 🌟\n" +
            "⚡ Favorite Universe: Marvel & Anime\n" +
            "🍿 Top Films: Avengers Endgame (8.4★), Demon Slayer Mugen Train (8.3★), The Dark Knight (9.0★)\n" +
            "🎧 Soundtrack Vibe: Synthwave & Lo-Fi Night Drive\n" +
            "Curated on VIBE — Music & Movies"

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = VibeSurface,
        title = {
            Text("🎬 My VIBE Taste Card", color = VibeText, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = VibeCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, VibeBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("VIBE MOVIE PROFILE", fontSize = 10.sp, fontWeight = FontWeight.Black, color = VibeGold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(tasteSummary, fontSize = 12.sp, color = VibeText)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("taste", tasteSummary))
                    Toast.makeText(context, "Taste Card copied to clipboard!", Toast.LENGTH_SHORT).show()
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = VibeAccent, contentColor = Color.White)
            ) {
                Text("📋 Copy Taste List")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = VibeMuted)
            }
        }
    )
}
