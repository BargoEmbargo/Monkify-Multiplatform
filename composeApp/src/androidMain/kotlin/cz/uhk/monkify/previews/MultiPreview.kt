package cz.uhk.monkify.previews

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview

private const val FigmaDeviceWidthPhone = 360
private const val FigmaDeviceHeightPhone = 736
private const val FigmaDeviceWidthTablet = 1098
private const val FigmaDeviceHeightTablet = 660

@Preview(
    name = "1)Phone light",
    showBackground = true,
    widthDp = FigmaDeviceWidthPhone,
    heightDp = FigmaDeviceHeightPhone,
)
annotation class PreviewPhoneLight

@Preview(
    name = "Small phone",
    showBackground = true,
    widthDp = 320,
    heightDp = 470,
)
annotation class PreviewPhoneSmall

@Preview(
    name = "2)Phone dark",
    showBackground = true,
    widthDp = FigmaDeviceWidthPhone,
    heightDp = FigmaDeviceHeightPhone,
    uiMode = UI_MODE_NIGHT_YES,
)
private annotation class PreviewPhoneDark

@Preview(
    name = "4)Phone light long",
    showBackground = true,
    widthDp = FigmaDeviceWidthPhone,
    heightDp = FigmaDeviceWidthTablet,
)
annotation class PreviewPhoneLong

@Preview(
    name = "3)Tablet portrait",
    showBackground = true,
    widthDp = FigmaDeviceHeightTablet,
    heightDp = FigmaDeviceWidthTablet,
)
private annotation class PreviewTabletPortrait

@Preview(
    name = "5)Tablet landscape",
    showBackground = true,
    widthDp = FigmaDeviceWidthTablet,
    heightDp = FigmaDeviceHeightTablet,
)
private annotation class PreviewTabletLandscape

@PreviewPhoneLight
@PreviewPhoneDark
@PreviewPhoneLong
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
private annotation class PreviewScreenPhone

@PreviewTabletPortrait
@PreviewTabletLandscape
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
private annotation class PreviewScreenTablet

@PreviewScreenPhone
@PreviewScreenTablet
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
annotation class PreviewScreen
