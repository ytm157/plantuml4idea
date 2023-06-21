import com.intellij.openapi.actionSystem.AnActionEvent
import liveplugin.*
import java.util.regex.Pattern

// One of the most fundamental things you can do in an Action is to modify text
// in the current editor using `com.intellij.openapi.editor.Document` API.
// See also https://plugins.jetbrains.com/docs/intellij/documents.html.

/**
 * 去除跳转链接
 * 将类似Actor -> Class2#FFE0A7 : [[D:/data/project2/HelloWorld/src/com/my/Class2.java#fun2 fun2(Boolean, String...):void]]
 * 改为 Actor -> Class2#FFE0A7 : fun2(Boolean, String...):void
 */
registerAction(id = "Plantuml Remove Link", keyStroke = "ctrl shift W") { event: AnActionEvent ->
    val project = event.project ?: return@registerAction // Can be null if there are no open projects.
    val editor = event.editor ?: return@registerAction
    val selectionModel = editor.selectionModel
    if (!selectionModel.hasSelection()) {
        return@registerAction
    }
    val txt = selectionModel.selectedText ?: return@registerAction
    var txtLines = ArrayList<String>()
    var lftype = "\r\n"
    if (txt.contains("\r\n")) {
        txtLines = txt.split("\r\n") as ArrayList<String>
    } else if (txt.contains("\n")) {
        txtLines = txt.split("\n") as ArrayList<String>
        lftype = "\n"
    } else {
        txtLines.add(txt)
        lftype = ""
    }

    val regex = "\\[\\[(.+?) (.+?)\\]\\]"
    val pattern = Pattern.compile(regex)
    val sb = StringBuilder()
    for (line in txtLines) {
        val matcher = pattern.matcher(line)
        if (!matcher.find()) {
            sb.append(line).append(lftype)
            continue
        }
        val link = matcher.group(1)
        val method = matcher.group(2)
        val newTxt = line.replace(matcher.group(0), method)
        sb.append(newTxt).append(lftype)
    }

    val startOffset = selectionModel.selectionStart
    val endOffset = selectionModel.selectionEnd
    // Document modifications must be done inside "commands" which will support undo/redo functionality.
    editor.document.executeCommand(project, description = "Plantuml Remove Link") {
        replaceString(startOffset, endOffset, sb.toString())
    }
}

if (!isIdeStartup) show("Loaded 'Plantuml Remove Link' action<br/>Use 'Ctrl+Shift+W' to run it")
