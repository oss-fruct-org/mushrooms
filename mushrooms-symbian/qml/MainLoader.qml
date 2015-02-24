import QtQuick 1.1
import com.nokia.symbian 1.1

Rectangle {
    id: main
    width: 480
    height: 854
    state: "splash"

    Loader {
        id: loader
        anchors.fill: parent
        visible: loader.source != ""
    }

    states: [
        State {
            name: "main"
            PropertyChanges {
                target: loader
                source: "main.qml"
            }
        },
        State {
            name: "splash"
            PropertyChanges {
                target: loader
                source: "SplashScreen.qml"
            }
        }
    ]
}
