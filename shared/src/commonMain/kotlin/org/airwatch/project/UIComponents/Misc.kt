package org.airwatch.project.UIComponents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun CheckBoxRow(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .toggleable(value = checked, onValueChange = onCheckedChange, role = Role.Checkbox)
            .padding(vertical = 6.dp, horizontal = 4.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null,
            colors = CheckboxDefaults.colors(
                checkedColor = RadarColors.Amber,
                uncheckedColor = RadarColors.TextMuted,
                checkmarkColor = RadarColors.Background
            )
        )
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun ColumnDivider() {
    HorizontalDivider(thickness = 1.dp, color = RadarColors.Border)
}
@Composable
fun RowDivider() {
    VerticalDivider(thickness = 1.dp, color = RadarColors.Border)
}

@Composable
fun BasicButton(
    onClick: () -> Unit,
    selected: Boolean = false,
    content: @Composable RowScope.() -> Unit
) {
    if (selected) {
        androidx.compose.material3.Button(
            onClick = onClick,
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = RadarColors.TextPrimary,
                contentColor = RadarColors.Background
            ),
            content = content
        )
    } else {
        OutlinedButton(
            onClick = onClick,
            shape = RoundedCornerShape(4.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, RadarColors.Amber),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = RadarColors.Amber),
            content = content
        )
    }
}

@Composable
fun <T> AutoScrollLazyColumn(
    items: List<T>,
    modifier: Modifier = Modifier,
    itemKey: ((T) -> Any)? = null,
    itemContent: @Composable (T) -> Unit
) {
    val listState = rememberLazyListState()

    androidx.compose.runtime.LaunchedEffect(items.size) {
        if (items.isNotEmpty()) {
            listState.animateScrollToItem(items.lastIndex)
        }
    }

    LazyColumn(state = listState, modifier = modifier) {
        items(
            count = items.size,
            key = if (itemKey != null) { index -> itemKey(items[index]) } else null
        ) { index ->
            itemContent(items[index])
        }
    }
}
@Composable
fun SideBar(
    isVisible: Boolean,
    modifier: Modifier,
    contentFun: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally(initialOffsetX = { -it }),
        exit = slideOutHorizontally(targetOffsetX = { -it }),
    ) {
        Row(
            modifier = modifier
                .background(RadarColors.SurfaceRaised)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {}
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(3.dp)
                    .background(RadarColors.Amber)
            )
            Box(modifier = Modifier.padding(start = 12.dp)) {
                contentFun()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarFilter(suggestions: HashSet<String>, queryList: MutableList<String>) {
    var query by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val filteredSuggestions by remember {
        derivedStateOf { suggestions.filter { it.contains(query, ignoreCase = true) } }
    }

    DockedSearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction = 0.8f),
        expanded = expanded,
        onExpandedChange = { expanded = it },
        colors = SearchBarDefaults.colors(containerColor = RadarColors.Surface),
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = { query = it },
                onSearch = { expanded = false },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = { Text("Search here...", color = RadarColors.TextMuted) },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear", tint = RadarColors.TextMuted)
                        }
                    }
                }
            )
        }
    ) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(filteredSuggestions, key = { it }) { suggestion ->
                ListItem(
                    colors = androidx.compose.material3.ListItemDefaults.colors(containerColor = RadarColors.Surface),
                    headlineContent = {
                        CheckBoxRow(
                            text = suggestion,
                            checked = suggestion in queryList,
                            onCheckedChange = { next ->
                                if (next) queryList.add(suggestion) else queryList.remove(suggestion)
                            }
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun TextBoxForDouble(onValueChange: (value: Double) -> Unit, label: @Composable () -> Unit, initialVal: String?) {
    var text by remember { mutableStateOf(initialVal ?: "") }
    OutlinedTextField(
        value = text,
        onValueChange = { input ->
            val hasSingleDot = input.count { it == '.' } <= 1
            val hasOnlyDigitsAndDots = input.all { it.isDigit() || it == '.' }
            if (hasSingleDot && hasOnlyDigitsAndDots) text = input
            onValueChange(text.toDoubleOrNull() ?: 0.0)
        },
        label = label,
        textStyle = MaterialTheme.typography.bodySmall.copy(color = RadarColors.TextPrimary),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = RadarColors.Amber,
            unfocusedBorderColor = RadarColors.Border,
            focusedLabelColor = RadarColors.Amber,
            unfocusedLabelColor = RadarColors.TextMuted
        )
    )
}
