// import QtQuick 1.0 // to target S60 5th Edition or Maemo 5
import QtQuick 1.1
import com.nokia.symbian 1.1
import "UIConstants.js" as UI
import "constants.js" as ExtrasConstants

Item {
    Column {
        anchors.fill: parent
        anchors.margins: UI.DEFAULT_MARGIN
        spacing: 10 * appWindow.scaleFactor

        Text {
            font.pixelSize: 26 * appWindow.scaleFactor
            font.weight: Font.Bold
            wrapMode: Text.Wrap
            width: parent.width
            text: qsTr("Mushrooms Guide v1.1") + rootItem.emptyString
            horizontalAlignment: Text.AlignHCenter
            color: !true ? UI.COLOR_FOREGROUND : UI.COLOR_INVERTED_FOREGROUND
        }

        Text {
            font.pixelSize: 22 * appWindow.scaleFactor
            width: parent.width
            wrapMode: Text.Wrap
            horizontalAlignment: Text.AlignJustify
            text: qsTr("<font>&copy;</font> 2012 Mobile and Wireless Systems Lab of IT-park of Petrozavodsk State University (member of FRUCT association).") +
                  rootItem.emptyString
            color: true ? ExtrasConstants.LIST_SUBTITLE_COLOR_INVERTED : ExtrasConstants.LIST_SUBTITLE_COLOR
        }

        Text {
            font.pixelSize: 22 * appWindow.scaleFactor
            width: parent.width
            wrapMode: Text.Wrap
            horizontalAlignment: Text.AlignJustify
            text: qsTr("This project is supported by grant KA179 \"Complex development of regional cooperation in the field of open ICT innovations\" of Karelia EMPI programme, which is co-funded by the European Union, the Russian Federation and the Republic of Finland.") +
                  rootItem.emptyString
            color: true ? ExtrasConstants.LIST_SUBTITLE_COLOR_INVERTED : ExtrasConstants.LIST_SUBTITLE_COLOR
        }

        Text {
            font.pixelSize: 22 * appWindow.scaleFactor
            width: parent.width
            wrapMode: Text.Wrap
            horizontalAlignment: Text.AlignJustify
            text: qsTr("Materials from web-sites <a style='color:#006abe' href='http://vsegriby.com/'>http://vsegriby.com/</a> and <a style='color:#006abe' href='http://www.foragingguide.com/'>http://www.foragingguide.com/</a> are used in this app.") +
                  rootItem.emptyString
            color: true ? ExtrasConstants.LIST_SUBTITLE_COLOR_INVERTED : ExtrasConstants.LIST_SUBTITLE_COLOR
            onLinkActivated: Qt.openUrlExternally(link)
        }

        Text {
            font.pixelSize: 22 * appWindow.scaleFactor
            width: parent.width
            wrapMode: Text.Wrap
            horizontalAlignment: Text.AlignJustify
            text: qsTr("Detailed information you can find on <a style='color:#006abe' href='http://oss.fruct.org/projects/mushrooms/'>http://oss.fruct.org/projects/mushrooms/</a>.") + rootItem.emptyString
            color: true ? ExtrasConstants.LIST_SUBTITLE_COLOR_INVERTED : ExtrasConstants.LIST_SUBTITLE_COLOR
            onLinkActivated: Qt.openUrlExternally(link)
        }

        Text {
            font.pixelSize: 22 * appWindow.scaleFactor
            width: parent.width
            wrapMode: Text.Wrap
            horizontalAlignment: Text.AlignJustify
            text: qsTr("We accept no responsibility for any injury, sickness or death arising from mushrooms eaten, picked or used in any way whatsoever.") + rootItem.emptyString
            color: true ? ExtrasConstants.LIST_SUBTITLE_COLOR_INVERTED : ExtrasConstants.LIST_SUBTITLE_COLOR
            font.weight: Font.Bold
            visible: appWindow.selectedLanguage == 'en'
        }
    }
}
