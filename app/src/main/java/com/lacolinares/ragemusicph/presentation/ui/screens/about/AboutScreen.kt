package com.lacolinares.ragemusicph.presentation.ui.screens.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lacolinares.ragemusicph.R
import com.lacolinares.ragemusicph.custom.Space
import com.lacolinares.ragemusicph.presentation.ui.theme.HyperLinkColor
import com.lacolinares.ragemusicph.presentation.ui.theme.orbitronFamily
import com.lacolinares.ragemusicph.utils.Constants

private const val RAGE_MUSIC_PAGE_TAG = "rage_music_page"
private const val DEV_TAG = "dev_tag"

@Composable
fun AboutScreen() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 48.dp)
            .fillMaxSize()
    ) {
        Text(
            text = context.getString(R.string.company_name),
            color = Color.White,
            fontFamily = orbitronFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )
        Space(48)
        Text(
            text = context.getString(R.string.about_company),
            color = Color.White,
            fontWeight = FontWeight.Light,
            fontSize = 16.sp,
            textAlign = TextAlign.Left,
            lineHeight = 28.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Space(48)
        AnnotatedString(
            text = "Facebook Page: ",
            link = Constants.RAGE_PAGE_WEBSITE,
            segment = Constants.RAGE_PAGE_NAME,
        )
        Space(12)
        AnnotatedString(
            text = "Developer: ",
            link = Constants.DEVELOPER_WEBSITE,
            segment = Constants.DEVELOPER_NAME,
        )
    }
}

@Composable
private fun AnnotatedString(
    text: String,
    link: String,
    segment: String,
) {
    Text(
        text = buildAnnotatedString {
            append(text)
            withLink(
                link = LinkAnnotation.Url(
                    url = link,
                    styles = TextLinkStyles(style = SpanStyle(
                        color = HyperLinkColor,
                        textDecoration = TextDecoration.Underline
                        )
                    )
                )
            ) {
                append(segment)
            }
        },
        color = Color.White
    )
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFF151513)
@Composable
fun About() {
    AboutScreen()
}