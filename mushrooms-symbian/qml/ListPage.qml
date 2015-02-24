// import QtQuick 1.0 // to target S60 5th Edition or Maemo 5
import QtQuick 1.1
import com.nokia.symbian 1.1
import "UIConstants.js" as UI
import "category.js" as Category

Page {
    id: listPage
    property string category
    property string currentCategory: ""
    property bool isSearch: false
    property XmlListModel mushroomModel: defaultModel
    property string base_query: "/mushrooms/mushroom"
    property bool isLoading: false
    orientationLock: PageOrientation.LockPortrait

    tools: listTools

    ToolBarLayout {
        id: listTools
        ToolButton {
            iconSource: "toolbar-back"
            anchors.left: (parent === undefined) ? undefined : parent.left
            onClicked: {
                pageStack.pop();
                search.show = false;
            }
        }

        ToolButton {
            iconSource: "toolbar-search"
            onClicked: {
                search.toggle()
            }
        }

        ToolButton {
            iconSource: "toolbar-view-menu"
            anchors.right: (parent === undefined) ? undefined : parent.right
            onClicked: (myMenu.status === DialogStatus.Closed) ? myMenu.open() : myMenu.close()
        }
    }

    function showEdible() {
        currentCategory = (appWindow.selectedLanguage == "ru") ? "Съедобные грибы" : "Edible mushrooms";
        edibleModel.source = (appWindow.selectedLanguage == "ru") ? "edible.xml" : "edible_en.xml"
        edibleModel.query = "/mushrooms/mushroom";
        mushroomModel = edibleModel;
        descriptionpage.dModel = mushroomModel;
    }

    function showToxic() {
        currentCategory = (appWindow.selectedLanguage == "ru") ? "Ядовитые грибы" : "Inedible mushrooms";
        toxicModel.source = (appWindow.selectedLanguage == "ru") ? "toxic.xml" : "toxic_en.xml"
        toxicModel.query = "/mushrooms/mushroom";
        mushroomModel = toxicModel;
        descriptionpage.dModel = mushroomModel;
    }

    function showCondEdible() {
        currentCategory = (appWindow.selectedLanguage == "ru") ? "Условно съедобные грибы" : "Choice mushrooms";
        condModel.source = (appWindow.selectedLanguage == "ru") ? "cond_edible.xml" : "cond_edible_en.xml"
        condModel.query = "/mushrooms/mushroom";
        mushroomModel = condModel;
        descriptionpage.dModel = mushroomModel;
    }

    XmlListModel {
        id: defaultModel
    }

    XmlListModel {
        id: edibleModel

        XmlRole { name: "pic"; query: "image/string()" }
        XmlRole { name: "name"; query: "title/string()" }
        XmlRole { name: "lat_name"; query: "lat_title/string()" }
        XmlRole { name: "grow"; query: "grow/string()" }
        XmlRole { name: "cap"; query: "cap/string()" }
        XmlRole { name: "leg"; query: "leg/string()" }
        XmlRole { name: "meat"; query: "meat/string()" }
        XmlRole { name: "plates"; query: "plates/string()" }
        XmlRole { name: "thumb"; query: "id/string()" }
        XmlRole { name: "type"; query: "type/string()" }
        XmlRole { name: "text_begin"; query: "text_begin/string()" }
        XmlRole { name: "text_end"; query: "text_end/string()" }
        XmlRole { name: "m_category"; query: "category/string()" }
        XmlRole { name: "location"; query: "location/string()" }
        XmlRole { name: "synonyms"; query: "synonyms/string()" }
        XmlRole { name: "color"; query: "color/string()" }
        XmlRole { name: "size"; query: "size/string()" }

        onStatusChanged: {
            if (status === XmlListModel.Loading)
                listpage.isLoading = true;
            else
                listpage.isLoading = false;
        }
    }

    XmlListModel {
        id: toxicModel

        XmlRole { name: "pic"; query: "image/string()" }
        XmlRole { name: "name"; query: "title/string()" }
        XmlRole { name: "lat_name"; query: "lat_title/string()" }
        XmlRole { name: "grow"; query: "grow/string()" }
        XmlRole { name: "cap"; query: "cap/string()" }
        XmlRole { name: "leg"; query: "leg/string()" }
        XmlRole { name: "meat"; query: "meat/string()" }
        XmlRole { name: "plates"; query: "plates/string()" }
        XmlRole { name: "thumb"; query: "id/string()" }
        XmlRole { name: "type"; query: "type/string()" }
        XmlRole { name: "text_begin"; query: "text_begin/string()" }
        XmlRole { name: "text_end"; query: "text_end/string()" }
        XmlRole { name: "m_category"; query: "category/string()" }
        XmlRole { name: "location"; query: "location/string()" }
        XmlRole { name: "synonyms"; query: "synonyms/string()" }
        XmlRole { name: "color"; query: "color/string()" }
        XmlRole { name: "size"; query: "size/string()" }

        onStatusChanged: {
            if (status === XmlListModel.Loading)
                listpage.isLoading = true;
            else
                listpage.isLoading = false;
        }
    }

    XmlListModel {
        id: condModel

        XmlRole { name: "pic"; query: "image/string()" }
        XmlRole { name: "name"; query: "title/string()" }
        XmlRole { name: "lat_name"; query: "lat_title/string()" }
        XmlRole { name: "grow"; query: "grow/string()" }
        XmlRole { name: "cap"; query: "cap/string()" }
        XmlRole { name: "leg"; query: "leg/string()" }
        XmlRole { name: "meat"; query: "meat/string()" }
        XmlRole { name: "plates"; query: "plates/string()" }
        XmlRole { name: "thumb"; query: "id/string()" }
        XmlRole { name: "type"; query: "type/string()" }
        XmlRole { name: "text_begin"; query: "text_begin/string()" }
        XmlRole { name: "text_end"; query: "text_end/string()" }
        XmlRole { name: "m_category"; query: "category/string()" }
        XmlRole { name: "location"; query: "location/string()" }
        XmlRole { name: "synonyms"; query: "synonyms/string()" }
        XmlRole { name: "color"; query: "color/string()" }
        XmlRole { name: "size"; query: "size/string()" }

        onStatusChanged: {
            if (status === XmlListModel.Loading)
                listpage.isLoading = true;
            else
                listpage.isLoading = false;
        }
    }

    BusyIndicator {
        id: busy
        anchors.centerIn: parent
        visible: listpage.isLoading
        running: listpage.isLoading
        width: 100
        height: 100
    }

    Component {
        id: mushroom
        Item {
            width: gridview.cellWidth
            height: gridview.cellHeight

            Item {
                anchors.fill: parent
                anchors.margins: 3 * appWindow.scaleFactor

                Rectangle {
                    border.width: 2
                    border.color: "white"
                    anchors.fill: parent

                    Image {
                        source: "thumbnails/thumb_" + thumb + ".jpg";
                        x: 1; y:1;
                        anchors.fill: parent
                        fillMode: Image.PreserveAspectCrop
                        clip: true
                        smooth: true

                        Text {
                            anchors.bottom: parent.bottom
                            anchors.right: parent.right
                            text: Category.getCategoryId(m_category)
                            anchors.margins: 6 * appWindow.scaleFactor
                            opacity: 0.7
                            color: "white"
                            style: Text.Outline
                            font.pixelSize: UI.FONT_XLARGE
                            smooth: true
                            visible: appWindow.showCategories
                        }

                        MouseArea {
                            anchors.fill: parent
                            onClicked: {
                                descriptionpage.dIndex = index;
                                pageStack.push(descriptionpage);
                                search.show = false;
                            }
                        }
                    }
                }
            }
        }
    }


    GridView {
        id: gridview
        cellWidth: (parent.width / parent.height > 1.5) ? 213 * appWindow.scaleFactor : 160 * appWindow.scaleFactor;
        cellHeight: cellWidth
        model: mushroomModel
        anchors.fill: parent
        anchors.bottomMargin: 4 * appWindow.scaleFactor
        anchors.topMargin: 4 * appWindow.scaleFactor
        cacheBuffer: 2*height
        visible: !isLoading
        delegate: mushroom
        header: Header {
            id: categoryHeader

            title: currentCategory
            headerMargin: 4 * appWindow.scaleFactor
            fontSize: 32 * appWindow.scaleFactor

            Connections {
                target: listPage

                onCurrentCategoryChanged: {
                    categoryHeader.title = currentCategory
                }
            }
        }

        MouseArea {
            anchors.fill: parent
            enabled: search.show

            onClicked: {
                search.show = false;
                search.hideSearch();
            }
        }
    }

    SearchField {
        id: search
        anchors.left: parent.left
        anchors.right: parent.right
        anchors.bottom: parent.bottom

        onNameFilterChanged: {
            if (filterName !== "") {
                mushroomModel.query = base_query;
                mushroomModel.query += "[contains(lower-case(lat_title),lower-case('"+ filterName +
                        "')) or contains(lower-case(synonyms),lower-case('"+ filterName +
                        "')) or contains(lower-case(title),lower-case('"+ filterName + "'))]";
            } else {
                mushroomModel.query = base_query;
            }
        }

        Connections {
            target: mainPage
            onStatusChanged: {
                if (status === PageStatus.Activating ) {
                    search.searchText = "";
                    gridview.focus = true;
                    mushroomModel.query = base_query;
                }
            }
        }
    }

    onStatusChanged: {
        if (status == PageStatus.Activating) {
            gridview.contentY = 0
        }
    }

    Connections {
        target: rootItem
        onLanguageChanged: {
            var str = mushroomModel.source.toString()
            if ((str.search('en.xml') >= 0) && (language != 'en')) {
                mushroomModel.source = str.replace('_en.xml', '.xml')
                if (currentCategory == "Edible mushrooms")
                    currentCategory = "Съедобные грибы";
                else
                    if (currentCategory == "Inedible mushrooms")
                        currentCategory = "Ядовитые грибы";
                    else
                        currentCategory = "Условно съедобные грибы";
            }
            else {
                if (language != 'ru') {
                    mushroomModel.source = str.replace('.xml', '_en.xml')
                    if (currentCategory == "Съедобные грибы")
                        currentCategory = "Edible mushrooms";
                    else
                        if (currentCategory == "Ядовитые грибы")
                            currentCategory = "Inedible mushrooms";
                        else
                            currentCategory = "Choice mushrooms";
                }
            }
        }
    }
}

