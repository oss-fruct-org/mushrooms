// import QtQuick 1.0 // to target S60 5th Edition or Maemo 5
import QtQuick 1.1
import com.nokia.symbian 1.1
import "UIConstants.js" as UI
import "constants.js" as ExtrasConstants
import "category.js" as Category
import mushrooms 1.0

Page {
    id: descriptionPage
    property XmlListModel dModel
    property int dIndex: 0
    orientationLock: PageOrientation.LockPortrait

    onDIndexChanged: {
        dView.positionViewAtIndex(dIndex, ListView.Center)
    }

    ListView {
        id: dView
        anchors.fill: parent
        model: dModel
        orientation: ListView.Horizontal
        flickDeceleration: 500
        snapMode: ListView.SnapOneItem
        highlightRangeMode: ListView.StrictlyEnforceRange
        preferredHighlightBegin: 0
        preferredHighlightEnd: 0
        cacheBuffer: width
        currentIndex: -1
        delegate: mushroomDelegate
    }

    Component {
        id: mushroomDelegate

        Item {
            width: dView.width
            height: dView.height

            Flickable {
                id: flickable
                contentHeight: column.height + textColumn.height + UI.DEFAULT_MARGIN
                anchors.fill: parent

                Column {
                    id: textColumn
                    anchors.left: parent.left
                    anchors.right: parent.right
                    anchors.top: parent.top

                    Header {
                        id: descriptionHeader
                        title: (appWindow.selectedLanguage == 'en') ? lat_name : name
                        fontSize: 32 * appWindow.scaleFactor
                    }

                    Item {
                        anchors.left: parent.left
                        anchors.right: parent.right
                        height: 340 * appWindow.scaleFactor

                        ImageWithBorder {
                            id: mPic
                            anchors.fill: parent
                            anchors.margins: UI.DEFAULT_MARGIN
                            source: pic
                            borderWidth: 2
                        }
                    }
                }

                Column {
                    id: column
                    anchors.left: parent.left
                    anchors.right: parent.right
                    anchors.top: textColumn.bottom
                    anchors.leftMargin: UI.DEFAULT_MARGIN
                    anchors.rightMargin: UI.DEFAULT_MARGIN
                    spacing: 6 * appWindow.scaleFactor

                    Text {
                        text: type
                        font.pixelSize: UI.FONT_DEFAULT
                        font.italic: true
                        visible: (type === "") ? false : true
                        smooth: true
                        color: !true ? UI.COLOR_FOREGROUND : UI.COLOR_INVERTED_FOREGROUND
                    }

                    Text {
                        text: qsTr("<b>Category:</b> ") + "<i>" + Category.getCategoryName(m_category) + "</i>" + rootItem.emptyString
                        font.pixelSize: UI.FONT_DEFAULT * appWindow.scaleFactor
                        smooth: true
                        color: !true ? UI.COLOR_FOREGROUND : UI.COLOR_INVERTED_FOREGROUND
                    }

                    MushroomInfoItem {
                        title: qsTr("Latin name") + rootItem.emptyString
                        content: lat_name
                        visible: (appWindow.selectedLanguage != 'en') && (lat_name != "") ? true : false
                    }

                    MushroomInfoItem {
                        title: qsTr("Synonyms") + rootItem.emptyString
                        content: (name && synonyms) ? name + ", " + synonyms : name + synonyms
                        visible: content
                    }

                    Text {
                        text: text_begin
                        font.pixelSize: UI.FONT_LSMALL * appWindow.scaleFactor
                        visible: (text_begin === "") ? false : true
                        smooth: true
                        font.weight: Font.Light
                        width: parent.width
                        wrapMode: Text.Wrap
                        color: true ? ExtrasConstants.LIST_SUBTITLE_COLOR_INVERTED : ExtrasConstants.LIST_SUBTITLE_COLOR
                    }   

                    MushroomInfoItem {
                        title: qsTr("Location") + rootItem.emptyString
                        content: location
                        visible: (location === "") ? false : true
                    }

                    MushroomInfoItem {
                        title: qsTr("Fungus color") + rootItem.emptyString
                        content: color
                        visible: (color === "") ? false : true
                    }

                    MushroomInfoItem {
                        title: qsTr("Normal size") + rootItem.emptyString
                        content: size
                        visible: (size === "") ? false : true
                    }

                    MushroomInfoItem {
                        title: qsTr("Cap") + rootItem.emptyString
                        content: cap
                        visible: (cap === "") ? false : true
                    }

                    MushroomInfoItem {
                        title: qsTr("Stem") + rootItem.emptyString
                        content: leg
                        visible: (leg === "") ? false : true
                    }

                    MushroomInfoItem {
                        title: qsTr("Flesh") + rootItem.emptyString
                        content: meat
                        visible: (meat === "") ? false : true
                    }

                    MushroomInfoItem {
                        title: qsTr("Habitat") + rootItem.emptyString
                        content: grow
                        visible: (grow === "") ? false : true
                    }

                    MushroomInfoItem {
                        title: qsTr("Plates") + rootItem.emptyString
                        content: plates
                        visible: (plates === "") ? false : true
                    }

                    Text {
                        text: text_end
                        font.pixelSize: UI.FONT_LSMALL * appWindow.scaleFactor
                        visible: (text_end === "") ? false : true
                        smooth: true
                        font.weight: Font.Light
                        width: parent.width
                        wrapMode: Text.Wrap
                        color: true ? ExtrasConstants.LIST_SUBTITLE_COLOR_INVERTED : ExtrasConstants.LIST_SUBTITLE_COLOR
                    }
                }
            }
        }
    }

    SequentialAnimation {
        id: activatingAnimation
        running: false
        NumberAnimation { target:descriptionPage; property:"opacity"; from: 0; to: 1; duration:500 }
    }

    SequentialAnimation {
        id: deactivatingAnimation
        running: false
        NumberAnimation { target:descriptionPage; property:"opacity"; from: 1; to: 0; duration:500 }
    }

    onStatusChanged: {
        if (status == PageStatus.Activating) {
            activatingAnimation.start()
        }

        if (status == PageStatus.Active) {
            activatingAnimation.complete()
        }

        if (status == PageStatus.Deactivating) {
            deactivatingAnimation.start()
        }

        if (status == PageStatus.Inactive) {
            deactivatingAnimation.complete()
        }
    }

    Connections {
        target: rootItem

        onLanguageChanged: {
            if (descriptionPage.status == PageStatus.Active) {
                appWindow.pageStack.pop();
            }
        }
    }
}
