package com.homeofhope.app.ui.screens.member

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.homeofhope.app.data.local.entity.ImpactStory
import com.homeofhope.app.ui.theme.HomeOfHopeTheme

@Preview(showBackground = true)
@Composable
fun StoryCardPreview() {
    HomeOfHopeTheme {
        StoryCard(
            ImpactStory(
                title = "Education for All",
                description = "Thanks to your donations, 50 more children started school this month.",
                date = System.currentTimeMillis()
            )
        )
    }
}
