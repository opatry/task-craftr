/*
 * Copyright (c) 2024 Olivier Patry
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.opatry.tasks.app.ui.tooling

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import net.opatry.tasks.app.ui.theme.TaskfolioTheme

@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.FUNCTION
)
//@Preview(
//    name = "Light",
//    widthDp = 400,
//    heightDp = 600,
//    showBackground = true,
//    uiMode = UI_MODE_NIGHT_NO or UI_MODE_TYPE_NORMAL,
//    device = Devices.NEXUS_6)
//@Preview(
//    name = "Night",
//    widthDp = 400,
//    heightDp = 600,
//    showBackground = true,
//    uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL,
//    device = Devices.NEXUS_6)
annotation class TaskfolioPreview

@Composable
fun TaskfolioThemedPreview(content: @Composable () -> Unit) {
    TaskfolioTheme {
        Surface {
            content()
        }
    }
}