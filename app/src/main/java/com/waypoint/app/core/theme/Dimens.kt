package com.waypoint.app.core.theme

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Reverse-engineered scale from the literal dp/sp values observed across
 * res/drawable and res/layout. The XML source has no named scale of its
 * own (no dimens.xml) - these names are new, the values are not.
 */

// Corner radii - every distinct android:radius value found across
// res/drawable/*.xml, named by its most common usage.
val RadiusHandle = 2.dp // new-trip sheet drag handle
val RadiusThumbnail = 12.dp // place/booking thumbnail blocks, calendar day cells
val RadiusTogglePill = 12.5.dp // settings notification toggle track
val RadiusButton = 14.dp // cards, filled/outline buttons, inputs, search boxes, widgets
val RadiusRow = 16.dp // row surfaces, map CTA banner, empty-state boxes
val RadiusCard = 18.dp // card surfaces, filter pill chips
val RadiusChip = 20.dp // status badges, summary/add/pdf chips
val RadiusHero = 22.dp // home upcoming-trip hero card
val RadiusDeco = 24.dp // login/register decorative block

// Spacing - the values that recur across multiple screens. Per-screen
// root padding (top/bottom in particular) varies screen to screen in the
// source and is NOT a consistent scale, so it stays inline on each
// converted screen rather than being forced into shared constants here.
val SpacingXs = 4.dp
val SpacingSm = 8.dp // secondary stack gap (calendar rows, wrapped filter chips)
val SpacingInline = 12.dp // thumbnail-to-text horizontal gap in rows
val SpacingMd = 16.dp // default gap between stacked elements (most common margin)
val SpacingLg = 24.dp // settings row-to-row gap
val SpacingScreenH = 22.dp // most screens' left/right root padding

// Type scale (sp) - every distinct textSize value found across
// res/layout/*.xml.
val TextSize10 = 10.sp
val TextSize11 = 11.sp
val TextSize12 = 12.sp
val TextSize13 = 13.sp
val TextSize14 = 14.sp
val TextSize15 = 15.sp
val TextSize17 = 17.sp
