import QtQuick 1.1
import com.nokia.symbian 1.1
import 'settings.js' as Settings

Item {
    id: splashScreenContainer
    anchors.fill: parent

    // image source is kept as an property alias, so that it can be set from outside
    property string imageSource: (appWindow.selectedLanguage == 'en') ? "qrc:/gfx/splash-portrait_en.png" : "qrc:/gfx/splash-portrait.png"

    // signal emits when splashscreen animation completes
    signal splashScreenCompleted()
    Image {
        id: splashImage
        source: imageSource
        anchors.fill: splashScreenContainer // do specify the size and position
    }

    // simple QML animation to give a good user experience
    SequentialAnimation {
        id:splashanimation
        PauseAnimation { duration: 2500 }
        onCompleted: {
            splashScreenContainer.splashScreenCompleted()
        }
    }
    //starts the splashScreen
    Component.onCompleted: {
        splashanimation.start()
        Settings.initialize()
         appWindow.selectedLanguage = Settings.getSetting('lang')
        rootItem.selectLanguage(appWindow.selectedLanguage)
    }
    onSplashScreenCompleted: main.state = 'main'
}
