import QtQuick 1.1
import com.nokia.symbian 1.1
import "UIConstants.js" as UI

Item {
    id: globalSearch
    height: searchInput.height + UI.DEFAULT_MARGIN
    width: parent.width
    opacity: 0

    property bool isShown: (opacity > 0)
    property alias searchText: searchInput.text
    signal nameFilterChanged(string filterName)

    function show() {
        showAnimation.start()
    }

    function hide() {
        hideAnimation.start()
    }

    Row {
        id: searchArea
        anchors.fill: parent
        anchors.margins: UI.DEFAULT_MARGIN
        spacing: 10

        TextField {
            id: searchInput
            placeholderText: qsTr("Search") + rootItem.emptyString
            width: parent.width - searchButton.width - 10
            anchors.verticalCenter: parent.verticalCenter


            Image {
                id: clearText
                anchors.right: parent.right
                anchors.verticalCenter: parent.verticalCenter
                source: searchInput.text ?
                            'image://theme/icon-m-input-clear' :
                            'image://theme/icon-m-common-search'
            }

            MouseArea {
                id: searchInputMouseArea
                anchors.fill: clearText
                onClicked: {
                    searchInput.text = ''
                    searchInput.closeSoftwareInputPanel();
                    nameFilterChanged('')
                }
            }
        }

        Button {
            id: searchButton
            text: qsTr("Search") + rootItem.emptyString
            anchors.verticalCenter: parent.verticalCenter
            width: 100
            enabled: searchInput.text !== ''
            onClicked: {
                nameFilterChanged(searchInput.text)
            }
        }
    }

    SequentialAnimation {
        id: showAnimation
        running: false
        NumberAnimation { target:globalSearch; property:"opacity"; from: 0; to: 1; duration:500 }
    }

    SequentialAnimation {
        id: hideAnimation
        running: false
        NumberAnimation { target:globalSearch; property:"opacity"; from: 1; to: 0; duration:500 }
    }
}
