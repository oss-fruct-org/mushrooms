// import QtQuick 1.0 // to target S60 5th Edition or Maemo 5
import QtQuick 1.1
import com.nokia.symbian 1.1
import "UIConstants.js" as UI

Rectangle {
    height: parent.height / 3 - 11 * appWindow.scaleFactor
    width: parent.width
    radius: 10 * appWindow.scaleFactor
    color: true ? "#494948" : "#cecece"

    property alias title: buttonTitle.text
    property alias icon: buttonIcon.source

    signal buttonClicked()

    Row {
        anchors.fill: parent
        spacing: 20 * appWindow.scaleFactor
        anchors.margins: UI.DEFAULT_MARGIN

        Image {
            id: buttonIcon
            height: parent.height - 2*UI.DEFAULT_MARGIN
            width: height
            anchors.verticalCenter: parent.verticalCenter
        }

        Text {
            id: buttonTitle
            anchors.verticalCenter: parent.verticalCenter
            color: !true ? UI.COLOR_FOREGROUND : UI.COLOR_INVERTED_FOREGROUND
            width: parent.width - buttonIcon.width - parent.spacing - UI.DEFAULT_MARGIN
            wrapMode: Text.Wrap
            smooth: true
            font.pixelSize: UI.FONT_LARGE * appWindow.scaleFactor
            font.family: mushroomFont.name
        }
    }

    MouseArea {
        anchors.fill: parent
        onClicked: buttonClicked()
    }
}
