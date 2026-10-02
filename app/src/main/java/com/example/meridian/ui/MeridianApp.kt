package com.example.meridian.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meridian.data.model.SyncStatus
import com.example.meridian.data.repository.MeridianRepo
import com.example.meridian.ui.components.DevPanelBottomSheet
import com.example.meridian.ui.components.OutboxBanner
import com.example.meridian.ui.components.PersonaSwitcher
import com.example.meridian.ui.components.SyncIndicator
import com.example.meridian.ui.screens.*
import com.example.meridian.ui.screens.onboarding.OnboardingDialog
import com.example.meridian.ui.screens.privacy.PrivacyCenterSheet
import com.example.meridian.ui.screens.settings.SpaceManagementSheet
import com.example.meridian.ui.screens.story.OurStorySheet
import com.example.meridian.ui.screens.story.StreakSheet
import com.example.meridian.ui.screens.vault.VaultInviteDialog
import com.example.meridian.ui.screens.vault.VaultPinLockSheet
import com.example.meridian.ui.screens.vault.VaultScreen
import com.example.ui.theme.FrauncesFontFamily
import com.example.ui.theme.MeridianApricot

enum class MeridianTab(val title: String) {
    US("Us"),
    TALK("Talk"),
    TOGETHER("Together"),
    CARE("Care"),
    PLAN("Plan")
}

@Composable
fun MeridianApp(
    repo: MeridianRepo,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(MeridianTab.US) }
    var showDevPanel by remember { mutableStateOf(false) }
    var showPrivacyCenter by remember { mutableStateOf(false) }
    var showOnboarding by remember { mutableStateOf(false) }
    var showOurStory by remember { mutableStateOf(false) }
    var showStreak by remember { mutableStateOf(false) }
    var showVaultPinSheet by remember { mutableStateOf(false) }
    var showVaultScreen by remember { mutableStateOf(false) }
    var showVaultInviteSheet by remember { mutableStateOf(false) }
    var showSpaceManagementSheet by remember { mutableStateOf(false) }

    val activeUser by repo.activeUser.collectAsState()
    val partnerUser by repo.partnerUser.collectAsState()
    val syncStatus by repo.syncStatus.collectAsState()
    val hasUnreadTalk by repo.hasUnreadTalk.collectAsState()
    val isFingerprintVerified by repo.isFingerprintVerified.collectAsState()
    val isPrivateModeActive by repo.isPrivateModeActive.collectAsState()
    val isVaultUnlocked by repo.isVaultUnlocked.collectAsState()

    if (showVaultScreen) {
        VaultScreen(
            repo = repo,
            onQuickHide = {
                repo.lockVault()
                showVaultScreen = false
            }
        )
    } else {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .testTag("meridian_app_scaffold"),
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = {
                MeridianTopBar(
                    activeUid = activeUser.uid,
                    syncStatus = syncStatus,
                    isPrivateModeActive = isPrivateModeActive,
                    onSwitchPersona = { repo.switchPersona(it) },
                    onOpenDev = { showDevPanel = true },
                    onOpenPrivacy = { showPrivacyCenter = true },
                    onOpenOnboarding = { showOnboarding = true },
                    onOpenOurStory = { showOurStory = true },
                    onOpenStreak = { showStreak = true },
                    onOpenVault = {
                        if (isVaultUnlocked) {
                            showVaultScreen = true
                        } else {
                            showVaultPinSheet = true
                        }
                    },
                    onOpenVaultInvite = { showVaultInviteSheet = true },
                    onOpenSpaceSettings = { showSpaceManagementSheet = true }
                )
            },
            bottomBar = {
                MeridianBottomBar(
                    selectedTab = selectedTab,
                    hasUnreadTalk = hasUnreadTalk,
                    onSelectTab = { selectedTab = it }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Outbox status banner if offline or queued items exist
                OutboxBanner(
                    syncStatus = syncStatus,
                    queuedCount = if (syncStatus == SyncStatus.SAVED_OFFLINE) 1 else 0,
                    onRetry = { repo.setOfflineSimulation(false) }
                )

                Box(modifier = Modifier.fillMaxSize()) {
                    when (selectedTab) {
                        MeridianTab.US -> UsScreen(repo = repo)
                        MeridianTab.TALK -> TalkScreen(repo = repo)
                        MeridianTab.TOGETHER -> TogetherScreen(repo = repo)
                        MeridianTab.CARE -> CareScreen(repo = repo)
                        MeridianTab.PLAN -> PlanScreen(repo = repo)
                    }
                }
            }

            if (showDevPanel) {
                DevPanelBottomSheet(
                    activeUid = activeUser.uid,
                    onSwitchPersona = { repo.switchPersona(it) },
                    onToggleOffline = { repo.setOfflineSimulation(it) },
                    onDismiss = { showDevPanel = false }
                )
            }

            if (showPrivacyCenter) {
                PrivacyCenterSheet(
                    myProfile = activeUser,
                    partnerProfile = partnerUser,
                    isFingerprintVerified = isFingerprintVerified,
                    onMarkVerified = { repo.markFingerprintVerified() },
                    onDismiss = { showPrivacyCenter = false }
                )
            }

            if (showOnboarding) {
                OnboardingDialog(
                    onDismiss = { showOnboarding = false }
                )
            }

            if (showOurStory) {
                OurStorySheet(
                    repo = repo,
                    onDismiss = { showOurStory = false }
                )
            }

            if (showStreak) {
                StreakSheet(
                    repo = repo,
                    onDismiss = { showStreak = false }
                )
            }

            if (showVaultPinSheet) {
                VaultPinLockSheet(
                    onUnlockSuccess = {
                        showVaultPinSheet = false
                        repo.unlockVault("123456")
                        showVaultScreen = true
                    },
                    onDismiss = { showVaultPinSheet = false }
                )
            }

            if (showVaultInviteSheet) {
                VaultInviteDialog(
                    partnerName = partnerUser.displayName,
                    onOptIn = { repo.optInPrivateMode() },
                    onDismiss = { showVaultInviteSheet = false }
                )
            }

            if (showSpaceManagementSheet) {
                SpaceManagementSheet(
                    repo = repo,
                    onDismiss = { showSpaceManagementSheet = false }
                )
            }
        }
    }
}

@Composable
private fun MeridianTopBar(
    activeUid: String,
    syncStatus: SyncStatus,
    isPrivateModeActive: Boolean,
    onSwitchPersona: (String) -> Unit,
    onOpenDev: () -> Unit,
    onOpenPrivacy: () -> Unit,
    onOpenOnboarding: () -> Unit,
    onOpenOurStory: () -> Unit,
    onOpenStreak: () -> Unit,
    onOpenVault: () -> Unit,
    onOpenVaultInvite: () -> Unit,
    onOpenSpaceSettings: () -> Unit
) {
    var showKeepsakesMenu by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.background,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Eos",
                    fontFamily = FrauncesFontFamily,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(8.dp))
                SyncIndicator(status = syncStatus)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                PersonaSwitcher(
                    activeUid = activeUid,
                    onSelectPersona = onSwitchPersona
                )

                // Quiet Key Icon: Visible ONLY when Private Mode is active for both
                if (isPrivateModeActive) {
                    IconButton(
                        onClick = onOpenVault,
                        modifier = Modifier.size(34.dp).testTag("vault_key_button")
                    ) {
                        Icon(
                            Icons.Outlined.Key,
                            contentDescription = "Private Vault",
                            tint = MeridianApricot,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }

                // Keepsakes Dropdown Menu (Our Story, Streak, Export, Space Settings)
                Box {
                    IconButton(
                        onClick = { showKeepsakesMenu = true },
                        modifier = Modifier.size(34.dp).testTag("keepsakes_button")
                    ) {
                        Icon(
                            Icons.Outlined.AutoStories,
                            contentDescription = "Keepsakes",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showKeepsakesMenu,
                        onDismissRequest = { showKeepsakesMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Our Story & Visits 📖") },
                            onClick = {
                                showKeepsakesMenu = false
                                onOpenOurStory()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Connection Days & Streak 🧭") },
                            onClick = {
                                showKeepsakesMenu = false
                                onOpenStreak()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Private Mode (18+) Settings 🔒") },
                            onClick = {
                                showKeepsakesMenu = false
                                onOpenVaultInvite()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Data Export & Space Settings 📦") },
                            onClick = {
                                showKeepsakesMenu = false
                                onOpenSpaceSettings()
                            }
                        )
                    }
                }

                IconButton(
                    onClick = onOpenPrivacy,
                    modifier = Modifier.size(34.dp).testTag("privacy_button")
                ) {
                    Icon(
                        Icons.Outlined.Shield,
                        contentDescription = "Privacy Center",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = onOpenOnboarding,
                    modifier = Modifier.size(34.dp).testTag("pairing_button")
                ) {
                    Icon(
                        Icons.Outlined.Link,
                        contentDescription = "Pairing & Recovery Key",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(19.dp)
                    )
                }

                IconButton(
                    onClick = onOpenDev,
                    modifier = Modifier.size(34.dp).testTag("dev_tools_button")
                ) {
                    Icon(
                        Icons.Outlined.Build,
                        contentDescription = "Developer tools & Time machine",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MeridianBottomBar(
    selectedTab: MeridianTab,
    hasUnreadTalk: Boolean,
    onSelectTab: (MeridianTab) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp
    ) {
        MeridianTab.values().forEach { tab ->
            val isSelected = selectedTab == tab
            val icon = when (tab) {
                MeridianTab.US -> if (isSelected) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder
                MeridianTab.TALK -> if (isSelected) Icons.Filled.Mail else Icons.Outlined.MailOutline
                MeridianTab.TOGETHER -> if (isSelected) Icons.Filled.Celebration else Icons.Outlined.Celebration
                MeridianTab.CARE -> if (isSelected) Icons.Filled.Spa else Icons.Outlined.Spa
                MeridianTab.PLAN -> if (isSelected) Icons.Filled.Flag else Icons.Outlined.OutlinedFlag
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = { onSelectTab(tab) },
                icon = {
                    Box {
                        Icon(icon, contentDescription = tab.title)
                        if (tab == MeridianTab.TALK && hasUnreadTalk) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(MeridianApricot)
                                    .align(Alignment.TopEnd)
                            )
                        }
                    }
                },
                label = { Text(text = tab.title, fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MeridianApricot,
                    selectedTextColor = MeridianApricot,
                    indicatorColor = MeridianApricot.copy(alpha = 0.15f),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
            )
        }
    }
}
