import QtQuick 1.0
import "UIConstants.js" as UI
import "constants.js" as ExtrasConstants

Item {
    width: parent.width
    height: titleText.height + contentText.height

    property string title: ""
    property string content: ""

    Column {
        width: parent.width

        Text {
            id: titleText
            width: parent.width
            wrapMode: Text.Wrap
            horizontalAlignment: Text.AlignLeft
            font.pixelSize: UI.FONT_DEFAULT * appWindow.scaleFactor
            font.weight: Font.Bold
            smooth: true
            text: title
            color: !true ? UI.COLOR_FOREGROUND : UI.COLOR_INVERTED_FOREGROUND
        }

        Text {
            id: contentText
            width: parent.width
            wrapMode: Text.Wrap
            horizontalAlignment: Text.AlignLeft
            font.pixelSize: UI.FONT_LSMALL * appWindow.scaleFactor
            smooth: true
            font.weight: Font.Light
            color: true ? ExtrasConstants.LIST_SUBTITLE_COLOR_INVERTED : ExtrasConstants.LIST_SUBTITLE_COLOR
            text: content
        }
    }
}
