import QtQuick 1.1
import com.nokia.symbian 1.1
import QtMobility.sensors 1.1
import 'settings.js' as Settings

PageStackWindow {
    id: appWindow

    property bool showCategories
    property string selectedLanguage

    property real scaleFactor: appWindow.width / 480

    Page {
        id: mainPage
        tools: mainTools
        orientationLock: PageOrientation.LockPortrait

        ToolBarLayout {
            id: mainTools

            ToolButton {
                iconSource: "toolbar-view-menu"
                anchors.right: (parent === undefined) ? undefined : parent.right
                onClicked: (myMenu.status === DialogStatus.Closed) ? myMenu.open() : myMenu.close()
            }
        }

        function load() {
            mainLoader.source = "MainPagePortrait.qml"
        }

        Loader {
            id: mainLoader;
            anchors.fill: parent;
        }

        onStatusChanged: {
            if (status == PageStatus.Activating) {
                activatingAnimation.start()
            }
        }

        SequentialAnimation {
            id: activatingAnimation
            running: false
            NumberAnimation { target:mainPage; property:"opacity"; from: 0; to: 1; duration:500 }
        }
    }

    Page {
        id: helpPage
        tools: commonTools
        orientationLock: PageOrientation.LockPortrait

        function load() {
            helpLoader.source = "HelpPage.qml"
        }

        Loader {id: helpLoader; anchors.fill: parent;}
    }

    Page {
        id: aboutPage
        tools: commonTools
        orientationLock: PageOrientation.LockPortrait

        ToolBarLayout {
            id: commonTools
            ToolButton {
                iconSource: "toolbar-back"
                anchors.left: (parent === undefined) ? undefined : parent.left
                onClicked: pageStack.pop();

            }

            ToolButton {
                iconSource: "toolbar-view-menu"
                anchors.right: (parent === undefined) ? undefined : parent.right
                onClicked: (myMenu.status === DialogStatus.Closed) ? myMenu.open() : myMenu.close()
            }
        }

        function load() {
            aboutLoader.source = "AboutPage.qml"
        }

        Loader {id: aboutLoader; anchors.fill: parent;}
    }

    Menu {
        id: myMenu
        visualParent: pageStack

        MenuLayout {
            MenuItem {
                text: qsTr("Language") + rootItem.emptyString
                onClicked: {
                    langDialog.open()
                }
            }

            MenuItem {
                text: qsTr("Help") + rootItem.emptyString
                onClicked: {
                    myMenu.close();
                    helpPage.load();
                    pageStack.push(helpPage);
                }
            }

            MenuItem {
                text: qsTr("About") + rootItem.emptyString
                onClicked: {
                    myMenu.close();
                    aboutPage.load();
                    pageStack.push(aboutPage);
                }
            }
        }
    }

    SelectionDialog {
        id: langDialog
        titleText: qsTr("Choose Language") + rootItem.emptyString

        model: ListModel {
            ListElement { name: "Русский"}
            ListElement { name: "English"}

        }
        onSelectedIndexChanged: {
            if ((selectedIndex == 0) && (selectedLanguage != 'ru')) {
                rootItem.selectLanguage("ru")
                Settings.setSetting('lang', 'ru')
                appWindow.selectedLanguage = 'ru'
            }
            else {
                if ((selectedLanguage != 'en') && (selectedIndex == 1)) {
                    rootItem.selectLanguage("en")
                    Settings.setSetting('lang', 'en')
                    appWindow.selectedLanguage = 'en'
                }
            }
        }
    }

    Image {
        id: splashImage
        anchors.fill: parent
        source: (appWindow.selectedLanguage == 'en') ? "qrc:/gfx/splash-portrait_en.png" : "qrc:/gfx/splash-portrait.png"

        SequentialAnimation {
            id:splashanimation
            PauseAnimation { duration: 3000 }
            PropertyAnimation {
                target: splashImage
                duration: 700
                properties: "opacity"
                to: 0
            }
        }
    }

    FontLoader {
        id: mushroomFont
        source: "qrc:/qml/Gothic.ttf"
    }

    Component.onCompleted: {
        splashanimation.start()
        mainPage.load()
        Settings.initialize()
        if (Settings.getSetting('first_start') == 'Unknown') {
            Settings.setSetting('first_start', 'true');
            Settings.setSetting('lang', systemLocale)
        }
        appWindow.selectedLanguage = Settings.getSetting('lang')
        console.log(appWindow.selectedLanguage)
        rootItem.selectLanguage(appWindow.selectedLanguage)
        pageStack.push(mainPage)
    }
}
