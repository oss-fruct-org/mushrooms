import QtQuick 1.1
import com.nokia.symbian 1.1
import "UIConstants.js" as UI

Item {
    ListPage {
        id: listpage
    }

    DescriptionPage {
        id: descriptionpage
        tools: commonTools

        ToolBarLayout {
            id: commonTools
            ToolButton {
                iconSource: "toolbar-back"
                anchors.left: (parent === undefined) ? undefined : parent.left
                onClicked: {
                    descriptionpage.dIndex = -1;
                    pageStack.pop();
                }
            }

            ToolButton {
                iconSource: "toolbar-view-menu"
                anchors.right: (parent === undefined) ? undefined : parent.right
                onClicked: (myMenu.status === DialogStatus.Closed) ? myMenu.open() : myMenu.close()
            }
        }

    }

    Header {
        id: mainHeader
        title: qsTr("Mushroom Guide") + rootItem.emptyString
        fontSize: 36 * appWindow.scaleFactor
    }

//    Text {
//        id: header
//        anchors.horizontalCenter: parent.horizontalCenter
//        anchors.top: parent.top
//        anchors.margins: UI.DEFAULT_MARGIN
//        text: qsTr("Mushroom Guide") + rootItem.emptyString
//        smooth: true
//        font.family: mushroomFont.name
//        font.pixelSize: UI.FONT_XLARGE
//        color: !true ? UI.COLOR_FOREGROUND : UI.COLOR_INVERTED_FOREGROUND
//    }

    Column {
        id: categoryButtons
        anchors.top: mainHeader.bottom
        anchors.bottom: parent.bottom
        anchors.left: parent.left
        anchors.right: parent.right
        anchors.margins: UI.DEFAULT_MARGIN
        spacing: 16 * appWindow.scaleFactor

        CategoryButtonP {
            title: qsTr("Edible") + rootItem.emptyString
            icon: "qrc:/gfx/good.svg"
            onButtonClicked: {
                listpage.showEdible();
                pageStack.push(listpage);
            }
        }

        CategoryButtonP {
            title: qsTr("Choice") + rootItem.emptyString
            icon: "qrc:/gfx/cond.svg"
            onButtonClicked: {
                listpage.showCondEdible();
                pageStack.push(listpage);
            }
        }

        CategoryButtonP {
            title: qsTr("Inedible") + rootItem.emptyString
            icon: "qrc:/gfx/bad.svg"
            onButtonClicked: {
                listpage.showToxic();
                pageStack.push(listpage);
            }
        }
    }
}
