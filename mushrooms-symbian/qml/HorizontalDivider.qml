// import QtQuick 1.0 // to target S60 5th Edition or Maemo 5
import QtQuick 1.1
import "constants.js" as ExtrasConstants

Rectangle {
    property int margins: 0

    anchors.left: parent.left
    anchors.right: parent.right
    anchors.leftMargin: margins
    anchors.rightMargin: margins
    height: 1
    color: true ? ExtrasConstants.LIST_SUBTITLE_COLOR_INVERTED : ExtrasConstants.LIST_SUBTITLE_COLOR
}
