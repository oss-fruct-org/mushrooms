// import QtQuick 1.0 // to target S60 5th Edition or Maemo 5
import QtQuick 1.1
import "UIConstants.js" as UI

Item {
    id: header
    height: 80 * appWindow.scaleFactor + headerMargin
    width: parent.width

    property string title: ""
    property alias fontSize: titleText.font.pixelSize
    property int headerMargin: 0

    Rectangle {
        id: rect
        color: "#429438"
        anchors.fill: parent
        anchors.bottomMargin: headerMargin

        Text {
            id: titleText
            anchors.left: parent.left
            anchors.leftMargin: UI.DEFAULT_MARGIN
            anchors.right: parent.right
            anchors.rightMargin: UI.DEFAULT_MARGIN
            anchors.verticalCenter: parent.verticalCenter
            anchors.verticalCenterOffset: 3 * appWindow.scaleFactor
            elide: Text.ElideRight
            text: title
            font.family: mushroomFont.name
            font.pixelSize: UI.FONT_LARGE
            font.weight: Font.Normal
            color: "white"
            smooth: true
            width: parent.width
        }
    }
}
