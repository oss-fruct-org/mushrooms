// import QtQuick 1.0 // to target S60 5th Edition or Maemo 5
import QtQuick 1.1
import com.nokia.symbian 1.1
import "UIConstants.js" as UI
import "constants.js" as ExtrasConstants

Item {

    Flickable {
        anchors.fill: parent
        contentHeight: col.height + 2*UI.DEFAULT_MARGIN

        Column {
            id: col
            anchors.left: parent.left
            anchors.right: parent.right
            anchors.top: parent.top
            anchors.margins: UI.DEFAULT_MARGIN
            spacing: 10 * appWindow.scaleFactor

            Text {
                font.pixelSize: 28 * appWindow.scaleFactor
                font.weight: Font.Bold
                width: parent.width
                wrapMode: Text.Wrap
                horizontalAlignment: Text.AlignHCenter
                color: !true ? UI.COLOR_FOREGROUND : UI.COLOR_INVERTED_FOREGROUND
                text: qsTr("Mushroom Guide") + rootItem.emptyString
            }

            Text {
                font.pixelSize: 22 * appWindow.scaleFactor
                width: parent.width
                wrapMode: Text.Wrap
                horizontalAlignment: Text.AlignJustify
                color: true ? ExtrasConstants.LIST_SUBTITLE_COLOR_INVERTED : ExtrasConstants.LIST_SUBTITLE_COLOR
                text: qsTr("Mushrooms is an electronic encyclopedia. It contains descriptions of 158 species of mushrooms and can be used to find information about particular specie by its photo, or by name.") +
                      rootItem.emptyString
            }

            Text {
                font.pixelSize: 22 * appWindow.scaleFactor
                width: parent.width
                wrapMode: Text.Wrap
                horizontalAlignment: Text.AlignJustify
                color: true ? ExtrasConstants.LIST_SUBTITLE_COLOR_INVERTED : ExtrasConstants.LIST_SUBTITLE_COLOR
                text: qsTr("Materials from web-sites <a style='color:#006abe' href='http://vsegriby.com/'>http://vsegriby.com/</a> and <a style='color:#006abe' href='http://www.foragingguide.com/'>http://www.foragingguide.com/</a> are used in this app.") +
                      rootItem.emptyString
                onLinkActivated: Qt.openUrlExternally(link)
            }

            Text {
                font.pixelSize: 26 * appWindow.scaleFactor
                font.weight: Font.Bold
                wrapMode: Text.Wrap
                width: parent.width
                text: qsTr("How to collect wild mushrooms") + rootItem.emptyString
                horizontalAlignment: Text.AlignHCenter
                color: !true ? UI.COLOR_FOREGROUND : UI.COLOR_INVERTED_FOREGROUND
            }

            Text {
                font.pixelSize: 22 * appWindow.scaleFactor
                width: parent.width
                wrapMode: Text.Wrap
                horizontalAlignment: Text.AlignJustify
                color: true ? ExtrasConstants.LIST_SUBTITLE_COLOR_INVERTED : ExtrasConstants.LIST_SUBTITLE_COLOR
                text: qsTr("When first starting out mushroom hunting its usually best to limit your self to a few common and easily identified species. As you become confident with these gradually extend your list. If possible, your first trips should be with a more experienced mushroom forager that can show you the ropes. There are many mushroom forays organised by local foraging groups or mycological societies which are a good starting point. There are also commercial courses where you pay to attend and get expert tuition.") +
                      rootItem.emptyString
            }

            Text {
                font.pixelSize: 22 * appWindow.scaleFactor
                width: parent.width
                wrapMode: Text.Wrap
                horizontalAlignment: Text.AlignJustify
                color: true ? ExtrasConstants.LIST_SUBTITLE_COLOR_INVERTED : ExtrasConstants.LIST_SUBTITLE_COLOR
                text: qsTr("So you\'ve spotted a fungus. You\'ve now got decide whether its edible. Use your field guide and systematically check all of the features of the mushroom. If they all match the descriptions of a mushroom your list, well done, pop it in the basket. You might consider taking those you\'re not sure of home for closer inspection, but keep them separate from your main collection.") +
                      rootItem.emptyString
            }

            Text {
                font.pixelSize: 22 * appWindow.scaleFactor
                width: parent.width
                wrapMode: Text.Wrap
                horizontalAlignment: Text.AlignJustify
                color: true ? ExtrasConstants.LIST_SUBTITLE_COLOR_INVERTED : ExtrasConstants.LIST_SUBTITLE_COLOR
                text: qsTr("With your mushroom safely in your basket search very carefully in the immediate area for others. Many species grow in groups or rings so others may to be close by. Even if you don\'t find anything in the immediate area, still pay close attention to the area around where you made your find. You\'ve found one mushroom that liked the conditions there, others might too.") +
                      rootItem.emptyString
            }

            Text {
                font.pixelSize: 22 * appWindow.scaleFactor
                width: parent.width
                wrapMode: Text.Wrap
                horizontalAlignment: Text.AlignJustify
                color: true ? ExtrasConstants.LIST_SUBTITLE_COLOR_INVERTED : ExtrasConstants.LIST_SUBTITLE_COLOR
                text: qsTr("As soon as you get home sort through your mushrooms. Double check the identity of each and discard any you\'re not 100\% sure of. It\'s worth getting a second mushroom identification book for this double checking as it may make slightly different observations which could help. It will also have another set of pictures to check against.") +
                      rootItem.emptyString
            }

            Text {
                font.pixelSize: 22 * appWindow.scaleFactor
                width: parent.width
                wrapMode: Text.Wrap
                horizontalAlignment: Text.AlignJustify
                color: true ? ExtrasConstants.LIST_SUBTITLE_COLOR_INVERTED : ExtrasConstants.LIST_SUBTITLE_COLOR
                text: qsTr("Clean your mushrooms and trim them to remove any damaged parts before putting them in the fridge. Paper bags are good for storing mushrooms or wrap them in kitchen paper put in a loosely sealed containers. Kept in the fridge like this most species will last several days, some even longer. A few species don\'t keep well however well you store them (particularly the Inkcaps).") +
                      rootItem.emptyString
            }
        }
    }
}
